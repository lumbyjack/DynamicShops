package com.hotmail.idiotonastic.plugins.DynamicShops;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.hotmail.idiotonastic.plugins.DynamicShops.Utils.IngreedientFinder;

public class Shop implements Listener {
	private static Main plugin = Main.getPlugin(Main.class);
	private static Economy econ = plugin.getEconomy();
	private static double mod = plugin.getConfig().getDouble("Modifier");
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Date date = new Date();
	private static Log logs = new Log();
	private static Material[] oddities = 
		{
		Material.REDSTONE,
		Material.LAPIS_LAZULI,
		Material.IRON_NUGGET,
		Material.GOLD_NUGGET,
		Material.DIAMOND,
		Material.EMERALD
		};
	
	public static double getprice(Material item){
		double price = round(Double.valueOf(plugin.getConfig().getString(item.name())),2);
		for(int x = 0; x < oddities.length; x++){
			if(item.equals(oddities[x])){
				return round(price,2);
			}
		}
		double tPrice = price;
		if (plugin.getConfig().getInt(item.name()) != -1){
			ItemStack is = new ItemStack (item,1);
			if( Bukkit.getServer().getRecipesFor(is) != null && IngreedientFinder.getRecipeIngreedients(is)[0].getType() != item){
				ItemStack[] i = IngreedientFinder.getRecipeIngreedients(is);
					if (i.length > 1) {
						tPrice = 0.0;
						for(int x = 0; x < i.length; x++){
								price = round(plugin.getConfig().getDouble(i[x].getType().name().toUpperCase()),2);
								tPrice = tPrice + price;
						}	
					}
					return round(tPrice, 2);
			} else {
				price = plugin.getConfig().getDouble(item.name());
				if (price <= 0 ){
					price = 0.1;
					return round(price,2);
				}
				return round(price,2);
			}
		}
		if (IngreedientFinder.getOutputAmnt(new ItemStack(item)) > 0 && tPrice > 0){
			tPrice = tPrice / IngreedientFinder.getOutputAmnt(new ItemStack(item));
			if (tPrice <= 0){
				tPrice = 0.1;
			}
			return round(tPrice, 2);
		}
		return round(price,2);
	}


	private static void setprice(Material item, int amnt, boolean buy){
		plugin.getConfig().options().copyDefaults(true);
		ItemStack[] i = IngreedientFinder.getRecipeIngreedients(new ItemStack (item,1));
		double priceM = 0.0;
		double priceT = 0.0;
		for(int x = 0; x < i.length; x++){
			double price = plugin.getConfig().getDouble(i[x].getType().name());
			priceM = modifyPrice(buy, price, amnt);
			if (priceM <= 0){
				priceM = 0.1;
			}
			plugin.getConfig().set(i[x].getType().name().toUpperCase(), Double.valueOf(round(priceM,2)));
			priceT += priceM;
		}
		plugin.getConfig().set(item.name().toUpperCase(),Double.valueOf(round(priceT,2)));
		plugin.saveConfig();
		plugin.getConfig();
		return;
	}



	private static void setpriceOveride(Material item, double priceM){
		plugin.getConfig().options().copyDefaults(true);
		plugin.getConfig().set(item.name().toUpperCase(), round(priceM,2));
		plugin.saveConfig();
		plugin.getConfig();
		return;
	}
	//Commmand set price
	public static void set(Player p,String[] args) {
		String itemName = args[1].toUpperCase();
		double amnt = Double.valueOf(args[2]);
		Material item = Material.getMaterial(itemName);
		setpriceOveride(item, amnt);
		log(p.getDisplayName(),item.name(),0,amnt,false,true);
	}
	public static void set(Player p, ItemStack item, String string) {
		double price = Double.valueOf(string);
		setpriceOveride(item.getType(),price);
		log(p.getDisplayName(),item.getType().name(),0,price,false,true);
	}
	//Command get price
	public static void price(Player p, String[] args) {
		String itemName = args[1].toUpperCase();
		Material item = Material.getMaterial(itemName);
		price(p, item);
	}
	public static void price(Player p, Material item ) {
		double op = getprice(item);
		if (op == -1){
			p.sendMessage("Item not avalible for trade, please contact your server admin for info");
			return;
		}
		p.sendMessage(String.format("price for 1 %s is: %s", item.name().toString().toLowerCase(), econ.format(op)));
	}
	
	public static void log(String name, String item, int ammount, double price, boolean BuySell, boolean Set){
		String time = dateFormat.format(date);
		logs.logs(name,item,ammount,price,time,BuySell,Set);
	}
	
	public static void buy(Player p, String[] args) {
		String itemName = args[1].toUpperCase();
		int amnt = Integer.parseInt(args[2]);
		Material item = Material.getMaterial(itemName);
		ItemStack itemstack = new ItemStack(item,amnt);
		buy(p, itemstack);
	}
	public static void buy(Player player, ItemStack item) {
		double total = 0.0;
		double op = getprice(item.getType());
		int amnt = item.getAmount();
		
		if ( op == -1){
			player.sendMessage("Item not avalible for trade, please contact your server admin for info");
			return;
		}
		total = modifyPrice(true, op, amnt);
		if(!(econ.hasAccount(player))){
			econ.createPlayerAccount(player);
		}
		EconomyResponse r = econ.withdrawPlayer(player, total);
		
		setprice(item.getType(), amnt, true);
		if(r.transactionSuccess()) {
            player.sendMessage(String.format("You have spent %s and now have %s", econ.format(r.amount), econ.format(r.balance)));
            player.getInventory().addItem(item);
            log(player.getDisplayName(),item.getType().name().toString().toLowerCase(),amnt,total,true,false);
            return;
		 } else {
			 player.sendMessage("You do not have the required funds.");
			 return;
	     }
		
	}
	public static void sell(Player p, String[] args) {
		String itemName = args[1].toUpperCase();
		int amnt = Integer.parseInt(args[2]);
		Material item = Material.getMaterial(itemName);
		ItemStack itemstack = new ItemStack(item,amnt);
		sell(p,itemstack);
	}
	public static void sell(Player p, ItemStack itemstack) {
		sell(p, itemstack.getType(),itemstack.getAmount(), false);
	}
	public static void sell(Player p, Inventory inv, boolean GUI) {	
		int y = 0;
		Inventory minv = inv;
		for(int i = 0; i < 54; i++){
			int amount = getAmount(minv, inv.getContents()[y].getType());
			if (amount != 0 && inv.getContents()[y].getType().isItem()){
				Material m = inv.getContents()[y].getType();
				minv.remove(m);
				sell(p,m,amount, true);
			}
			y++;
		}
		minv.clear();
		return;
	}
	public static boolean sell(Player p, Material mat, int amount, boolean GUI) {
		ItemStack itemstack = new ItemStack(mat,amount);
		if(!GUI){
			if (!p.getInventory().containsAtLeast(itemstack, 1)){
				p.sendMessage("You do not have enough " + itemstack.getType().name().toString().toLowerCase() + ".");
				return false;
			} 
		}
		double total = 0.0;
		double op = getprice(itemstack.getType());
		if (op == -1){
			p.sendMessage("Item not avalible for trade, please contact your server admin for info");
			if (GUI) {
				p.getInventory().addItem(itemstack);
			}
			return false;
		}
		total = op * amount;
		
		setprice(itemstack.getType(), amount, false);
		EconomyResponse r = econ.depositPlayer(p, total);
	if(r.transactionSuccess()) {
            p.sendMessage(String.format("You were given %s for %s x %s and now have %s", econ.format(r.amount),amount,itemstack.getType().name().toLowerCase(), econ.format(r.balance)));
            if (!GUI){
            	p.getInventory().remove(itemstack);
            }
            log(p.getDisplayName(),itemstack.getType().name().toString().toLowerCase() ,itemstack.getAmount(),total,false,false);
            return true;
	 } else {
		 	p.sendMessage(String.format("An error occured: %s", r.errorMessage));
		 	return false;
     }

	}
	public static int getAmount(Inventory inv, Material material) {
        if (material == null)
            return 0;
        int amount = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack slot = inv.getItem(i);
            if (slot == null || !slot.isSimilar(new ItemStack (material,1))){
                continue;
            }
            amount += slot.getAmount();
        }
        return amount;
    }
	private static double modifyPrice(boolean buy, double opBD, int amnt){
		double total = 0;
		long op = (long) (round(opBD,2) * 100); //0.01 = 1
		long transMod = (long) (round(mod,2) * 100); //% modifier x 100 so 0.01 = 1 
		if (op <= 1){
			op = 100;
		}
		if(buy){
			total = op + (transMod / op);
		} else {
			total = op - (transMod / op);
		}
		
		return round((total / 100), 2);
		
	}
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}

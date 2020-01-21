package com.hotmail.idiotonastic.plugins.DynamicShops;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.hotmail.idiotonastic.plugins.DynamicShops.Utils.IngreedientFinder;

public class Shop implements Listener {
	private static Main plugin = Main.getPlugin(Main.class);
	private static Economy econ = plugin.getEconomy();
	private static int mod = plugin.getConfig().getDefaults().getInt("Modifier");
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Date date = new Date();
	private static Log logs = new Log();
	
	public static BigDecimal getprice(Material item){
		BigDecimal price = new BigDecimal(plugin.getConfig().getDouble(item.name()));
		price.setScale(4, BigDecimal.ROUND_UP);
		BigDecimal tPrice = price;
		tPrice.setScale(4, BigDecimal.ROUND_UP);
		if (plugin.getConfig().getInt(item.name()) != -1){
			ItemStack is = new ItemStack (item,1);
			if( Bukkit.getServer().getRecipesFor(is) != null){
				ItemStack[] i = IngreedientFinder.getIngreedients(is);
				
				if (i.length > 1) {
					tPrice = new BigDecimal(0.0);
					for(int x = 0; x < i.length; x++){
							price = new BigDecimal(plugin.getConfig().getDouble(i[x].getType().name().toUpperCase()));
							tPrice = tPrice.add(price);
					}	
				}
			} else {
			price = new BigDecimal(plugin.getConfig().getDouble(item.name()));
			return price;
			}
		}
		if (IngreedientFinder.getOutputAmnt(new ItemStack(item)) > 0 && tPrice.intValue() > 0){
			tPrice = tPrice.divide(new BigDecimal(IngreedientFinder.getOutputAmnt(new ItemStack(item))), BigDecimal.ROUND_DOWN);
		}
		
		return tPrice;
	}


	private static void setprice(Material item, int amnt, boolean buy){
		plugin.getConfig().options().copyDefaults(true);
		ItemStack[] i = IngreedientFinder.getIngreedients(new ItemStack (item,1));
		BigDecimal priceM = new BigDecimal(0.0);
		BigDecimal priceT = new BigDecimal(0.0);
		for(int x = 0; x < i.length; x++){
			BigDecimal price = new BigDecimal(plugin.getConfig().getDouble(i[x].getType().name()));
			priceM = modifyPrice(buy, price, amnt);
			if (priceM.doubleValue() < 0){
				priceM = new BigDecimal(0.0);
			}
			plugin.getConfig().set(i[x].getType().name().toUpperCase(), priceM.doubleValue());
		}
		plugin.getConfig().set(item.name().toUpperCase(),priceT);
		plugin.saveConfig();
		plugin.reloadConfig();
		return;
	}
	private static void setpriceOveride(Material item, BigDecimal priceM){
		plugin.getConfig().options().copyDefaults(true);
		plugin.getConfig().set(item.name().toUpperCase(), priceM);
		plugin.saveConfig();
		plugin.reloadConfig();
		return;
	}
	//Commmand set price
	public static void set(Player p,String[] args) {
		String itemName = args[1].toUpperCase();
		BigDecimal amnt = new BigDecimal (Integer.parseInt(args[2]));
		Material item = Material.getMaterial(itemName);
		setpriceOveride(item, amnt);
		log(p.getDisplayName(),item.name(),0,amnt.doubleValue(),false,true);
	}
	public static void set(Player p, ItemStack item, String string) {
		setpriceOveride(item.getType(),new BigDecimal(string));
		log(p.getDisplayName(),item.getType().name(),0,Double.valueOf(string),false,true);
	}
	//Command get price
	public static void price(Player p, String[] args) {
		String itemName = args[1].toUpperCase();
		Material item = Material.getMaterial(itemName);
		price(p, item);
	}
	public static void price(Player p, Material item ) {
		BigDecimal op = getprice(item);
		if (op.doubleValue() == -1){
			p.sendMessage("Item not avalible for trade, please contact your server admin for info");
			return;
		}
		p.sendMessage(String.format("price for 1 %s is: %s", item.name().toString().toLowerCase(), econ.format(op.doubleValue())));
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
		BigDecimal total = new BigDecimal(0);
		BigDecimal op = getprice(item.getType());
		int amnt = item.getAmount();
		
		if ( op.intValue() == -1){
			player.sendMessage("Item not avalible for trade, please contact your server admin for info");
			return;
		}
		total = modifyPrice(true, op, amnt);
		if(!(econ.hasAccount(player))){
			econ.createPlayerAccount(player);
		}
		EconomyResponse r = econ.withdrawPlayer(player, total.doubleValue());
		
		setprice(item.getType(), amnt, true);
		if(r.transactionSuccess()) {
            player.sendMessage(String.format("You have spent %s and now have %s", econ.format(r.amount), econ.format(r.balance)));
            player.getInventory().addItem(item);
            log(player.getDisplayName(),item.getType().name().toString().toLowerCase(),amnt,total.doubleValue(),true,false);
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
		sell(p, itemstack, false);
	}
	public static void sell(Player p, ItemStack[] itemstack, boolean GUI) {
		ItemStack item = new ItemStack(Material.WRITTEN_BOOK,1);
		for (int i = 0; i < itemstack.length ; i++){
			item = itemstack[i];
			if (item != null && !item.getType().equals(Material.WRITTEN_BOOK)){
				sell(p,itemstack[i],true);
			}	
		}
	}
	public static boolean sell(Player p, ItemStack itemstack, boolean GUI) {
		int amnt = itemstack.getAmount();
		if(!GUI){
			if (!p.getInventory().containsAtLeast(itemstack, 1)){
				p.sendMessage("You do not have enough " + itemstack.getType().name().toString().toLowerCase() + ".");
				return false;
			} 
		}
		BigDecimal total = new BigDecimal(0.0);
		BigDecimal op = getprice(itemstack.getType());
		if (op.doubleValue() == -1){
			p.sendMessage("Item not avalible for trade, please contact your server admin for info");
			if (GUI) {
				p.getInventory().addItem(itemstack);
			}
			return false;
		}
		total = modifyPrice(false, op, amnt);
		
		setprice(itemstack.getType(), amnt, false);
		EconomyResponse r = econ.depositPlayer(p, new Double(total.toString()));
		if(r.transactionSuccess()) {
            p.sendMessage(String.format("You were given %s for %s x %s and now have %s", econ.format(r.amount),itemstack.getAmount(),itemstack.getType().name().toLowerCase(), econ.format(r.balance)));
            if (!GUI){
            	p.getInventory().remove(itemstack);
            }
            log(p.getDisplayName(),itemstack.getType().name().toString().toLowerCase() ,itemstack.getAmount(),total.doubleValue(),false,false);
            return true;
	 } else {
		 	p.sendMessage(String.format("An error occured: %s", r.errorMessage));
		 	return false;
     }
	}
	private static BigDecimal modifyPrice(boolean buy,BigDecimal opBD, int amnt){
		double total= 0;
		Double op = new Double(opBD.toString());
		if(buy){
			double inc = 0;
			if (op <= 0){
				inc = 0.01;
			} else {
				inc = ((op/100) * 10) / mod;
			}
			for(int x = 0; x < amnt; x++){
				total += op;
				op = op + inc;
			}
		} else {
			double dec = 0;
			if (op <= 0){
				dec = 0;
			} else {
				dec = ((op/100) * 10) / mod;
			}
			for(int x = 0; x < amnt; x++){
				total += op;
				op = op - dec;
			}
		}
		BigDecimal BT = new BigDecimal(total);
		return BT;
		
	}

}

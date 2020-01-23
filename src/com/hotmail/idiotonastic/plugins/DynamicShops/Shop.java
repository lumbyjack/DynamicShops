package com.hotmail.idiotonastic.plugins.DynamicShops;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.hotmail.idiotonastic.plugins.DynamicShops.Main.State;
import com.hotmail.idiotonastic.plugins.DynamicShops.Utils.IngreedientFinder;

public class Shop implements Listener {
	private static Main plugin = Main.getPlugin(Main.class);
	private static Economy econ = plugin.getEconomy();
	private static double mod = plugin.getConfig().getDouble("Modifier");
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Date date = new Date();
	private static Log logs = new Log();
	
	public static double getprice(Material item){
		if (item.equals(Material.DEBUG_STICK)){
			return getAveragePlankPrice();
		}
		double price = Double.valueOf(plugin.getConfig().getString(item.name()));
		if (price <=0.0){
			price = 1.0;
		}
		double tPrice = price;
		if (plugin.getConfig().getString(item.name()) != "-1"){
			ItemStack is = new ItemStack (item,1);
			if (is != null){
				try {				
					if(Bukkit.getServer().getRecipesFor(is).size() != 0 || !Bukkit.getServer().getRecipesFor(is).isEmpty()){
						if (IngreedientFinder.getRecipeIngreedients(is)[0].getType() != item){
							ItemStack[] i = IngreedientFinder.getRecipeIngreedients(is);
								if (i.length < 2) {
									tPrice = 0.0;
									for(int x = 0; x < i.length; x++){
										if (i[x].getType().equals((Material.DEBUG_STICK))){
											price = getAveragePlankPrice();
										} else  {
											price = Double.valueOf(plugin.getConfig().getString(i[x].getType().name().toUpperCase()));
										}
										price = price * i[x].getAmount();
										tPrice = tPrice + price;
									}	
									return round(tPrice , 2);
								}
								ItemStack singItemRec = i[0];
								if(singItemRec != null){
									double siPrice = Double.valueOf(plugin.getConfig().getString(item.name()));
									if((singItemRec.getType().name().contains("_BLOCK"))){
										ItemStack[] ii = IngreedientFinder.getRecipeIngreedients(singItemRec);
										if (ii.length > 1) {
											siPrice = 0.0;
											double p = 0.0;
											for(int x = 0; x < ii.length; x++){
												if (ii[x].getType().equals((Material.DEBUG_STICK))){
													p = getAveragePlankPrice();
												} else  {
													p = Double.valueOf(plugin.getConfig().getString(ii[x].getType().name().toUpperCase()));
												}
												//price = price / ii[x].getAmount();
												siPrice = siPrice + p;
											}	
										}
										if((singItemRec.getType().name().contains("_NUGGET"))){
											return round(siPrice/9, 2);
										}
										return round(siPrice, 2);
									}
									return round(siPrice,2);
								} else {
									return round(tPrice,2);
								}
								
							}
							
					} else {
						price = Double.valueOf(plugin.getConfig().getString(item.name()));
						if (price <= 0.0 ){
							price = 0.1;
							return price;
						}
						return round(price,2);
					}
				}  catch(Exception e){
					price = Double.valueOf(plugin.getConfig().getString(item.name()));
					if (price <= 0.0 ){
						price = 0.1;
						return price;
					}
					return price;
				}
				
			}
			return Double.valueOf(plugin.getConfig().getString(item.name()));
		}
		if (IngreedientFinder.getOutputAmnt(new ItemStack(item)) > 0 && tPrice > 0){
			tPrice = tPrice / IngreedientFinder.getOutputAmnt(new ItemStack(item));
			if (tPrice <= 0.0){
				tPrice = 0.1;
			}
			return round(tPrice,2);
		}
		return round(price,2);
	}


	private static void setprice(Material item, double price, State s , int amount){
		plugin.getConfig().options().copyDefaults(true);
		if (item.equals(Material.DEBUG_STICK)){
			return;
		}
		double priceM = 0.0;
		double priceT = 0.0;
		try {
			if (Bukkit.getServer().getRecipesFor(new ItemStack(item,1)).size() != 0 || !Bukkit.getServer().getRecipesFor(new ItemStack(item,1)).isEmpty()){
				ItemStack[] i = IngreedientFinder.getRecipeIngreedients(new ItemStack (item,1));
				double rPrice = 0.0;
				for(int x = 0; x < i.length; x++){
					if(i[x].getAmount()> 0){
						if (i[x].getType().equals(Material.DEBUG_STICK)){
							rPrice = modifyPrice(s,getAveragePlankPrice(),i[x].getAmount());
							
							priceM = round( rPrice / i[x].getAmount(),2);
							setAveragePlankPrice(priceM);
						} else  {
							rPrice = modifyPrice(s,Double.valueOf(plugin.getConfig().getString(i[x].getType().name())),i[x].getAmount());
							priceM = round( rPrice / i[x].getAmount(),2);
							plugin.getConfig().set(i[x].getType().name().toUpperCase(), priceM);
						}
						priceT += rPrice;
					}
				}
			} else {
				priceT += price;
			}
		}catch(Exception e){
			price = Double.valueOf(plugin.getConfig().getString(item.name()));
			priceT += price;
		}
		priceT = round(priceT,2);
		double PriceF = priceT/amount;
		plugin.getConfig().set(item.name().toUpperCase(),PriceF);
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
		log(p.getDisplayName(),item,0,amnt,State.SET);
	}
	public static void set(Player p, ItemStack item, String string) {
		double price = Double.valueOf(string);
		log(p.getDisplayName(),item.getType(),0,price,State.SET);
	}
	//Command get price
	public static void priceC(Player p, String[] args) {
		String itemName = args[1].toUpperCase();
		Material item = Material.getMaterial(itemName);
		price(p, item);
	}
	public static void price(Player p, Material item ) {
		double op = getprice(item);
		if (op == -1){
			p.sendMessage(ChatColor.GRAY + item.name().toString().toLowerCase().replace("_", " ") + ": is not avalible for trade.");
			return;
		}
		p.sendMessage(String.format(ChatColor.AQUA + "price for 1 %s is: %s", item.name().toString().toLowerCase().replace("_", " "), econ.format(op).substring(0,1)+ op));
	}
	
	public static void log(String name, Material item, int ammount, double price, State state){
		String time = dateFormat.format(date);
		if(state == State.SET){
			setpriceOveride(item,price);
			
		} else {
			setprice(item,price, state,ammount);
		}
		logs.logs(name,item.name(),ammount,price,time,state);
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
			player.sendMessage(ChatColor.GRAY + item.getType().name().toString().toLowerCase().replace("_", " ") + ": is not avalible for trade.");
			return;
		}
		
		if(!(econ.hasAccount(player))){
			econ.createPlayerAccount(player);
		}
		total = modifyPrice(State.BUY,op ,amnt);
		EconomyResponse r = econ.withdrawPlayer(player, total);
		if(r.transactionSuccess()) {
            player.sendMessage(String.format(ChatColor.GREEN +"You have spent %s and now have %s", econ.format(r.amount), econ.format(r.balance)));
            player.getInventory().addItem(new ItemStack(item.getType(), item.getAmount()));
            log(player.getDisplayName(),item.getType(),amnt,total,State.BUY);
            return;
		 } else {
			 player.sendMessage(ChatColor.RED + "You do not have the required funds.");
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
		Inventory minv = inv;
		for(int i = 0; i < 54; i++){
			if (minv != null){
				
				if (minv.getContents()[i] != null){
					int amount = getAmount(minv, minv.getContents()[i].getType());
					Material m = minv.getContents()[i].getType();
					
					sell(p,m,amount, true);
					minv.remove(m);
					
				}
			}

		}
		return;
	}
	public static boolean sell(Player p, Material mat, int amount, boolean GUI) {
		ItemStack itemstack = new ItemStack(mat,amount);
		if(!GUI){
			if (!p.getInventory().containsAtLeast(itemstack, 1)){
				p.sendMessage(ChatColor.GRAY + "You do not have enough " + mat.name().toString().toLowerCase().replace("_", " ") + ".");
				return false;
			} 
		}
		double total = 0.0;
		double op = getprice(mat);
		if (op == -1){
			p.sendMessage(ChatColor.GRAY + mat.name().toString().toLowerCase().replace("_", " ") + ": is not avalible for trade.");
			if (GUI) {
				p.getInventory().addItem(itemstack);
			}
			return false;
		}
		if(!(econ.hasAccount(p))){
			econ.createPlayerAccount(p);
		}
		
		total = modifyPrice(State.SELL, op, amount);
		EconomyResponse r = econ.depositPlayer(p, total);
	if(r.transactionSuccess()) {
            p.sendMessage(String.format(ChatColor.AQUA + "You were given %s for %s x %s and now have %s", econ.format(r.amount),amount,mat.name().toString().toLowerCase().replace("_", " "), econ.format(r.balance)));
            if (!GUI){
            	p.getInventory().remove(itemstack);
            }
            log(p.getDisplayName(),itemstack.getType() ,itemstack.getAmount(),total,State.SELL);
            return true;
	 } else {
		 	p.sendMessage(String.format(ChatColor.RED + "An error occured: %s", r.errorMessage));
		 	return false;
     }

	}
	public static int getAmount(Inventory inv, Material material) {
        if (material == null)
            return 0;
        int amount = 0;
        for (int i = 0; i < 54; i++) {
            ItemStack slot = inv.getItem(i);
            if (slot == null || !slot.isSimilar(new ItemStack (material,1))){
                continue;
            }
            amount += slot.getAmount();
        }
        return amount;
    }
	private static double modifyPrice(State s, double opBD, int amnt){
		double total = 0;
		double op = round(opBD,2); //0.01 = 1
		double transMod = round(mod,2); //%0.01 = 1 
			if (op <= 1){
				op = 1; 
			}
			//  increase = [mod] * (op)
			// Decrease = [mod] * (op)
			double modifiyAmount = (transMod * op );
			if(s.equals(State.BUY)){
				total = (op + modifiyAmount);// total = (price + increase)
			} else if (s.equals(State.SELL)){
				total = (op - modifiyAmount);// total = (price - decrease)
			}
			total = total * amnt;
			total = round((total), 5);

		
		return total;
		
	}
	public static double round(double value, int places ) {
	    if (places < 0) throw new IllegalArgumentException();
	    places+=1;
	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.UP);
	    return bd.doubleValue();
	}
	private static double getAveragePlankPrice(){
		double price = 0.0;
		double totalForAllPlanks = 0.0;
		for (int x = 0; x < matLogs.length; x++){
			double matPrice = Double.valueOf(plugin.getConfig().getString(matLogs[x].name()));
			totalForAllPlanks += (matPrice/4);
		}
		price = totalForAllPlanks / matLogs.length;
		return price;
		
	}
	private static void setAveragePlankPrice(double price) {
		double priceForLog = 0.0;
		priceForLog = price *4;
		for (int x = 0; x < matLogs.length; x++){
			setpriceOveride(matLogs[x], priceForLog);
		}
		return;
		
	}

	static Material[] matLogs = 
		{
			Material.OAK_LOG,
			Material.SPRUCE_LOG,
			Material.BIRCH_LOG,
			Material.JUNGLE_LOG,
			Material.ACACIA_LOG,
			Material.DARK_OAK_LOG
		};
}

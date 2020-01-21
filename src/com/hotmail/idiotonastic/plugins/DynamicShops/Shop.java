package com.hotmail.idiotonastic.plugins.DynamicShops;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Shop implements Listener {
	private static Main plugin = Main.getPlugin(Main.class);
	private static Economy econ = plugin.getEconomy();
	private static int mod = plugin.getConfig().getDefaults().getInt("Modifier");
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Date date = new Date();
	private static Log logs = new Log();
	private static String[] blocks = {
			"Iron_Block",
			"Gold_Block",
			"Lapis_Block",
			"Redstone_Block",
			"Diamond_Block",
			"Emerald_Block",
			"Hay_Block",
			"Bone_Block"
		};
	private static String[] bars = {
			"Iron_Ingot",
			"Gold_Ingot",
			"Lapis_Lazuli",
			"Redstone",
			"Diamond",
			"Emerald",
			"Wheat",
			"Bone_Meal"
			};
	private static String[] equip = {
			"_pickaxe",
			"_axe",
			"_shovel",
			"_hoe",
			"_sword",
			"_helmet",
			"_chestplate",
			"_leggings",
			"_boots"
	};
	private static int[] matamnt = {
			3,
			3,
			1,
			2,
			2,
			5,
			8,
			7,
			4	
	};
	public static double getprice(Material item){
		
		ItemStack i = getResourceMat(item);
		int a = i.getAmount();
		Double d = new Double(a);//first way.
		double nP = new Double(plugin.getConfig().getDouble(i.getType().toString()));
		double price = d * nP;
		return price;
	}
	public static ItemStack getResourceMat(Material item){
		ItemStack stack = new ItemStack(item,1); 
		for (int i = 0; i < 10; i++){
			if (i < blocks.length){
				if (item.name().equalsIgnoreCase(blocks[i])){
					stack.setType(Material.getMaterial(bars[i].toUpperCase()));
					stack.setAmount(9);
					return stack;
				}
			}
			if (i < equip.length){
				if (item.name().contains(equip[i].toUpperCase()) && !item.name().contains("CHAINMAIL")&& !item.name().contains("TURTLE")){
					String[] name = item.name().split("_");
					if (name[0].equalsIgnoreCase("iron")){
						name[0] = "Iron_Ingot";
					} else if (name[0].equalsIgnoreCase("golden")){
						name[0] = "Gold_Ingot";
					} else if (name[0].equalsIgnoreCase("wooden")){
						return stack;
					}
					stack.setType(Material.getMaterial(name[0].toUpperCase()));
					stack.setAmount(matamnt[i]); 
					return stack;
				}
			}
		}
		return stack;
	}
	private static void setprice(Material item, double x){
		ItemStack ri = getResourceMat(item);
		int a = ri.getAmount();
		Double d = new Double(a);
		double y = x / d;
		plugin.getConfig().options().copyDefaults(true);
		plugin.getConfig().set(ri.getType().name().toUpperCase(), y);
		plugin.getConfig().set(item.name().toUpperCase(), x);
		plugin.saveConfig();
		plugin.reloadConfig();
		return;
	}
	public static void set(Player p,String[] args) {
		String itemName = args[1].toUpperCase();
		int amnt = Integer.parseInt(args[2]);
		Material item = Material.getMaterial(itemName);
		setprice(item, amnt);
		log(p.getDisplayName(),item.name(),0,amnt,false,true);
	}
	public static void set(Player p, ItemStack item, String string) {
		setprice(item.getType(),Double.valueOf(string));
		log(p.getDisplayName(),item.getType().name(),0,Double.valueOf(string),false,true);
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
	public void sell(Player p, ItemStack[] itemstack, boolean GUI) {
		ItemStack item = new ItemStack(Material.WRITTEN_BOOK,1);
		for (int i = 0; i < itemstack.length ; i++){
			item = itemstack[i];
			if (item != null && !item.getType().equals(Material.WRITTEN_BOOK)){
				sell(p,itemstack[i],true);
			}	
		}
	}
	public static boolean sell(Player p, ItemStack itemstack, boolean GUI) {
		if(!GUI){
			if (!p.getInventory().containsAtLeast(itemstack, 1)){
				p.sendMessage("You do not have enough " + itemstack.getType().name().toString().toLowerCase() + ".");
				return false;
			} 
		}
		double total = 0;
		double op = getprice(itemstack.getType());
		if (op == -1){
			p.sendMessage("Item not avalible for trade, please contact your server admin for info");
			if (GUI) {
				p.getInventory().addItem(itemstack);
			}
			return false;
		}
		double dec = 0;
		if (op <= 0){
			dec = 0;
		} else {
			dec = ((op/100) * 10) / mod;
		}
		for(int x = 0; x < itemstack.getAmount(); x++){
			total += op;
			op = op - dec;
		}
		setprice(itemstack.getType(), op);
		EconomyResponse r = econ.depositPlayer(p, total);
		if(r.transactionSuccess()) {
            p.sendMessage(String.format("You were given %s for %s x %s and now have %s", econ.format(r.amount),itemstack.getAmount(),itemstack.getType().name().toLowerCase(), econ.format(r.balance)));
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
	public static void price(Player p, String[] args) {
		String itemName = args[1].toUpperCase();
		Material item = Material.getMaterial(itemName);
		price(p, item);
	}
	public static void price(Player p, Material item ) {
		
		Material itemM = getResourceMat(item).getType();
		double op = getprice(itemM);
		if (op == -1){
			p.sendMessage("Item not avalible for trade, please contact your server admin for info");
			return;
		}
		p.sendMessage(String.format("price for %d %s is: %s", getResourceMat(item).getAmount(), getResourceMat(item).getType().name().toString().toLowerCase(), econ.format(getResourceMat(item).getAmount() * op)));
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
		double total = 0;
		double op = getprice(item.getType());
		int amnt = item.getAmount();
		
		if (op == -1){
			player.sendMessage("Item not avalible for trade, please contact your server admin for info");
			return;
		}
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
		if(!(econ.hasAccount(player))){
			econ.createPlayerAccount(player);
		}
		EconomyResponse r = econ.withdrawPlayer(player, total);
		
		setprice(item.getType(), op);
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

}

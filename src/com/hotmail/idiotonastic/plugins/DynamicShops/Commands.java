package com.hotmail.idiotonastic.plugins.DynamicShops;

import java.util.UUID;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Commands implements Listener, CommandExecutor {
	
	private static Main plugin = Main.getPlugin(Main.class);
	private boolean GUI = plugin.getGUI();
	private static Economy econ = plugin.getEconomy();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("Sell")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if ((p.isOp()) || (p.hasPermission("dynamicshops.cantrade"))) {
					if(GUI){
						ShopScreen is = new ShopScreen();
						is.sellScreen(p);
						return true;
					}
				}
				sender.sendMessage("Sorry you lack permission to use Dynamic Shops");
				return false;
			}
			sender.sendMessage("Sorry only players can use Dynamic Shops");
			return false;
		}
		if (args[0].toLowerCase().equals("price")){
			Player p = (Player) sender;
			if (args.length == 1) { 
				Shop.price(p, p.getInventory().getItemInMainHand().getType());
				return true;
			} else {
				try {
					Material.getMaterial(args[1].toUpperCase());
				} catch(NullPointerException e) {
					p.sendMessage("Item not found.");
					return false;
				}
				Shop.priceC(p,args);
				return true;
			}
			
		}
		if (cmd.getName().equalsIgnoreCase("DS") || cmd.getName().equalsIgnoreCase("Shop")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				
				
				if ((p.isOp()) || (p.hasPermission("dynamicshops.cantrade"))) {
					if (args.length == 0) { 
						if(GUI){
							ShopScreen is = new ShopScreen();
							is.menuScreen(p);
							return true;
						}
						p.sendMessage("Please use " + ChatColor.BLUE + "/Shop [ buy | sell | price ]");
						return false;
					}
					if(args[0].toLowerCase().equals("buy")){
						if(GUI){
							ShopScreen is = new ShopScreen();
							is.menuScreen(p);
							return true;
						}
						try {
							Material.getMaterial(args[1].toUpperCase());
						} catch(NullPointerException e) {
							p.sendMessage("Item not found.");
							return false;
						}
						Shop.buy(p,args);
					} else if (args[0].toLowerCase().equals("sell")){
						if(GUI){
							ShopScreen is = new ShopScreen();
							is.sellScreen(p);
							return true;
						}
						try {
							Material.getMaterial(args[1].toUpperCase());
						} catch(NullPointerException e) {
							p.sendMessage("Item not found.");
							return false;
						}
						Shop.sell(p,args);
					} else if (args[0].toLowerCase().equals("price")){
						if (args.length == 1) { 
							Shop.price(p, p.getInventory().getItemInMainHand().getType());
							return true;
						} else {
							try {
								Material.getMaterial(args[1].toUpperCase());
							} catch(NullPointerException e) {
								p.sendMessage("Item not found.");
								return false;
							}
							Shop.priceC(p,args);
							return true;
						}
						
					} else{
						p.sendMessage("Please use " + ChatColor.BLUE + "/shop [ buy | sell | price ]");
						return false;
					}
					return true;
				} else {
					sender.sendMessage("Sorry you lack permission to use Dynamic Shops");
					return true;
				}
			} else {
				sender.sendMessage("Sorry only players can use Dynamic Shops");
				return true;
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("DSA")) {
			Boolean doReload = false;

			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.hasPermission("dynamicshops.admin")) {
					if (args.length == 0) { 
							p.sendMessage("Please use " + ChatColor.BLUE + "/DSA [ reload | set ]");
							return false;
					}
					if(args[0].toLowerCase().equals("reload")){
						doReload = true;
					} else if (args[0].toLowerCase().equals("set")){
						Shop.set(p,p.getInventory().getItemInMainHand(),args[1]);
						double price = Shop.getprice(p.getInventory().getItemInMainHand().getType());
						p.sendMessage(String.format("Price for: %s set to %s",p.getInventory().getItemInMainHand().getType(), econ.format(price)));
						return true;
					} else {
						p.sendMessage("Please use " + ChatColor.BLUE + "/DSA [ reload | set ]");
						return false;
					}
				} else {
					p.sendMessage("You do not have permission to DSA.");
					return false;
				}
			} else {
				// command is from console so allow
				if(args[0].toLowerCase() == "reload"){
					doReload = true;
				} else if (args[0].toLowerCase() == "set"){
					try {
						Material.getMaterial(args[1].toUpperCase());
					} catch(NullPointerException e) {
						sender.sendMessage("Item not found.");
						return false;
					}
					Shop.set(null,args);
					return true;
				} else if (args[0].toLowerCase() == "gui"){
					plugin.getConfig().set("GUI", !GUI);
					return true;
				} else{
					sender.sendMessage("Please use " + ChatColor.BLUE + "/DSA [ reload | set | GUI ]");
					return false;
				}
			}

			if (doReload) {
				plugin.reloadConfig();
				sender.sendMessage("Config reloaded.");
				return true;
			}
		}
		return false;

		}
	public UUID getUUIDfromName(String name){
		UUID pID = null; 
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
		  if(p.getName().toLowerCase().equals(name.toLowerCase())) {
		    pID = p.getUniqueId();
		    break;
		  }
		}
		return pID;
	}

}
package com.hotmail.idiotonastic.plugins.TrulyDynamicShops;

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

import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Main.State;

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
		if (cmd.getName().equalsIgnoreCase("price")){
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if ((p.isOp()) || (p.hasPermission("dynamicshops.cantrade"))) {
					Double price = Shop.getPrice( p.getInventory().getItemInMainHand(), State.QUERY);
					p.sendMessage(ChatColor.AQUA+ String.format("The price for %s of %s is %s.",  p.getInventory().getItemInMainHand().getAmount(),  p.getInventory().getItemInMainHand().getType().name(),econ.format(price)));
					return true;
				} 
				sender.sendMessage("Sorry you lack permission to use Dynamic Shops");
				return false;
			}
			sender.sendMessage("Sorry only players can use Dynamic Shops");
			return false;
		}
			
		
		if (cmd.getName().equalsIgnoreCase("Shop")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				
				
				if ((p.isOp()) || (p.hasPermission("trulydynamicshops.cantrade"))) {
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
							Shop.getPrice( p.getInventory().getItemInMainHand(), State.QUERY);
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
		
		if (cmd.getName().equalsIgnoreCase("TDS")) {

			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.hasPermission("trulydynamicshops.admin")) {
					if (args.length == 0) { 
							p.sendMessage("Please use " + ChatColor.BLUE + "/TDS [ reload | set ]");
							return false;
					}
					if(args[0].toLowerCase().equals("reload")){
						plugin.doReload();
					} else if (args[0].toLowerCase().equals("set")){
						Material mat = p.getInventory().getItemInMainHand().getType();
						double value = 0.0;
						try {
							value = Double.valueOf(args[1]);
						} catch(NullPointerException e) {
							sender.sendMessage("Item not found.");
							return false;
						}
						plugin.getItemMan().getItemList(mat).put(mat.name(), value);
						p.sendMessage(String.format("Price for: %s set to %s",p.getInventory().getItemInMainHand().getType(), econ.format(value)));
						
						return true;
					}  else {
						p.sendMessage("Please use " + ChatColor.BLUE + "/TDS [ reload | set ]");
						return false;
					}
				} else {
					p.sendMessage("You do not have permission to TDS.");
					return false;
				}
			} else {
				// command is from console so allow
				if(args[0].toLowerCase().equals("reload")){
					plugin.doReload();
				} else if (args[0].toLowerCase().equals("set")){
					Material mat;
					double value = 0.0;
					try {
						mat = Material.getMaterial(args[1].toUpperCase());
						value = Double.valueOf(args[2]);
					} catch(NullPointerException e) {
						sender.sendMessage("Item not found.");
						return false;
					}
					plugin.getItemMan().getItemList(mat).put(mat.name(), value);
					return true;
				} else if (args[0].toLowerCase().equals("gui")){
					plugin.getConfig().set("GUI", !GUI);
					return true;
				} else{
					sender.sendMessage("Please use " + ChatColor.BLUE + "/DSA [ reload | set | GUI ]");
					return false;
				}
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
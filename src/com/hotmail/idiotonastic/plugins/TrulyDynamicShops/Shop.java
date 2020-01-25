package com.hotmail.idiotonastic.plugins.TrulyDynamicShops;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Main.State;
import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Utils.IngreedientFinder;
import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Utils.IngreedientMod;
import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Utils.Log;

public class Shop implements Listener {
	private static Main plugin = Main.getPlugin(Main.class);
	private static Economy econ = plugin.getEconomy();
	private static double transMod = plugin.getConfig().getDouble("transaction_modifier");
	private static double craftMod = plugin.getConfig().getDouble("crafting_modifier");
	private static Map<String, Double> rawPrices = new HashMap<>();
	private static Log transLog = new Log();
	
	
	
	
	public static List<IngreedientMod> getRawMat(Material item, int amount){

		List<IngreedientMod> recipeList = new ArrayList<>();
		ItemStack getFor = new ItemStack(item, amount);
		recipeList = IngreedientFinder.getRecipeIngreedients(getFor);
		int size = recipeList.size();
		boolean firstpass = true;
		if(size > 1 || firstpass){
			firstpass = false;
			List<IngreedientMod> newRecipeList = new ArrayList<>();
			for(int y = 0; y < size ; y++){
				if (plugin.getItemMan().getRawList().get(recipeList.get(y).getItem().getType().name()) == null){
					newRecipeList = getRawMat(recipeList.get(y).getItem().getType(), recipeList.get(y).getItem().getAmount());
					recipeList.addAll(newRecipeList); 
					recipeList.remove(y);
				}
			}
			 
		}
		return recipeList;
	}

	public static double getRawPrice(Material item, int Amount,State state){
		List<IngreedientMod> recipeList = new ArrayList<>();
		double totalPrice = 0.0;
		double op = 0.0;
		rawPrices.clear();
		recipeList = getRawMat(item,Amount);
		int size = recipeList.size();
		for (int i = 0; i < size; i++){
			String name = recipeList.get(i).getItem().getType().name();
			if (name.equals("STICK")){
				 op = getAveragePlankPrice()/2;
			} else {
			 op = plugin.getItemMan().getRawList().get(name) * recipeList.get(i).getMod();
			}
			double itemPrice = modifyPrice(state,op,recipeList.get(i).getItem().getAmount());
			totalPrice += itemPrice;
			rawPrices.put(recipeList.get(i).getItem().getType().name(), (itemPrice/recipeList.get(i).getItem().getAmount()));
		}

		return totalPrice;
	}
	public static double getPrice(ItemStack item, State state){
		boolean log = false;
		switch(state){
			case BUY:
				log = true;
			case SELL:
				log = true;
				break;
			default:
				break;
		}
		double price = 0.0;
		String itemName = item.getType().name();
		if (item.getType().name().contains("_PLANK")){
			for(int i = 0; i < matPlank.length; i++){
				if(matPlank[i].equals(item.getType())){
					item = new ItemStack(matLogs[i], item.getAmount());
					price = modifyPrice(state,plugin.getItemMan().getRawList().get(matLogs[i].name()), item.getAmount())/4;
					if(log){
						plugin.getItemMan().getRawList().put(matLogs[i].name(), round((price/item.getAmount()),2));
					}
				}
			}

		}
		else
		if (item.getType().equals(Material.DEBUG_STICK)){
			price = modifyPrice(state,getAveragePlankPrice(), item.getAmount());
			if(log){
				setAveragePlankPrice(price);
			}
		} else
		if (plugin.getItemMan().getRawList().get(itemName) != null) {

				price = modifyPrice(state,plugin.getItemMan().getRawList().get(itemName), item.getAmount());

			
			if(log){
				plugin.getItemMan().getRawList().put(itemName, round((price/item.getAmount()),2));

			}
		} else 
		if (plugin.getItemMan().getCraftableList().get(itemName) != null){
			price = (plugin.getRawBase() ? getRawPrice(item.getType(),item.getAmount(),state) : plugin.getItemMan().getCraftableList().get(itemName));
			price += price * craftMod;
			if(log){
				plugin.getItemMan().getCraftableList().put(itemName, round((price/item.getAmount()),2));
				plugin.getItemMan().getRawList().putAll(rawPrices);
				rawPrices.clear();
			}
			
		} else 
		if (plugin.getItemMan().getOreList().get(itemName) != null){
			price = modifyPrice(state, plugin.getItemMan().getOreList().get(itemName), item.getAmount());
			price -= price * craftMod;
			if(log){
				plugin.getItemMan().getOreList().put(itemName, round((price/item.getAmount()),2));
			}
		} else 	
		if (plugin.getItemMan().getItem_blockList().get(itemName) != null){
			price = (plugin.getRawBase() ? getRawPrice(item.getType(),item.getAmount(),state) : plugin.getItemMan().getItem_blockList().get(itemName));
			if(log){
				plugin.getItemMan().getItem_blockList().put(itemName, round((price/item.getAmount()),2));
			}
		} else 
		if (plugin.getItemMan().getNo_recipeList().get(itemName) != null){
			price = (plugin.getRawBase() ? getRawPrice(Material.getMaterial(item.getType().name()+"_POWDER"),item.getAmount(),state) * craftMod : plugin.getItemMan().getNo_recipeList().get(itemName)); 
			if(log){
				plugin.getItemMan().getNo_recipeList().put(itemName, (price/item.getAmount()));
			}
		} else 
		if (plugin.getItemMan().getNot_craftableList().get(itemName) != null){
			price = modifyPrice(state,plugin.getItemMan().getNot_craftableList().get(itemName), item.getAmount());
			if(log){
				plugin.getItemMan().getNot_craftableList().put(itemName, round((price/item.getAmount()),2));
			}
		} else 
		if (plugin.getItemMan().getMob_eggsList().get(itemName) != null){
			price = modifyPrice(state,plugin.getItemMan().getMob_eggsList().get(itemName), item.getAmount());
			if(log){
				plugin.getItemMan().getMob_eggsList().put(itemName, round((price/item.getAmount()),2));
			}
		} else 	
		if (plugin.getItemMan().getEffectList().get(itemName) != null){
			price =  modifyPrice(state,plugin.getItemMan().getEffectList().get(itemName), item.getAmount());
			if(log){
				plugin.getItemMan().getEffectList().put(itemName, round((price/item.getAmount()),2));
			}
		} else 
		if (plugin.getItemMan().getUnobtainableList().get(itemName) != null){
			price = modifyPrice(state,plugin.getItemMan().getUnobtainableList().get(itemName), item.getAmount());
			if(log){
				plugin.getItemMan().getUnobtainableList().put(itemName, round((price/item.getAmount()),2));
			}
		} else 
		if (plugin.getItemMan().getAdminList().get(itemName) != null){
			price = modifyPrice(state,plugin.getItemMan().getAdminList().get(itemName), item.getAmount());
			if(log){
				plugin.getItemMan().getAdminList().put(itemName, round((price/item.getAmount()),2));
			}
		} else 	{
			return -1;
		}
		
		return price;
		
	}
	//Command get price
	public static void priceC(Player p, String[] args) {
		String itemName = args[1].toUpperCase();
		Material item = Material.getMaterial(itemName);
		double op = getPrice(new ItemStack(item,1), State.QUERY);
		if (op == -1){
			p.sendMessage(ChatColor.GRAY + item.name().toString().toLowerCase().replace("_", " ") + ": is not avalible for trade.");
			return;
		}
		p.sendMessage(String.format(ChatColor.AQUA + "price for 1 %s is: %s", item.name().toString().toLowerCase().replace("_", " "), /* econ symbol ->*/econ.format(op).substring(0,1)+ op));
	}	
	public static void buy(Player p, String[] args) {
		String itemName = args[1].toUpperCase();
		int amnt = Integer.parseInt(args[2]);
		Material item = Material.getMaterial(itemName);
		ItemStack itemstack = new ItemStack(item,amnt);
		buy(p, itemstack);
	}
	public static void buy(Player player, ItemStack item) {
		double total = getPrice(item, State.BUY);
		int amnt = item.getAmount();
		
		if ( total == -1){
			player.sendMessage(ChatColor.GRAY + item.getType().name().toString().toLowerCase().replace("_", " ") + ": is not avalible for trade.");
			return;
		}
		
		if(!(econ.hasAccount(player))){
			econ.createPlayerAccount(player);
		}
		EconomyResponse r = econ.withdrawPlayer(player, total);
		if(r.transactionSuccess()) {
            player.sendMessage(String.format(ChatColor.GREEN +"You have spent %s and now have %s", econ.format(r.amount), econ.format(r.balance)));
            player.getInventory().addItem(new ItemStack(item.getType(), item.getAmount()));
            transLog.logThis(player.getDisplayName(),item.getType().name(),amnt,total,State.BUY);
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
		double total = getPrice(itemstack, State.SELL);
		if (total == -1){
			p.sendMessage(ChatColor.GRAY + mat.name().toString().toLowerCase().replace("_", " ") + ": is not avalible for trade.");
			if (GUI) {
				p.getInventory().addItem(itemstack);
			}
			return false;
		}
		if(!(econ.hasAccount(p))){
			econ.createPlayerAccount(p);
		}
		
		EconomyResponse r = econ.depositPlayer(p, total);
	if(r.transactionSuccess()) {
            p.sendMessage(String.format(ChatColor.AQUA + "You were given %s for %s x %s and now have %s", econ.format(r.amount),amount,mat.name().toString().toLowerCase().replace("_", " "), econ.format(r.balance)));
            if (!GUI){
            	p.getInventory().remove(itemstack);
            }
            transLog.logThis(p.getDisplayName(),mat.name(),amount,total,State.SELL);
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
	private static double getAveragePlankPrice(){
		double price = 0.0;
		double totalForAllPlanks = 0.0;
		for (int x = 0; x < matLogs.length; x++){
			double matPrice = plugin.getItemMan().getRawList().get(matLogs[x].name());
			totalForAllPlanks += (matPrice/4);
		}
		price = totalForAllPlanks / matLogs.length;
		return price;
		
	}
	private static void setAveragePlankPrice(Double set){
		for (int x = 0; x < matPlank.length; x++){
			plugin.getItemMan().getRawList().put(matPlank[x].name(),set/matPlank.length);
		}
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
	static Material[] matPlank = 
		{
			Material.OAK_PLANKS,
			Material.SPRUCE_PLANKS,
			Material.BIRCH_PLANKS,
			Material.JUNGLE_PLANKS,
			Material.ACACIA_PLANKS,
			Material.DARK_OAK_PLANKS
		};

	private static double modifyPrice(State s, double opBD, int amnt){
		if (amnt == 0){
			return 0.0;
		}
		double total = 0;
		double op = round(opBD,2); //0.01 = 1
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
			} else if (s.equals(State.SET)){
				return round((op), 5); //return just the price of the item with only a rounding modification
			} else if (s.equals(State.QUERY)){
				total = op * amnt;
				total = round((total), 5);
				return total;
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

}

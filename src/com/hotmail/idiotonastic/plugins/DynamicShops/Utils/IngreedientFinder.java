package com.hotmail.idiotonastic.plugins.DynamicShops.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class IngreedientFinder {
	
	public static ItemStack[] getRecipeIngreedients(ItemStack item){
		Inventory inv = Bukkit.createInventory(null,54);
		for(Recipe recipe : Bukkit.getServer().getRecipesFor(item)) {
		    if(recipe instanceof ShapedRecipe) {
		        ShapedRecipe shaped = (ShapedRecipe)recipe;
		        char[] key = {
		        		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'
		        };
		        for (int q = 0; q < 9; q++){
		        	if(shaped.getIngredientMap().get(key[q]) != null){
		        		inv.addItem(shaped.getIngredientMap().get(key[q]));
		        		
		        	}
		        }
		    }
		    else if(recipe instanceof ShapelessRecipe) {
		    	
		        ShapelessRecipe shapeless = (ShapelessRecipe)recipe;
		        for (int i = 0; i <shapeless.getIngredientList().size(); i++){
		        	if(shapeless.getIngredientList().get(i) != null){
		        		if(item.getType().name().contains("_BLOCK")){
		        			inv.addItem(shapeless.getIngredientList().get(i));
		        		}
		        		
		        	}
		        }
		        
		    }
		    else if(recipe instanceof FurnaceRecipe) {
		        FurnaceRecipe furnace = (FurnaceRecipe)recipe;
		        if(!((item.getType().name().contains("_ORE") && (!item.getType().name().contains("IRON") || !item.getType().name().contains("GOLD"))))){
		        	inv.addItem(furnace.getInput());
		        }
		        	
		    }
		}
		
		List<String> compact = new ArrayList<String>();
		compact = getInv(inv);
		
		ItemStack[] returns = new ItemStack[compact.size()];
		for (int x =0; x < returns.length; x++){
			String[] compactSplit = compact.get(x).split(":");
			returns[x]= new ItemStack(Material.getMaterial(compactSplit[0]),Integer.valueOf(compactSplit[1]));
		}
		return returns;
	}
	@Deprecated
	public static ItemStack[] getIngreedients(ItemStack item){
		List<ItemStack> recipeItems = new ArrayList<ItemStack>();
		ItemStack[] recipeItemsCondensed;
		ItemStack[] singleItemStack = {new ItemStack(item.getType())};
		for(Recipe recipe : Bukkit.getServer().getRecipesFor(item)) {
		    if(recipe instanceof ShapedRecipe) {
		        ShapedRecipe shaped = (ShapedRecipe)recipe;
		        char[] key = {
		        		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'
		        };
		        for (int q = 0; q < 9; q++){
		        	if(shaped.getIngredientMap().get(key[q]) != null){
		        		Map<Character, ItemStack> ingM = shaped.getIngredientMap();
		        		recipeItems.add(new ItemStack(ingM.get(key[q])));
		        	}
		        }
		    }
		    else if(recipe instanceof ShapelessRecipe) {
		    	
		        ShapelessRecipe shapeless = (ShapelessRecipe)recipe;
		        for (int i = 0; i <shapeless.getIngredientList().size(); i++){
		        	if(shapeless.getIngredientList().get(i) != null){
		        		recipeItems.add(shapeless.getIngredientList().get(i));
		        	}
		        }
		        
		    }
		    else if(recipe instanceof FurnaceRecipe) {
		        FurnaceRecipe furnace = (FurnaceRecipe)recipe;
		        singleItemStack[0] = furnace.getInput(); 
		    }
		}


		recipeItemsCondensed = new ItemStack[recipeItems.size()];

		for(int x = 0; x < recipeItemsCondensed.length; x++){
			
			recipeItemsCondensed[x] = recipeItems.get(x);
		}
		return recipeItemsCondensed;
		
	}
	public static List<String> getInv(Inventory inv) {	
		ItemStack items[] = inv.getContents();
		List<String> count = new ArrayList<String>();
		for(int i = 0; i < items.length; i++){
			if(items[i] == null){
				continue;
			}
			int amount = getMatAmount(inv, items[i]);
			ItemStack item = items[i];
			count.add(String.format("%s:%s", item.getType().name(),amount));
			inv.remove(items[i].getType());
		}
		return count;
	}
	public static int getMatAmount(Inventory inv, ItemStack item) {
        if (item == null)
            return 0;
        int amount = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack slot = inv.getItem(i);
            if (slot == null || !slot.isSimilar(item))
                continue;
            amount += slot.getAmount();
        }
        return amount;
    }
	public static int getOutputAmnt(ItemStack item){
		for(Recipe recipe : Bukkit.getServer().getRecipesFor(item)) {
		    if(recipe instanceof ShapedRecipe) {
		        return recipe.getResult().getAmount();
		    }
		    else if(recipe instanceof ShapelessRecipe) {
		    	return recipe.getResult().getAmount();
		    }
		    else if(recipe instanceof FurnaceRecipe) {
		    	return recipe.getResult().getAmount();
		    }
		}
		return 0;
	}
	
}

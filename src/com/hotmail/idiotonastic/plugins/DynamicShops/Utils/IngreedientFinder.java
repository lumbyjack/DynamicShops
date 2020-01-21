package com.hotmail.idiotonastic.plugins.DynamicShops.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class IngreedientFinder {

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

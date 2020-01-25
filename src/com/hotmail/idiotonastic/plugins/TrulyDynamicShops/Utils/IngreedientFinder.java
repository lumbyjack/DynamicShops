package com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Utils;

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

	
	public static List<IngreedientMod> getRecipeIngreedients(ItemStack item){
		List<IngreedientMod> recipeInv = new ArrayList<>();
		boolean plankCheck = false;
		for (int x = 0; x < genericPlankItems.length; x++){
			if(item.getType() == genericPlankItems[x]){
				plankCheck = true;
				break;
			}
		}
		//Inventory inv = Bukkit.createInventory(null,54);
		for(Recipe recipe : Bukkit.getServer().getRecipesFor(item)) {
	        int ingAmnt = 0;
	        double outAmnt = item.getAmount();
		    if(recipe instanceof ShapedRecipe) {
		        ShapedRecipe shaped = (ShapedRecipe)recipe;

		        char[] key = {
		        		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'
		        };
		        for (int q = 0; q < 9; q++){
		        	if(shaped.getIngredientMap().get(key[q]) != null){	
		        		if (item.getType().name().equalsIgnoreCase("STICK")){ 
			        		if (!(shaped.getIngredientMap().get(key[q]).getType().name().equalsIgnoreCase("BAMBOO"))){
			        			ingAmnt = shaped.getIngredientMap().get(key[q]).getAmount();
			        			recipeInv.add(new IngreedientMod(shaped.getIngredientMap().get(key[q]),ingAmnt/(double)outAmnt));
			        		}
			        	} else if (shaped.getIngredientMap().get(key[q]).getType().name().contains("_PLANKS")&&plankCheck){
		        			ItemStack i = new ItemStack(Material.DEBUG_STICK,shaped.getIngredientMap().get(key[q]).getAmount());
		        			ingAmnt = shaped.getIngredientMap().get(key[q]).getAmount();
		        			recipeInv.add(new IngreedientMod(i,Double.valueOf(ingAmnt/(double)outAmnt)));
		    			} else if(item.getType().name().contains("_BLOCK")){
			        		if (shaped.getIngredientMap().get(key[q]).getType().name().contains("_INGOT")){
			        			ItemStack i = new ItemStack(Material.getMaterial(shaped.getIngredientMap().get(key[q]).getType().name().replace("_INGOT", "_NUGGET")), 9);
			        			ingAmnt = shaped.getIngredientMap().get(key[q]).getAmount();
			        			recipeInv.add(new IngreedientMod(i,outAmnt/(double)ingAmnt));
			        		}
			        	}  else if(item.getType().name().contains("_INGOT")){
			        		if (shaped.getIngredientMap().get(key[q]).getType().name().contains("_NUGGET")){
			        			ItemStack i = new ItemStack(Material.getMaterial(shaped.getIngredientMap().get(key[q]).getType().name().replace("_INGOT", "_NUGGET")), 1);
			        			ingAmnt = shaped.getIngredientMap().get(key[q]).getAmount();
			        			recipeInv.add(new IngreedientMod(i,outAmnt/(double)ingAmnt));
			        		}
			        	} else {
		        			ingAmnt = shaped.getIngredientMap().get(key[q]).getAmount();
		        			recipeInv.add(new IngreedientMod(shaped.getIngredientMap().get(key[q]),ingAmnt/(double)outAmnt));
		    			}
	    			}
		        }
		    }
		    else if(recipe instanceof ShapelessRecipe) {
		    	
		        ShapelessRecipe shapeless = (ShapelessRecipe)recipe;
        			for (int i = 0; i <shapeless.getIngredientList().size(); i++){
        				if(item.getType().name().contains("_INGOT")){
			        		if (shapeless.getIngredientList().get(i).getType().name().contains("_NUGGET")){
			        			ingAmnt = shapeless.getIngredientList().get(i).getAmount();
			        			recipeInv.add(new IngreedientMod(shapeless.getIngredientList().get(i),Double.valueOf(ingAmnt/outAmnt)));
			        		} 
			        	} else if(item.getType().name().contains("_BLOCK")){
			        		if (shapeless.getIngredientList().get(i).getType().name().contains("_INGOT")){
			        			ingAmnt = shapeless.getIngredientList().get(i).getAmount();
			        			recipeInv.add(new IngreedientMod(shapeless.getIngredientList().get(i),Double.valueOf(ingAmnt/outAmnt)));
			        		}
			        	} else {
		        			ingAmnt = shapeless.getIngredientList().get(i).getAmount();
		        			recipeInv.add(new IngreedientMod(shapeless.getIngredientList().get(i),Double.valueOf(ingAmnt/outAmnt)));	
			        	}
	        		}
		        
		    }
		    else if(recipe instanceof FurnaceRecipe) {
		        FurnaceRecipe furnace = (FurnaceRecipe)recipe;
		        if(item.getType().isEdible() && (item.getType().name().contains("COOKED_")||item.getType().name().contains("BAKED"))){
        			ingAmnt = furnace.getInput().getAmount();
        			recipeInv.add(new IngreedientMod(furnace.getResult(),Double.valueOf(ingAmnt/outAmnt)));
		        } else if (item.getType().name().contains("_ORE")){
        			ingAmnt = furnace.getInput().getAmount();
        			recipeInv.add(new IngreedientMod(furnace.getResult(),Double.valueOf(ingAmnt/outAmnt)));
		        }
		        	
		    }
		}
		List<IngreedientMod> buffer = new ArrayList<>();
		buffer = recipeInv;

		for(int i = 0; i < recipeInv.size(); i++){
			IngreedientMod temp = recipeInv.get(i);
			for(int y = 1; y < buffer.size(); y++){
				int anmt = temp.getItem().getAmount();
				if(buffer.get(y).compare(temp)){
					anmt += buffer.get(y).getItem().getAmount();
					buffer.remove(y);
				}
				temp.setItemAmount(anmt);
			}
			recipeInv.set(i,temp);
		}
		buffer = buffer;
		return buffer; //TOD STCAK ITTERTIVE
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
	static Material[] genericPlankItems = 
		{
			Material.STICK,
			Material.WOODEN_AXE,
			Material.WOODEN_HOE,
			Material.WOODEN_SWORD,
			Material.WOODEN_PICKAXE,
			Material.WOODEN_SHOVEL,
			Material.CRAFTING_TABLE,
			Material.CHEST,
			Material.BARREL,
			Material.COMPOSTER,
			Material.SMITHING_TABLE,
			Material.SHIELD,
			Material.BOOKSHELF,
			Material.LECTERN,
			Material.JUKEBOX,
			Material.NOTE_BLOCK,
			Material.FLETCHING_TABLE,
			Material.LOOM,
			Material.CARTOGRAPHY_TABLE,
			Material.GRINDSTONE,
			Material.BEEHIVE,
			Material.PISTON,
			Material.BOWL,
			Material.TRIPWIRE_HOOK,
			Material.WHITE_BED,
			Material.ORANGE_BED,
			Material.MAGENTA_BED,
			Material.LIGHT_BLUE_BED,
			Material.YELLOW_BED,
			Material.LIME_BED,
			Material.PINK_BED,
			Material.GRAY_BED,
			Material.LIGHT_GRAY_BED,
			Material.CYAN_BED,
			Material.PURPLE_BED,
			Material.BLUE_BED,
			Material.BROWN_BED,
			Material.GREEN_BED,
			Material.RED_BED,
			Material.BLACK_BED
		};
	@Deprecated
	public static ItemStack[] getIngreedients(ItemStack item){
		List<ItemStack> recipeItems = new ArrayList<ItemStack>();
		ItemStack[] recipeItemsCondensed;
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
		}


		recipeItemsCondensed = new ItemStack[recipeItems.size()];

		for(int x = 0; x < recipeItemsCondensed.length; x++){
			
			recipeItemsCondensed[x] = recipeItems.get(x);
		}
		return recipeItemsCondensed;
		
	}
}

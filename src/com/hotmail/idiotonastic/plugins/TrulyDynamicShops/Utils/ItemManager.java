package com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;






import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Main;

public class ItemManager{
	private static Logger logger;
	private static Main plugin;
	
	private StorableHashMap<String,Double> rawList;		
	private StorableHashMap<String,Double> craftableList;
	private StorableHashMap<String,Double> oreList;		
	private StorableHashMap<String,Double> item_blockList;
	private StorableHashMap<String,Double> no_recipeList;	
	private StorableHashMap<String,Double> not_craftableList;
	private StorableHashMap<String,Double> mob_eggsList;
	private StorableHashMap<String,Double> effectList;	
	private StorableHashMap<String,Double> unobtainableList;
	private StorableHashMap<String,Double> adminList;	
		
	public boolean initilaizeItemManager(Main pl){
		plugin = pl;
		logger = plugin.getLogger();
		File f = new File(plugin.getDataFolder() +"/data/");
		try {
		    rawList 				= new StorableHashMap<>(f, "rawList");
		    craftableList  		= new StorableHashMap<>(f, "craftableList");
		    oreList  			= new StorableHashMap<>(f, "oreList");
		    item_blockList  		= new StorableHashMap<>(f, "item_blockList");
		    no_recipeList 		= new StorableHashMap<>(f, "no_recipeList");
		    not_craftableList  	= new StorableHashMap<>(f, "not_craftableList");
		    mob_eggsList 		= new StorableHashMap<>(f, "mob_eggsList");
		    effectList  			= new StorableHashMap<>(f, "effectList");
		    unobtainableList  	= new StorableHashMap<>(f, "unobtainableList");
		    adminList  			= new StorableHashMap<>(f, "adminList");
		}  catch (IOException e) {
		        logger.log(Level.WARNING, "Error creating Item save files!", e);
		    }
		
		if(rawList.keySet().isEmpty()){
			loadDefaults();
		}
		//convertOldConfig();
		return loadLists();
	}
	public boolean loadLists(){
		logger.info("[TrulyDynamicShops] Loading pricelist...");
		int maxItems = Material.values().length;
		int items = 0;
		try {
			rawList.loadFromFile();
			items += rawList.size();
			
			craftableList.loadFromFile();
			items += craftableList.size();
			
			oreList.loadFromFile();
			items += oreList.size();
			
			item_blockList.loadFromFile();
			items += item_blockList.size();
			
			no_recipeList.loadFromFile();
			items += no_recipeList.size();
			
			not_craftableList.loadFromFile();
			items += not_craftableList.size();
			
			mob_eggsList.loadFromFile();
			items += mob_eggsList.size();
			
			effectList.loadFromFile();
			items += effectList.size();
			
			unobtainableList.loadFromFile();
			items += unobtainableList.size();
			
			adminList.loadFromFile();
			items += adminList.size();
			
			logger.info(String.format("[TrulyDynamicShops] Pricelist loaded [%s items / %s possible items] sucessfuly.",items,maxItems));
		} catch(Exception e){
			logger.warning(String.format("[TrulyDynamicShops] Config incorrect at Item: %s, please check for errors.",items));
			logger.warning("[TrulyDynamicShops] Will now be disabled.");
			return false;
		}
		return true;
	 }
	public void saveLists(){
		logger.info("[TrulyDynamicShops] Saving pricelist...");
		int maxItems = Material.values().length;
		int items = 0;
		try {
			rawList.saveToFile();
			items += rawList.size();
			
			craftableList.saveToFile();
			items += craftableList.size();
			
			oreList.saveToFile();
			items += oreList.size();
			
			item_blockList.saveToFile();
			items += item_blockList.size();
			
			no_recipeList.saveToFile();
			items += no_recipeList.size();
			
			not_craftableList.saveToFile();
			items += not_craftableList.size();
			
			mob_eggsList.saveToFile();
			items += mob_eggsList.size();
			
			effectList.saveToFile();
			items += effectList.size();
			
			unobtainableList.saveToFile();
			items += unobtainableList.size();
			
			adminList.saveToFile();
			items += adminList.size();
						
			logger.info(String.format("[TrulyDynamicShops] Pricelist saved [%s items / %s possible items] sucessfuly.",items,maxItems));
		} catch(Exception e){
			logger.warning(String.format("[TrulyDynamicShops] Config incorrect at Item: %s, please check for errors.",items));
			logger.warning("[TrulyDynamicShops] Please contact the developer for more information.");
		}
	 }

	public Map<String,Double> getItemList(Material item){
		String itemName = item.name();
		if (rawList.keySet().contains(itemName)){
			return rawList;
		} else 
		if (craftableList.keySet().contains(itemName)){
			return craftableList;
		} else 
		if (oreList.keySet().contains(itemName)){
			return oreList;
		} else 	
		if (item_blockList.keySet().contains(itemName)){
			return item_blockList;
		} else 
		if (no_recipeList.keySet().contains(itemName)){
			return no_recipeList;
		} else 
		if (not_craftableList.keySet().contains(itemName)){
			return not_craftableList;
		} else 
		if (mob_eggsList.keySet().contains(itemName)){
			return mob_eggsList;
		} else 	
		if (effectList.keySet().contains(itemName)){
			return effectList;
		} else 
		if (unobtainableList.keySet().contains(itemName)){
			return unobtainableList;
		} else 
		if (adminList.keySet().contains(itemName)){
			return adminList;
		} else 	{
			Map<String,Double> n = new HashMap<String,Double>();
			return n;
		}
	}
	
	public void convertOldConfig(){

		FileConfiguration oldConfig;
		Map<String,Object> oldList	= new HashMap<>();
		File file;
		file = new File(plugin.getDataFolder() + File.separator + "oldconfig.yml");
		if (file.exists()) {
			logger.info("[TrulyDynamicShops] oldconfig.yml found ...");
			logger.info("[TrulyDynamicShops] Conveting pricelist...");
			oldConfig= YamlConfiguration.loadConfiguration(file);
			oldList.putAll(oldConfig.getValues(false));
			int i = 0;
			Material mat;
			for(i = 0; i < oldList.size(); i++){
				String key = oldList.keySet().toArray()[i].toString();
				try {
					mat = Material.getMaterial(key);
					
					getItemList(mat).put(key, Double.valueOf((String) oldList.get(key)));
				} catch(Exception e){
					logger.throwing("[TrulyDynamicShops] Item not recognised: ",key, e);
				}
			}
			logger.info("[TrulyDynamicShops] items converted:" + i);
			saveLists();
		} else {
			logger.info("[TrulyDynamicShops] oldconfig.yml not found.");
		}
	}
	
	public void setRawList(Map<String,Double> set){
		rawList.putAll(set);
	}

	public void setCraftableList(Map<String,Double> set){
		craftableList.putAll(set);
	}

	public void setOreList(Map<String,Double> set){
		oreList.putAll(set);
	}
	public void setItem_blockList(Map<String,Double> set){
		item_blockList.putAll(set);
	}
	public void setNo_recipeList(Map<String,Double> set){
		no_recipeList.putAll(set);
	}
	public void setNot_craftableList(Map<String,Double> set){
		not_craftableList.putAll(set);
	}
	public void setMob_eggsList(Map<String,Double> set){
		mob_eggsList.putAll(set);
	}
	public void setEffectList(Map<String,Double> set){
		effectList.putAll(set);
	}
	public void setUnobtainableList(Map<String,Double> set){
		unobtainableList.putAll(set);
	}
	public void setAdminList(Map<String,Double> set){
		adminList.putAll(set);
	}
	public Map<String,Double> getRawList(){
		return rawList;
	}
	public Map<String,Double> getCraftableList(){
		return craftableList;
	}
	public Map<String,Double> getOreList(){
		return oreList;
	}
	public Map<String,Double> getItem_blockList(){
		return item_blockList;
	}
	public Map<String,Double> getNo_recipeList(){
		return no_recipeList;
	}
	public Map<String,Double> getNot_craftableList(){
		return not_craftableList;
	}
	public Map<String,Double> getMob_eggsList(){
		return mob_eggsList;
	}
	public Map<String,Double> getEffectList(){
		return effectList;
	}
	public Map<String,Double> getUnobtainableList(){
		return unobtainableList;
	}
	public Map<String,Double> getAdminList(){
		return adminList;
	}
	
	 private void loadDefaults(){
		 for(int i = 0; i <D_rawList_Keyset_A.length; i++){
			 rawList.put(D_rawList_Keyset_A[i],Double.valueOf("20.00"));
		 }
		for(int i = 0; i <D_craftableList_Keyset_A.length; i++){
			craftableList.put(D_craftableList_Keyset_A[i],Double.valueOf("0.00"));
		}
		for(int i = 0; i <D_oreList_Keyset_A.length; i++){
			oreList.put(D_oreList_Keyset_A[i],Double.valueOf("50.00"));
		}
		for(int i = 0; i <D_item_blockList_Keyset_A.length; i++){
			item_blockList.put(D_item_blockList_Keyset_A[i],Double.valueOf("0.00"));
		}
		for(int i = 0; i <D_no_recipeList_Keyset_A.length; i++){
			no_recipeList.put(D_no_recipeList_Keyset_A[i],Double.valueOf("10.00"));
		}
		for(int i = 0; i <D_not_craftableList_Keyset_A.length; i++){
			 not_craftableList.put(D_not_craftableList_Keyset_A[i],Double.valueOf("10.00"));
		}
		for(int i = 0; i <D_mob_eggsList_Keyset_A.length; i++){
			 mob_eggsList.put(D_mob_eggsList_Keyset_A[i],Double.valueOf("10.00"));
		}
		for(int i = 0; i <D_effectList_Keyset_A.length; i++){
			effectList.put(D_effectList_Keyset_A[i],Double.valueOf("0.00"));
		}
		for(int i = 0; i <D_unobtainableList_Keyset_A.length; i++){
			unobtainableList.put(D_unobtainableList_Keyset_A[i],Double.valueOf("0.00")); 
		}
		for(int i = 0; i <D_adminList_Keyset_A.length; i++){
			adminList.put(D_adminList_Keyset_A[i],Double.valueOf("0.00"));
		}
 
	 }
	 List<String> a = new ArrayList<String>();
	
	 	String[] D_rawList_Keyset_A = {
	 			"ACACIA_LOG",
	 			"BIRCH_LOG",
	 			"DARK_OAK_LOG",
	 			"JUNGLE_LOG",
	 			"OAK_LOG",
	 			"SPRUCE_LOG",
	 			"STRIPPED_ACACIA_LOG",
	 			"STRIPPED_BIRCH_LOG",
	 			"STRIPPED_DARK_OAK_LOG",
	 			"STRIPPED_JUNGLE_LOG",
	 			"STRIPPED_OAK_LOG",
	 			"STRIPPED_SPRUCE_LOG",
	 			"ACACIA_LEAVES",
	 			"BIRCH_LEAVES",
	 			"DARK_OAK_LEAVES",
	 			"JUNGLE_LEAVES",
	 			"OAK_LEAVES",
	 			"SPRUCE_LEAVES",
	 			"APPLE",
	 			"BAMBOO",
	 			"ANDESITE",
	 			"BONE",
	 			"COD_BUCKET",
	 			"LAVA_BUCKET",
	 			"MILK_BUCKET",
	 			"PUFFERFISH_BUCKET",
	 			"SALMON_BUCKET",
	 			"TROPICAL_FISH_BUCKET",
	 			"WATER_BUCKET",
	 			"COD",
	 			"PUFFERFISH",
	 			"SALMON",
	 			"SHULKER_SHELL",
	 			"TERRACOTTA",
	 			"LEATHER",
	 			"KELP",
	 			"EGG",
	 			"DIRT",
	 			"COBBLESTONE",
	 			"FLINT",
	 			"GRAVEL",
	 			"GRANITE",
	 			"DIORITE",
	 			"DIAMOND",
	 			"CLAY_BALL",
	 			"BEEF",
	 			"FEATHER",
	 			"EMERALD",
	 			"GLOWSTONE_DUST",
	 			"GOLD_NUGGET",
	 			"GUNPOWDER",
	 			"HEART_OF_THE_SEA",
	 			"HONEYCOMB",
	 			"MUTTON",
	 			"NAUTILUS_SHELL",
	 			"NETHERRACK",
	 			"NETHER_WART",
	 			"OBSIDIAN",
	 			"PHANTOM_MEMBRANE",
	 			"POISONOUS_POTATO",
	 			"PORKCHOP",
	 			"POTATO",
	 			"PRISMARINE_CRYSTALS",
	 			"PRISMARINE_SHARD",
	 			"PUMPKIN",
	 			"QUARTZ",
	 			"RABBIT",
	 			"RABBIT_FOOT",
	 			"RABBIT_HIDE",
	 			"RED_MUSHROOM",
	 			"RED_SAND",
	 			"ROTTEN_FLESH",
	 			"REDSTONE",
	 			"SAND",
	 			"SCUTE",
	 			"SLIME_BALL",
	 			"SNOWBALL",
	 			"SOUL_SAND",
	 			"SUGAR_CANE",
	 			"SWEET_BERRIES",
	 			"END_STONE",
	 			"CREEPER_HEAD",
	 			"DRAGON_HEAD",
	 			"PLAYER_HEAD",
	 			"ZOMBIE_HEAD",
	 			"SKELETON_SKULL",
	 			"WITHER_SKELETON_SKULL",
	 			"WHEAT",
	 			"VINE",
	 			"MELON_SLICE",
	 			"LAPIS_LAZULI",
	 			"IRON_NUGGET",
	 			"GHAST_TEAR",
	 			"DRAGON_BREATH",
	 			"COAL",
	 			"CHORUS_FRUIT",
	 			"BEETROOT",
	 			"CARROT",
	 			"CHICKEN",
	 			"BROWN_MUSHROOM",
	 			"SPIDER_EYE",
	 			"ICE",
	 			"ALLIUM",
	 			"AZURE_BLUET",
	 			"WHITE_TULIP",
	 			"WITHER_ROSE",
	 			"CACTUS",
	 			"POPPY",
	 			"PINK_TULIP",
	 			"ORANGE_TULIP",
	 			"LILAC",
	 			"LILY_OF_THE_VALLEY",
	 			"OXEYE_DAISY",
	 			"DANDELION",
	 			"COCOA_BEANS",
	 			"BLUE_ORCHID",
	 			"CORNFLOWER",
	 			"INK_SAC",
	 			"PEONY",
	 			"RED_TULIP",
	 			"SUNFLOWER",
	 			"ROSE_BUSH",
	 			"ACACIA_SAPLING",
	 			"BIRCH_SAPLING",
	 			"DARK_OAK_SAPLING",
	 			"JUNGLE_SAPLING",
	 			"OAK_SAPLING",
	 			"SPRUCE_SAPLING",
	 			"WHEAT_SEEDS",
	 			"BEETROOT_SEEDS",
	 			"CHORUS_FLOWER",
	 			"PUMPKIN_SEEDS",
	 			"MELON_SEEDS",
	 			"DEBUG_STICK"
	 	};
	 	String[] D_craftableList_Keyset_A = {
 				"DIAMOND_AXE",
 				"DIAMOND_HOE",
 				"DIAMOND_PICKAXE",
 				"DIAMOND_SHOVEL",
 				"DIAMOND_SWORD",
 				"GOLDEN_AXE",
 				"GOLDEN_HOE",
 				"GOLDEN_PICKAXE",
 				"GOLDEN_SHOVEL",
 				"GOLDEN_SWORD",
 				"IRON_AXE",
 				"IRON_HOE",
 				"IRON_PICKAXE",
 				"IRON_SHOVEL",
 				"IRON_SWORD",
 				"STONE_AXE",
 				"STONE_HOE",
 				"STONE_PICKAXE",
 				"STONE_SHOVEL",
 				"STONE_SWORD",
 				"WOODEN_AXE",
 				"WOODEN_HOE",
 				"WOODEN_PICKAXE",
 				"WOODEN_SHOVEL",
 				"WOODEN_SWORD",
 				"BLACK_CONCRETE_POWDER",
 				"BLUE_CONCRETE_POWDER",
 				"BROWN_CONCRETE_POWDER",
 				"CYAN_CONCRETE_POWDER",
 				"GRAY_CONCRETE_POWDER",
 				"GREEN_CONCRETE_POWDER",
 				"LIGHT_BLUE_CONCRETE_POWDER",
 				"LIGHT_GRAY_CONCRETE_POWDER",
 				"LIME_CONCRETE_POWDER",
 				"MAGENTA_CONCRETE_POWDER",
 				"ORANGE_CONCRETE_POWDER",
 				"PINK_CONCRETE_POWDER",
 				"PURPLE_CONCRETE_POWDER",
 				"RED_CONCRETE_POWDER",
 				"WHITE_CONCRETE_POWDER",
 				"YELLOW_CONCRETE_POWDER",
 				"SUSPICIOUS_STEW",
 				"BLACK_WOOL",
 				"BLUE_WOOL",
 				"BROWN_WOOL",
 				"CYAN_WOOL",
 				"GRAY_WOOL",
 				"GREEN_WOOL",
 				"LIGHT_BLUE_WOOL",
 				"LIGHT_GRAY_WOOL",
 				"LIME_WOOL",
 				"MAGENTA_WOOL",
 				"ORANGE_WOOL",
 				"PINK_WOOL",
 				"PURPLE_WOOL",
 				"RED_WOOL",
 				"YELLOW_WOOL",
 				"ACACIA_WOOD",
 				"ACACIA_BOAT",
 				"ACACIA_BUTTON",
 				"ACACIA_DOOR",
 				"ACACIA_FENCE",
 				"ACACIA_FENCE_GATE",
 				"ACACIA_PLANKS",
 				"ACACIA_PRESSURE_PLATE",
 				"ACACIA_SIGN",
 				"ACACIA_SLAB",
 				"ACACIA_STAIRS",
 				"ACACIA_TRAPDOOR",
 				"ACACIA_WALL_SIGN",
 				"BIRCH_BOAT",
 				"BIRCH_BUTTON",
 				"BIRCH_DOOR",
 				"BIRCH_FENCE",
 				"BIRCH_FENCE_GATE",
 				"BIRCH_PLANKS",
 				"BIRCH_PRESSURE_PLATE",
 				"BIRCH_SIGN",
 				"BIRCH_SLAB",
 				"BIRCH_STAIRS",
 				"BIRCH_TRAPDOOR",
 				"BIRCH_WALL_SIGN",
 				"BIRCH_WOOD",
 				"DARK_OAK_BOAT",
 				"DARK_OAK_BUTTON",
 				"DARK_OAK_DOOR",
 				"DARK_OAK_FENCE",
 				"DARK_OAK_FENCE_GATE",
 				"DARK_OAK_PLANKS",
 				"DARK_OAK_PRESSURE_PLATE",
 				"DARK_OAK_SIGN",
 				"DARK_OAK_SLAB",
 				"DARK_OAK_STAIRS",
 				"DARK_OAK_TRAPDOOR",
 				"DARK_OAK_WALL_SIGN",
 				"DARK_OAK_WOOD",
 				"JUNGLE_BOAT",
 				"JUNGLE_BUTTON",
 				"JUNGLE_DOOR",
 				"JUNGLE_FENCE",
 				"JUNGLE_FENCE_GATE",
 				"JUNGLE_PLANKS",
 				"JUNGLE_PRESSURE_PLATE",
 				"JUNGLE_SIGN",
 				"JUNGLE_SLAB",
 				"JUNGLE_STAIRS",
 				"JUNGLE_TRAPDOOR",
 				"JUNGLE_WALL_SIGN",
 				"JUNGLE_WOOD",
 				"OAK_BOAT",
 				"OAK_BUTTON",
 				"OAK_DOOR",
 				"OAK_FENCE",
 				"OAK_FENCE_GATE",
 				"OAK_PLANKS",
 				"OAK_PRESSURE_PLATE",
 				"OAK_SIGN",
 				"OAK_SLAB",
 				"OAK_STAIRS",
 				"OAK_TRAPDOOR",
 				"OAK_WALL_SIGN",
 				"OAK_WOOD",
 				"OAK_SLAB",
 				"SPRUCE_BOAT",
 				"SPRUCE_BUTTON",
 				"SPRUCE_DOOR",
 				"SPRUCE_FENCE",
 				"SPRUCE_FENCE_GATE",
 				"SPRUCE_PLANKS",
 				"SPRUCE_PRESSURE_PLATE",
 				"SPRUCE_SIGN",
 				"SPRUCE_SLAB",
 				"SPRUCE_STAIRS",
 				"SPRUCE_TRAPDOOR",
 				"SPRUCE_WALL_SIGN",
 				"SPRUCE_WOOD",
 				"ACACIA_WOOD",
 				"BIRCH_WOOD",
 				"DARK_OAK_WOOD",
 				"JUNGLE_WOOD",
 				"OAK_WOOD",
 				"SPRUCE_WOOD",
 				"ACTIVATOR_RAIL",
 				"ANDESITE_SLAB",
 				"ANDESITE_STAIRS",
 				"ANDESITE_WALL",
 				"ANVIL",
 				"ARMOR_STAND",
 				"BEEHIVE", 
 				"ARROW",
 				"BONE_MEAL",
 				"BOOK",
 				"BOOKSHELF",
 				"CHISELED_QUARTZ_BLOCK",
 				"NOTE_BLOCK",
 				"BEETROOT_SOUP",
 				"BARREL",
 				"BEACON",
 				"BLACK_CARPET",
 				"BLUE_CARPET",
 				"BROWN_CARPET",
 				"CYAN_CARPET",
 				"GRAY_CARPET",
 				"GREEN_CARPET",
 				"LIGHT_BLUE_CARPET",
 				"LIGHT_GRAY_CARPET",
 				"LIME_CARPET",
 				"MAGENTA_CARPET",
 				"ORANGE_CARPET",
 				"PINK_CARPET",
 				"PURPLE_CARPET",
 				"RED_CARPET",
 				"WHITE_CARPET",
 				"YELLOW_CARPET",
 				"BLACK_BANNER",
 				"BLUE_BANNER",
 				"BROWN_BANNER",
 				"CREEPER_BANNER_PATTERN",
 				"CYAN_BANNER",
 				"FLOWER_BANNER_PATTERN",
 				"GLOBE_BANNER_PATTERN",
 				"GRAY_BANNER",
 				"GREEN_BANNER",
 				"LIGHT_BLUE_BANNER",
 				"LIGHT_GRAY_BANNER",
 				"LIME_BANNER",
 				"MAGENTA_BANNER",
 				"MOJANG_BANNER_PATTERN",
 				"ORANGE_BANNER",
 				"PINK_BANNER",
 				"PURPLE_BANNER",
 				"RED_BANNER",
 				"SKULL_BANNER_PATTERN",
 				"WHITE_BANNER",
 				"YELLOW_BANNER",
 				"BLACK_STAINED_GLASS",
 				"BLACK_STAINED_GLASS_PANE",
 				"BLUE_STAINED_GLASS",
 				"BLUE_STAINED_GLASS_PANE",
 				"BROWN_STAINED_GLASS",
 				"BROWN_STAINED_GLASS_PANE",
 				"CYAN_STAINED_GLASS",
 				"CYAN_STAINED_GLASS_PANE",
 				"GRAY_STAINED_GLASS",
 				"GRAY_STAINED_GLASS_PANE",
 				"GREEN_STAINED_GLASS",
 				"GREEN_STAINED_GLASS_PANE",
 				"LIGHT_BLUE_STAINED_GLASS",
 				"LIGHT_BLUE_STAINED_GLASS_PANE",
 				"LIGHT_GRAY_STAINED_GLASS",
 				"LIGHT_GRAY_STAINED_GLASS_PANE",
 				"LIME_STAINED_GLASS",
 				"LIME_STAINED_GLASS_PANE",
 				"MAGENTA_STAINED_GLASS",
 				"MAGENTA_STAINED_GLASS_PANE",
 				"ORANGE_STAINED_GLASS",
 				"ORANGE_STAINED_GLASS_PANE",
 				"PINK_STAINED_GLASS",
 				"PINK_STAINED_GLASS_PANE",
 				"PURPLE_STAINED_GLASS",
 				"PURPLE_STAINED_GLASS_PANE",
 				"RED_STAINED_GLASS",
 				"RED_STAINED_GLASS_PANE",
 				"WHITE_STAINED_GLASS",
 				"WHITE_STAINED_GLASS_PANE",
 				"YELLOW_STAINED_GLASS",
 				"YELLOW_STAINED_GLASS_PANE",
 				"BLACK_BED",
 				"BLUE_BED",
 				"BROWN_BED",
 				"CYAN_BED",
 				"GRAY_BED",
 				"GREEN_BED",
 				"LIGHT_BLUE_BED",
 				"LIGHT_GRAY_BED",
 				"LIME_BED",
 				"MAGENTA_BED",
 				"ORANGE_BED",
 				"PINK_BED",
 				"PURPLE_BED",
 				"RED_BED",
 				"WHITE_BED",
 				"YELLOW_BED",
 				"BLACK_GLAZED_TERRACOTTA",
 				"BLACK_TERRACOTTA",
 				"BLUE_GLAZED_TERRACOTTA",
 				"BLUE_TERRACOTTA",
 				"BROWN_GLAZED_TERRACOTTA",
 				"BROWN_TERRACOTTA",
 				"CYAN_GLAZED_TERRACOTTA",
 				"CYAN_TERRACOTTA",
 				"GRAY_GLAZED_TERRACOTTA",
 				"GRAY_TERRACOTTA",
 				"GREEN_GLAZED_TERRACOTTA",
 				"GREEN_TERRACOTTA",
 				"LIGHT_BLUE_GLAZED_TERRACOTTA",
 				"LIGHT_BLUE_TERRACOTTA",
 				"LIGHT_GRAY_GLAZED_TERRACOTTA",
 				"LIGHT_GRAY_TERRACOTTA",
 				"LIME_GLAZED_TERRACOTTA",
 				"LIME_TERRACOTTA",
 				"MAGENTA_GLAZED_TERRACOTTA",
 				"MAGENTA_TERRACOTTA",
 				"ORANGE_GLAZED_TERRACOTTA",
 				"ORANGE_TERRACOTTA",
 				"PINK_GLAZED_TERRACOTTA",
 				"PINK_TERRACOTTA",
 				"PURPLE_GLAZED_TERRACOTTA",
 				"PURPLE_TERRACOTTA",
 				"RED_GLAZED_TERRACOTTA",
 				"RED_TERRACOTTA",
 				"WHITE_GLAZED_TERRACOTTA",
 				"WHITE_TERRACOTTA",
 				"YELLOW_GLAZED_TERRACOTTA",
 				"YELLOW_TERRACOTTA",
 				"BLACK_SHULKER_BOX",
 				"BLUE_SHULKER_BOX",
 				"BROWN_SHULKER_BOX",
 				"CYAN_SHULKER_BOX",
 				"GRAY_SHULKER_BOX",
 				"GREEN_SHULKER_BOX",
 				"LIGHT_BLUE_SHULKER_BOX",
 				"LIGHT_GRAY_SHULKER_BOX",
 				"LIME_SHULKER_BOX",
 				"MAGENTA_SHULKER_BOX",
 				"ORANGE_SHULKER_BOX",
 				"PINK_SHULKER_BOX",
 				"PURPLE_SHULKER_BOX",
 				"RED_SHULKER_BOX",
 				"SHULKER_BOX",
 				"HONEY_BOTTLE",
 				"WHITE_SHULKER_BOX",
 				"YELLOW_SHULKER_BOX",
 				"MAGMA_CREAM",
 				"END_ROD",
 				"SPECTRAL_ARROW",
 				"STRING",
 				"PACKED_ICE",
 				"BLUE_ICE",
 				"BAKED_POTATO",
 				"BLAST_FURNACE",
 				"BLAZE_POWDER",
 				"BLAZE_ROD",
 				"BOW",
 				"BOWL",
 				"BREAD",
 				"BREWING_STAND",
 				"BRICK",
 				"BRICK_SLAB",
 				"BRICK_STAIRS",
 				"BRICK_WALL",
 				"BRICKS",
 				"BUCKET",
 				"CAKE",
 				"CAMPFIRE",
 				"CARROT_ON_A_STICK",
 				"CARTOGRAPHY_TABLE",
 				"CARVED_PUMPKIN",
 				"CAULDRON",
 				"CHARCOAL",
 				"CHEST",
 				"CHEST_MINECART",
 				"CHISELED_RED_SANDSTONE",
 				"CHISELED_SANDSTONE",
 				"CHISELED_STONE_BRICKS",
 				"CLAY",
 				"CLOCK",
 				"COARSE_DIRT",
 				"COBBLESTONE_SLAB",
 				"COBBLESTONE_STAIRS",
 				"COBBLESTONE_WALL",
 				"COMPARATOR",
 				"COMPASS",
 				"COMPOSTER",
 				"CONDUIT",
 				"COOKED_BEEF",
 				"COOKED_CHICKEN",
 				"COOKED_MUTTON",
 				"COOKED_PORKCHOP",
 				"COOKED_RABBIT",
 				"COOKIE",
 				"CRACKED_STONE_BRICKS",
 				"CRAFTING_TABLE",
 				"CROSSBOW",
 				"CUT_RED_SANDSTONE",
 				"CUT_RED_SANDSTONE_SLAB",
 				"CUT_SANDSTONE",
 				"CUT_SANDSTONE_SLAB",
 				"DARK_PRISMARINE",
 				"DARK_PRISMARINE_SLAB",
 				"DARK_PRISMARINE_STAIRS",
 				"DAYLIGHT_DETECTOR",
 				"DETECTOR_RAIL",
 				"DIAMOND_BOOTS",
 				"DIAMOND_CHESTPLATE",
 				"DIAMOND_HELMET",
 				"DIAMOND_LEGGINGS",
 				"DIORITE_SLAB",
 				"DIORITE_STAIRS",
 				"DIORITE_WALL",
 				"DISPENSER",
 				"DRIED_KELP",
 				"DROPPER",
 				"ENCHANTING_TABLE",
 				"END_STONE_BRICK_SLAB",
 				"END_STONE_BRICK_STAIRS",
 				"END_STONE_BRICK_WALL",
 				"END_STONE_BRICKS",
 				"ENDER_CHEST",
 				"ENDER_EYE",
 				"ENDER_PEARL",
 				"FERMENTED_SPIDER_EYE",
 				"FILLED_MAP",
 				"FIRE_CHARGE",
 				"FISHING_ROD",
 				"FLETCHING_TABLE",
 				"FLINT_AND_STEEL",
 				"FLOWER_POT",
 				"FURNACE",
 				"FURNACE_MINECART",
 				"GLASS",
 				"GLASS_BOTTLE",
 				"GLASS_PANE",
 				"GLISTERING_MELON_SLICE",
 				"GLOWSTONE",
 				"GOLD_INGOT",
 				"GOLDEN_APPLE",
 				"GOLDEN_BOOTS",
 				"GOLDEN_CARROT",
 				"GOLDEN_CHESTPLATE",
 				"GOLDEN_HELMET",
 				"GOLDEN_LEGGINGS",
 				"GRANITE_SLAB",
 				"GRANITE_STAIRS",
 				"GRANITE_WALL",
 				"GRINDSTONE",
 				"HEAVY_WEIGHTED_PRESSURE_PLATE",
 				"HOPPER",
 				"HOPPER_MINECART",
 				"IRON_BARS",
 				"IRON_BOOTS",
 				"IRON_CHESTPLATE",
 				"IRON_DOOR",
 				"IRON_HELMET",
 				"IRON_INGOT",
 				"IRON_LEGGINGS",
 				"IRON_TRAPDOOR",
 				"ITEM_FRAME",
 				"JACK_O_LANTERN",
 				"JUKEBOX",
 				"LADDER",
 				"LANTERN",
 				"LEAD",
 				"LEATHER_BOOTS",
 				"LEATHER_CHESTPLATE",
 				"LEATHER_HELMET",
 				"LEATHER_HORSE_ARMOR",
 				"LEATHER_LEGGINGS",
 				"LECTERN",
 				"LEVER",
 				"LIGHT_WEIGHTED_PRESSURE_PLATE",
 				"LOOM",
 				"MAP",
 				"MINECART",
 				"MOSSY_COBBLESTONE",
 				"MOSSY_COBBLESTONE_SLAB",
 				"MOSSY_COBBLESTONE_STAIRS",
 				"MOSSY_COBBLESTONE_WALL",
 				"MOSSY_STONE_BRICK_SLAB",
 				"MOSSY_STONE_BRICK_STAIRS",
 				"MOSSY_STONE_BRICK_WALL",
 				"MOSSY_STONE_BRICKS",
 				"MUSHROOM_STEW",
 				"NETHER_BRICK",
 				"NETHER_BRICK_FENCE",
 				"NETHER_BRICK_SLAB",
 				"NETHER_BRICK_STAIRS",
 				"NETHER_BRICK_WALL",
 				"NETHER_BRICKS",
 				"NETHER_STAR",
 				"OBSERVER",
 				"PAINTING",
 				"PAPER",
 				"PISTON",
 				"POLISHED_ANDESITE",
 				"POLISHED_ANDESITE_SLAB",
 				"POLISHED_ANDESITE_STAIRS",
 				"POLISHED_DIORITE",
 				"POLISHED_DIORITE_SLAB",
 				"POLISHED_DIORITE_STAIRS",
 				"POLISHED_GRANITE",
 				"POLISHED_GRANITE_SLAB",
 				"POLISHED_GRANITE_STAIRS",
 				"POPPED_CHORUS_FRUIT",
 				"POWERED_RAIL",
 				"PRISMARINE",
 				"PRISMARINE_BRICK_SLAB",
 				"PRISMARINE_BRICK_STAIRS",
 				"PRISMARINE_BRICKS",
 				"PRISMARINE_SLAB",
 				"PRISMARINE_STAIRS",
 				"PRISMARINE_WALL",
 				"PUMPKIN_PIE",
 				"PURPUR_PILLAR",
 				"PURPUR_SLAB",
 				"PURPUR_STAIRS",
 				"QUARTZ_PILLAR",
 				"QUARTZ_SLAB",
 				"QUARTZ_STAIRS",
 				"RABBIT_STEW",
 				"RAIL",
 				"RED_NETHER_BRICK_SLAB",
 				"RED_NETHER_BRICK_STAIRS",
 				"RED_NETHER_BRICK_WALL",
 				"RED_NETHER_BRICKS",
 				"RED_SANDSTONE",
 				"RED_SANDSTONE_SLAB",
 				"RED_SANDSTONE_STAIRS",
 				"RED_SANDSTONE_WALL",
 				"REDSTONE_LAMP",
 				"REDSTONE_TORCH",
 				"REPEATER",
 				"SANDSTONE",
 				"SANDSTONE_SLAB",
 				"SANDSTONE_STAIRS",
 				"SANDSTONE_WALL",
 				"SCAFFOLDING",
 				"SEA_LANTERN",
 				"SHEARS",
 				"SHIELD",
 				"SMITHING_TABLE",
 				"SMOKER",
 				"SMOOTH_QUARTZ",
 				"SMOOTH_QUARTZ_SLAB",
 				"SMOOTH_QUARTZ_STAIRS",
 				"SMOOTH_RED_SANDSTONE",
 				"SMOOTH_RED_SANDSTONE_SLAB",
 				"SMOOTH_RED_SANDSTONE_STAIRS",
 				"SMOOTH_SANDSTONE",
 				"SMOOTH_SANDSTONE_SLAB",
 				"SMOOTH_SANDSTONE_STAIRS",
 				"SMOOTH_STONE",
 				"SMOOTH_STONE_SLAB",
 				"SNOW",
 				"STICK",
 				"STICKY_PISTON",
 				"STONE",
 				"STONE_BRICK_SLAB",
 				"STONE_BRICK_STAIRS",
 				"STONE_BRICK_WALL",
 				"STONE_BRICKS",
 				"STONE_BUTTON",
 				"STONE_PRESSURE_PLATE",
 				"STONE_SLAB",
 				"STONE_STAIRS",
 				"STONECUTTER",
 				"SUGAR",
 				"TNT",
 				"TNT_MINECART",
 				"TORCH",
 				"TRAPPED_CHEST",
 				"TRIPWIRE_HOOK",
 				"TURTLE_HELMET",
 				"WRITABLE_BOOK",
 				"WRITTEN_BOOK",
 				"COOKED_COD",
 				"COOKED_SALMON",
 				"BLACK_DYE",
 				"BLUE_DYE",
 				"BROWN_DYE",
 				"CYAN_DYE",
 				"GRAY_DYE",
 				"GREEN_DYE",
 				"LIGHT_BLUE_DYE",
 				"LIGHT_GRAY_DYE",
 				"LIME_DYE",
 				"MAGENTA_DYE",
 				"ORANGE_DYE",
 				"PINK_DYE",
 				"PURPLE_DYE",
 				"RED_DYE",
 				"WHITE_DYE",
 				"YELLOW_DYE"
	 	};
		String[] D_oreList_Keyset_A = {
			"COAL_ORE",
			"DIAMOND_ORE",
			"EMERALD_ORE",
			"GOLD_ORE",
			"IRON_ORE",
			"LAPIS_ORE",
			"NETHER_QUARTZ_ORE",
			"REDSTONE_ORE"
		};
		String[] D_item_blockList_Keyset_A = {
			"BONE_BLOCK",
			"COAL_BLOCK",
			"DIAMOND_BLOCK",
			"DRIED_KELP_BLOCK",
			"EMERALD_BLOCK",
			"GOLD_BLOCK",
			"HAY_BLOCK",
			"HONEY_BLOCK",
			"HONEYCOMB_BLOCK",
			"IRON_BLOCK",
			"LAPIS_BLOCK",
			"MAGMA_BLOCK",
			"NETHER_WART_BLOCK",
			"PURPUR_BLOCK",
			"QUARTZ_BLOCK",
			"REDSTONE_BLOCK",
			"MELON",
			"SLIME_BLOCK",
			"SNOW_BLOCK"
		};
		String[] D_no_recipeList_Keyset_A = {
			"BLACK_CONCRETE",
			"BLUE_CONCRETE",
			"BROWN_CONCRETE",
			"CYAN_CONCRETE",
			"GRAY_CONCRETE",
			"GREEN_CONCRETE",
			"LIGHT_BLUE_CONCRETE",
			"LIGHT_GRAY_CONCRETE",
			"LIME_CONCRETE",
			"MAGENTA_CONCRETE",
			"ORANGE_CONCRETE",
			"PINK_CONCRETE",
			"PURPLE_CONCRETE",
			"RED_CONCRETE",
			"WHITE_CONCRETE",
			"YELLOW_CONCRETE"
		};
		String[] D_not_craftableList_Keyset_A = {
			"WHITE_WOOL",
			"BELL",
			"PODZOL",
			"LARGE_FERN",
			"FERN",
			"MYCELIUM",
			"MUSHROOM_STEM",
			"BROWN_MUSHROOM_BLOCK",
			"RED_MUSHROOM_BLOCK",
			"DEAD_BUSH",
			"BRAIN_CORAL",
			"BRAIN_CORAL_FAN",
			"BRAIN_CORAL_WALL_FAN",
			"BUBBLE_CORAL",
			"BUBBLE_CORAL_FAN",
			"DEAD_BRAIN_CORAL",
			"DEAD_BRAIN_CORAL_FAN",
			"DEAD_BUBBLE_CORAL",
			"DEAD_BUBBLE_CORAL_FAN",
			"DEAD_FIRE_CORAL",
			"DEAD_FIRE_CORAL_FAN",
			"DEAD_HORN_CORAL",
			"DEAD_HORN_CORAL_FAN",
			"DEAD_TUBE_CORAL",
			"DEAD_TUBE_CORAL_FAN",
			"BRAIN_CORAL_BLOCK",
			"BUBBLE_CORAL_BLOCK",
			"DEAD_BRAIN_CORAL_BLOCK",
			"DEAD_BUBBLE_CORAL_BLOCK",
			"DEAD_FIRE_CORAL_BLOCK",
			"DEAD_HORN_CORAL_BLOCK",
			"DEAD_TUBE_CORAL_BLOCK",
			"FIRE_CORAL_BLOCK",
			"HORN_CORAL_BLOCK",
			"TUBE_CORAL_BLOCK",
			"BUBBLE_CORAL_WALL_FAN",
			"DEAD_BRAIN_CORAL_WALL_FAN",
			"DEAD_BUBBLE_CORAL_WALL_FAN",
			"DEAD_FIRE_CORAL_WALL_FAN",
			"DEAD_HORN_CORAL_WALL_FAN",
			"DEAD_TUBE_CORAL_WALL_FAN",
			"FIRE_CORAL_WALL_FAN",
			"HORN_CORAL_WALL_FAN",
			"TUBE_CORAL_WALL_FAN",
			"FIRE_CORAL",
			"FIRE_CORAL_FAN",
			"HORN_CORAL",
			"HORN_CORAL_FAN",
			"TUBE_CORAL",
			"TUBE_CORAL_FAN",
			"SADDLE",
			"GRASS_BLOCK",
			"MUSIC_DISC_11",
			"MUSIC_DISC_13",
			"MUSIC_DISC_BLOCKS",
			"MUSIC_DISC_CAT",
			"MUSIC_DISC_CHIRP",
			"MUSIC_DISC_FAR",
			"MUSIC_DISC_MALL",
			"MUSIC_DISC_MELLOHI",
			"MUSIC_DISC_STAL",
			"MUSIC_DISC_STRAD",
			"MUSIC_DISC_WAIT",
			"MUSIC_DISC_WARD",
			"CHAINMAIL_BOOTS",
			"CHAINMAIL_CHESTPLATE",
			"CHAINMAIL_HELMET",
			"CHAINMAIL_LEGGINGS",
			"TRIDENT",
			"TOTEM_OF_UNDYING",
			"GRASS",
			"TALL_GRASS",
			"TALL_SEAGRASS",
			"SEA_PICKLE",
			"SEAGRASS",
			"SPONGE",
			"SPAWNER",
			"LILY_PAD",
			"COBWEB",
			"ELYTRA",
			"ENCHANTED_GOLDEN_APPLE",
			"TROPICAL_FISH",
			"WET_SPONGE",
			"NAME_TAG",
			"IRON_HORSE_ARMOR",
			"GOLDEN_HORSE_ARMOR",
			"DIAMOND_HORSE_ARMOR",
			"EXPERIENCE_BOTTLE"
		};
		String[] D_mob_eggsList_Keyset_A = {
			"BEE_SPAWN_EGG",
			"BAT_SPAWN_EGG",
			"BLAZE_SPAWN_EGG",
			"CAVE_SPIDER_SPAWN_EGG",
			"CAT_SPAWN_EGG",
			"CHICKEN_SPAWN_EGG",
			"COD_SPAWN_EGG",
			"COW_SPAWN_EGG",
			"CREEPER_SPAWN_EGG",
			"DOLPHIN_SPAWN_EGG",
			"DONKEY_SPAWN_EGG",
			"DRAGON_EGG",
			"DROWNED_SPAWN_EGG",
			"ELDER_GUARDIAN_SPAWN_EGG",
			"ENDERMAN_SPAWN_EGG",
			"ENDERMITE_SPAWN_EGG",
			"EVOKER_SPAWN_EGG",
			"FOX_SPAWN_EGG",
			"GHAST_SPAWN_EGG",
			"GUARDIAN_SPAWN_EGG",
			"HORSE_SPAWN_EGG",
			"HUSK_SPAWN_EGG",
			"LLAMA_SPAWN_EGG",
			"MAGMA_CUBE_SPAWN_EGG",
			"MOOSHROOM_SPAWN_EGG",
			"MULE_SPAWN_EGG",
			"OCELOT_SPAWN_EGG",
			"PANDA_SPAWN_EGG",
			"PARROT_SPAWN_EGG",
			"PHANTOM_SPAWN_EGG",
			"PIG_SPAWN_EGG",
			"PILLAGER_SPAWN_EGG",
			"POLAR_BEAR_SPAWN_EGG",
			"PUFFERFISH_SPAWN_EGG",
			"RABBIT_SPAWN_EGG",
			"RAVAGER_SPAWN_EGG",
			"SALMON_SPAWN_EGG",
			"SHEEP_SPAWN_EGG",
			"SHULKER_SPAWN_EGG",
			"SILVERFISH_SPAWN_EGG",
			"SKELETON_HORSE_SPAWN_EGG",
			"SKELETON_SPAWN_EGG",
			"SLIME_SPAWN_EGG",
			"SPIDER_SPAWN_EGG",
			"SQUID_SPAWN_EGG",
			"STRAY_SPAWN_EGG",
			"TRADER_LLAMA_SPAWN_EGG",
			"TROPICAL_FISH_SPAWN_EGG",
			"TURTLE_EGG",
			"TURTLE_SPAWN_EGG",
			"VEX_SPAWN_EGG",
			"VILLAGER_SPAWN_EGG",
			"VINDICATOR_SPAWN_EGG",
			"WANDERING_TRADER_SPAWN_EGG",
			"WITCH_SPAWN_EGG",
			"WITHER_SKELETON_SPAWN_EGG",
			"WOLF_SPAWN_EGG",
			"ZOMBIE_HORSE_SPAWN_EGG",
			"ZOMBIE_PIGMAN_SPAWN_EGG",
			"ZOMBIE_SPAWN_EGG",
			"ZOMBIE_VILLAGER_SPAWN_EGG"
		};
		String[] D_effectList_Keyset_A = {
			"LINGERING_POTION",
			"POTION",
			"SPLASH_POTION",
			"TIPPED_ARROW",
			"ENCHANTED_BOOK"
		};
		String[] D_unobtainableList_Keyset_A = {

			"AIR",
			"CAVE_AIR",
			"VOID_AIR",
			"ATTACHED_MELON_STEM",
			"ATTACHED_PUMPKIN_STEM",
			"MELON_STEM",
			"PUMPKIN_STEM",
			"BEDROCK",
			"BARRIER",
			"BAMBOO_SAPLING",
			"BEE_NEST",
			"BEETROOTS",
			"CARROTS",
			"POTATOES",
			"COCOA",
			"KELP_PLANT",
			"BLACK_WALL_BANNER",
			"BLUE_WALL_BANNER",
			"BROWN_WALL_BANNER",
			"CYAN_WALL_BANNER",
			"GRAY_WALL_BANNER",
			"GREEN_WALL_BANNER",
			"LIGHT_BLUE_WALL_BANNER",
			"LIGHT_GRAY_WALL_BANNER",
			"LIME_WALL_BANNER",
			"MAGENTA_WALL_BANNER",
			"ORANGE_WALL_BANNER",
			"PINK_WALL_BANNER",
			"PURPLE_WALL_BANNER",
			"RED_WALL_BANNER",
			"WHITE_WALL_BANNER",
			"YELLOW_WALL_BANNER",
			"SWEET_BERRY_BUSH",
			"POTTED_ALLIUM",
			"POTTED_AZURE_BLUET",
			"POTTED_BAMBOO",
			"POTTED_BLUE_ORCHID",
			"POTTED_BROWN_MUSHROOM",
			"POTTED_CACTUS",
			"POTTED_CORNFLOWER",
			"POTTED_DANDELION",
			"POTTED_DEAD_BUSH",
			"POTTED_FERN",
			"POTTED_LILY_OF_THE_VALLEY",
			"POTTED_ORANGE_TULIP",
			"POTTED_OXEYE_DAISY",
			"POTTED_PINK_TULIP",
			"POTTED_POPPY",
			"POTTED_RED_MUSHROOM",
			"POTTED_RED_TULIP",
			"POTTED_WHITE_TULIP",
			"POTTED_WITHER_ROSE",
			"POTTED_ACACIA_SAPLING",
			"POTTED_BIRCH_SAPLING",
			"POTTED_DARK_OAK_SAPLING",
			"POTTED_JUNGLE_SAPLING",
			"POTTED_OAK_SAPLING",
			"POTTED_SPRUCE_SAPLING",
			"PISTON_HEAD",
			"CHIPPED_ANVIL",
			"DAMAGED_ANVIL",
			"WATER",
			"LAVA",
			"CREEPER_WALL_HEAD",
			"DRAGON_WALL_HEAD",
			"PLAYER_WALL_HEAD",
			"ZOMBIE_WALL_HEAD",
			"KNOWLEDGE_BOOK",
			"GRASS_PATH",
			"FARMLAND",
			"END_CRYSTAL",
			"END_GATEWAY",
			"END_PORTAL",
			"END_PORTAL_FRAME",
			"NETHER_PORTAL",
			"BUBBLE_COLUMN",
			"MOVING_PISTON",
			"INFESTED_CHISELED_STONE_BRICKS",
			"INFESTED_COBBLESTONE",
			"INFESTED_CRACKED_STONE_BRICKS",
			"INFESTED_MOSSY_STONE_BRICKS",
			"INFESTED_STONE",
			"INFESTED_STONE_BRICKS",
			"SKELETON_WALL_SKULL",
			"WITHER_SKELETON_WALL_SKULL",
			"REDSTONE_WALL_TORCH",
			"WALL_TORCH",
			"REDSTONE_WIRE",
			"FIRE",
			"TRIPWIRE",
			"FROSTED_ICE",
			"FIREWORK_ROCKET",
			"FIREWORK_STAR",
			"CHORUS_PLANT"

		};
		String[] D_adminList_Keyset_A = {
				"CHAIN_COMMAND_BLOCK",
				"COMMAND_BLOCK",
				"COMMAND_BLOCK_MINECART",
				"REPEATING_COMMAND_BLOCK",
				"STRUCTURE_BLOCK",
				"STRUCTURE_VOID",
				"JIGSAW"
		};

}


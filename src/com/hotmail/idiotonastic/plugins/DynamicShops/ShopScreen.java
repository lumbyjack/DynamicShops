package com.hotmail.idiotonastic.plugins.DynamicShops;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class ShopScreen implements Listener {

	private static Main plugin = Main.getPlugin(Main.class);
	private static Economy econ = plugin.getEconomy();
	private static String menuTitle = ChatColor.GOLD + "Shop Menu";
	private static String ammountTitle = ChatColor.GOLD + "Ammount";
	private static String itemTitle = ChatColor.GOLD + "Item Slection";
	private static String prevPage = ChatColor.YELLOW + "Previous Page";
	private static String nextPage = ChatColor.YELLOW + "next Page";
	private static String backTitle = ChatColor.YELLOW + "Back to Menu";
	private static String sellTitle = ChatColor.YELLOW + "Sell";
	private static String sellConfirm = ChatColor.GREEN + "Sell";
	private static RoundingMode round = RoundingMode.UP;
	/*
	 * Building blocks
	 * Decoration blocks
	 * Redstone
	 * Transportation
	 * Miscellaneous
	 * Foodstuffs
	 * Tools
	 * Combat
	 * Brewing
	 */

	//TODO add potions


	public void menuScreen(Player player) {

		Inventory i = plugin.getServer().createInventory(null, 9, menuTitle);
		ItemStack Building_blocks = new ItemStack(Material.BRICKS, 1);
		ItemMeta meta = Building_blocks.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Building blocks");
		Building_blocks.setItemMeta(meta);
		ItemStack Decoration_blocks = new ItemStack(Material.PEONY, 1);
		meta = Decoration_blocks.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Decoration blocks");
		Decoration_blocks.setItemMeta(meta);
		ItemStack Redstone = new ItemStack(Material.REDSTONE_TORCH, 1);
		meta = Redstone.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "RedStone");
		Redstone.setItemMeta(meta);
		ItemStack Transportation = new ItemStack(Material.POWERED_RAIL, 1);
		meta = Transportation.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Transportation");
		Transportation.setItemMeta(meta);
		ItemStack Miscellaneous = new ItemStack(Material.LAVA_BUCKET, 1);
		meta = Miscellaneous.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Miscellaneous");
		Miscellaneous.setItemMeta(meta);
		ItemStack Foodstuffs = new ItemStack(Material.APPLE, 1);
		meta = Foodstuffs.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Foodstuffs");
		Foodstuffs.setItemMeta(meta);
		ItemStack Tools = new ItemStack(Material.IRON_AXE, 1);
		meta = Tools.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Tools");
		Tools.setItemMeta(meta);
		ItemStack Combat = new ItemStack(Material.GOLDEN_SWORD, 1);
		meta = Combat.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Combat");
		Combat.setItemMeta(meta);
		ItemStack Brewing = new ItemStack(Material.POTION, 1);
		meta = Brewing.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Brewing");
		Brewing.setItemMeta(meta);


		i.setItem(0, Building_blocks);
		i.setItem(1, Decoration_blocks);
		i.setItem(2, Redstone);
		i.setItem(3, Transportation);
		i.setItem(4, Miscellaneous);
		i.setItem(5, Foodstuffs);
		i.setItem(6, Tools);
		i.setItem(7, Combat);
		i.setItem(8, Brewing);

		player.openInventory(i);

	}
	public void sellScreen(Player player){
		
		Inventory i = plugin.getServer().createInventory(null, (54), sellTitle);
		player.openInventory(i);
	}
	public void itemScreen(Player player, int screenNo) {
		itemScreen(player, screenNo, 1);
	}
	public void itemScreen(Player player, int screenNo, int pageNo) {
		String items[] = getScreen(screenNo);
		Inventory i = plugin.getServer().createInventory(null, (54), itemTitle);
		int y = 0;
		y = ((pageNo-1) * 45);
		if ( y != 0){
			y +=1;
		}
		for (int x = 0; x < 45; x++){
			if ((y) < items.length ){
				
				if(plugin.getConfig().contains(items[y].toUpperCase()))//is in config
				{
					if(plugin.getConfig().getInt(items[y].toUpperCase()) != -1)//is not -1 price
					{   
						//BigDecimal price = Shop.getprice(Material.getMaterial(items[y].toUpperCase()));
						BigDecimal price = new BigDecimal(plugin.getConfig().getDouble(items[y].toUpperCase()));
						if (price.intValue()>0){
							price.setScale(4, round);
						} else {
							price.setScale(4);
						}
						
						ItemStack item = new ItemStack(Material.getMaterial(items[y].toUpperCase()), 1);
						ItemMeta iM = item.getItemMeta();
						try {
						iM.setLore(Arrays.asList("Price: " + econ.format(price.doubleValue())));
						item.setItemMeta(iM);
						} catch (NullPointerException e){
							e.printStackTrace();
							System.out.println("incorrect name: " + items[y] + " " + price);
						}
						i.setItem(x, item);
					} else {
						x -= 1;
					}
				}
				

			}
			y++;
		}
		ItemStack page = new ItemStack(Material.PRISMARINE_SHARD, 1);
		ItemMeta pageM = page.getItemMeta();
		pageM.setLore(Arrays.asList(ChatColor.MAGIC+items[0].toString()));
		if (pageNo > 1 && pageNo-1 != 0){
			pageM.setDisplayName(prevPage);
			page.setItemMeta(pageM);
			page.setAmount(pageNo-1);
			i.setItem(46, page);
		}
		if((y+1) < items.length){
			pageM.setDisplayName(nextPage);
			page.setItemMeta(pageM);
			page.setAmount(pageNo+1);
			i.setItem(52, page);
		}
		ItemStack back = new ItemStack(Material.DARK_OAK_DOOR, 1);
		ItemMeta bM = back.getItemMeta();
		bM.setDisplayName(backTitle);
		back.setItemMeta(bM);
		i.setItem(53, back);
		
		player.openInventory(i);

	}
	public void amountScreen(Player player, Material mat) {

		Inventory i = plugin.getServer().createInventory(null, 9, ammountTitle);
		ItemStack item = new ItemStack(mat, 1);
		ItemStack back = new ItemStack(Material.DARK_OAK_DOOR, 1);
		ItemMeta iM = item.getItemMeta();
		ItemMeta bM = back.getItemMeta();
		bM.setDisplayName(backTitle);
		back.setItemMeta(bM);
		BigDecimal price = Shop.getprice(mat);
		if (price.intValue()>0){
			price.setScale(4, round);
		} else {
			price.setScale(4);
		}
		
		iM.setLore(Arrays.asList("Price: " + econ.format(price.doubleValue())));
		item.setItemMeta(iM);
		i.setItem(1, item);
		item.setAmount(4);
		iM.setLore(Arrays.asList("Price: " + econ.format(price.doubleValue()*4)));
		item.setItemMeta(iM);
		i.setItem(2, item);
		item.setAmount(8);
		iM.setLore(Arrays.asList("Price: " + econ.format(price.doubleValue()*8)));
		item.setItemMeta(iM);
		i.setItem(3, item);
		item.setAmount(16);
		iM.setLore(Arrays.asList("Price: " + econ.format(price.doubleValue()*16)));
		item.setItemMeta(iM);
		i.setItem(4, item);
		item.setAmount(32);
		iM.setLore(Arrays.asList("Price: " + econ.format(price.doubleValue()*32)));
		item.setItemMeta(iM);
		i.setItem(5, item);
		item.setAmount(64);
		iM.setLore(Arrays.asList("Price: " + econ.format(price.doubleValue()*64)));
		item.setItemMeta(iM);
		i.setItem(6, item);

		i.setItem(8, back);
		player.openInventory(i);

	}
	public static void processBuy (Player player, ItemStack items) {

	}

	private static String[] getScreen(int screenNo) {
		switch (screenNo){
		case 0:
			return buildingBlocks;
		case 1:
			return decorationBlocks;
		case 2:
			return redstone;
		case 3:
			return transportation;
		case 4:
			return miscellaneous;
		case 5:
			return eggs;
		case 6:
			return foodstuffs;
		case 7:
			return tools;
		case 8:
			return combat;
		default:
			return brewing;
		}
	}

	public static int getScreenNo(Material m){
		if (m.name().equalsIgnoreCase(Material.REDSTONE_TORCH.name())){
			return 2;
		}
		if (m.name().equalsIgnoreCase(Material.POTION.name())){
			return 9;
		}
		for (int x = 0; x < 9; x++){
			List<String> list = Arrays.asList(getScreen(x));
			if (list.stream().anyMatch(m.name().toString()::equalsIgnoreCase)){
				return x;
			}
		}
		return 0;
	}

	public static String getMenuTitle(){
		return menuTitle;
	}
	public static String getAmmountTitle(){
		return ammountTitle;
	}
	public static String getItemTitle(){
		return itemTitle;
	}
	public static String getNextPageTitle(){
		return nextPage;
	}
	public static String getPrevPageTitle(){
		return prevPage;
	}
	public static String getBackTitle() {
		return backTitle;
	}
	public static String getSellTitle() {
		return sellTitle;
	}
	public static String getSellConfirm() {
		return sellConfirm;
	}
	private static String[] buildingBlocks = {
	    "Stone",
	    "Granite",
	    "Polished_Granite",
	    "Diorite",
	    "Polished_Diorite",
	    "Andesite",
	    "Polished_Andesite",
	    "Grass_Block",
	    "Dirt",
	    "Coarse_Dirt",
	    "Podzol",
	    "Cobblestone",
	    "Oak_Planks",
	    "Spruce_Planks",
	    "Birch_Planks",
	    "Jungle_Planks",
	    "Acacia_Planks",
	    "Dark_Oak_Planks",
	    "Bedrock",
	    "Sand",
	    "Red_Sand",
	    "Gravel",
	    "Gold_Ore",
	    "Iron_Ore",
	    "Coal_Ore",
	    "Oak_Log",
	    "Spruce_Log",
	    "Birch_Log",
	    "Jungle_Log",
	    "Acacia_Log",
	    "Dark_Oak_Log",
	    "Stripped_Oak_Log",
	    "Stripped_Spruce_Log",
	    "Stripped_Birch_Log",
	    "Stripped_Jungle_Log",
	    "Stripped_Acacia_Log",
	    "Stripped_Dark_Oak_Log",
	    "Oak_Wood",
	    "Spruce_Wood",
	    "Birch_Wood",
	    "Jungle_Wood",
	    "Acacia_Wood",
	    "Dark_Oak_Wood",
	    "Stripped_Oak_Wood",
	    "Stripped_Spruce_Wood",
	    "Stripped_Birch_Wood",
	    "Stripped_Jungle_Wood",
	    "Stripped_Acacia_Wood",
	    "Stripped_Dark_Oak_Wood",
	    "Sponge",
	    "Wet_Sponge",
	    "Glass",
	    "Lapis_Ore",
	    "Lapis_Block",
	    "Sandstone",
	    "Chiseled_Sandstone",
	    "Cut_Sandstone",
	    "Smooth_Sandstone",
	    "Red_Sandstone",
	    "Chiseled_Red_Sandstone",
	    "Cut_Red_Sandstone",
	    "Smooth_Red_Sandstone",
	    "White_Wool",
	    "Orange_Wool",
	    "Magenta_Wool",
	    "Light_Blue_Wool",
	    "Yellow_Wool",
	    "Lime_Wool",
	    "Pink_Wool",
	    "Gray_Wool",
	    "Light_Gray_Wool",
	    "Cyan_Wool",
	    "Purple_Wool",
	    "Blue_Wool",
	    "Brown_Wool",
	    "Green_Wool",
	    "Red_Wool",
	    "Black_Wool",
	    "Gold_Block",
	    "Iron_Block",
	    "Oak_Slab",
	    "Spruce_Slab",
	    "Birch_Slab",
	    "Jungle_Slab",
	    "Acacia_Slab",
	    "Dark_Oak_Slab",
	    "Stone_Slab",
	    "Smooth_Stone_Slab",
	    "Cobblestone_Slab",
	    "Mossy_Cobblestone_Slab",
	    "Stone_Brick_Slab",
	    "Mossy_Stone_Brick_Slab",
	    "Andesite_Slab",
	    "Polished_Andesite_Slab",
	    "Diorite_Slab",
	    "Polished_Diorite_Slab",
	    "Granite_Slab",
	    "Polished_Granite_Slab",
	    "Sandstone_Slab",
	    "Cut_Sandstone_Slab",
	    "Smooth_Sandstone_Slab",
	    "Red_Sandstone_Slab",
	    "Cut_Red_Sandstone_Slab",
	    "Smooth_Red_Sandstone_Slab",
	    "Brick_Slab",
	    "Prismarine_Slab",
	    "Prismarine_Brick_Slab",
	    "Dark_Prismarine_Slab",
	    "Nether_Brick_Slab",
	    "Red_Nether_Brick_Slab",
	    "Quartz_Slab",
	    "Smooth_Quartz_Slab",
	    "Purpur_Slab",
	    "End_Stone_Brick_Slab",
	    "Petrified_Oak_Slab",
	    "Bricks",
	    "Bookshelf",
	    "Mossy_Cobblestone",
	    "Obsidian",
	    "Purpur_Block",
	    "Purpur_Pillar",
	    "Oak_Stairs",
	    "Spruce_Stairs",
	    "Birch_Stairs",
	    "Jungle_Stairs",
	    "Acacia_Stairs",
	    "Dark_Oak_Stairs",
	    "Stone_Stairs",
	    "Cobblestone_Stairs",
	    "Mossy_Cobblestone_Stairs",
	    "Stone_Brick_Stairs",
	    "Mossy_Stone_Brick_Stairs",
	    "Andesite_Stairs",
	    "Polished_Andesite_Stairs",
	    "Diorite_Stairs",
	    "Polished_Diorite_Stairs",
	    "Granite_Stairs",
	    "Polished_Granite_Stairs",
	    "Sandstone_Stairs",
	    "Smooth_Sandstone_Stairs",
	    "Red_Sandstone_Stairs",
	    "Smooth_Red_Sandstone_Stairs",
	    "Brick_Stairs",
	    "Prismarine_Stairs",
	    "Prismarine_Brick_Stairs",
	    "Dark_Prismarine_Stairs",
	    "Nether_Brick_Stairs",
	    "Red_Nether_Brick_Stairs",
	    "Quartz_Stairs",
	    "Smooth_Quartz_Stairs",
	    "Purpur_Stairs",
	    "End_Stone_Brick_Stairs",
	    "Diamond_Ore",
	    "Diamond_Block",
	    "Redstone_Ore",
	    "Ice",
	    "Snow_Block",
	    "Clay",
	    "Pumpkin",
	    "Carved_Pumpkin",
	    "Netherrack",
	    "Soul_Sand",
	    "Glowstone",
	    "Jack_o_Lantern",
	    "Stone_Bricks",
	    "Cracked_Stone_Bricks",
	    "Mossy_Stone_Bricks",
	    "Chiseled_Stone_Bricks",
	    "Melon",
	    "Mycelium",
	    "Nether_Bricks",
	    "End_Stone",
	    "End_Stone_Bricks",
	    "Emerald_Ore",
	    "Emerald_Block",
	    "Nether_Quartz_Ore",
	    "Quartz_Block",
	    "Chiseled_Quartz_Block",
	    "Quartz_Pillar",
	    "Smooth_Quartz",
	    "Terracotta",
	    "White_Terracotta",
	    "Orange_Terracotta",
	    "Magenta_Terracotta",
	    "Light_Blue_Terracotta",
	    "Yellow_Terracotta",
	    "Lime_Terracotta",
	    "Pink_Terracotta",
	    "Gray_Terracotta",
	    "Light_Gray_Terracotta",
	    "Cyan_Terracotta",
	    "Purple_Terracotta",
	    "Blue_Terracotta",
	    "Brown_Terracotta",
	    "Green_Terracotta",
	    "Red_Terracotta",
	    "Black_Terracotta",
	    "Hay_Block",
	    "Terracotta",
	    "Coal_Block",
	    "Packed_Ice",
	    "Glass",
	    "White_Stained_Glass",
	    "Orange_Stained_Glass",
	    "Magenta_Stained_Glass",
	    "Light_Blue_Stained_Glass",
	    "Yellow_Stained_Glass",
	    "Lime_Stained_Glass",
	    "Pink_Stained_Glass",
	    "Gray_Stained_Glass",
	    "Light_Gray_Stained_Glass",
	    "Cyan_Stained_Glass",
	    "Purple_Stained_Glass",
	    "Blue_Stained_Glass",
	    "Brown_Stained_Glass",
	    "Green_Stained_Glass",
	    "Red_Stained_Glass",
	    "Black_Stained_Glass",
	    "Prismarine",
	    "Prismarine_Bricks",
	    "Dark_Prismarine",
	    "Sea_Lantern",
	    "Magma_Block",
	    "Nether_Wart_Block",
	    "Red_Nether_Bricks",
	    "Bone_Block",
	    "White_Concrete",
	    "Orange_Concrete",
	    "Magenta_Concrete",
	    "Light_Blue_Concrete",
	    "Yellow_Concrete",
	    "Lime_Concrete",
	    "Pink_Concrete",
	    "Gray_Concrete",
	    "Light_Gray_Concrete",
	    "Cyan_Concrete",
	    "Purple_Concrete",
	    "Blue_Concrete",
	    "Brown_Concrete",
	    "Green_Concrete",
	    "Red_Concrete",
	    "White_Concrete_Powder",
	    "Orange_Concrete_Powder",
	    "Magenta_Concrete_Powder",
	    "Light_Blue_Concrete_Powder",
	    "Yellow_Concrete_Powder",
	    "Lime_Concrete_Powder",
	    "Pink_Concrete_Powder",
	    "Gray_Concrete_Powder",
	    "Light_Gray_Concrete_Powder",
	    "Cyan_Concrete_Powder",
	    "Purple_Concrete_Powder",
	    "Blue_Concrete_Powder",
	    "Brown_Concrete_Powder",
	    "Green_Concrete_Powder",
	    "Red_Concrete_Powder",
	    "Tube_Coral_Block",
	    "Brain_Coral_Block",
	    "Bubble_Coral_Block",
	    "Fire_Coral_Block",
	    "Horn_Coral_Block",
	    "Dead_Tube_Coral_Block",
	    "Dead_Brain_Coral_Block",
	    "Dead_Bubble_Coral_Block",
	    "Dead_Fire_Coral_Block",
	    "Dead_Horn_Coral_Block",
	    "Blue_Ice",
	    "Dried_Kelp_Block"
	};

	private static String[] decorationBlocks = {

	    "Oak_Sapling",
	    "Spruce_Sapling",
	    "Birch_Sapling",
	    "Jungle_Sapling",
	    "Acacia_Sapling",
	    "Dark_Oak_Sapling",
	    "Oak_Leaves",
	    "Spruce_Leaves",
	    "Birch_Leaves",
	    "Jungle_Leaves",
	    "Acacia_Leaves",
	    "Dark_Oak_Leaves",
	    "Cobweb",
	    "Grass",
	    "Fern",
	    "Dead_Bush",
	    "Seagrass",
	    "Sea_Pickle",
	    "Dandelion",
	    "Poppy",
	    "Blue_Orchid",
	    "Allium",
	    "Azure_Bluet",
	    "Red_Tulip",
	    "Orange_Tulip",
	    "White_Tulip",
	    "Pink_Tulip",
	    "Oxeye_Daisy",
	    "Cornflower",
	    "Lily_of_the_Valley",
	    "Wither_Rose",
	    "Sunflower",
	    "Lilac",
	    "Rose_Bush",
	    "Peony",
	    "Brown_Mushroom",
	    "Red_Mushroom",
	    "Torch",
	    "End_Rod",
	    "Chorus_Plant",
	    "Chorus_Flower",
	    "Chest",
	    "Crafting_Table",
	    "Farmland",
	    "Furnace",
	    "Ladder",
	    "Snow",
	    "Cactus",
	    "Jukebox",
	    "Oak_Fence",
	    "Spruce_Fence",
	    "Birch_Fence",
	    "Jungle_Fence",
	    "Acacia_Fence",
	    "Dark_Oak_Fence",
	    "Nether_Brick_Fence",
	    "Infested_Stone",
	    "Infested_Cobblestone",
	    "Infested_Stone_Bricks",
	    "Infested_Cracked_Stone_Bricks",
	    "Infested_Mossy_Stone_Bricks",
	    "Infested_Chiseled_Stone_Bricks",
	    "Brown_Mushroom_Block",
	    "Red_Mushroom_Block",
	    "Mushroom_Stem",
	    "Iron_Bars",
	    "Glass_Pane",
	    "Vine",
	    "Lily_Pad",
	    "Nether_Brick_Fence",
	    "Enchanting_Table",
	    "End_Portal_Frame",
	    "Ender_Chest",
	    "Cobblestone_Wall",
	    "Mossy_Cobblestone_Wall",
	    "Stone_Brick_Wall",
	    "Mossy_Stone_Brick_Wall",
	    "Andesite_Wall",
	    "Diorite_Wall",
	    "Granite_Wall",
	    "Sandstone_Wall",
	    "Red_Sandstone_Wall",
	    "Brick_Wall",
	    "Prismarine_Wall",
	    "Nether_Brick_Wall",
	    "Red_Nether_Brick_Wall",
	    "End_Stone_Brick_Wall",
	    "Anvil",
	    "Chipped_Anvil",
	    "Damaged_Anvil",
	    "White_Carpet",
	    "Orange_Carpet",
	    "Magenta_Carpet",
	    "Light_Blue_Carpet",
	    "Yellow_Carpet",
	    "Lime_Carpet",
	    "Pink_Carpet",
	    "Gray_Carpet",
	    "Light_Gray_Carpet",
	    "Cyan_Carpet",
	    "Purple_Carpet",
	    "Blue_Carpet",
	    "Brown_Carpet",
	    "Green_Carpet",
	    "Red_Carpet",
	    "Black_Carpet",
	    "Slime_Block",
	    "Grass_Path",
	    "Tall_Grass",
	    "Large_Fern",
	    "Glass_Pane",
	    "White_Stained_Glass_Pane",
	    "Orange_Stained_Glass_Pane",
	    "Magenta_Stained_Glass_Pane",
	    "Light_Blue_Stained_Glass_Pane",
	    "Yellow_Stained_Glass_Pane",
	    "Lime_Stained_Glass_Pane",
	    "Pink_Stained_Glass_Pane",
	    "Gray_Stained_Glass_Pane",
	    "Light_Gray_Stained_Glass_Pane",
	    "Cyan_Stained_Glass_Pane",
	    "Purple_Stained_Glass_Pane",
	    "Blue_Stained_Glass_Pane",
	    "Brown_Stained_Glass_Pane",
	    "Green_Stained_Glass_Pane",
	    "Red_Stained_Glass_Pane",
	    "Black_Stained_Glass_Pane",
	    "Terracotta",
	    "White_Terracotta",
	    "Orange_Terracotta",
	    "Magenta_Terracotta",
	    "Light_blue_Terracotta",
	    "Yellow_Terracotta",
	    "Lime_Terracotta",
	    "Pink_Terracotta",
	    "Gray_Terracotta",
	    "Light_gray_Terracotta",
	    "Cyan_Terracotta",
	    "Purple_Terracotta",
	    "Blue_Terracotta",
	    "Brown_Terracotta",
	    "Green_Terracotta",
	    "Red_Terracotta",
	    "Black_Terracotta",
	    "White_Glazed_Terracotta",
	    "Orange_Glazed_Terracotta",
	    "Magenta_Glazed_Terracotta",
	    "Light_Blue_Glazed_Terracotta",
	    "Yellow_Glazed_Terracotta",
	    "Lime_Glazed_Terracotta",
	    "Pink_Glazed_Terracotta",
	    "Gray_Glazed_Terracotta",
	    "Light_Gray_Glazed_Terracotta",
	    "Cyan_Glazed_Terracotta",
	    "Purple_Glazed_Terracotta",
	    "Blue_Glazed_Terracotta",
	    "Brown_Glazed_Terracotta",
	    "Green_Glazed_Terracotta",
	    "Red_Glazed_Terracotta",
	    "Black_Glazed_Terracotta",
	    "Tube_Coral",
	    "Brain_Coral",
	    "Bubble_Coral",
	    "Fire_Coral",
	    "Horn_Coral",
	    "Dead_Tube_Coral",
	    "Dead_Brain_Coral",
	    "Dead_Bubble_Coral",
	    "Dead_Fire_Coral",
	    "Dead_Horn_Coral",
	    "Tube_Coral_Fan",
	    "Brain_Coral_Fan",
	    "Bubble_Coral_Fan",
	    "Fire_Coral_Fan",
	    "Horn_Coral_Fan",
	    "Dead_Tube_Coral_Fan",
	    "Dead_Brain_Coral_Fan",
	    "Dead_Bubble_Coral_Fan",
	    "Dead_Fire_Coral_Fan",
	    "Dead_Horn_Coral_Fan",
	    "Scaffolding",
	    "Painting",
	    "Oak_Sign",
	    "Spruce_Sign",
	    "Birch_Sign",
	    "Jungle_Sign",
	    "Acacia_Sign",
	    "Dark_Oak_Sign",
	    "White_Bed",
	    "Orange_Bed",
	    "Magenta_Bed",
	    "Light_Blue_Bed",
	    "Yellow_Bed",
	    "Lime_Bed",
	    "Pink_Bed",
	    "Gray_Bed",
	    "Light_Gray_Bed",
	    "Cyan_Bed",
	    "Purple_Bed",
	    "Blue_Bed",
	    "Brown_Bed",
	    "Green_Bed",
	    "Red_Bed",
	    "Black_Bed",
	    "Item_Frame",
	    "Flower_Pot",
	    "Skeleton_skull",
	    "Wither_Skeleton_skull",
	    "Zombie_head",
	    "Player_head",
	    "Creeper_head",
	    "Dragon_head",
	    "Armor_Stand",
	    "White_Banner",
	    "Orange_Banner",
	    "Magenta_Banner",
	    "Light_blue_Banner",
	    "Yellow_Banner",
	    "Lime_Banner",
	    "Pink_Banner",
	    "Gray_Banner",
	    "Light_gray_Banner",
	    "Cyan_Banner",
	    "Purple_Banner",
	    "Blue_Banner",
	    "Brown_Banner",
	    "Green_Banner",
	    "Red_Banner",
	    "Black_Banner",
	    "End_Crystal",
	    "Loom",
	    "Barrel",
	    "Smoker",
	    "Blast_Furnace",
	    "Cartography_Table",
	    "Fletching_Table",
	    "Grindstone",
	    "Smithing_Table",
	    "Stonecutter",
	    "Bell",
	    "Lantern",
	    "Campfire",
	    "Beehive",
	    "Bee_Nest",
	    "Honey_Block",
	    "Honeycomb_Block"
	};

	private static String[] redstone = {

	    "Dispenser",
	    "Note_Block",
	    "Piston",
	    "Sticky_Piston",
	    "TNT",
	    "Lever",
	    "Stone_Pressure_Plate",
	    "Heavy_Weighted_Pressure_Plate",
	    "Light_Weighted_Pressure_Plate",
	    "Oak_Pressure_Plate",
	    "Spruce_Pressure_Plate",
	    "Birch_Pressure_Plate",
	    "Jungle_Pressure_Plate",
	    "Acacia_Pressure_Plate",
	    "Dark_Oak_Pressure_Plate",
	    "Redstone_Torch",
	    "Stone_Button",
	    "Oak_Button",
	    "Spruce_Button",
	    "Birch_Button",
	    "Jungle_Button",
	    "Acacia_Button",
	    "Dark_Oak_Button",
	    "Iron_Trapdoor",
	    "Oak_Trapdoor",
	    "Spruce_Trapdoor",
	    "Birch_Trapdoor",
	    "Jungle_Trapdoor",
	    "Acacia_Trapdoor",
	    "Dark_Oak_Trapdoor",
	    "Oak_Fence_Gate",
	    "Spruce_Fence_Gate",
	    "Birch_Fence_Gate",
	    "Jungle_Fence_Gate",
	    "Acacia_Fence_Gate",
	    "Dark_Oak_Fence_Gate",
	    "Redstone_Lamp",
	    "Tripwire_Hook",
	    "Trapped_Chest",
	    "Daylight_Detector",
	    "Redstone_Block",
	    "Hopper",
	    "Dropper",
	    "Observer",
	    "Oak_Door",
	    "Spruce_Door",
	    "Birch_Door",
	    "Jungle_Door",
	    "Acacia_Door",
	    "Dark_Oak_Door",
	    "Iron_Door",
	    "Repeater",
	    "Comparator",
	    "Redstone",
	    "Lectern"

	};

	private static String[] transportation = {

	    "Powered_Rail",
	    "Detector_Rail",
	    "Rail",
	    "Activator_Rail",
	    "Minecart",
	    "Saddle",
	    "Chest_Minecart",
	    "Furnace_Minecart",
	    "Carrot_on_a_Stick",
	    "TNT_Minecart",
	    "Hopper_Minecart",
	    "Elytra",
	    "Oak_Boat",
	    "Spruce_Boat",
	    "Birch_Boat",
	    "Jungle_Boat",
	    "Acacia_Boat",
	    "Dark_Oak_Boat"
	};

	private static String[] miscellaneous = {

	    "Beacon",
	    "Turtle_Egg",
	    "Conduit",
	    "Composter",
	    "Scute",
	    "Coal",
	    "Charcoal",
	    "Diamond",
	    "Iron_Ingot",
	    "Gold_Ingot",
	    "Stick",
	    "Bowl",
	    "String",
	    "Feather",
	    "Gunpowder",
	    "Wheat_Seeds",
	    "Wheat",
	    "Flint",
	    "Bucket",
	    "Water_Bucket",
	    "Lava_Bucket",
	    "Snowball",
	    "Leather",
	    "Milk_Bucket",
	    "Cod_Bucket",
	    "Salmon_Bucket",
	    "Pufferfish_Bucket",
	    "Tropical_Fish_Bucket",
	    "Brick",
	    "Clay_ball",
	    "Sugar_Cane",
	    "Kelp",
	    "Bamboo",
	    "Paper",
	    "Book",
	    "Slime_ball",
	    "Egg",
	    "Glowstone_Dust",
	    "Ink_Sac",
	    "Red_Dye",
	    "Green_Dye",
	    "Purple_Dye",
	    "Cyan_Dye",
	    "Light_Gray_Dye",
	    "Gray_Dye",
	    "Pink_Dye",
	    "Lime_Dye",
	    "Yellow_Dye",
	    "Light_Blue_Dye",
	    "Magenta_Dye",
	    "Orange_Dye",
	    "Black_Dye",
	    "Brown_Dye",
	    "Blue_Dye",
	    "White_Dye",
	    "Cocoa_Beans",
	    "Lapis_Lazuli",
	    "Bone_Meal",
	    "Bone",
	    "Sugar",
	    "Pumpkin_Seeds",
	    "Melon_Seeds",
	    "Ender_Pearl",
	    "Blaze_Rod",
	    "Gold_Nugget",
	    "Nether_Wart",
	    "Ender_Eye",
	    "Experience_Bottle",
	    "Fire_Charge",
	    "Writable_Book",
	    "Emerald",
	    "Map",
	    "Nether_Star",
	    "Firework_Rocket",
	    "Firework_Star",
	    "Nether_Brick",
	    "Quartz",
	    "Prismarine_Shard",
	    "Prismarine_Crystals",
	    "Rabbit_Hide",
	    "Leather_Horse_Armor",
	    "Iron_Horse_Armor",
	    "Golden_Horse_Armor",
	    "Diamond_Horse_Armor",
	    "Chorus_Fruit",
	    "Popped_Chorus_Fruit",
	    "Beetroot_Seeds",
	    "Shulker_Shell",
	    "Iron_Nugget",
	    "Music_Disc_13",
	    "Music_Disc_cat",
	    "Music_Disc_blocks",
	    "Music_Disc_chirp",
	    "Music_Disc_far",
	    "Music_Disc_mall",
	    "Music_Disc_mellohi",
	    "Music_Disc_stal",
	    "Music_Disc_strad",
	    "Music_Disc_ward",
	    "Music_Disc_11",
	    "Music_Disc_wait",
	    "Nautilus_Shell",
	    "Heart_of_the_Sea",
	    "Flower_Banner_Pattern",
	    "Creeper_Banner_Pattern",
	    "Skull_Banner_Pattern",
	    "Mojang_Banner_Pattern",
	    "Globe_Banner_Pattern",
	    "Honeycomb"
	};

	private static String[] eggs = {
	    "Bat_Spawn_Egg",
	    "Bee_Spawn_Egg",
	    "Blaze_Spawn_Egg",
	    "Cave_Spider_Spawn_Egg",
	    "Chicken_Spawn_Egg",
	    "Cat_Spawn_Egg",
	    "Cod_Spawn_Egg",
	    "Cow_Spawn_Egg",
	    "Creeper_Spawn_Egg",
	    "Dolphin_Spawn_Egg",
	    "Donkey_Spawn_Egg",
	    "Drowned_Spawn_Egg",
	    "Elder_Guardian_Spawn_Egg",
	    "Enderman_Spawn_Egg",
	    "Endermite_Spawn_Egg",
	    "Evoker_Spawn_Egg",
	    "Fox_Spawn_Egg",
	    "Ghast_Spawn_Egg",
	    "Guardian_Spawn_Egg",
	    "Horse_Spawn_Egg",
	    "Husk_Spawn_Egg",
	    "Llama_Spawn_Egg",
	    "Magma_Cube_Spawn_Egg",
	    "Mooshroom_Spawn_Egg",
	    "Mule_Spawn_Egg",
	    "Ocelot_Spawn_Egg",
	    "Panda_Spawn_Egg",
	    "Parrot_Spawn_Egg",
	    "Phantom_Spawn_Egg",
	    "Pig_Spawn_Egg",
	    "Pillager_Spawn_Egg",
	    "Polar_Bear_Spawn_Egg",
	    "Pufferfish_Spawn_Egg",
	    "Rabbit_Spawn_Egg",
	    "Ravager_Spawn_Egg",
	    "Salmon_Spawn_Egg",
	    "Sheep_Spawn_Egg",
	    "Shulker_Spawn_Egg",
	    "Silverfish_Spawn_Egg",
	    "Skeleton_Spawn_Egg",
	    "Skeleton_Horse_Spawn_Egg",
	    "Slime_Spawn_Egg",
	    "Spider_Spawn_Egg",
	    "Squid_Spawn_Egg",
	    "Stray_Spawn_Egg",
	    "Trader_Llama_Spawn_Egg",
	    "Tropical_Fish_Spawn_Egg",
	    "Turtle_Spawn_Egg",
	    "Vex_Spawn_Egg",
	    "Villager_Spawn_Egg",
	    "Vindicator_Spawn_Egg",
	    "Wandering_Trader_Spawn_Egg",
	    "Witch_Spawn_Egg",
	    "Wither_Skeleton_Spawn_Egg",
	    "Wolf_Spawn_Egg",
	    "Zombie_Spawn_Egg",
	    "Zombie_Horse_Spawn_Egg",
	    "Zombie_Pigman_Spawn_Egg",
	    "Zombie_Villager_Spawn_Egg"
	};

	private static String[] foodstuffs = {

	    "Apple",
	    "Mushroom_Stew",
	    "Bread",
	    "Porkchop",
	    "Cooked_Porkchop",
	    "Golden_Apple",
	    "Enchanted_Golden_Apple",
	    "Cod",
	    "Salmon",
	    "Tropical_Fish",
	    "Pufferfish",
	    "Cooked_Cod",
	    "Cooked_Salmon",
	    "Cake",
	    "Cookie",
	    "Melon_Slice",
	    "Dried_Kelp",
	    "Cooked_Beef",
	    "Raw_Beef",
	    "Chicken",
	    "Cooked_Chicken",
	    "Rotten_Flesh",
	    "Spider_Eye",
	    "Carrot",
	    "Potato",
	    "Baked_Potato",
	    "Poisonous_Potato",
	    "Pumpkin_Pie",
	    "Rabbit",
	    "Cooked_Rabbit",
	    "Rabbit_Stew",
	    "Mutton",
	    "Cooked_Mutton",
	    "Beetroot",
	    "Beetroot_Soup",
	    "Sweet_Berries",
	    "Honey_Bottle"
	};

	private static String[] tools = {

	    "Wooden_Shovel",
	    "Stone_Shovel",
	    "Iron_Shovel",
	    "Diamond_Shovel",
	    "Golden_Shovel",
	    "Wooden_Pickaxe",
	    "Stone_Pickaxe",
	    "Iron_Pickaxe",
	    "Diamond_Pickaxe",
	    "Golden_Pickaxe",
	    "Wooden_Axe",
	    "Stone_Axe",
	    "Iron_Axe",
	    "Diamond_Axe",
	    "Golden_Axe",
	    "Wooden_Hoe",
	    "Stone_Hoe",
	    "Iron_Hoe",
	    "Diamond_Hoe",
	    "Golden_Hoe",
	    "Flint_and_Steel",
	    "Compass",
	    "Fishing_Rod",
	    "Clock",
	    "Shears",
	    "Lead",
	    "Name_Tag"

	};

	private static String[] combat ={

	    "Bow",
	    "Arrow",
	    "Wooden_Sword",
	    "Stone_Sword",
	    "Iron_Sword",
	    "Diamond_Sword",
	    "Golden_Sword",
	    "Leather_Helmet",
	    "Leather_Chestplate",
	    "Leather_Leggings",
	    "Leather_Boots",
	    "Chainmail_Helmet",
	    "Chainmail_Chestplate",
	    "Chainmail_Leggings",
	    "Chainmail_Boots",
	    "Iron_Helmet",
	    "Iron_Chestplate",
	    "Iron_Leggings",
	    "Iron_Boots",
	    "Diamond_Helmet",
	    "Diamond_Chestplate",
	    "Diamond_Leggings",
	    "Diamond_Boots",
	    "Golden_Helmet",
	    "Golden_Chestplate",
	    "Golden_Leggings",
	    "Golden_Boots",
	    "Turtle_Helmet",
	    "Shield",
	    "Totem_of_Undying",
	    "Trident",
	    "Crossbow"

	};

	private static String[] brewing = {

	    "Ghast_Tear",
	    "Glass_Bottle",
	    "Fermented_Spider_Eye",
	    "Blaze_Powder",
	    "Magma_Cream",
	    "Brewing_Stand",
	    "Cauldron",
	    "Glistering_Melon_Slice",
	    "Golden_Carrot",
	    "Rabbit_Foot",
	    "Dragon_Breath",
	    "Phantom_Membrane"

	};
	//TODO add potions
	/*
	private static String[] Potions = {
	Potions
	Splash_Potions
	Lingering_Potions
	};
	*/



}
package com.hotmail.idiotonastic.plugins.DynamicShops.Events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.hotmail.idiotonastic.plugins.DynamicShops.Shop;
import com.hotmail.idiotonastic.plugins.DynamicShops.ShopScreen;

public class EventsClass implements Listener {

	private static String menu = ShopScreen.getMenuTitle();
	private static String ammount = ShopScreen.getAmmountTitle();
	private static String itemM = ShopScreen.getItemTitle();
	private static String prevPage = ShopScreen.getPrevPageTitle();
	private static String nextPage = ShopScreen.getNextPageTitle();
	private static String backTitle = ShopScreen.getBackTitle();
	private static String sellTitle = ShopScreen.getSellTitle();
	//private static String sellConfirm = ShopScreen.getSellConfirm();
	@EventHandler
	public void InvenEsc(InventoryCloseEvent event){
		if (event.getView().getTitle().equals(sellTitle)) {
			Player player = (Player) event.getPlayer();
			ItemStack[] openS = event.getInventory().getContents();
			Shop.sell(player, openS, true);
			return;
		}
	}
	@EventHandler
	public void InvenClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		Inventory open = event.getClickedInventory();
		ItemStack item = event.getCurrentItem();
		//Shop s = new Shop();
		
		if (open == null) {
			return;
		} else 
		if (item == null) {
			return;
		} 
		
		if ((event.getView().getTitle().equals(menu)||event.getView().getTitle().equals(sellTitle)||event.getView().getTitle().equals(itemM)||event.getView().getTitle().equals(ammount) )) {
			if (item.getItemMeta().getDisplayName().equalsIgnoreCase(backTitle)){
				event.setCancelled(true);
				ShopScreen is = new ShopScreen();
				is.menuScreen(player);
				return;
			}
		} 
		if (event.getView().getTitle().equals(menu)) {
			
			event.setCancelled(true);
			
			ShopScreen is = new ShopScreen();
			player.closeInventory();
			is.itemScreen(player, ShopScreen.getScreenNo(item.getType()));
			return;

		} else
		if (event.getView().getTitle().equals(itemM)) {
			event.setCancelled(true);
			
			player.closeInventory();
			ShopScreen as = new ShopScreen();
			if (item.getType().equals(Material.PRISMARINE_SHARD) && (item.getItemMeta().getDisplayName().equalsIgnoreCase(prevPage) || item.getItemMeta().getDisplayName().equalsIgnoreCase(nextPage))){
				String enc = item.getItemMeta().getLore().toString().substring(3,item.getItemMeta().getLore().toString().length()-1);
				Material mat = Material.matchMaterial(enc);
				as.itemScreen(player, ShopScreen.getScreenNo(mat),item.getAmount());
				return;
			}
			as.amountScreen(player, item.getType());
			return;
		} else
		if (event.getView().getTitle().equals(ammount)) {
			event.setCancelled(true);
			player.closeInventory();
			Shop.buy(player, item);
			return;
		}
		return;
	}
}

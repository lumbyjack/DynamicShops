package com.hotmail.idiotonastic.plugins.TrulyDynamicShops;

import java.util.logging.Logger;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Callable;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Utils.ItemManager;
import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Utils.Metrics;
import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Commands;
import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Events.EventsClass;


public class Main extends JavaPlugin implements Listener {
	
	private static final Logger logger = Logger.getLogger("Minecraft");
	private static final ItemManager itemMan = new ItemManager();
	private static Economy econ;
	private static String 	MCVersion = "1.15.1";
	private static boolean debug = false;
	private boolean GUI = true;
	private boolean rawBase = true;
	


	@Override
	public void onEnable() {
		if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new EventsClass(), this);
        this.saveDefaultConfig();
		if (!itemMan.initilaizeItemManager(this)) {
            this.getLogger().severe("Disabled due to item config errors!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
		Commands commands = new Commands();
		getCommand("Shop").setExecutor(commands);
		getCommand("Sell").setExecutor(commands);
		getCommand("Price").setExecutor(commands);
		getCommand("TDS").setExecutor(commands);
		Metrics metrics = new Metrics(this, 6341);
		metrics.addCustomChart(new Metrics.AdvancedPie("options_stats", new Callable<Map<String, Integer>>() {
		        @Override
		        public Map<String, Integer> call() throws Exception {
		        Map<String, Integer> map = new HashMap<>();
		        int gInt = (GUI ? 1:0);
		        int rInt = (rawBase ? 1:0);
		        map.put("GUI", gInt);
		        map.put("Raw", rInt);
		        return map;
		    }
		}));
		metrics.addCustomChart(new Metrics.SimplePie("transaction_modifier", new Callable<String>() {
		  @Override
		   public String call() throws Exception {
		          return Double.toString(getConfig().getDouble("transaction_modifier"));
		     }
		}));
		metrics.addCustomChart(new Metrics.SimplePie("crafting_modifier", new Callable<String>() {
			  @Override
			   public String call() throws Exception {
			          return Double.toString(getConfig().getDouble("crafting_modifier"));
			     }
		}));
	}


	public void setGUI(){
		GUI = this.getConfig().getBoolean("GUI");
	}
	public boolean getGUI(){
		return GUI;
	}
	public void setRawBase(){
		rawBase = this.getConfig().getBoolean("Calculate_from_raw");
	}
	public boolean getRawBase(){
		return rawBase;
	}
	@Override
	public void onDisable() {
		itemMan.saveLists();
		logger.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
	    
	}
	
	private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	public File getFolder(){
		return this.getDataFolder();
	}
	public String getMCVersion(){
		return MCVersion;
	}
	public Logger getLogger(){
		return logger;
	}
	public Economy getEconomy(){
		return econ;
	}
	public boolean getDebug(){
		return debug;
	}

	public void doReload(){
		this.getPluginLoader().disablePlugin(this);
		this.getPluginLoader().enablePlugin(this);
	}
	public enum State 
	{
			BUY, SELL, SET, QUERY
	}
	public ItemManager getItemMan() {
		return itemMan;
	};
}
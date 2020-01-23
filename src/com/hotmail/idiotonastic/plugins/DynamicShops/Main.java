package com.hotmail.idiotonastic.plugins.DynamicShops;

import java.util.logging.Logger;
import java.io.File;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.hotmail.idiotonastic.plugins.DynamicShops.Commands;
import com.hotmail.idiotonastic.plugins.DynamicShops.Events.EventsClass;


public class Main extends JavaPlugin implements Listener {
	
	private static final Logger logger = Logger.getLogger("Minecraft");
	private static Economy econ;
	private static String 	MCVersion = "1.15.1";
	private static boolean debug = false;
	private boolean GUI = true;
	public void onEnable() {
        if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(new EventsClass(), this);
        this.saveDefaultConfig();
		Commands commands = new Commands();
		getCommand("Shop").setExecutor(commands);
		getCommand("Sell").setExecutor(commands);
		getCommand("Price").setExecutor(commands);
		getCommand("DS").setExecutor(commands);
		getCommand("DSA").setExecutor(commands);
	}

	
	public boolean getGUI(){
		return GUI;
	}
	public void onDisable() {
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
			BUY,
			SELL,
			SET
	};
}
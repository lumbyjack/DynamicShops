package com.hotmail.idiotonastic.plugins.DynamicShops;

import java.io.*;
import java.util.logging.Logger;

import org.bukkit.event.Listener;

public class Log implements Listener {
	private static Main plugin = Main.getPlugin(Main.class);
	private static Logger logger = plugin.getLogger();
	private static File Log = new File(plugin.getDataFolder() + File.separator + "logger.txt");

	public void logs(String name, String Item, int Ammount, double Price, String Time, boolean BuySell, boolean set){
		if (!Log.exists()) {
				logger.info("[DS] Generating Log file...");
			try {
				Log.createNewFile();
			}catch(IOException e) {
				logger.info("[DS] Unable make file. Does the file location exist? " );
			}
		}
		try {
		FileWriter write = new FileWriter(Log, true);
        if (BuySell){ //buy
        	write.write("BOUGHT | Name: " + name + "| Item: " + Item + "| Ammount: " + Ammount + "| Price: " + Price + "| Date/Time: " + Time + System.getProperty( "line.separator"));
        } else if (!BuySell) { //sell
        	write.write("SOLD   | Name: " + name + "| Item: " + Item + "| Ammount: " + Ammount + "| Price: " + Price + "| Date/Time: " + Time + System.getProperty( "line.separator"));
        } else if (set){
        	write.write("SET    | Name: " + name + "| Item: " + Item + "| Price: " + Price + "| Date/Time: " + Time + System.getProperty( "line.separator"));
            
        }
        write.close();
	 } catch(IOException e) {
		 logger.info("[DS] Unable to access file. Does the file exist? " );
     }
	}
}

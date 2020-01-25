package com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Utils;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.bukkit.event.Listener;

import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Main;
import com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Main.State;

public class Log implements Listener {
	private static Main plugin = Main.getPlugin(Main.class);
	private static Logger logger = plugin.getLogger();
	private static File Log = new File(plugin.getDataFolder() + File.separator + "TransactionLogger.txt");
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Date date = new Date();
	
	public void logThis(String playerName, String Item, int Ammount, double Price, State state){
		String Time = dateFormat.format(date);
		if (!Log.exists()) {
				logger.info(String.format("[%s] Generating Log file...", plugin.getName()));
			try {
				Log.createNewFile();
			}catch(IOException e) {
				logger.warning(String.format("[%s] Unable make file. Does the file location exist? ", plugin.getName() ));
			}
		}
		try {
		FileWriter write = new FileWriter(Log, true);
        if (state == State.BUY){ //buy
        	write.write("BOUGHT | Name: " + playerName + "| Item: " + Item + "| Ammount: " + Ammount + "| Price: " + Price + "| Date/Time: " + Time + System.getProperty( "line.separator"));
        } else if (state == State.SELL) { //sell
        	write.write("SOLD   | Name: " + playerName + "| Item: " + Item + "| Ammount: " + Ammount + "| Price: " + Price + "| Date/Time: " + Time + System.getProperty( "line.separator"));
        } else if(state == State.SET){
        	write.write("SET    | Name: " + playerName + "| Item: " + Item + "| Price: " + Price + "| Date/Time: " + Time + System.getProperty( "line.separator"));
        }
        write.close();
	 } catch(IOException e) {
		 logger.warning(String.format("[%s] Unable log transaction." , plugin.getName() ));
     }
	}
}


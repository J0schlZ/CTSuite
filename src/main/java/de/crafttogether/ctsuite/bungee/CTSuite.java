package de.crafttogether.ctsuite.bungee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;

import com.google.common.io.ByteStreams;

import de.crafttogether.ctsuite.bungee.messaging.MessagingService;
import de.crafttogether.ctsuite.bungee.messaging.Packet;
import de.crafttogether.ctsuite.bungee.messaging.MessagingService.Adapter;
import de.crafttogether.ctsuite.bungee.database.AsyncMySQLHandler;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 * CTSuite
 * @author J0schlZ
 * @version 0.0.1-SNAPSHOT
 */

public class CTSuite extends Plugin {
	private static CTSuite plugin;
	
    private Configuration config;
	private AsyncMySQLHandler db;
	private MessagingService messaging;

    @Override
	public void onEnable() {
    	plugin = this;
		//db = new AsyncMySQLHandler("127.0.0.1", 3306, "ct_ctogether", "ctogether", "");	

		loadConfig();
		
		messaging = new MessagingService(Adapter.CTSOCKETS);
        
    	System.out.println(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");
    	
		// Run 5 Sek later as test
		plugin.getProxy().getScheduler().schedule(this, new Runnable() {
			@Override
			public void run() {
				Packet msg = new Packet("testMessage");
				msg.put("testvar", "Hey, hier ist Bungee! *wink*");
				msg.sendAll();
			}
		}, 5, TimeUnit.SECONDS);
	}

    @Override
	public void onDisable() {
    	//db.disconnect();
		System.out.println(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " disabled");
    }
    
	private Configuration loadConfig() {
        if (!getDataFolder().exists()) {
        	this.getDataFolder().mkdir();
        }
        
        File configFile = new File(getDataFolder(), "config.yml");
        
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
                InputStream is = getResourceAsStream("bungeeconfig.yml");
                OutputStream os = new FileOutputStream(configFile);
                ByteStreams.copy(is, os);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to create config.yml", e);
        }
        try {
        	config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(configFile), "UTF8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return config;
	}
	
	public MessagingService getMessagingService() {
		return messaging;
	}
    
	public Configuration getConfig() {
		return config;
	}
	
	public AsyncMySQLHandler getDb() {
		return db;
	}

	public static CTSuite getInstance() {
		return plugin;
	}
}

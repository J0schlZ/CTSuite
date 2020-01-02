package de.crafttogether.ctsuite.bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import com.google.common.io.ByteStreams;

import de.crafttogether.ctsuite.bukkit.messaging.MessagingService;
import de.crafttogether.ctsuite.bukkit.messaging.ReceivedPacket;
import de.crafttogether.ctsuite.bukkit.messaging.MessagingService.Adapter;
import de.crafttogether.ctsuite.bukkit.messaging.MessagingService.Callback;
import de.crafttogether.ctsuite.bukkit.messaging.Packet;
import de.crafttogether.ctsuite.bukkit.database.AsyncMySQLHandler;

import de.crafttogether.ctsuite.bukkit.handlers.PlayerHandler;
import de.crafttogether.ctsuite.bukkit.handlers.ServerHandler;
import de.crafttogether.ctsuite.bukkit.handlers.WorldHandler;

/**
 * CTSuite
 * @author J0schlZ
 * @version 0.0.1-SNAPSHOT
 */

public class CTSuite extends JavaPlugin {
	private static CTSuite plugin;
	
	private FileConfiguration config;
	private AsyncMySQLHandler db;
	
	private MessagingService messaging;
	
	private ServerHandler serverHandler;
	private PlayerHandler playerHandler;
	private WorldHandler worldHandler;

	@Override
	public void onEnable() {
		plugin = this;
		//db = new AsyncMySQLHandler("127.0.0.1", 3306, "ct_ctogether", "ctogether", "");
        
		loadConfig();
		//new RegisterCommands();
		
		messaging = new MessagingService(Adapter.CTSOCKETS);
		
		serverHandler = new ServerHandler();
		playerHandler = new PlayerHandler();
		worldHandler = new WorldHandler();
		
		System.out.println(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");

		messaging.on("server-connection", new Callback() {
			@Override
		    public void run(ReceivedPacket packet) {
		    	plugin.getLogger().info("Server '" + packet.getValues().get("serverName") + "' verbunden.");
		    }
		});
		
		messaging.on("testMessage", new Callback() {
			@Override
		    public void run(ReceivedPacket packet) {
				JSONObject values = packet.getValues();
		    	System.out.println("Message Received! Sender: " + packet.getSender());
		    	System.out.println("testvar: " + values.getString("testvar"));
		    }
		});
		
		
		// Run 5 Sek later as test
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				Packet msg = new Packet("testMessage");
				msg.put("testvar", "Joscha ist cool!");
				msg.sendServer();
			}
		}, 20L*5);
	}

	@Override
	public void onDisable() {
		//db.disconnect();
		System.out.println(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " disabled");
	}
	
	private FileConfiguration loadConfig() {
        if (!getDataFolder().exists()) {
        	this.getDataFolder().mkdir();
        }
        
        File configFile = new File(getDataFolder(), "config.yml");
        
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
                InputStream is = getResource("bukkitconfig.yml");
                OutputStream os = new FileOutputStream(configFile);
                ByteStreams.copy(is, os);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to create config.yml", e);
        }

        try {
			config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(configFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
        return config;
	}
	
	public ServerHandler getServerHandler() {
		return serverHandler;
	}
	
	public PlayerHandler getPlayerHandler() {
		return playerHandler;
	}
	
	public WorldHandler getWorldHandler() {
		return worldHandler;
	}
	
	public MessagingService getMessagingService() {
		return messaging;
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public AsyncMySQLHandler getDb() {
		return db;
	}

	public static CTSuite getInstance() {
		return plugin;
	}
}

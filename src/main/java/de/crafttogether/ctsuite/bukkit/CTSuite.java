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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;

import com.google.common.io.ByteStreams;
import com.onarandombox.MultiverseCore.MultiverseCore;

import de.crafttogether.ctsuite.messaging.MessagingService;
import de.crafttogether.ctsuite.messaging.ReceivedPacket;
import de.crafttogether.ctsuite.messaging.MessagingService.Adapter;
import de.crafttogether.ctsuite.messaging.MessagingService.Callback;
import de.crafttogether.ctsuite.util.CTServer;
import de.crafttogether.ctsuite.util.CTWorld;
import de.crafttogether.ctsuite.util.PluginEnvironment;
import de.crafttogether.ctsuite.messaging.Packet;
import de.crafttogether.ctsuite.database.AsyncMySQLHandler;
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
	private static PluginEnvironment environment = PluginEnvironment.BUKKIT;
	
	private FileConfiguration config;
	private AsyncMySQLHandler db;
	private MessagingService messaging;
	
	private ServerHandler serverHandler;
	private PlayerHandler playerHandler;
	private WorldHandler worldHandler;
	
	private MultiverseCore multiverse = null;

	@Override
	public void onEnable() {
		plugin = this;
		//db = new AsyncMySQLHandler(environment, "127.0.0.1", 3306, "ct_ctogether", "ctogether", "");
        
		loadConfig();
		loadPlugins();
		//new RegisterCommands();
		
		messaging = new MessagingService(Adapter.CTSOCKETS, environment);
		
		serverHandler = new ServerHandler();
		playerHandler = new PlayerHandler();
		worldHandler = new WorldHandler();
		
		messaging.on("socket-connection", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				Packet packet = new Packet("server-register");
				
				JSONArray worldList = new JSONArray();
				for (CTWorld world : worldHandler.getWorlds())
					worldList.put(world.getName());

				packet.put("serverName", messaging.getAdapter().getClientName());
				packet.put("worldList", worldList);
				packet.sendAll();
			}
		});
		
		System.out.println(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");

		// Run 5 Sek later as test
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				plugin.getLogger().info("Server:");
				for (CTServer server : serverHandler.getServer())
					plugin.getLogger().info(server.getName());
				plugin.getLogger().info("Worlds:");
				for (CTWorld world : worldHandler.getWorlds())
					plugin.getLogger().info(world.getName());
			}
		}, 20L*5, 20L*5);
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
	
    private void loadPlugins() {
    	PluginManager pm = getServer().getPluginManager();
    	Plugin loadedPlugin;
        if (pm.getPlugin("Multiverse-Core") != null) {
        	loadedPlugin = pm.getPlugin("Multiverse-Core");
            if (loadedPlugin instanceof MultiverseCore)
                this.multiverse = (MultiverseCore) loadedPlugin;
            else
                this.getLogger().warning("Couln't find Multiverse-Core.");
        }
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
	
	public MultiverseCore getMultiverseCore() {
		return multiverse;
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public AsyncMySQLHandler getDb() {
		return db;
	}
	
	public static PluginEnvironment getEnvironment() {
		return environment;
	}

	public static CTSuite getInstance() {
		return plugin;
	}
}

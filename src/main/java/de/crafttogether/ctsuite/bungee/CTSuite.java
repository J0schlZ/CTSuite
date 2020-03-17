package de.crafttogether.ctsuite.bungee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import de.crafttogether.ctsuite.bungee.handlers.ServerHandler;
import de.crafttogether.ctsuite.bungee.handlers.WorldHandler;
import de.crafttogether.ctsuite.messaging.MessagingService;
import de.crafttogether.ctsuite.messaging.MessagingService.Adapter;
import de.crafttogether.ctsuite.util.CTServer;
import de.crafttogether.ctsuite.util.CTWorld;
import de.crafttogether.ctsuite.util.MySQLHandler;
import de.crafttogether.ctsuite.util.MySQLHandler.Callback;
import de.crafttogether.ctsuite.util.PluginEnvironment;
import de.crafttogether.ctsuite.util.Util;

/**
 * CTSuite
 * @author J0schlZ
 * @version 0.0.1-SNAPSHOT
 */

public class CTSuite extends Plugin {
	private static CTSuite plugin;
	private static PluginEnvironment environment = PluginEnvironment.BUNGEE;
	
    private Configuration config;
	private MySQLHandler db;
	private MessagingService messaging;

	private ServerHandler serverHandler;
	private WorldHandler worldHandler;

    @Override
	public void onEnable() {
    	plugin = this;
		loadConfig();
		
		//db = new MySQLHandler(environment, config.getString("MySQL.host"), config.getInt("MySQL.port"), config.getString("MySQL.database"), config.getString("MySQL.username"), config.getString("MySQL.password"));
		messaging = new MessagingService(environment, Adapter.CTSOCKETS);
        
		serverHandler = new ServerHandler();
		worldHandler = new WorldHandler();
    	
		// Run 5 Sek later as test
		plugin.getProxy().getScheduler().schedule(this, new Runnable() {
			@Override
			public void run() {
				plugin.getLogger().info("Server:");
				for (CTServer server : serverHandler.getServer())
					plugin.getLogger().info(server.getName());
				plugin.getLogger().info("Worlds:");
				for (CTWorld world : worldHandler.getWorlds())
					plugin.getLogger().info(world.getName());
			}
		}, 5, 5, TimeUnit.SECONDS);
		
		/*plugin.getLogger().info("Installiere Tabellen...");
		db.queryAsync(Util.readFile(getResourceAsStream("tables.sql")), new Callback<ResultSet, SQLException>() {
			@Override
			public void call(ResultSet result, SQLException err) {
				if (err != null) {
					err.printStackTrace();
				}
				
				plugin.getLogger().info("Tabellen installiert");
			}
		});*/
		
		plugin.getLogger().info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");
	}

    @Override
	public void onDisable() {
    	PluginManager pm = getProxy().getPluginManager();
    	//db.disconnect();
    	pm.unregisterListeners(this);
    	pm.unregisterCommands(this);
    	
    	plugin.getLogger().info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " disabled");
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
	
	public ServerHandler getServerHandler() {
		return serverHandler;
	}
		
	public WorldHandler getWorldHandler() {
		return worldHandler;
	}
	public MessagingService getMessagingService() {
		return messaging;
	}
    
	public Configuration getConfig() {
		return config;
	}
	
	public MySQLHandler getDb() {
		return db;
	}
	
	public static PluginEnvironment getEnvironment() {
		return environment;
	}

	public static CTSuite getInstance() {
		return plugin;
	}
}

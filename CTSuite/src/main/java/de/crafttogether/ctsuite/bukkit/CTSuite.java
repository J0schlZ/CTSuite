package de.crafttogether.ctsuite.bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteStreams;

import de.crafttogether.database.AsyncMySQLHandler;
import de.crafttogether.ctsockets.bukkit.CTSockets;
import de.crafttogether.ctsuite.bukkit.commands.RegisterCommands;
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
	private CTSockets ctSockets;
	
	private ServerHandler serverHandler;
	private PlayerHandler playerHandler;
	private WorldHandler worldHandler;
	
	@Override
	public void onEnable() {
		plugin = this;
		db = new AsyncMySQLHandler("127.0.0.1", 3306, "ct_ctogether", "ctogether", "Pj9va*33");
		ctSockets = (CTSockets) Bukkit.getServer().getPluginManager().getPlugin("CTSockets");
    	
        if (ctSockets == null || !(ctSockets instanceof CTSockets)) {
            getLogger().warning("Couln't find CTSockets");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
		loadConfig();
		//new RegisterCommands();
		
		serverHandler = new ServerHandler();
		playerHandler = new PlayerHandler();
		worldHandler = new WorldHandler();
		
		System.out.println(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");
	}

	@Override
	public void onDisable() {
		serverHandler.shutdown();
		db.disconnect();
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
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public AsyncMySQLHandler getDb() {
		return db;
	}
	
	public CTSockets getCTSocket() {
		return ctSockets;
	}

	public static CTSuite getInstance() {
		return plugin;
	}
}

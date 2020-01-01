package de.crafttogether.ctsuite.bukkit.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

import de.crafttogether.ctsuite.bukkit.CTSuite;
import de.crafttogether.ctsuite.util.AsyncMySQLHandler;
import de.crafttogether.ctsuite.util.CTServer;

public class ServerHandler {
	private CTSuite plugin;
	private AsyncMySQLHandler db;
	private FileConfiguration config;
	
	private HashMap<String, CTServer> serverMap;
	
	public ServerHandler() {
		plugin	= CTSuite.getInstance();
		config	= plugin.getConfig();
		db 		= plugin.getDb();
		
		//TODO: Get servers from database
		//TODO: Process ServerConnectedEvent from CTSockets
		
		//TODO: Register server at database
		registerAtDatabase();
	}

	private void registerAtDatabase() {
		ResultSet result = null;
		try {
			result = db.query("SELECT * FROM `?`.`?_server` WHERE `name` = '?'",
				plugin.getConfig().getString("mysql.database"),
				plugin.getConfig().getString("mysql.prefix"),
				config.getString("settings.serverName")
			);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (result == null) {
				try {
					db.execute("");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
					
				}
			}
			else {
				try {
					db.update("");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
					
				}
			}
		}
	}
	
	public Collection<CTServer> getServer() {
		return serverMap.values();
	}
	
	public CTServer getServer(String name) {
		if (serverMap.containsKey(name))
			return serverMap.get(name);
		return null;
	}

	public void shutdown() {
		// TODO Shutdown ServerHandler
	}
}

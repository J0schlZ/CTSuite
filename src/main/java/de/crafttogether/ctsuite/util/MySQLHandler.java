package de.crafttogether.ctsuite.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jetbrains.annotations.Nullable;

import com.zaxxer.hikari.HikariDataSource;

import de.crafttogether.ctsuite.bungee.CTSuite;

public class MySQLHandler {
	private CTSuite plugin;
	private PluginEnvironment environment;
	private HikariDataSource dataSource;
	
	public interface Callback<V extends Object, T extends Throwable> {
	    public void call(V result, T thrown);
	}
	
	public MySQLHandler(PluginEnvironment environment, String host, int port, String database, String user, String password) {
		this.plugin = CTSuite.getInstance();
		this.environment = environment;
		
        this.dataSource = new HikariDataSource();
        this.dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        this.dataSource.addDataSourceProperty("serverName", host);
        this.dataSource.addDataSourceProperty("port", port);
        this.dataSource.addDataSourceProperty("databaseName", database);
        this.dataSource.addDataSourceProperty("user", user);
        this.dataSource.addDataSourceProperty("password", password);
	}
	
	private void execute(Runnable run) {
		switch (this.environment) {
		
		case BUNGEE:
			de.crafttogether.ctsuite.bungee.CTSuite bungeePlugin = de.crafttogether.ctsuite.bungee.CTSuite.getInstance();
			bungeePlugin.getProxy().getScheduler().runAsync(bungeePlugin, run);
			break;
			
		case BUKKIT:
			de.crafttogether.ctsuite.bukkit.CTSuite bukkitPlugin = de.crafttogether.ctsuite.bukkit.CTSuite.getInstance();
			bukkitPlugin.getServer().getScheduler().runTaskAsynchronously(bukkitPlugin, run);
			break;
		}
	}
	
	public void disconnect() {
		this.dataSource.close();
	}
	
	public ResultSet query(String statement, final Object ...args) throws SQLException {
		if (args.length > 0) statement = String.format(statement, args);
		String finalStatement = statement;
		
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        connection = dataSource.getConnection();
        preparedStatement = connection.prepareStatement(finalStatement);
        resultSet = preparedStatement.executeQuery();

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
            	plugin.getLogger().warning(e.getMessage());
            }
        }
       
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
            	plugin.getLogger().warning(e.getMessage());
            }
        }
       
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            	plugin.getLogger().warning(e.getMessage());
            }
        }
        
        return resultSet;
	}
	
	public void queryAsync(String statement, final @Nullable Callback<ResultSet, SQLException> callback, final Object ...args) {
		if (args.length > 0) statement = String.format(statement, args);
		final String finalStatement = statement;
		
		execute(new Runnable() {
			public void run() {
				try {
		            ResultSet resultSet = query(finalStatement);
		            callback.call(resultSet, null);
		        } catch (SQLException e) {
		            callback.call(null, e);
		        }
		    }
		});
	}

	public int update(String statement, final Object ...args) throws SQLException {
		if (args.length > 0) statement = String.format(statement, args);
		String finalStatement = statement;
					
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int rows = 0;
		
		connection = dataSource.getConnection();
		preparedStatement = connection.prepareStatement(finalStatement);
		rows = preparedStatement.executeUpdate();

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
            	plugin.getLogger().warning(e.getMessage());
            }
        }
       
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            	plugin.getLogger().warning(e.getMessage());
            }
        }
        
        return rows;
    }

	public void updateAsync(String statement, final @Nullable Callback<Integer, SQLException> callback, final Object ...args) {
		if (args.length > 0) statement = String.format(statement, args);
		final String finalStatement = statement;
		
		execute(new Runnable() {
			public void run() {
				try {
		            int rows = update(finalStatement);
		            callback.call(rows, null);
		        } catch (SQLException e) {
		            callback.call(null, e);
		        }
		    }
		});
	}
	
	public Boolean execute(String statement, final Object ...args) throws SQLException {
		if (args.length > 0) statement = String.format(statement, args);
		String finalStatement = statement;
						
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Boolean result = false;
		
		connection = dataSource.getConnection();
		preparedStatement = connection.prepareStatement(finalStatement);
		result = preparedStatement.execute();
		
		if (preparedStatement != null) {
		    try {
		        preparedStatement.close();
		    } catch (SQLException e) {
		    	plugin.getLogger().warning(e.getMessage());
		    }
		}
		   
		if (connection != null) {
		    try {
		        connection.close();
		    } catch (SQLException e) {
		    	plugin.getLogger().warning(e.getMessage());
		    }
		}
		
		return result;
	}

	public void executeAsync(String statement, final @Nullable Callback<Boolean, SQLException> callback, final Object ...args) {
		if (args.length > 0) statement = String.format(statement, args);
		final String finalStatement = statement;
		
		execute(new Runnable() {
			public void run() {
				try {
					Boolean result = execute(finalStatement);
		            callback.call(result, null);
		        } catch (SQLException e) {
		            callback.call(null, e);
		        }
		    }
		});
	}
}

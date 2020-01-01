package de.crafttogether.ctsuite.bukkit.commands;

import java.lang.reflect.Method;

import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import de.crafttogether.ctsuite.bukkit.CTSuite;

public class RegisterCommands {
	private CTSuite plugin;
	
	public RegisterCommands() {
		plugin = CTSuite.getInstance();
		registerCommand(new CommandGamemode(), plugin);
	}
	
    public static void registerCommand(Command _cmd, Plugin plugin) {
    	try {
    		PluginCommand cmd = (PluginCommand) _cmd;
    		cmd.setTabCompleter((TabCompleter) _cmd);
    		
			Method commandMap = plugin.getServer().getClass().getMethod("getCommandMap", null);
			Object cmdmap = commandMap.invoke(plugin.getServer(), null);
			Method register = cmdmap.getClass().getMethod("register", String.class, Command.class);
			register.invoke(cmdmap, cmd.getName(), cmd);
		} catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e) {
            e.printStackTrace();
		}
    }
}

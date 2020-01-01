package de.crafttogether.ctsuite.bukkit.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CommandGamemode extends BukkitCommand implements TabCompleter {
	
    public CommandGamemode() {
        super("joscha");
        
        ArrayList<String> aliases = new ArrayList<String>();
        aliases.add("josch");
        aliases.add("jtothemofuckingoscha");
        
        this.description = "Just want to test some shit";
        this.usageMessage = "/joscha <gameMode> <player>";
        this.setPermission("ctsuite.commands.joscha");
        this.setAliases(aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!sender.hasPermission(this.getPermission())) {
            sender.sendMessage(ChatColor.RED + "You dont have permission to do this!");
            return true;
        }
        
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players can do this!");
            return false;
        }

        sender.sendMessage(ChatColor.GREEN + "Vallah gute Plugin");
        return false;
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
    	ArrayList<String> newList = new ArrayList<String>();
    	ArrayList<String> proposals = new ArrayList<String>();
    	
		proposals.add("test");
		proposals.add("eins");
		proposals.add("zwei");
		proposals.add("drei");
    	
        if (args.length < 1 || args[args.length - 1].equals(""))
            newList = proposals;
        else {
            for (String value : proposals) {
                if (value.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                    newList.add(value);
            }
        }
        
        return newList;
    }
}
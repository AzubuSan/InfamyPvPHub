package org.gd.InfamyPvPHub.CommandWrapper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gd.InfamyPvPHub.InfamyPvPHub;
import org.gd.InfamyPvPHub.Utils.Utils;

public abstract class CommandWrapper implements CommandExecutor {
	
	private boolean requiresPlayer;
	private String permission;
	private String usage;
	
	/**
	 * Creates a wrapper for a command that checks if the sender has
	 * permission to use this command, and if the command requires a player to
	 * be the executor. If the onExecte returns false, then the sender will be
	 * displayed with the correct usage
	 * 
	 * @param name The command
	 * @param permission The required permission (null for none)
	 * @param requiresPlayer Does the command require a player
	 * @param usage The required usage
	 * @author Vilsol
	 */
	public CommandWrapper(String name, String permission, boolean requiresPlayer, String usage) {
		this.requiresPlayer = requiresPlayer;
		this.permission = permission;
		this.usage = usage;
		if(InfamyPvPHub.getPlugin().getCommand(name) != null)
			InfamyPvPHub.getPlugin().getCommand(name).setExecutor(this);
		else
			InfamyPvPHub.getPlugin().getLogger().severe((new StringBuilder("Failed registering command: ")).append(name).toString());
	}
	
	/**
	 * Executed when console dispatches command
	 * 
	 * @param sender Console
	 * @param command Command
	 * @param label Command name
	 * @param args Arguments
	 * @return If false, the correct usage will be displayed
	 * @author Vilsol
	 */
	public abstract boolean onExecute(CommandSender sender, Command command, String label, String[] args);
	
	/**
	 * Executed when a player dispatches command
	 * 
	 * @param player Player
	 * @param command Command
	 * @param label Command name
	 * @param args Arguments
	 * @return If false, the correct usage will be displayed
	 * @author Vilsol
	 */
	public abstract boolean onExecute(Player player, Command command, String s, String as[]);
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
		if(permission != null && !sender.hasPermission(permission)) {
			sender.sendMessage((new StringBuilder(String.valueOf(Utils.prefixe))).append("You don't have permission to do that!").toString());
			return true;
		}
		if(requiresPlayer) {
			if(sender instanceof Player) {
				if(!onExecute((Player) sender, command, label, args)) sender.sendMessage((new StringBuilder(String.valueOf(Utils.prefixe))).append("Correct usage: ").append(usage).toString());
			} else {
				sender.sendMessage((new StringBuilder(String.valueOf(Utils.prefixe))).append("You must be a player to use this command!").toString());
			}
		} else if(!onExecute(sender, command, label, args)) sender.sendMessage((new StringBuilder(String.valueOf(Utils.prefixe))).append("Correct usage: ").append(usage).toString());
		return true;
	}
	
}

package org.gd.InfamyPvPHub.CommandWrapper.Commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gd.InfamyPvPHub.InfamyPvPHub;
import org.gd.InfamyPvPHub.CommandWrapper.CommandWrapper;
import org.gd.InfamyPvPHub.Utils.CM;
import org.gd.InfamyPvPHub.Utils.Lib;

public class BossBarCommand extends CommandWrapper {

	public InfamyPvPHub pl;

	public BossBarCommand(InfamyPvPHub plugin) {
		super("hubbar", "infamyhub.hubbar", true, "/Hubbar");
		this.pl = plugin;
	}

	@Override
	public boolean onExecute(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0){
			printHelp(sender);
		} else {
			// Command: ADD
			if (args[0].equalsIgnoreCase("add")) {

				if (!sender.hasPermission("infamyhub.add")) {
					sender.sendMessage(CM.noperm);
					return true;
				}
				if (args.length > 2) {
					if (isInteger(args[1])) {
						int percent = Integer.parseInt(args[1]);
						if (percent < 101 && percent > 0) {

							List<String> listmsg = new ArrayList<>();
							for (int i = 2;i <= (args.length-1);i++) {
								listmsg.add(args[i]);
							}

							String textmsg = StringUtils.join(listmsg, " ");
							Bukkit.broadcastMessage(textmsg);
							List<String> rawmessage = new ArrayList<>();
							rawmessage.add(textmsg);
							rawmessage.add(args[1]);


							List<String> message = new ArrayList<>();
							message.add(textmsg);
							message.add(args[1]);
							message.set(0, ChatColor.translateAlternateColorCodes('&', message.get(0)));

							CM.messages.add(message);
							CM.rawmessages.add(rawmessage);
							CM.config.set("BossMessage.Messages", CM.rawmessages);
							CM.save();
							sender.sendMessage(ChatColor.GREEN + "Your message was successfully added!");

						} else {
							sender.sendMessage(ChatColor.RED + "The percent must be between 1 and 100!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + args[1] + ChatColor.DARK_RED + " is not a valid number!");
					}
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/Hubbar add <percent> <message>");
				}

			}

			else if (args[0].equalsIgnoreCase("remove")) {

				if (!sender.hasPermission("InfamyPvPHub.remove")) {
					sender.sendMessage(CM.noperm);
					return true;
				}
				if (args.length == 2) {
					if (isInteger(args[1])) {
						int num = Integer.parseInt(args[1]);
						if (CM.messages.size() >= num && num > 0) {
							CM.messages.remove(num - 1);
							CM.rawmessages.remove(num - 1);
							CM.config.set("InfamyPvPHub.Messages", CM.rawmessages);
							CM.save();
							Lib.resetCount();
							sender.sendMessage(ChatColor.GREEN + "Message #" + num + " was successfully removed!");
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "Message " + ChatColor.RED + args[1] + ChatColor.DARK_RED + "was not found!");
						}
					}
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/Hubbar remove <#>");
				}
			}
			// Command: LIST
			else if (args[0].equalsIgnoreCase("list")) {

				if (!sender.hasPermission("InfamyPvPHub.list")) {
					sender.sendMessage(CM.noperm);
					return true;
				}

				sender.sendMessage(ChatColor.GREEN + "=== Message list ===");
				int i = 0;
				for (List<String> msg:CM.messages) {
					i++;
					sender.sendMessage(ChatColor.DARK_GREEN + "" + i + ". " + ChatColor.RESET + msg.get(0));
				}
			}
			// Command: RELOAD
			else if (args[0].equalsIgnoreCase("reload")) {

				if (!sender.hasPermission("InfamyPvPHub.reload")) {
					sender.sendMessage(CM.noperm);
					return true;
				}

				InfamyPvPHub.plm.disablePlugin(InfamyPvPHub.plm.getPlugin("InfamyPvPHub"));
				InfamyPvPHub.plm.enablePlugin(InfamyPvPHub.plm.getPlugin("InfamyPvPHub"));
				sender.sendMessage(ChatColor.GREEN + "Plugin was successfully reloaded!");

			}
			// Command: HELP
			else if (args[0].equalsIgnoreCase("help")) {

				printHelp(sender);

			}
			// Command: INTERVAL
			else if (args[0].equalsIgnoreCase("interval")) {

				if (!sender.hasPermission("InfamyPvPHub.interval")) {
					sender.sendMessage(CM.noperm);
					return true;
				}
				if (args.length == 2) {
					if (isInteger(args[1])) {
						int interval = Integer.parseInt(args[1]);
						CM.config.set("InfamyPvPHub.Interval", interval);
						CM.save();
						CM.interval = interval;
						sender.sendMessage(ChatColor.DARK_GREEN + "The Interval is now set to: " + ChatColor.GREEN + args[1]);
					} else {
						sender.sendMessage(ChatColor.RED + args[1] + ChatColor.DARK_RED + " is not a valid number!");
					}
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/Hubbar interval <ticks>");
				}

			}
			// Command: SHOW
			else if (args[0].equalsIgnoreCase("show")) {

				if (!sender.hasPermission("InfamyPvPHub.show")) {
					sender.sendMessage(CM.noperm);
					return true;
				}
				if (args.length == 2) {
					if (isInteger(args[1])) {
						int show = Integer.parseInt(args[1]);
						CM.config.set("InfamyPvPHub.Show", show);
						CM.save();
						CM.show = show;
						sender.sendMessage(ChatColor.DARK_GREEN + "The Show is now set to: " + ChatColor.GREEN + args[1]);
					} else {
						sender.sendMessage(ChatColor.RED + args[1] + ChatColor.DARK_RED + " is not a valid number!");
					}
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/Hubbar show <ticks>");
				}

			} else {
				sender.sendMessage(ChatColor.DARK_RED + "Invalid command! Usage: " + ChatColor.RED + "/Hubbar help");
			}
		}

		return false;
	}


	@Override
	public boolean onExecute(Player player, Command cmd, String label, String[] args) {
		if (args.length == 0){
			printHelp(player);
		} else {
			// Command: ADD
			if (args[0].equalsIgnoreCase("add")) {

				if (!player.hasPermission("infamyhub.add")) {
					player.sendMessage(CM.noperm);
					return true;
				}
				if (args.length > 2) {
					if (isInteger(args[1])) {
						int percent = Integer.parseInt(args[1]);
						if (percent < 101 && percent > 0) {

							List<String> listmsg = new ArrayList<>();
							for (int i = 2;i <= (args.length-1);i++) {
								listmsg.add(args[i]);
							}

							String textmsg = StringUtils.join(listmsg, " ");
							Bukkit.broadcastMessage(textmsg);
							List<String> rawmessage = new ArrayList<>();
							rawmessage.add(textmsg);
							rawmessage.add(args[1]);


							List<String> message = new ArrayList<>();
							message.add(textmsg);
							message.add(args[1]);
							message.set(0, ChatColor.translateAlternateColorCodes('&', message.get(0)));

							CM.messages.add(message);
							CM.rawmessages.add(rawmessage);
							CM.config.set("BossMessage.Messages", CM.rawmessages);
							CM.save();
							player.sendMessage(ChatColor.GREEN + "Your message was successfully added!");

						} else {
							player.sendMessage(ChatColor.RED + "The percent must be between 1 and 100!");
						}
					} else {
						player.sendMessage(ChatColor.RED + args[1] + ChatColor.DARK_RED + " is not a valid number!");
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/Hubbar add <percent> <message>");
				}

			}

			else if (args[0].equalsIgnoreCase("remove")) {

				if (!player.hasPermission("InfamyPvPHub.remove")) {
					player.sendMessage(CM.noperm);
					return true;
				}
				if (args.length == 2) {
					if (isInteger(args[1])) {
						int num = Integer.parseInt(args[1]);
						if (CM.messages.size() >= num && num > 0) {
							CM.messages.remove(num - 1);
							CM.rawmessages.remove(num - 1);
							CM.config.set("InfamyPvPHub.Messages", CM.rawmessages);
							CM.save();
							Lib.resetCount();
							player.sendMessage(ChatColor.GREEN + "Message #" + num + " was successfully removed!");
						} else {
							player.sendMessage(ChatColor.DARK_RED + "Message " + ChatColor.RED + args[1] + ChatColor.DARK_RED + "was not found!");
						}
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/Hubbar remove <#>");
				}
			}
			// Command: LIST
			else if (args[0].equalsIgnoreCase("list")) {

				if (!player.hasPermission("InfamyPvPHub.list")) {
					player.sendMessage(CM.noperm);
					return true;
				}

				player.sendMessage(ChatColor.GREEN + "=== Message list ===");
				int i = 0;
				for (List<String> msg:CM.messages) {
					i++;
					player.sendMessage(ChatColor.DARK_GREEN + "" + i + ". " + ChatColor.RESET + msg.get(0));
				}
			}
			// Command: RELOAD
			else if (args[0].equalsIgnoreCase("reload")) {

				if (!player.hasPermission("InfamyPvPHub.reload")) {
					player.sendMessage(CM.noperm);
					return true;
				}

				InfamyPvPHub.plm.disablePlugin(InfamyPvPHub.plm.getPlugin("InfamyPvPHub"));
				InfamyPvPHub.plm.enablePlugin(InfamyPvPHub.plm.getPlugin("InfamyPvPHub"));
				player.sendMessage(ChatColor.GREEN + "Plugin was successfully reloaded!");

			}
			// Command: HELP
			else if (args[0].equalsIgnoreCase("help")) {

				printHelp(player);

			}
			// Command: INTERVAL
			else if (args[0].equalsIgnoreCase("interval")) {

				if (!player.hasPermission("InfamyPvPHub.interval")) {
					player.sendMessage(CM.noperm);
					return true;
				}
				if (args.length == 2) {
					if (isInteger(args[1])) {
						int interval = Integer.parseInt(args[1]);
						CM.config.set("InfamyPvPHub.Interval", interval);
						CM.save();
						CM.interval = interval;
						player.sendMessage(ChatColor.DARK_GREEN + "The Interval is now set to: " + ChatColor.GREEN + args[1]);
					} else {
						player.sendMessage(ChatColor.RED + args[1] + ChatColor.DARK_RED + " is not a valid number!");
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/Hubbar interval <ticks>");
				}

			}
			// Command: SHOW
			else if (args[0].equalsIgnoreCase("show")) {

				if (!player.hasPermission("InfamyPvPHub.show")) {
					player.sendMessage(CM.noperm);
					return true;
				}
				if (args.length == 2) {
					if (isInteger(args[1])) {
						int show = Integer.parseInt(args[1]);
						CM.config.set("InfamyPvPHub.Show", show);
						CM.save();
						CM.show = show;
						player.sendMessage(ChatColor.DARK_GREEN + "The Show is now set to: " + ChatColor.GREEN + args[1]);
					} else {
						player.sendMessage(ChatColor.RED + args[1] + ChatColor.DARK_RED + " is not a valid number!");
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/Hubbar show <ticks>");
				}

			} else {
				player.sendMessage(ChatColor.DARK_RED + "Invalid command! Usage: " + ChatColor.RED + "/Hubbar help");
			}
		}

		return false;
	}

	public static void printHelp(CommandSender player) {

		player.sendMessage(ChatColor.DARK_AQUA + "=0=" + ChatColor.GOLD + " InfamyPvPHub Plugin by Global Development's §cDeGambler " + ChatColor.DARK_AQUA + "=0=");
		player.sendMessage(ChatColor.DARK_GREEN + "Usage: " + ChatColor.GREEN + "/Hubbar <params>");
		if (player.hasPermission("infamyhub.add")) {
			player.sendMessage(ChatColor.GOLD + "/Hubbar add <percent> <message> - adds a message");
		}
		if (player.hasPermission("infamyhub.remove")) {
			player.sendMessage(ChatColor.GOLD + "/Hubbar remove <#> - removes a message");
		}
		if (player.hasPermission("infamyhub.list")) {
			player.sendMessage(ChatColor.GOLD + "/Hubbar list - lists the messages");
		}
		if (player.hasPermission("infamyhub.reload")) {
			player.sendMessage(ChatColor.GOLD + "/Hubbar reload - reloads the plugin");
		}
		if (player.hasPermission("infamyhub.interval")) {
			player.sendMessage(ChatColor.GOLD + "/Hubbar interval <ticks> - sets the interval between messages");
		}
		if (player.hasPermission("infamyhub.show")) {
			player.sendMessage(ChatColor.GOLD + "/Hubbar show <ticks> - sets the message broadcast length");
		}
	}

	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		}
		return true;
	}
}

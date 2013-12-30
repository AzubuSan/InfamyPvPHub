package org.gd.InfamyPvPHub.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.gd.InfamyPvPHub.InfamyPvPHub;

public class VanishPlayersEvent implements Listener {


	public static InfamyPvPHub plugin;

	@SuppressWarnings("static-access")
	public VanishPlayersEvent(InfamyPvPHub p) {
		this.plugin = p;
	}

	public List<String> hidesothers = new ArrayList<String>();
	public List<String> onCooldown = new ArrayList<String>();

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if (event.getPlayer().getItemInHand() != null) {
			if (event.getPlayer().getItemInHand().hasItemMeta()) {
				if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName() == plugin.getConfig().getString("Hide_Item.name").replaceAll("&", "§")) {
					if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
						if (!onCooldown.contains(event.getPlayer().getName())) {
							if (!hidesothers.contains(event.getPlayer().getName())) {
								for (Player others : Bukkit.getServer().getOnlinePlayers()) {
									event.getPlayer().hidePlayer(others);
									event.getPlayer().sendMessage(ChatColor.GOLD + "All other players have been hidden!");
									event.setCancelled(true);
									hidesothers.add(event.getPlayer().getName());
								}
							}
						}
						if (!onCooldown.contains(event.getPlayer().getName())) {
							if (hidesothers.contains(event.getPlayer().getName())) {
								for (Player others : Bukkit.getServer().getOnlinePlayers()) {
									event.getPlayer().showPlayer(others);
									event.setCancelled(true);
									event.getPlayer().sendMessage(ChatColor.AQUA + "All other players have been revealed!");
									hidesothers.remove(event.getPlayer().getName());
								}
							}
						}
						if (!onCooldown.contains(event.getPlayer().getName())) {
							if (plugin != null) {
								event.getPlayer().sendMessage(ChatColor.DARK_RED + "Error" + ChatColor.RED + ": Please wait 5 seconds before using this tool again!");
								Bukkit.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable() {

									@Override
									public void run() {
										onCooldown.remove(event.getPlayer().getName());
									}
								}, 100L); 
							}
						}
					}
				}
			}
		}
	}
}	
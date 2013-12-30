package org.gd.InfamyPvPHub;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.gd.InfamyPvPHub.BarAPI.BarAPI;
import org.gd.InfamyPvPHub.CommandWrapper.Commands.BossBarCommand;
import org.gd.InfamyPvPHub.Events.GiveVanishItemEvent;
import org.gd.InfamyPvPHub.Events.VanishPlayersEvent;
import org.gd.InfamyPvPHub.Utils.CM;
import org.gd.InfamyPvPHub.Utils.Lib;
import org.gd.InfamyPvPHub.Utils.RandomExt;

public class InfamyPvPHub extends JavaPlugin implements Listener {

	private static InfamyPvPHub plugin;

	public static String PREFIX = "§6InfamyPvPHub";
	RandomExt random = new RandomExt(new Random());
	public static PluginManager plm = Bukkit.getPluginManager();
	public static BukkitScheduler scr = Bukkit.getScheduler();;
	public static List<String> current = new ArrayList<>();
	public static boolean isset = false;

	public void onEnable() {
		plugin = this;

		saveDefaultConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(new GiveVanishItemEvent(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new VanishPlayersEvent(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new BarAPI(this), this);

		getCommand("hubbar").setExecutor(new BossBarCommand(this));

		File datadir = new File(getDataFolder() + File.separator + "interndata");

		if (!datadir.exists()) {
			try {
				datadir.mkdir();
				CM.createConfig();
				CM.readConfig();

				getLogger().info("Created data directory!");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		CM.createConfig();
		CM.readConfig();
		
		startProcess();
	}

	public static InfamyPvPHub getPlugin() {
		return plugin;
	}

	private void startProcess() {
		if (!CM.enabled) {
			for (int i = 0; 3 > i ;i++)
				getLogger().warning("Disabled: to enable set 'enabled' in the BossMessage config to 'true'");
			return;
		}

		BukkitScheduler scr = Bukkit.getScheduler();
		scr.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				current = Lib.getMessage();
				Lib.setMsg(current);
				isset = true;
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						for (Player p:Bukkit.getOnlinePlayers()) {
							BarAPI.removeBar(p);
						}
						isset = false;
					}
				}, CM.show*50);
			}
		}, 20L, CM.interval + CM.show + 2L);

	}

	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent e) {
		Player p = e.getPlayer();
		if (!CM.enabled) {
			return;
		}

		if (CM.whitelist) {
			List<String> worlds = CM.worlds;
			if (worlds.contains(e.getTo().getWorld().getName())) {
				Lib.setPlayerMsg(p, current);
			} else {
				BarAPI.removeBar(p);
			}
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		if (!CM.enabled) {
			return;
		}

		if (CM.whitelist) {
			List<String> worlds = CM.worlds;
			if (worlds.contains(e.getTo().getWorld().getName())) {
				Lib.setPlayerMsg(p, current);
			} else {
				BarAPI.removeBar(p);
			}
		}
	}


	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!CM.enabled) {
			return;
		}
		if (isset) {
			if (CM.whitelist) {
				if (CM.worlds.contains(p.getWorld().getName())) {
					Lib.setPlayerMsg(p, current);
				}
			} else {
				Lib.setPlayerMsg(p, current);
			}
		}
	}


}

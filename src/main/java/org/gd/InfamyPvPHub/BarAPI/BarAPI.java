package org.gd.InfamyPvPHub.BarAPI;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.gd.InfamyPvPHub.InfamyPvPHub;
import org.gd.InfamyPvPHub.BarAPI.nms.FakeDragon;

public class BarAPI implements Listener {

	private static InfamyPvPHub plugin;

	private static HashMap<String, FakeDragon> players = new HashMap<String, FakeDragon>();
	private static HashMap<String, Integer> timers = new HashMap<String, Integer>();


	public BarAPI(InfamyPvPHub pl) {
		this.plugin = pl;
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void PlayerLoggout(PlayerQuitEvent event) {
		quit(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event) {
		quit(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(final PlayerTeleportEvent event) {
		handleTeleport(event.getPlayer(), event.getTo().clone());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(final PlayerRespawnEvent event) {
		handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
	}

	private void handleTeleport(final Player player, final Location loc) {

		if (!hasBar(player))
			return;

		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

			@Override
			public void run() {
				// Check if the player still has a dragon after the two ticks! ;)
				if (!hasBar(player))
					return;

		FakeDragon oldDragon = getDragon(player, "");

		float health = oldDragon.health;
		String message = oldDragon.name;

		Util.sendPacket(player, getDragon(player, "").getDestroyPacket());

		players.remove(player.getName());

		FakeDragon dragon = addDragon(player, loc, message);
		dragon.health = health;

		sendDragon(dragon, player);
			}

		}, 2L);
	}

	private void quit(Player player) {
		removeBar(player);
	}

	/**
	 * Set a message for the given player.<br>
	 * It will remain there until the player logs off or another plugin overrides it.<br>
	 * This method will show a full health bar and will cancel any running timers.
	 * 
	 * @param player
	 *            The player who should see the given message.
	 * @param message
	 *            The message shown to the player.<br>
	 *            Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
	 *            It will be cut to that size automatically.
	 */
	public static void setMessage(Player player, String message) {
		FakeDragon dragon = getDragon(player, message);

		dragon.name = cleanMessage(message);
		dragon.health = FakeDragon.MAX_HEALTH;

		cancelTimer(player);

		sendDragon(dragon, player);

	}

	/**
	 * Set a message for the given player.<br>
	 * It will remain there until the player logs off or another plugin overrides it.<br>
	 * This method will show a health bar using the given percentage value and will cancel any running timers.
	 * 
	 * @param player
	 *            The player who should see the given message.
	 * @param message
	 *            The message shown to the player.<br>
	 *            Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
	 *            It will be cut to that size automatically.
	 * @param percent
	 *            The percentage of the health bar filled.<br>
	 *            This value must be between 0F and 100F (inclusive).
	 */
	public static void setMessage(Player player, String message, float percent) {
		FakeDragon dragon = getDragon(player, message);

		dragon.name = cleanMessage(message);
		dragon.health = (percent / 100f) * FakeDragon.MAX_HEALTH;

		cancelTimer(player);

		sendDragon(dragon, player);
	}

	/**
	 * Set a message for the given player.<br>
	 * It will remain there until the player logs off or another plugin overrides it.<br>
	 * This method will use the health bar as a decreasing timer, all previously started timers will be cancelled.<br>
	 * The timer starts with a full bar.<br>
	 * The health bar will be removed automatically if it hits zero.
	 * 
	 * @param player
	 *            The player who should see the given timer/message.
	 * @param message
	 *            The message shown to the player.<br>
	 *            Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
	 *            It will be cut to that size automatically.
	 * @param seconds
	 *            The amount of seconds displayed by the timer.<br>
	 *            Supports values from 1 (inclusive) to 200 (inclusive).
	 */
	public static void setMessage(final Player player, String message, int seconds) {
		FakeDragon dragon = getDragon(player, message);

		dragon.name = cleanMessage(message);
		dragon.health = FakeDragon.MAX_HEALTH;

		final int dragonHealthMinus = FakeDragon.MAX_HEALTH / seconds;

		cancelTimer(player);

		timers.put(player.getName(), Bukkit.getScheduler().runTaskTimer(plugin, new BukkitRunnable() {

			@Override
			public void run() {
				FakeDragon drag = getDragon(player, "");
				drag.health -= dragonHealthMinus;

				if (drag.health <= 0) {
					removeBar(player);
					cancelTimer(player);
				} else {
					sendDragon(drag, player);
				}
			}

		}, 20L, 20L).getTaskId());

		sendDragon(dragon, player);
	}

	/**
	 * Checks whether the given player has a bar.
	 * 
	 * @param player
	 *            The player who should be checked.
	 * @return True, if the player has a bar, False otherwise.
	 */
	public static boolean hasBar(Player player) {
		return players.get(player.getName()) != null;
	}

	/**
	 * Removes the bar from the given player.<br>
	 * If the player has no bar, this method does nothing.
	 * 
	 * @param player
	 *            The player whose bar should be removed.
	 */
	public static void removeBar(Player player) {
		if (!hasBar(player))
			return;

		Util.sendPacket(player, getDragon(player, "").getDestroyPacket());

		players.remove(player.getName());

		cancelTimer(player);
	}

	/**
	 * Modifies the health of an existing bar.<br>
	 * If the player has no bar, this method does nothing.
	 * 
	 * @param player
	 *            The player whose bar should be modified.
	 * @param percent
	 *            The percentage of the health bar filled.<br>
	 *            This value must be between 0F and 100F (inclusive).
	 */
	public static void setHealth(Player player, float percent) {
		if (!hasBar(player))
			return;

		FakeDragon dragon = getDragon(player, "");
		dragon.health = (percent / 100f) * FakeDragon.MAX_HEALTH;

		cancelTimer(player);

		sendDragon(dragon, player);
	}

	/**
	 * Get the health of an existing bar.
	 * 
	 * @param player
	 *            The player whose bar's health should be returned.
	 * @return The current absolute health of the bar.<br>
	 *         If the player has no bar, this method returns -1.
	 */
	public static float getHealth(Player player) {
		if (!hasBar(player))
			return -1;

		return getDragon(player, "").health;
	}

	/**
	 * Get the message of an existing bar.
	 * 
	 * @param player
	 *            The player whose bar's message should be returned.
	 * @return The current message displayed to the player.<br>
	 *         If the player has no bar, this method returns an empty string.
	 */
	public static String getMessage(Player player) {
		if (!hasBar(player))
			return "";

		return getDragon(player, "").name;
	}

	private static String cleanMessage(String message) {
		if (message.length() > 64)
			message = message.substring(0, 63);

		return message;
	}

	private static void cancelTimer(Player player) {
		Integer timerID = timers.remove(player.getName());

		if (timerID != null) {
			Bukkit.getScheduler().cancelTask(timerID);
		}
	}

	private static void sendDragon(FakeDragon dragon, Player player) {
		Util.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
		Util.sendPacket(player, dragon.getTeleportPacket(player.getLocation().add(0, -200, 0)));
	}

	private static FakeDragon getDragon(Player player, String message) {
		if (hasBar(player)) {
			return players.get(player.getName());
		} else
			return addDragon(player, cleanMessage(message));
	}

	private static FakeDragon addDragon(Player player, String message) {
		FakeDragon dragon = Util.newDragon(message, player.getLocation().add(0, -200, 0));

		Util.sendPacket(player, dragon.getSpawnPacket());

		players.put(player.getName(), dragon);

		return dragon;
	}

	private static FakeDragon addDragon(Player player, Location loc, String message) {
		FakeDragon dragon = Util.newDragon(message, loc.add(0, -200, 0));

		Util.sendPacket(player, dragon.getSpawnPacket());

		players.put(player.getName(), dragon);

		return dragon;
	}
}


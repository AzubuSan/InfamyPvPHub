package org.gd.InfamyPvPHub.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.gd.InfamyPvPHub.InfamyPvPHub;

public class GiveVanishItemEvent implements Listener {

	private InfamyPvPHub pl;

	public GiveVanishItemEvent(InfamyPvPHub p) {
		pl = p;
		this.pl = p;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent event) {
		if (pl.getConfig() != null) {
			if (pl.getConfig().getString("Hide_Item.itemId") != null) {

				ItemStack vItem = new ItemStack(pl.getConfig().getInt("Hide_Item.itemId"), 1);
				ItemMeta vItemMeta = vItem.getItemMeta();
				vItemMeta.setDisplayName(pl.getConfig().getString("Hide_Item.name").replaceAll("&", "§"));
				List<String> Lore = new ArrayList<String>();
				Lore.add(pl.getConfig().getString("Hide_Item.lore").replaceAll("&", "§"));
				vItemMeta.setLore(Lore);
				vItem.setItemMeta(vItemMeta);
				if (event.getPlayer().getInventory().getItemInHand().equals(Material.AIR)) {
					event.getPlayer().getInventory().addItem(vItem);
					event.getPlayer().sendMessage(ChatColor.RED + "Please make room in your inventory for the vanish item!");
					return;
				}
				
			}
		}
	}
}

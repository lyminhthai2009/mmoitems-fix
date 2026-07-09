package net.Indyuce.mmoitems.api.edition.input;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.edition.Edition;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEdition extends PlayerInputHandler implements Listener {
	private Listener paperListener;

	/**
	 * Allows to retrieve player input using chat messages
	 * 
	 * @param edition The type of data being edited
	 */
	public ChatEdition(Edition edition) {
		super(edition);

		if (MythicLib.plugin.getVersion().isPaper()) {
			try {
				Class<?> clazz = Class.forName("net.Indyuce.mmoitems.paper.PaperChatEdition");
				paperListener = (Listener) clazz.getConstructor(ChatEdition.class).newInstance(this);
				Bukkit.getPluginManager().registerEvents(paperListener, MMOItems.plugin);
			} catch (Exception e) {
				Bukkit.getPluginManager().registerEvents(this, MMOItems.plugin);
			}
		} else {
			Bukkit.getPluginManager().registerEvents(this, MMOItems.plugin);
		}
	}

	@Override
	public void close() {
		HandlerList.unregisterAll(this);
		if (paperListener != null) {
			HandlerList.unregisterAll(paperListener);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void a(AsyncPlayerChatEvent event) {
		if (getPlayer() != null && event.getPlayer().equals(getPlayer())) {
			event.setCancelled(true);
			Bukkit.getScheduler().runTask(MMOItems.plugin, () -> registerInput(event.getMessage()));
		}
	}

	// cancel stat edition when opening any gui
	@EventHandler
	public void b(InventoryOpenEvent event) {
		if (event.getPlayer().equals(getPlayer()))
			close();
	}
}

package net.Indyuce.mmoitems.paper;

import net.Indyuce.mmoitems.api.edition.input.ChatEdition;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import net.Indyuce.mmoitems.MMOItems;

public class PaperChatEdition implements Listener {
    private final ChatEdition parent;

    public PaperChatEdition(ChatEdition parent) {
        this.parent = parent;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (parent.getPlayer() != null && event.getPlayer().equals(parent.getPlayer())) {
            event.setCancelled(true);
            String message = PlainTextComponentSerializer.plainText().serialize(event.message());
            Bukkit.getScheduler().runTask(MMOItems.plugin, () -> parent.registerInput(message));
        }
    }
}

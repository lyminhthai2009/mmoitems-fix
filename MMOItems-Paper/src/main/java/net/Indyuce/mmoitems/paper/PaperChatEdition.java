package net.Indyuce.mmoitems.paper;

import net.Indyuce.mmoitems.api.edition.input.ChatEdition;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import net.Indyuce.mmoitems.MMOItems;

import net.Indyuce.mmoitems.api.edition.Edition;
import net.Indyuce.mmoitems.api.edition.StatEdition;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class PaperChatEdition implements Listener {
    private final ChatEdition parent;

    public PaperChatEdition(ChatEdition parent) {
        this.parent = parent;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (parent.getPlayer() != null && event.getPlayer().equals(parent.getPlayer())) {
            event.setCancelled(true);
            
            boolean keepColors = false;
            Edition edition = parent.getEdition();
            if (edition instanceof StatEdition) {
                String statId = ((StatEdition) edition).getStat().getId();
                if (statId.equals("NAME") || statId.equals("LORE") || statId.equals("LORE_FORMAT") || statId.equals("DISPLAY_NAME")) {
                    keepColors = true;
                }
            }

            String message;
            if (keepColors) {
                message = LegacyComponentSerializer.legacyAmpersand().serialize(event.message());
            } else {
                message = PlainTextComponentSerializer.plainText().serialize(event.message());
            }

            Bukkit.getScheduler().runTask(MMOItems.plugin, () -> parent.registerInput(message));
        }
    }
}

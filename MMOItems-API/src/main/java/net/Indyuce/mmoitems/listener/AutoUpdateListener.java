package net.Indyuce.mmoitems.listener;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ReforgeOptions;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.api.util.MMOItemReforger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class AutoUpdateListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        updateItemIfNeeded(player, item);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Delay checking inventory slightly to not lag the login process
        Bukkit.getScheduler().runTaskLater(MMOItems.plugin, () -> {
            if (!player.isOnline()) return;
            PlayerInventory inv = player.getInventory();
            for (int i = 0; i < inv.getSize(); i++) {
                ItemStack item = inv.getItem(i);
                updateItemIfNeeded(player, item);
            }
        }, 20L); // 1 second delay
    }

    private boolean updateItemIfNeeded(Player player, ItemStack item) {
        if (item == null || item.getType().isAir()) return false;

        NBTItem nbtItem = NBTItem.get(item);
        if (!nbtItem.hasType()) return false;

        String type = nbtItem.getString("MMOITEMS_ITEM_TYPE");
        String id = nbtItem.getString("MMOITEMS_ITEM_ID");
        if (id == null || id.isEmpty() || type == null || type.isEmpty()) return false;

        MMOItemTemplate template = MMOItems.plugin.getTemplates().getTemplate(type, id);
        if (template == null) return false;

        int currentRevId = nbtItem.getInteger("MMOITEMS_REVISION_ID");
        if (currentRevId < template.getRevisionId()) {
            // Needs update!
            MMOItemReforger reforger = new MMOItemReforger(item, nbtItem);
            reforger.update(player, new ReforgeOptions());
            return true;
        }
        return false;
    }
}

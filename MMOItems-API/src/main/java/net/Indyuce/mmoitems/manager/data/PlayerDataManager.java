package net.Indyuce.mmoitems.manager.data;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.data.DefaultOfflineDataHolder;
import io.lumine.mythic.lib.data.SynchronizedDataManager;
import io.lumine.mythic.lib.profile.DefaultProfileDataModule;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.player.PlayerData;
import org.jetbrains.annotations.NotNull;

public class PlayerDataManager extends SynchronizedDataManager<PlayerData, DefaultOfflineDataHolder> {
    public PlayerDataManager(MMOItems plugin) {
        super(plugin);
    }

    @Override
    public PlayerData newPlayerData(@NotNull MMOPlayerData mmoPlayerData) {
        return new PlayerData(mmoPlayerData);
    }

    @Override
    public Object newProfileDataModule() {
        return new DefaultProfileDataModule(this);
    }

    @Override
    public void loadEmptyPlayerData(@NotNull PlayerData playerData) {
        // Nothing to do
    }

    @Override
    protected void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        try {
            super.onQuit(event);
        } catch (NullPointerException e) {
            // Ignore NPE when player disconnects before data is fully loaded
        }
    }
}

package net.Indyuce.mmoitems.api.util;

import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class TemplateAutoUpdater {
    private static File file;
    private static YamlConfiguration config;

    public static void initialize() {
        file = new File(MMOItems.plugin.getDataFolder(), "template-hashes.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                MMOItems.plugin.getLogger().log(Level.SEVERE, "Could not create template-hashes.yml", e);
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static synchronized int getAndUpdateRevisionId(String type, String id, int currentHash, int originalRevId) {
        if (config == null) {
            initialize();
        }

        String path = type + "." + id;
        if (!config.contains(path)) {
            config.set(path + ".hash", currentHash);
            config.set(path + ".revision-id", originalRevId);
            save();
            return originalRevId;
        }

        int savedHash = config.getInt(path + ".hash", 0);
        int savedRevId = config.getInt(path + ".revision-id", originalRevId);

        int finalRevId;
        boolean changed = false;

        if (currentHash != savedHash) {
            finalRevId = Math.max(originalRevId, savedRevId + 1);
            config.set(path + ".hash", currentHash);
            config.set(path + ".revision-id", finalRevId);
            changed = true;
        } else {
            finalRevId = Math.max(originalRevId, savedRevId);
            if (finalRevId != savedRevId) {
                config.set(path + ".revision-id", finalRevId);
                changed = true;
            }
        }

        if (changed) {
            save();
        }

        return finalRevId;
    }

    private static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            MMOItems.plugin.getLogger().log(Level.SEVERE, "Could not save template-hashes.yml", e);
        }
    }
}

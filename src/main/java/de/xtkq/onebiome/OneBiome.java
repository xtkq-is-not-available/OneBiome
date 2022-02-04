package de.xtkq.onebiome;

import de.xtkq.onebiome.events.EventManager;
import de.xtkq.onebiome.provider.instances.OneBiomeProvider;
import de.xtkq.onebiome.settings.SettingsManager;
import de.xtkq.onebiome.utils.UpdateUtils;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class OneBiome extends JavaPlugin {

    private static EventManager eventManager;

    @Override
    public BiomeProvider getDefaultBiomeProvider(String worldName, String id) {
        return new OneBiomeProvider(this, id);
    }

    @Override
    public void onEnable() {
        SettingsManager settingsManager = new SettingsManager(this);
        UpdateUtils updateUtils = new UpdateUtils(this);
        eventManager = new EventManager(this);

        if (settingsManager.getConfiguration().getCheckForUpdates()) {
            updateUtils.checkForUpdates();
            eventManager.initialize();
        }

    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        eventManager.terminate();
    }
}

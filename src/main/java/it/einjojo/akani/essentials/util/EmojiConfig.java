package it.einjojo.akani.essentials.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EmojiConfig {

    private final JavaPlugin javaPlugin;
    private final File configFile;
    private YamlConfiguration config;

    public EmojiConfig(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        this.configFile = new File(javaPlugin.getDataFolder(), "emojis.yml");
        load();
    }

    public void load() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public File configFile() {
        return configFile;
    }

    public YamlConfiguration config() {
        return config;
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            javaPlugin.getSLF4JLogger().error("Failed to save emoji-config", e);
        }
    }
}

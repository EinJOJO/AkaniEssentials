package it.einjojo.akani.essentials.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EssentialsConfig {
    private final JavaPlugin plugin;

    public EssentialsConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();
    }

    public FileConfiguration configuration() {
        return plugin.getConfig();
    }

    public String chatFormat() {
        return configuration().getString("chat.public.format");
    }

    public String privateChatFormat() {
        return configuration().getString("chat.msg.format");
    }


    public void load() {
        plugin.reloadConfig();
    }

    public void save() {
        plugin.saveConfig();
    }
}

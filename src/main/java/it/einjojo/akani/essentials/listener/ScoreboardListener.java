package it.einjojo.akani.essentials.listener;

import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {
    private final AkaniEssentialsPlugin plugin;


    public ScoreboardListener(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void registerScoreboardOnJoin(PlayerJoinEvent event) {
        plugin.scoreboardManager().createScoreboard(event.getPlayer());
    }

    @EventHandler
    public void unregisterScoreboardOnQuit(PlayerQuitEvent event) {
        plugin.scoreboardManager().removeScoreboard(event.getPlayer().getUniqueId());
    }


}

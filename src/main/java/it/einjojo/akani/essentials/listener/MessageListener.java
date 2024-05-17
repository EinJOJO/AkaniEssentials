package it.einjojo.akani.essentials.listener;


import it.einjojo.akani.core.paper.event.AsyncBackCreateEvent;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public record MessageListener(AkaniEssentialsPlugin plugin) implements Listener {
    public MessageListener(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void cancelJoinMessage(PlayerJoinEvent event) {
        event.joinMessage(null);
    }

    @EventHandler
    public void cancelQuitMessage(PlayerQuitEvent event) {
        event.quitMessage(null);
    }

    @EventHandler
    public void cancelAdvancementMessage(PlayerAdvancementDoneEvent event) {
        event.message(null);
    }

    @EventHandler
    public void cancelDeathMessage(PlayerDeathEvent event) {
        event.deathMessage(null);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void notifyBackUsage(AsyncBackCreateEvent event) {
        if (event.isCancelled()) return;
        plugin.sendMessage(event.player(), EssentialKey.of("back.death"));
    }


}

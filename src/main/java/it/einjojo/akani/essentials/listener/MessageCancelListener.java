package it.einjojo.akani.essentials.listener;


import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public record MessageCancelListener(AkaniEssentialsPlugin plugin) implements Listener {
    public MessageCancelListener(AkaniEssentialsPlugin plugin) {
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







}

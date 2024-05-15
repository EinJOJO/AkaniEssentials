package it.einjojo.akani.essentials.listener;

import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.cmdspy.CommandObserver;
import it.einjojo.akani.essentials.cmdspy.CommandObserverRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandSpyListener implements Listener {

    private final CommandObserverRegistry registry;


    public CommandSpyListener(AkaniEssentialsPlugin plugin, CommandObserverRegistry registry) {
        this.registry = registry;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        String command = event.getMessage();
        List<CommandObserver> observers = registry.observers();
        for (CommandObserver observer : observers) {
            observer.onCommand(player, command);
        }
    }


}

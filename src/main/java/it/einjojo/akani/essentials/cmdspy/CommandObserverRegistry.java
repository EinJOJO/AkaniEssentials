package it.einjojo.akani.essentials.cmdspy;

import it.einjojo.akani.essentials.AkaniEssentialsPlugin;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class CommandObserverRegistry {
    private final AkaniEssentialsPlugin plugin;
    private final List<CommandObserver> observers = new LinkedList<>();

    public CommandObserverRegistry(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isSubscribed(UUID observerUUID) {
        return observers.stream().anyMatch(observer -> observer.observer().equals(observerUUID));
    }

    public void subscribeToAllPlayerCommands(UUID observer) {
        observers.add(new CommandObserver.All(plugin, observer));
    }

    public void subscribeToPlayerCommands(UUID observer, UUID target) {
        observers.add(new CommandObserver.Target(plugin, observer, target));
    }

    public void unsubscribe(UUID observerUUID) {
        observers.removeIf(observer -> observer.observer().equals(observerUUID));
    }

    public AkaniEssentialsPlugin plugin() {
        return plugin;
    }

    public List<CommandObserver> observers() {
        return observers;
    }
}

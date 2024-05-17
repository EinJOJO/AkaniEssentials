package it.einjojo.akani.essentials.booster;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface Booster {

    String name();
    String description();

    void enable(Player player);

    void disable(Player player);

    boolean hasEnabled(UUID playerUuid);

    boolean canEnable(Player player);

    boolean hasUnlocked(Player player);

    void tick();
}

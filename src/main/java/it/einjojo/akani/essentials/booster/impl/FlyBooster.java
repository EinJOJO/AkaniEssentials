package it.einjojo.akani.essentials.booster.impl;

import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.booster.Booster;
import it.einjojo.akani.essentials.booster.ExpiryTracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FlyBooster implements Booster {
    private static final Duration DURATION = Duration.ofMinutes(5);
    private final Set<UUID> activatedPlayers = new HashSet<>();

    @Override
    public String name() {
        return "Fly-Booster";
    }    private final ExpiryTracker expiryTracker = new ExpiryTracker((uuid) -> {
        Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(this::disable);
    });

    @Override
    public String description() {
        return "Ermöglicht das Fliegen für %s Minuten.".formatted(DURATION.toMinutes());
    }

    @Override
    public boolean hasEnabled(UUID playerUuid) {
        return activatedPlayers.contains(playerUuid);
    }

    @Override
    public void enable(Player player) {
        player.setAllowFlight(true);
        activatedPlayers.add(player.getUniqueId());
        expiryTracker.track(player.getUniqueId(), DURATION);
    }

    @Override
    public void disable(Player player) {
        player.setAllowFlight(false);
        expiryTracker.remove(player.getUniqueId());
        activatedPlayers.remove(player.getUniqueId());
    }

    @Override
    public boolean canEnable(Player player) {
        return true;
    }

    @Override
    public boolean hasUnlocked(Player player) {
        return player.hasPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "booster.fly");
    }

    @Override
    public void tick() {
        expiryTracker.runCheck();
        if (activatedPlayers.isEmpty()) {
            return;
        }
        for (UUID uuid : activatedPlayers) {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresentOrElse((bukkitPlayer) -> {
                if (!bukkitPlayer.getAllowFlight()) {
                    disable(bukkitPlayer);
                }
            }, () -> {
                activatedPlayers.remove(uuid);
            });
        }
    }


}

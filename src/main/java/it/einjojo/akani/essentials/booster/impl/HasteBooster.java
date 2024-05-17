package it.einjojo.akani.essentials.booster.impl;

import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.booster.Booster;
import it.einjojo.akani.essentials.booster.ExpiryTracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HasteBooster implements Booster {
    private static final Duration DURATION = Duration.ofMinutes(5);
    private final Set<UUID> activatedPlayers = new HashSet<>();
    private final ExpiryTracker expiryTracker = new ExpiryTracker(activatedPlayers::remove);

    @Override
    public String name() {
        return "Haste-Booster";
    }

    @Override
    public String description() {
        return "Ermöglicht das schnelle Graben für 5 Minuten.";
    }

    @Override
    public void enable(Player player) {
        activatedPlayers.add(player.getUniqueId());
        expiryTracker.track(player.getUniqueId(), DURATION);
    }

    @Override
    public void disable(Player player) {
        activatedPlayers.remove(player.getUniqueId());
        expiryTracker.remove(player.getUniqueId());
    }

    @Override
    public boolean hasEnabled(UUID playerUuid) {
        return activatedPlayers.contains(playerUuid);
    }

    @Override
    public boolean canEnable(Player player) {
        return true;
    }

    @Override
    public boolean hasUnlocked(Player player) {
        return player.hasPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "booster.haste");
    }

    @Override
    public void tick() {
        if (activatedPlayers.isEmpty()) {
            return;
        }
        for (UUID uuid : activatedPlayers) {
            if (uuid == null) {
                continue;
            }
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                continue;
            }
            player.addPotionEffect(new PotionEffect(org.bukkit.potion.PotionEffectType.FAST_DIGGING, 100, 1, true, false, false));
        }
    }
}

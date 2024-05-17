package it.einjojo.akani.essentials;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EssentialsPlaceholderExpansion extends PlaceholderExpansion {
    private final Map<UUID, Long> playtimeCache = new ConcurrentHashMap<>();

    private final AkaniEssentialsPlugin plugin;

    public EssentialsPlaceholderExpansion(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }


    @Override
    public @NotNull String getIdentifier() {
        return "essentials";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Ein_Jojo";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("playtime")) {
            refreshPlaytimeCache(player.getUniqueId());
            Long millis = playtimeCache.get(player.getUniqueId());
            if (millis == null || millis == 0) {
                return "0m";
            }
            Duration duration = Duration.ofMillis(millis);
            long hours = duration.toHours();
            if (hours <= 2) {
                return duration.toMinutes() + "m";
            } else {
                return hours + "h";
            }
        }

        return "";
    }


    public void refreshPlaytimeCache(UUID player) {
        plugin.core().playerManager().loadPlayer(player).thenAcceptAsync(akaniOfflinePlayer -> {
            if (akaniOfflinePlayer.isEmpty()) return;
            playtimeCache.put(player, akaniOfflinePlayer.get().playtime().playtimeMillis());
        });
    }
}

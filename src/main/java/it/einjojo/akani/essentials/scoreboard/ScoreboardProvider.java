package it.einjojo.akani.essentials.scoreboard;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Provides the lines for the scoreboard
 */
public interface ScoreboardProvider extends Comparable<ScoreboardProvider> {

    boolean shouldProvide(Player player);

    int priority();

    void updateScoreboard(PlayerScoreboard scoreboard);

    @Override
    default int compareTo(@NotNull ScoreboardProvider o) {
        return Integer.compare(o.priority(), priority());
    }
}

package it.einjojo.akani.essentials.scoreboard;

import fr.mrmicky.fastboard.adventure.FastBoard;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Provides the lines for the scoreboard
 */
public interface ScoreboardProvider  {

    boolean shouldProvide(Player player);

    int priority();

    void updateScoreboard(FastBoard scoreboard);

}

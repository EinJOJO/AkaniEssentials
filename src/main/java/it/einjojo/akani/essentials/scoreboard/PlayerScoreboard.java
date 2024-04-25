package it.einjojo.akani.essentials.scoreboard;

import fr.mrmicky.fastboard.adventure.FastBoard;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerScoreboard {
    private final FastBoard fastBoard;
    private ScoreboardProvider activeProvider;

    public PlayerScoreboard(Player owner, ScoreboardProvider provider) {
        this.activeProvider = provider;
        this.fastBoard = new FastBoard(owner);
    }

    public void update() {
        activeProvider.updateScoreboard(fastBoard);
    }

    public Player owner() {
        return fastBoard.getPlayer();
    }

    protected void setActiveProvider(@NotNull ScoreboardProvider activeProvider) {
        this.activeProvider = activeProvider;
    }

    public ScoreboardProvider activeProvider() {
        return activeProvider;
    }

    public FastBoard fastBoard() {
        return fastBoard;
    }
}

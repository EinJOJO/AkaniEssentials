package it.einjojo.akani.essentials.scoreboard;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class SyncScoreboardUpdateTask implements Runnable {

    private final ScoreboardManager scoreboardManager;
    private BukkitTask task;

    public SyncScoreboardUpdateTask(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    public void start(JavaPlugin plugin) {
        stop();
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 20);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public void run() {
        scoreboardManager.updateActiveProviders();
        scoreboardManager.updateScoreboards();
    }
}

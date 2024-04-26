package it.einjojo.akani.essentials.scoreboard.defaults;

import it.einjojo.akani.essentials.scoreboard.PlayerScoreboard;
import it.einjojo.akani.essentials.scoreboard.ScoreboardProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class PlotworldScoreboardProvider implements ScoreboardProvider {

    @Override
    public boolean shouldProvide(Player player) {
        return player.getWorld().getName().equals("Plotwelt");
    }

    @Override
    public short priority() {
        return PRIORITY.NORMAL;

    }

    @Override
    public void updateScoreboard(PlayerScoreboard scoreboard) {
        int seconds = scoreboard.metadata("state", 0);
        scoreboard.setMetadata("state", seconds + 1);
        scoreboard.updateLines(
                Component.text("Plotworld Scoreboard"),
                Component.text("Tick: " + seconds)
        );
        if ((seconds / 4) % 2 == 0) { // flash every 4 seconds
            scoreboard.updateTitle(Component.text("§a§lPlotworld"));
        } else {
            scoreboard.updateTitle(Component.text("§c§lPlotworld"));
        }
        if (seconds > 100) {
            scoreboard.setMetadata("state", 0);
        }
    }
}

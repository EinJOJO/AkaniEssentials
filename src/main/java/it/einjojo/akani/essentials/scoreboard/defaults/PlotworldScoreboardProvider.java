package it.einjojo.akani.essentials.scoreboard.defaults;

import fr.mrmicky.fastboard.adventure.FastBoard;
import it.einjojo.akani.essentials.scoreboard.ScoreboardProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PlotworldScoreboardProvider implements ScoreboardProvider, Listener {

    @Override
    public boolean shouldProvide(Player player) {
        return player.getWorld().getName().equals("Plotwelt");
    }

    @Override
    public int priority() {
        return 10; // above default
    }

    @Override
    public void updateScoreboard(FastBoard scoreboard) {
        scoreboard.updateLines(
                Component.text("Plotworld Scoreboard")
        );
    }
}

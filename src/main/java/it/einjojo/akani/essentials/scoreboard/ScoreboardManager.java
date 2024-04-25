package it.einjojo.akani.essentials.scoreboard;

import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.scoreboard.defaults.DefaultScoreboardProvider;
import it.einjojo.akani.essentials.scoreboard.defaults.PlotworldScoreboardProvider;
import org.bukkit.entity.Player;

import java.util.*;

public class ScoreboardManager {
    private final Map<UUID, PlayerScoreboard> scoreboards = new HashMap<>();
    private final List<ScoreboardProvider> providers = new LinkedList<>();
    private final DefaultScoreboardProvider defaultScoreboardProvider;

    public ScoreboardManager(AkaniEssentialsPlugin plugin) {
        defaultScoreboardProvider = new DefaultScoreboardProvider(plugin);
        registerProvider(defaultScoreboardProvider);
        registerProvider(new PlotworldScoreboardProvider());
    }

    public PlayerScoreboard getScoreboard(UUID uuid) {

        return scoreboards.get(uuid);
    }

    public void createScoreboard(Player player) {
        PlayerScoreboard scoreboard = new PlayerScoreboard(player, defaultScoreboardProvider);
        scoreboards.put(player.getUniqueId(), scoreboard);
    }

    public void removeScoreboard(UUID uuid) {
        PlayerScoreboard removed = scoreboards.remove(uuid);
        if (removed != null) {
            removed.fastBoard().delete();
        }
    }

    public void registerProvider(ScoreboardProvider provider) {
        ListIterator<ScoreboardProvider> iterator = providers.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().priority() < provider.priority()) {
                iterator.previous();
                break;
            }
        }
        iterator.add(provider);

    }

    public void unregisterProvider(ScoreboardProvider provider) {
        providers.remove(provider);
    }

    public void updateScoreboards() {
        for (PlayerScoreboard scoreboard : scoreboards.values()) {
            scoreboard.update();
        }
    }

    public void updateActiveProviders() {
        for (PlayerScoreboard scoreboard : scoreboards.values()) {
            for (ScoreboardProvider provider : providers) {
                if (provider.shouldProvide(scoreboard.owner())) {
                    scoreboard.setActiveProvider(provider);
                    break;
                }
            }
        }
    }

}

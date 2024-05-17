package it.einjojo.akani.essentials.scoreboard.defaults;

import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.PaperMessageManager;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.scoreboard.PlayerScoreboard;
import it.einjojo.akani.essentials.scoreboard.ScoreboardProvider;
import it.einjojo.akani.essentials.util.EssentialKey;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class DefaultScoreboardProvider implements ScoreboardProvider {
    private final AkaniEssentialsPlugin plugin;

    public DefaultScoreboardProvider(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean shouldProvide(Player player) {
        return true; // always provide the default scoreboard
    }

    @Override
    public short priority() {
        return 0;
    }

    @Override
    public void updateScoreboard(PlayerScoreboard sb) {
        AkaniPlayer akaniPlayer = plugin.core().playerManager().onlinePlayer(sb.getPlayer().getUniqueId()).orElse(null);
        if (akaniPlayer == null) {
            return;
        }

        sb.updateTitle(messageManager().message(EssentialKey.of("sb.default.title")));
        List<Component> list = new LinkedList<>();
        list.add(ScoreboardDefaults.BAR);
        list.addAll(ScoreboardDefaults.playerSection(akaniPlayer));
        list.addAll(List.of(
                Component.empty(),
                deserialize("   <#f8c1a1><b>ꜱᴇʀᴠᴇʀ"),
                deserialize("    <dark_gray>▪ <gray>ɴᴀᴍᴇ: <white>" + plugin.core().serverName()),
                deserialize("    <dark_gray>▪ <gray>ᴏɴʟɪɴᴇ: <white>" + plugin.getServer().getOnlinePlayers().size()),
                deserialize("    <dark_gray>▪ <gray>ᴛᴘs: <white>" + "%.2f".formatted(plugin.getServer().getTPS()[0])),
                Component.empty(),
                deserialize("   <#f8c1a1><b>ᴇᴠᴇɴᴛꜱ"),
                deserialize("    <dark_gray>▪ <red>ɴᴏɴᴇ"),
                ScoreboardDefaults.BAR
        ));
        sb.updateLines(list);

    }


    protected Component deserialize(String message) {
        return messageManager().miniMessage().deserialize(message);
    }

    protected PaperMessageManager messageManager() {
        return plugin.core().messageManager();
    }
}

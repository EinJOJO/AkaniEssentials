package it.einjojo.akani.essentials.scoreboard.defaults;

import fr.mrmicky.fastboard.adventure.FastBoard;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.PaperMessageManager;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.scoreboard.ScoreboardProvider;
import it.einjojo.akani.essentials.util.MessageKey;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

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
    public int priority() {
        return 0;
    }

    @Override
    public void updateScoreboard(FastBoard sb) {
        AkaniPlayer akaniPlayer = plugin.core().playerManager().onlinePlayer(sb.getPlayer().getUniqueId()).orElse(null);
        if (akaniPlayer == null) {
            return;
        }

        sb.updateTitle(messageManager().message(MessageKey.of("sb.default.title")));
        sb.updateLines(
                deserialize("  <dark_gray>◆<st>                                  </st>◆  "),
                deserialize("   <#E5FBFD><b>ᴘʟᴀʏᴇʀ"),
                deserialize("     <dark_gray>▪ <gray>ʀᴀɴᴋ: <white>" + akaniPlayer.plainPrefix().join()),
                deserialize("     <dark_gray>▪ <gray>ᴄᴏɪɴs: <white>" + akaniPlayer.coins().balance()),
                deserialize("     <dark_gray>▪ <gray>ᴛᴀʟᴇʀ: <white>" + akaniPlayer.thaler().balance()),
                Component.empty(),
                deserialize("   <#E5FBFD><b>ꜱᴇʀᴠᴇʀ"),
                deserialize("     <dark_gray>▪ <gray>ᴏɴʟɪɴᴇ: <white>" + plugin.getServer().getOnlinePlayers().size()),
                deserialize("     <dark_gray>▪ <gray>ᴛᴘs: <white>" + "%.2f".formatted(plugin.getServer().getTPS()[0])),
                Component.empty(),
                deserialize("   <#E5FBFD><b>ᴇᴠᴇɴᴛꜱ"),
                deserialize("     <dark_gray>▪ <red>ɴᴏɴᴇ"),
                deserialize("  <dark_gray>◆<st>                                  </st>◆   ")
        );


    }

    protected Component deserialize(String message) {
        return messageManager().miniMessage().deserialize(message);
    }

    protected PaperMessageManager messageManager() {
        return plugin.core().messageManager();
    }
}

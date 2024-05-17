package it.einjojo.akani.essentials.scoreboard.defaults;

import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.util.TextUtil;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.scoreboard.PlayerScoreboard;
import it.einjojo.akani.essentials.scoreboard.ScoreboardProvider;
import it.einjojo.akani.essentials.util.EssentialKey;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class PlotworldScoreboardProvider implements ScoreboardProvider {

    private final AkaniEssentialsPlugin plugin;

    public PlotworldScoreboardProvider(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean shouldProvide(Player player) {
        return player.getWorld().getName().equals("Plotwelt");
    }

    @Override
    public short priority() {
        return PRIORITY.NORMAL;

    }

    @Override
    public void updateScoreboard(PlayerScoreboard sb) {
        AkaniPlayer akaniPlayer = plugin.core().playerManager().onlinePlayer(sb.getPlayer().getUniqueId()).orElse(null);
        if (akaniPlayer == null) {
            return;
        }
        Player bukkitPlayer = sb.getPlayer();
        String plotOwner = placeholders(bukkitPlayer, "%plotsquared_currentplot_owner%");
        sb.updateTitle(plugin.core().messageManager().message(EssentialKey.of("sb.default.title")));
        List<Component> content = new LinkedList<>();
        content.add(ScoreboardDefaults.BAR);
        content.addAll(ScoreboardDefaults.playerSection(akaniPlayer));
        content.add(deserialize(TextUtil.transformAmpersandToMiniMessage(placeholders(bukkitPlayer, "    &8▪ &7ᴘʟᴏᴛꜱ: &f%plotsquared_plot_count%&8/&7%plotsquared_allowed_plot_count%"))));
        content.addAll(List.of(
                Component.empty(),
                deserialize("   <#f8c1a1><b>ᴘʟᴏᴛɪɴꜰᴏ"),
                deserialize("    <dark_gray>▪ <gray>ᴘᴏꜱɪᴛɪᴏɴ: <gray>" + (plotOwner.isBlank() ? "-" : placeholders(bukkitPlayer, "%plotsquared_currentplot_xy%"))),
                deserialize("    <dark_gray>▪ <gray>ɪɴʜᴀʙᴇʀ: <gray>" + (plotOwner.isBlank() || plotOwner.contains(" ") ? "-" : TextUtil.transformAmpersandToMiniMessage(plotOwner))),
                Component.empty(),
                deserialize("   <#f8c1a1><b>ᴛɪᴘᴘꜱ")
        ));

        if (placeholders(bukkitPlayer, "%plotsquared_has_plot%").equals("false")) {
            content.add(deserialize(placeholders(bukkitPlayer, "    <dark_gray>▪ <yellow>Nutze /p auto")));
        } else {
            content.add(deserialize(placeholders(bukkitPlayer, "    <dark_gray>▪ <yellow>Nichts")));
        }
        content.add(ScoreboardDefaults.BAR);
        sb.updateLines(content);
    }

    protected String placeholders(Player player, String s) {
        return PlaceholderAPI.setPlaceholders(player, s);
    }

    protected Component deserialize(String message) {
        return plugin.core().miniMessage().deserialize(message);
    }


}

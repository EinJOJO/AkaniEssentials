package it.einjojo.akani.essentials.command;

import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.parser.Parser;
import org.incendo.cloud.annotations.suggestion.Suggestions;
import org.incendo.cloud.context.CommandContext;

import java.util.List;

public class AkaniPlayerCommand {

    private final AkaniEssentialsPlugin plugin;

    public AkaniPlayerCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Suggestions("onlinePlayers")
    public List<String> suggestOnlinePlayers(CommandContext<?> $, String input) {
        return plugin.core().playerManager().onlinePlayers().stream().map(AkaniPlayer::name).toList();
    }

    @Parser(suggestions = "onlinePlayers")
    public AkaniPlayer defaultOnlinePlayerParser(CommandContext<CommandSender> sender, String input) {
        var optional = plugin.core().playerManager().onlinePlayerByName(input);
        if (optional.isEmpty()) {
            sender.sender().sendMessage(plugin.miniMessage().deserialize("<red>Player <yellow>%s <red>not found.".formatted(input)));
            return null;
        }
        return optional.get();
    }


}

package it.einjojo.akani.essentials.command.msg;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.service.MessageService;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

@CommandAlias("reply|r")
public class ReplyCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public ReplyCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    public void reply(Player sender, String message) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.messageService().lastConversation(sender.getUniqueId()).ifPresentOrElse((cPartner) -> {
                plugin.core().playerManager().onlinePlayer(cPartner).ifPresentOrElse((partner) -> {
                    plugin.messageService().publishPrivateChatMessage(sender, partner, MessageService.sanitizeMessage(message));
                }, () -> {
                    plugin.sendMessage(sender, EssentialKey.PLAYER_NOT_ONLINE);
                });
            }, () -> {
                plugin.sendMessage(sender, EssentialKey.of("msg.no_conversation"));
            });
        });
    }

    @CatchUnknown
    public void unknown(Player sender) {
        plugin.sendCommandUsageMessage(sender, "/reply <message>");
    }

}

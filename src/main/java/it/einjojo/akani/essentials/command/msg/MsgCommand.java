package it.einjojo.akani.essentials.command.msg;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.service.MessageService;
import org.bukkit.entity.Player;

@CommandAlias("msg|message|tell|whisper")
public class MsgCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public MsgCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }


    @Default
    @CommandCompletion("@akaniplayers @nothing")
    public void msg(Player sender, AkaniPlayer receiver, String message) {
        if (message.isEmpty()) {
            plugin.sendMessage(sender, "msg.no_message");
            return;
        }
        plugin.messageService().publishPrivateChatMessage(sender, receiver, MessageService.sanitizeMessage(message));
    }

    @CatchUnknown
    public void unknown(Player sender) {
        plugin.sendCommandUsageMessage(sender, "/msg <player> <message>");
    }
}

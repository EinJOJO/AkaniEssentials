package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.PaperAkaniCore;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("teleport|tp")
public class TeleportCommand extends BaseCommand {
    private final PaperAkaniCore core;

    public TeleportCommand(AkaniEssentialsPlugin plugin) {
        this.core = plugin.core();
        plugin.commandManager().registerCommand(this);
    }


    @Default
    @Description("Teleport to a player")
    @CommandCompletion("@akaniplayers")
    public void teleportPlayer(Player player, AkaniPlayer target) {
        if (player.getUniqueId().equals(target.uuid())) {
            core.messageManager().sendMessage(player, EssentialMessage.TELEPORT_NOT_SELF.key());
            return;
        }
        AkaniPlayer sender = core.playerManager().onlinePlayer(player.getUniqueId()).orElseThrow();
        target.location().thenAccept(sender::teleport).exceptionally((e) -> {
            core.messageManager().sendMessage(player, EssentialMessage.GENERIC_ERROR.key());
            e.printStackTrace();
            return null;
        });

    }

    @Default
    @Description("Teleport to a player ")
    @CommandCompletion("@akaniplayers @akaniplayers")

    public void teleportPlayerToPlayer(CommandSender commandSender, AkaniPlayer player, AkaniPlayer target) {
        target.location().thenAccept(player::teleport).exceptionally((e) -> {
            core.messageManager().sendMessage(commandSender, EssentialMessage.GENERIC_ERROR.key());
            e.printStackTrace();
            return null;
        });
    }


}

package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("tpa|tpadeny|tpaaccept")
public class TpaCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public TpaCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @CommandCompletion("@akaniplayers")
    public void router(Player sender, @Optional AkaniPlayer receiver) {
        if (!plugin.core().serverName().equals("Citybuild-1")) {
            plugin.sendMessage(sender, EssentialKey.of("tpa.not-available"));
            return;
        }
        if (getExecCommandLabel().equals("tpa")) {
            if (receiver == null) {
                plugin.sendMessage(sender, EssentialKey.SPECIFY_PLAYER);
                return;
            }
            tpa(sender, receiver);
        } else if (getExecCommandLabel().equals("tpadeny")) {
            deny(sender);
        } else if (getExecCommandLabel().equals("tpaaccept")) {
            accept(sender);
        }
    }

    public void tpa(Player sender, AkaniPlayer receiver) {
        if (sender.getUniqueId().equals(receiver.uuid())) {
            plugin.sendMessage(sender, EssentialKey.SPECIFY_PLAYER);
            return;
        }
        plugin.tpaService().storeTpa(sender.getUniqueId(), receiver.uuid());
        plugin.sendMessage(receiver, EssentialKey.of("tpa.request-received"), s -> s.replaceAll("%player%", sender.getName()));
        plugin.sendMessage(sender, EssentialKey.of("tpa.request-sent"), s -> s.replaceAll("%player%", receiver.name()));

    }


    @Subcommand("deny")
    public void deny(Player player) {
        UUID senderUuid = plugin.tpaService().getTpa(player.getUniqueId());
        if (senderUuid == null) {
            plugin.sendMessage(player, EssentialKey.of("tpa.no-request"));
            return;
        }
        plugin.tpaService().removeTpa(player.getUniqueId());
        plugin.sendMessage(player, EssentialKey.of("tpa.request-denied"));
    }

    @Subcommand("accept")
    public void accept(Player player) {
        UUID senderUuid = plugin.tpaService().getTpa(player.getUniqueId());
        if (senderUuid == null) {
            plugin.sendMessage(player, EssentialKey.of("tpa.no-request"));
            return;
        }
        AkaniPlayer tpaReceiver = plugin.core().playerManager().onlinePlayer(player.getUniqueId()).orElseThrow(IllegalStateException::new);
        plugin.tpaService().removeTpa(player.getUniqueId());
        plugin.core().playerManager().onlinePlayer(senderUuid).ifPresentOrElse((sender) -> {
            tpaReceiver.location().thenAccept(sender::teleport);
            plugin.sendMessage(player, EssentialKey.of("tpa.request-accepted"), s -> s.replaceAll("%player%", sender.name()));
        }, () -> {

        });
    }


}

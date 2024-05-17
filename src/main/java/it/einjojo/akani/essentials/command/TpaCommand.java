package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("tpa")
public class TpaCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public TpaCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @CommandCompletion("@akaniplayers")
    public void router(Player sender, AkaniPlayer receiver) {
        if (receiver == null) {
            plugin.sendMessage(sender, EssentialKey.SPECIFY_PLAYER);
            return;
        }
        if (checkServer(sender)) tpa(sender, receiver);
    }

    private boolean checkServer(Player commandSender) {
        if (!plugin.core().serverName().equals("Citybuild-1")) {
            plugin.sendMessage(commandSender, EssentialKey.of("tpa.not-available"));
            return false;
        }
        return true;
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

    @Subcommand("here")
    @CommandAlias("tpahere")
    @CommandCompletion("@akaniplayers|all")
    public void preTpaHere(Player sender, String target) {
        if (!checkServer(sender)) return;
        AkaniPlayer akaniSender = plugin.core().playerManager().onlinePlayer(sender.getUniqueId()).orElseThrow(IllegalStateException::new);
        if (target.equals("all")) {
            for (AkaniPlayer akaniPlayer : plugin.core().playerManager().onlinePlayers()) {
                if (akaniPlayer.uuid().equals(akaniSender.uuid())) continue;
                tpaHere(akaniSender, akaniPlayer);
            }
        } else {
            AkaniPlayer akaniReceiver = plugin.core().playerManager().onlinePlayerByName(target).orElseThrow(() -> new TargetNotFoundException(target));
            tpaHere(akaniSender, akaniReceiver);
        }
    }

    public void tpaHere(AkaniPlayer sender, AkaniPlayer receiver) {
        if (sender.uuid().equals(receiver.uuid())) {
            plugin.sendMessage(sender, EssentialKey.SPECIFY_PLAYER);
            return;
        }
        plugin.tpaService().storeTpaHere(sender.uuid(), receiver.uuid());
        plugin.sendMessage(receiver, EssentialKey.of("tpahere.request-received"), s -> s.replaceAll("%player%", sender.name()));
        plugin.sendMessage(sender, EssentialKey.of("tpahere.request-sent"), s -> s.replaceAll("%player%", receiver.name()));
    }


    @Subcommand("deny")
    @CommandAlias("tpadeny|tpdeny")
    public void deny(Player player) {
        if (!checkServer(player)) return;
        UUID tpaSender = plugin.tpaService().getTpa(player.getUniqueId());
        UUID tpaHereSender = plugin.tpaService().getTpaHere(player.getUniqueId());
        if (tpaSender != null) {
            plugin.tpaService().removeTpa(player.getUniqueId());
            plugin.sendMessage(player, EssentialKey.of("tpa.request-denied"));
        } else if (tpaHereSender != null) {
            plugin.tpaService().removeTpaHere(player.getUniqueId());
            plugin.sendMessage(player, EssentialKey.of("tpa.request-denied"));
        } else {
            plugin.sendMessage(player, EssentialKey.of("tpa.no-request"));
        }

    }

    @Subcommand("accept")
    @CommandAlias("tpaaccept|tpaccept")
    public void accept(Player player) {
        if (!checkServer(player)) return;
        UUID senderUuid = plugin.tpaService().getTpa(player.getUniqueId());
        UUID senderHereUuid = plugin.tpaService().getTpaHere(player.getUniqueId());
        AkaniPlayer tpaReceiver = plugin.core().playerManager().onlinePlayer(player.getUniqueId()).orElseThrow(IllegalStateException::new);
        if (senderUuid != null) {
            plugin.tpaService().removeTpa(player.getUniqueId());
            plugin.core().playerManager().onlinePlayer(senderUuid).ifPresentOrElse((sender) -> {
                tpaReceiver.location().thenAccept(sender::teleport);
                plugin.sendMessage(player, EssentialKey.of("tpa.request-accepted"), s -> s.replaceAll("%player%", sender.name()));
            }, () -> {

            });
        } else if (senderHereUuid != null) {
            plugin.tpaService().removeTpaHere(player.getUniqueId());
            plugin.core().playerManager().onlinePlayer(senderHereUuid).ifPresentOrElse((sender) -> {
                sender.location().thenAccept(tpaReceiver::teleport);
                plugin.sendMessage(player, EssentialKey.of("tpa.request-accepted"), s -> s.replaceAll("%player%", sender.name()));
            }, () -> {

            });
        } else {
            plugin.sendMessage(player, EssentialKey.of("tpa.no-request"));
        }
    }


}

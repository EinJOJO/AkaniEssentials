package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

@CommandAlias("teleport|tp")
public class TeleportCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public TeleportCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }


    @Default
    @Description("Teleport to a player")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "teleport")
    @Syntax("<player> [player]")
    @CommandCompletion("@akaniplayers @akaniplayers:includeSender")
    public void teleportPlayer(Player bukkitSender, @Single AkaniPlayer target, @Optional AkaniPlayer destinationPlayer) {
        if (bukkitSender.getUniqueId().equals(target.uuid())) {
            plugin.sendMessage(bukkitSender, EssentialKey.of("teleport.not_self"));
            return;
        }
        if (destinationPlayer != null) { // Tp target -> destination
            destinationPlayer.location().thenAccept((networkLocation) -> {
                plugin.core().messageManager().sendMessage(target, EssentialKey.of("teleport.teleporting"), (s) -> s.replaceAll("%player%", destinationPlayer.name()));
                target.teleport(networkLocation);
            }).exceptionally((ex) -> {
                plugin.sendMessage(bukkitSender, EssentialKey.GENERIC_ERROR);
                plugin.getSLF4JLogger().error("Could not teleport player {} to {}.", target.name(), destinationPlayer.name(), ex);
                return null;
            });
        } else {

            target.location().thenAccept((networkLocation) -> {
                plugin.core().messageManager().sendMessage(bukkitSender, EssentialKey.of("teleport.teleporting"), (s) -> s.replaceAll("%player%", target.name()));
                plugin.core().playerManager().onlinePlayer(bukkitSender.getUniqueId()).ifPresent((p) -> {
                    p.teleport(networkLocation);
                });
            }).exceptionally((ex) -> {
                plugin.sendMessage(bukkitSender, EssentialKey.GENERIC_ERROR);
                plugin.getSLF4JLogger().error("Could not teleport player {} to {}.", bukkitSender.name(), target.name(), ex);
                return null;
            });
        }
    }

    @Subcommand("*")
    @Description("Teleport all players to a player")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "teleport.all")
    @CommandCompletion("@akaniplayers:includeSender")
    public void teleportAllPlayers(Player bukkitSender, AkaniPlayer target) {
        for (AkaniPlayer online : plugin.core().playerManager().onlinePlayers()) {
            if (online.equals(target)) continue;
            teleportPlayer(bukkitSender, online, target);
        }
    }

}

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
    @CommandCompletion("@akaniplayers|* @akaniplayers:includeSender")
    public void teleportPlayer(Player bukkitSender, String targetString, @Optional AkaniPlayer destinationPlayer) {
        AkaniPlayer target = !targetString.equals("*") ? plugin.core().playerManager().onlinePlayerByName(targetString).orElseThrow(() -> new TargetNotFoundException(targetString)) : null;
        if (target != null && bukkitSender.getUniqueId().equals(target.uuid())) {
            plugin.sendMessage(bukkitSender, EssentialKey.of("teleport.not_self"));
            return;
        }
        if (destinationPlayer != null) { // Tp target -> destination
            destinationPlayer.location().thenAccept((networkLocation) -> {
                if (target == null) {
                    for (AkaniPlayer online : plugin.core().playerManager().onlinePlayers()) {
                        plugin.core().messageManager().sendMessage(online, EssentialKey.of("teleport.teleporting"), (s) -> s.replaceAll("%player%", destinationPlayer.name()));
                        online.teleport(networkLocation);
                    }
                } else {
                    plugin.core().messageManager().sendMessage(target, EssentialKey.of("teleport.teleporting"), (s) -> s.replaceAll("%player%", destinationPlayer.name()));
                    target.teleport(networkLocation);
                }
            }).exceptionally((ex) -> {
                plugin.sendMessage(bukkitSender, EssentialKey.GENERIC_ERROR);
                plugin.getLogger().warning("Could not teleport player " + targetString + " to " + destinationPlayer.name() + ".");
                plugin.getLogger().warning(ex.getMessage());
                return null;
            });
        } else {
            if (target == null) {
                plugin.sendMessage(bukkitSender, EssentialKey.of("teleport.not_found"));
                return;
            }
            target.location().thenAccept((networkLocation) -> {
                plugin.core().messageManager().sendMessage(bukkitSender, EssentialKey.of("teleport.teleporting"), (s) -> s.replaceAll("%player%", target.name()));
                plugin.core().playerManager().onlinePlayer(bukkitSender.getUniqueId()).ifPresent((p) -> {
                    p.teleport(networkLocation);
                });
            }).exceptionally((ex) -> {
                plugin.sendMessage(bukkitSender, EssentialKey.GENERIC_ERROR);
                plugin.getLogger().warning("Could not teleport player " + bukkitSender.name() + " to " + target.name() + ".");
                plugin.getLogger().warning(ex.getMessage());
                return null;
            });
        }
    }

    @CatchUnknown
    public void unknown(Player sender) {
        plugin.sendCommandUsageMessage(sender, "/tp <player> [player]");
    }


}

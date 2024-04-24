package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.MessageKey;
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
    public void teleportPlayer(Player bukkitSender, AkaniPlayer target, @Optional AkaniPlayer destinationPlayer) {
        if (bukkitSender.getUniqueId().equals(target.uuid())) {
            plugin.sendMessage(bukkitSender, MessageKey.of("teleport.not_self"));
            return;
        }
        if (destinationPlayer != null) { // Tp target -> destination
            destinationPlayer.location().thenAccept((loc) -> {
                plugin.core().messageManager().sendMessage(target, MessageKey.of("teleport.teleporting"), (s) -> s.replaceAll("%player%", destinationPlayer.name()));
                target.teleport(loc);
            }).exceptionally((ex) -> {
                plugin.sendMessage(bukkitSender, MessageKey.GENERIC_ERROR);
                plugin.getLogger().warning("Could not teleport player " + target.name() + " to " + destinationPlayer.name() + ".");
                plugin.getLogger().warning(ex.getMessage());
                return null;
            });
        } else {
            target.location().thenAccept((loc) -> {
                plugin.core().messageManager().sendMessage(bukkitSender, MessageKey.of("teleport.teleporting"), (s) -> s.replaceAll("%player%", target.name()));
                plugin.core().playerManager().onlinePlayer(bukkitSender.getUniqueId()).ifPresent((p) -> {
                    p.teleport(loc);
                });
            }).exceptionally((ex) -> {
                plugin.sendMessage(bukkitSender, MessageKey.GENERIC_ERROR);
                plugin.getLogger().warning("Could not teleport player " + bukkitSender.name() + " to " + target.name() + ".");
                plugin.getLogger().warning(ex.getMessage());
                return null;
            });
        }
    }

    @Default
    @CatchUnknown
    public void unknown(Player sender) {
        plugin.sendCommandUsageMessage(sender, "/tp <player> [player]");
    }


}

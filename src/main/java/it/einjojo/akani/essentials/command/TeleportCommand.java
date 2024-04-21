package it.einjojo.akani.essentials.command;

import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.PaperAkaniCore;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;

public record TeleportCommand(PaperAkaniCore core) {

    @Command("teleport <player> <target>")
    @Permission(AkaniEssentialsPlugin.PERMISSION_BASE + "teleport")
    public void teleportPlayerToPlayer(@Argument AkaniPlayer player, @Argument AkaniPlayer target) {
        if (player.uuid().equals(target.uuid())) {
            core.messageManager().sendMessage(player, EssentialMessage.TELEPORT_NOT_SELF.key());
            return;
        }
        target.location().thenAccept(player::teleport).exceptionally((e) -> {
            core.messageManager().sendMessage(player, EssentialMessage.GENERIC_ERROR.key());
            e.printStackTrace();
            return null;
        });
    }

    @Command("teleport <player>")
    public void teleportPlayer(Player player, @Argument AkaniPlayer target) {
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

    @Command("teleport <location>")
    public void teleportToLocation(Player player, @Argument Location locParams) {
        var teleportDestination = new Location(player.getWorld(), locParams.getX(), locParams.getY(), locParams.getZ());
        player.teleportAsync(teleportDestination).exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });
    }


}

package it.einjojo.akani.essentials.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.PaperAkaniCore;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Command(name = "teleport", aliases = {"tp"})
@Permission(AkaniEssentialsPlugin.PERMISSION_BASE + "teleport")
public record TeleportCommand(PaperAkaniCore core) {

    @Execute
    @Async
    @Permission(AkaniEssentialsPlugin.PERMISSION_BASE + "teleport.others")
    public void teleportPlayerToPlayer(@Arg AkaniPlayer player, @Arg AkaniPlayer target) {
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


    @Execute
    @Async
    public void teleportPlayer(@Context Player player, @Arg AkaniPlayer target) {
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

    @Execute
    public void teleportToLocation(@Context Player player, @Arg Location locParams) {
        var teleportDestination = new Location(player.getWorld(), locParams.getX(), locParams.getY(), locParams.getZ());
        player.teleportAsync(teleportDestination).exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });
    }


}

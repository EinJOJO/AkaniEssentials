package it.einjojo.akani.essentials.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Command(name = "teleport", aliases = {"tp"})
@Permission(AkaniEssentialsPlugin.PERMISSION_BASE + "teleport")
public record TeleportCommand(AkaniCore core) {


    @Execute
    @Async
    @Permission(AkaniEssentialsPlugin.PERMISSION_BASE + "teleport.others")
    public void teleportPlayerToPlayer(@Arg AkaniPlayer player, @Arg AkaniPlayer target) {
        if (player.uuid().equals(target.uuid())) {
            player.sendMessage(Component.text("noSelfTeleport"));
            return;
        }
        target.location().thenAccept(player::teleport).exceptionally((e) -> {
            player.sendMessage(Component.text("GENERAL_ERROR"));
            e.printStackTrace();
            return null;
        });

    }


    @Execute
    @Async
    public void teleportPlayer(@Context Player player, @Arg AkaniPlayer target) {
        if (player.getUniqueId().equals(target.uuid())) {
            player.sendMessage(Component.text("noSelfTeleport"));
            return;
        }
        AkaniPlayer sender = core.playerManager().onlinePlayer(player.getUniqueId()).orElseThrow();
        target.location().thenAccept(sender::teleport).exceptionally((e) -> {
            player.sendMessage(Component.text("GENERAL_ERROR"));
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

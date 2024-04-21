package it.einjojo.akani.essentials.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.permission.Permission;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.warp.Warp;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "warp", aliases = "warps")
public record WarpCommand(AkaniEssentialsPlugin plugin) {

    @Execute()
    public void listWarps(@Context CommandSender sender) {
        for (String warpName : plugin.warpManager().warpNames()) {
            Component c = plugin.miniMessage().deserialize("<click:suggest_command:'/warp %s'><hover:show_text:'<yellow>Klicke zum Teleportieren.'><gray>- <green>%s</hover></click>".formatted(warpName, warpName));
            sender.sendMessage(c);
        }
    }


    @Permission(AkaniEssentialsPlugin.PERMISSION_BASE + "warp.create")
    @Execute(name = "create")
    @Async
    public void createWarp(@Context Player sender, @Arg("name") String warpName, @Flag("--groupSpecific") boolean isGroupSpecific) {
        plugin.warpManager().createWarp(
                warpName,
                sender.getLocation(),
                isGroupSpecific ? NetworkLocation.Type.GROUP : NetworkLocation.Type.SERVER
        );
        sender.sendMessage(plugin.miniMessage().deserialize("<green>Warp <yellow>%s <green>erstellt.".formatted(warpName)));
    }


    @Permission(AkaniEssentialsPlugin.PERMISSION_BASE + "warp.delete")
    @Execute(name = "delete")
    @Async
    public void deleteWarp(@Context Player sender, @Arg Warp warp) {
        plugin.warpManager().deleteWarp(warp);
    }

    @Execute
    public void teleportToWarp(@Context Player sender, @Arg Warp warp) {
        plugin.core().playerManager().onlinePlayer(sender.getUniqueId()).ifPresent(warp::warp);
    }


    @Execute
    @Async
    public void reloadWarps() {
        plugin.warpManager().load();
    }

}

package it.einjojo.akani.essentials.command;

import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.warp.Warp;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.*;
import org.incendo.cloud.annotations.parser.Parser;
import org.incendo.cloud.annotations.suggestion.Suggestions;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;


public record WarpCommand(AkaniEssentialsPlugin plugin) {

    @Command("warp list")
    @Command("warps")
    @CommandDescription("List all warps")
    public void listWarps(CommandSender sender) {
        for (String warpName : plugin.warpManager().warpNames()) {
            Component c = plugin.miniMessage().deserialize("<click:suggest_command:'/warp %s'><hover:show_text:'<yellow>Klicke zum Teleportieren.'><gray>- <green>%s</hover></click>".formatted(warpName, warpName));
            sender.sendMessage(c);
        }
    }


    @Permission(AkaniEssentialsPlugin.PERMISSION_BASE + "warp.create")
    @Command("warps create <name>")
    public void createWarp(Player sender, @Argument("name") String warpName, @Flag("--groupSpecific") boolean isGroupSpecific) {
        if (plugin.warpManager().warp(warpName) != null) {
            sender.sendMessage(plugin.miniMessage().deserialize("<red>Warp <yellow>%s <red>existiert bereits.".formatted(warpName)));
            return;
        }
        plugin.warpManager().createWarp(
                warpName,
                sender.getLocation(),
                isGroupSpecific ? NetworkLocation.Type.GROUP : NetworkLocation.Type.SERVER
        );
        sender.sendMessage(plugin.miniMessage().deserialize("<green>Warp <yellow>%s <green>erstellt.".formatted(warpName)));
    }


    @Permission(AkaniEssentialsPlugin.PERMISSION_BASE + "warp.delete")
    @Command("warp <warp> delete")
    public void deleteWarp(Player sender, @Argument("warp") Warp warp) {
        plugin.warpManager().deleteWarp(warp);
    }

    @Command("warp|warps <warp>")
    public void teleportToWarp(Player sender, @Argument("warp") Warp warp) {
        sender.playSound(sender, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        plugin.core().playerManager().onlinePlayer(sender.getUniqueId()).ifPresent(warp::warp);
    }

    @Parser(suggestions = "warps")
    public Warp parseWarp(CommandContext<CommandSender> context, CommandInput input) {
        String warpName = input.input();
        Warp warp = plugin.warpManager().warp(warpName);
        if (warp == null) {
            context.sender().sendMessage(plugin.miniMessage().deserialize("<red>Warp <yellow>%s <red>existiert nicht.".formatted(warpName)));
            return null;
        }
        return warp;
    }

    @Suggestions("warps")
    public Iterable<String> suggestWarps(CommandContext<CommandSender> context, String input) {
        return plugin.warpManager().warpNames();
    }


    @Command("warps reload")
    @Permission(AkaniEssentialsPlugin.PERMISSION_BASE + "warp.reload")
    public void reloadWarps(CommandSender sender) {
        plugin.warpManager().load();
        sender.sendMessage(plugin.miniMessage().deserialize("<green>Warps neu geladen."));
    }

}

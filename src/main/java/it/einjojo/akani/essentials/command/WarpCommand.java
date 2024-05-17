package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.warp.Warp;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;


@CommandAlias("warp|warps|spawn")
public class WarpCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public WarpCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().getCommandCompletions().registerAsyncCompletion("warps", c -> plugin.warpManager().warpNames());
        plugin.commandManager().getCommandContexts().registerContext(Warp.class, c -> plugin.warpManager().warp(c.popFirstArg()));
        plugin.commandManager().registerCommand(this);
    }


    @Default
    @CommandCompletion("@warps")
    public void listWarpsOrWarp(Player sender, @Optional Warp warp) {
        if (warp == null && getExecCommandLabel().equalsIgnoreCase("spawn")) {
            Warp spawn = plugin.warpManager().warp("spawn");
            if (spawn == null) {
                sender.sendMessage(plugin.miniMessage().deserialize("<red>Der Spawn wurde noch nicht gesetzt."));
                return;
            }
            listWarpsOrWarp(sender, spawn);
        }
        if (warp == null) {
            for (String warpName : plugin.warpManager().warpNames()) {
                Component c = plugin.miniMessage().deserialize("<click:suggest_command:'/warp %s'><hover:show_text:'<yellow>Klicke zum Teleportieren.'><gray>- <green>%s</hover></click>".formatted(warpName, warpName));
                sender.sendMessage(c);
            }
        } else {
            plugin.core().playerManager().onlinePlayer(sender.getUniqueId()).ifPresentOrElse((p) -> {
                plugin.sendMessage(sender, "warp.teleporting", (s) -> s.replaceAll("%warp%", warp.name()));
                warp.warp(p);
            }, () -> {
                sender.sendMessage(plugin.miniMessage().deserialize("<red>Es ist ein Fehler aufgetreten."));
            });

        }
    }


    @Subcommand("create")
    @Syntax("<name> [--groupSpecific]")
    @CommandCompletion("name --groupSpecific")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "warp.create")
    @Description("Erstellt einen neuen Warp")
    public void createWarp(Player sender, String warpName, @Optional String arg) {
        boolean isGroupSpecific = arg != null && arg.equals("--groupSpecific");
        if (plugin.warpManager().warp(warpName) != null) {
            sender.sendMessage(plugin.miniMessage().deserialize("<red>Warp <yellow>%s <red>existiert bereits.".formatted(warpName)));
            return;
        }
        plugin.warpManager().createWarp(warpName, sender.getLocation(), isGroupSpecific ? NetworkLocation.Type.GROUP : NetworkLocation.Type.SERVER);
        sender.sendMessage(plugin.miniMessage().deserialize("<green>Warp <yellow>%s <green>erstellt.".formatted(warpName)));
    }

    @Subcommand("delete")
    @Syntax("<warp>")
    @CommandCompletion("@warps")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "warp.delete")
    @Description("Löscht einen Warp")
    public void deleteWarp(Player sender, Warp warp) {
        plugin.warpManager().deleteWarp(warp);
        sender.sendMessage(plugin.miniMessage().deserialize("<red>Warp <yellow>%s <red>gelöscht.".formatted(warp.name())));
    }

    @Subcommand("reload")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "warp.reload")
    @Description("Lädt die Warps neu")
    public void reloadWarps(Player sender) {
        plugin.warpManager().load();
        sender.sendMessage(plugin.miniMessage().deserialize("<green>Warps neu geladen."));
    }


}

package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.network.Server;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

@CommandAlias("eserver")
public class ServerCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public ServerCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().getCommandCompletions().registerAsyncCompletion("servers", (c) -> plugin.core().networkManager().servers().stream().map(Server::name).toList());
        plugin.commandManager().registerCommand(this);
    }

    @Subcommand("connect")
    @CommandCompletion("@servers @akaniplayers")
    @Syntax("<server> [player]")
    public void connect(Player sender, String server, @Optional AkaniPlayer optionalTarget) {
        if (optionalTarget != null && !sender.hasPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "server.connect.other")) {
            plugin.sendMessage(sender, EssentialKey.NO_PERMISSION);
            return;
        }
        AkaniPlayer target = optionalTarget == null ? plugin.core().playerManager().onlinePlayer(sender.getUniqueId()).orElseThrow() : optionalTarget;
        plugin.sendMessage(sender, EssentialKey.of("server.connect"), (msg) -> msg.replaceAll("%player%", target.name()).replaceAll("%server%", server));
        target.connect(server);
    }

    @Subcommand("list")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "server.list")
    public void list(Player sender) {
        for (Server server : plugin.core().networkManager().servers()) {
            sender.sendMessage("§8- §e" + server.name() + " §7[§c" + server.groupName() + "§7]");
        }
    }

    @Subcommand("stop")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "server.stop")
    @CommandCompletion("@servers")
    @Syntax("<server>")
    public void stop(Player sender, String serverName) {
        plugin.sendMessage(sender, EssentialKey.of("server.stop"), (msg) -> msg.replaceAll("%server%", serverName));
        plugin.core().networkManager().server(serverName).ifPresentOrElse((server) -> {
            server.runCommand("stop");
        }, () -> {
            plugin.sendMessage(sender, EssentialKey.of("server.not_found"));
        });
    }


}

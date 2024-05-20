package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("plot|p|plots")
public class PlotSquaredCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public PlotSquaredCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @Description("Makes plotsquared commands available on other servers.")
    @CommandCompletion("h|home|tp|teleport|v|visit")
    public void route(Player sender, String args) {
        plugin.core().connectionHandler().connectPlayerToServer(sender.getUniqueId(), "Citybuild-1");
        delayedRemoteCommandExecution(sender.getUniqueId(), "Citybuild-1", "plot " + args);
    }

    public void delayedRemoteCommandExecution(UUID uuid, String targetServer, String command) {
        var optionalPlayer = plugin.core().playerManager().onlinePlayer(uuid);
        if (optionalPlayer.isEmpty()) {
            return;
        }
        var player = optionalPlayer.get();
        if (!player.serverName().equals(targetServer)) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> delayedRemoteCommandExecution(uuid, targetServer, command), 15L); // retry later
            return;
        }
        player.runCommand(command);
    }
}

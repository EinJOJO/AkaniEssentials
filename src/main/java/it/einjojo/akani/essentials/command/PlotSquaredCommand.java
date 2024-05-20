package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import net.kyori.adventure.title.Title;
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
        delayedRemoteCommandExecution(sender.getUniqueId(), "Citybuild-1", "plot " + args, 0);
    }

    public void delayedRemoteCommandExecution(UUID uuid, String targetServer, String command, int iteration) {
        Player bukkitPlayer = Bukkit.getPlayer(uuid);
        if (bukkitPlayer != null) {
            bukkitPlayer.showTitle(Title.title(
                    plugin.core().messageManager().message(EssentialKey.of("plots.connecting.title")),
                    plugin.core().messageManager().message("plots.connecting.subtitle")
            ));
        }

        var optionalPlayer = plugin.core().playerManager().onlinePlayer(uuid);
        if (optionalPlayer.isEmpty()) {
            return;
        }
        var player = optionalPlayer.get();
        if (!player.serverName().equals(targetServer)) {
            if (iteration > 3) {
                plugin.sendMessage(player, EssentialKey.of("plots.connecting.failed"));
                return;
            }
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> delayedRemoteCommandExecution(uuid, targetServer, command, iteration + 1), 15L); // retry later
            return;
        }
        player.runCommand(command);
    }
}

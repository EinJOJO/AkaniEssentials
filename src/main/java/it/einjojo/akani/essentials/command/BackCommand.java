package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.entity.Player;

@CommandAlias("back")
public class BackCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public BackCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    public void back(Player sender) {
        AkaniPlayer player = plugin.core().playerManager().onlinePlayer(sender.getUniqueId()).orElseThrow();
        plugin.core().backService().teleportBackAsync(player).thenAccept((success) -> {
            if (success) {
                sender.sendMessage("Teleported back");
            } else {
                sender.sendMessage("No back location found");
            }
        });
    }

}

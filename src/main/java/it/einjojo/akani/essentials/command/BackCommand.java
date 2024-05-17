package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Private;
import co.aikar.commands.annotation.Subcommand;
import it.einjojo.akani.core.api.economy.BadBalanceException;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

@CommandAlias("back")
public class BackCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public BackCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    public void preCheckPermission(Player sender) {
        if (sender.hasPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "back")) {
            sendBack(sender);
        } else {
            plugin.sendMessage(sender, EssentialKey.of("announce-costs"));
        }
    }

    @Subcommand("confirm")
    @Private
    public void sendBack(Player sender) {
        AkaniPlayer player = plugin.core().playerManager().onlinePlayer(sender.getUniqueId()).orElseThrow();
        if (!sender.hasPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "back")) {
            try {
                player.coins().removeBalance(300);
            } catch (BadBalanceException ex) {
                plugin.sendMessage(sender, EssentialKey.NOT_ENOUGH_COINS);
                return;
            }
        }
        plugin.core().backService().teleportBackAsync(player).thenAccept((success) -> {
            if (success) {
                plugin.sendMessage(sender, EssentialKey.of("back.success"));
            } else {
                plugin.sendMessage(sender, EssentialKey.of("back.no_location"));
            }
        });
    }


}

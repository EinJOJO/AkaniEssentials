package it.einjojo.akani.essentials.command.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.MessageKey;
import org.bukkit.entity.Player;

@CommandAlias("money|balance")
public class MoneyCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public MoneyCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }


    @Default
    @CommandCompletion("@akaniofflineplayers:limit=8")
    public void showBalance(Player sender, @Optional AkaniOfflinePlayer optionalTarget) {
        long balance = 0;
        if (optionalTarget != null) {
            optionalTarget.coinsAsync().thenAccept((c) -> {
                displayCoins(sender, c.balance());
            });
        } else {
            plugin.core().playerManager().onlinePlayer(sender.getUniqueId()).ifPresentOrElse((p) -> {
                displayCoins(sender, p.coins().balance());
            }, () -> {
                plugin.getLogger().warning("Could not get economy balance for player " + sender.getName() + " as they are not online.");
                plugin.sendMessage(sender, MessageKey.GENERIC_ERROR);
            });
        }

    }

    private void displayCoins(Player player, long coins) {
        plugin.sendMessage(player, MessageKey.of("economy.balance"), (s) -> s.replaceAll("%balance%", String.valueOf(coins)));
    }

}

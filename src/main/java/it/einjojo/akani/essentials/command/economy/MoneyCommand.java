package it.einjojo.akani.essentials.command.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.economy.BadBalanceException;
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

    @Subcommand("set")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "money.set")
    @CommandCompletion("@akaniofflineplayers:limit=8 0")
    @Syntax("<player> <amount>")
    public void setBalance(Player sender, AkaniOfflinePlayer target, long coins) {
        target.coinsAsync().thenAccept((c) -> {
            try {
                c.setBalance(coins);
                plugin.sendMessage(sender, MessageKey.of("economy.set"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
            } catch (BadBalanceException e) {
                plugin.sendMessage(sender, MessageKey.of("economy.error"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
            }
        });
        plugin.sendMessage(sender, MessageKey.of("economy.set"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
    }

    @Subcommand("remove")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "money.remove")
    @CommandCompletion("@akaniofflineplayers:limit=8 0")
    @Syntax("<player> <amount>")
    public void removeBalance(Player sender, AkaniOfflinePlayer target, long coins) {
        try {
            target.coins().removeBalance(coins);
            plugin.sendMessage(sender, MessageKey.of("economy.remove"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
        } catch (BadBalanceException e) {
            plugin.sendMessage(sender, MessageKey.of("economy.error"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
        }
    }

    @Subcommand("add")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "money.add")
    @CommandCompletion("@akaniofflineplayers:limit=8 0")
    @Syntax("<player> <amount>")
    public void addBalance(Player sender, AkaniOfflinePlayer target, long coins) {
        try {
            target.coins().addBalance(coins);
            plugin.sendMessage(sender, MessageKey.of("economy.add"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
        } catch (BadBalanceException e) {
            plugin.sendMessage(sender, MessageKey.of("economy.error"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
        }
    }


    private void displayCoins(Player player, long coins) {
        plugin.sendMessage(player, MessageKey.of("economy.balance"), (s) -> s.replaceAll("%balance%", String.valueOf(coins)));
    }

}

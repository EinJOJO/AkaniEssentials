package it.einjojo.akani.essentials.command.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.economy.BadBalanceException;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.MessageKey;
import org.bukkit.entity.Player;

@CommandAlias("thaler|taler")
public class ThalerCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public ThalerCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @CommandCompletion("@akaniofflineplayers:limit=8")
    public void showBalance(Player sender, @Optional AkaniOfflinePlayer optionalTarget) {
        if (optionalTarget != null) {
            displayOtherBalance(sender, optionalTarget);
        } else {
            displayOwnBalance(sender);
        }
    }

    private void displayOtherBalance(Player sender, AkaniOfflinePlayer target) {
        target.thalerAsync().thenAccept((c) -> {
            plugin.sendMessage(sender, MessageKey.of("thaler.balance.other"),
                    (s) -> s.replaceAll("%player%", target.name())
                            .replaceAll("%balance%", String.valueOf(c.balance())));
        }).exceptionally((ex) -> {
            plugin.sendMessage(sender, MessageKey.GENERIC_ERROR);
            plugin.getLogger().warning("Could not get economy balance for player " + target.name() + ".");
            plugin.getLogger().warning(ex.getMessage());
            return null;
        });
    }

    private void displayOwnBalance(Player sender) {
        plugin.core().playerManager().onlinePlayer(sender.getUniqueId()).ifPresentOrElse((p) -> {
            p.thalerAsync().thenAccept((c) -> {
                plugin.sendMessage(sender, MessageKey.of("thaler.balance.own"), (s) ->
                        s.replaceAll("%balance%", String.valueOf(c.balance())));
            }).exceptionally((ex) -> {
                plugin.getLogger().warning("Could not get economy balance for player " + sender.getName() + ".");
                plugin.getLogger().warning(ex.getMessage());
                plugin.sendMessage(sender, MessageKey.GENERIC_ERROR);
                return null;
            });
        }, () -> {
            plugin.getLogger().warning("Could not get economy balance for player " + sender.getName() + " as they are not online.");
            plugin.sendMessage(sender, MessageKey.GENERIC_ERROR);
        });
    }

    @Subcommand("set")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "thaler.set")
    @CommandCompletion("@akaniofflineplayers:limit=8 0")
    @Syntax("<player> <amount>")
    public void setBalance(Player sender, AkaniOfflinePlayer target, long coins) {
        target.thalerAsync().thenAccept((c) -> {
            try {
                c.setBalance(coins);
                plugin.sendMessage(sender, MessageKey.of("thaler.set"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
            } catch (BadBalanceException e) {
                plugin.sendMessage(sender, MessageKey.of("economy.error"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
            }
        });
        plugin.sendMessage(sender, MessageKey.of("thaler.set"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
    }

    @Subcommand("remove")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "thaler.remove")
    @CommandCompletion("@akaniofflineplayers:limit=8 0")
    @Syntax("<player> <amount>")
    public void removeBalance(Player sender, AkaniOfflinePlayer target, long coins) {
        target.thalerAsync().thenAccept((t) -> {
            try {
                t.removeBalance(coins);
                plugin.sendMessage(sender, MessageKey.of("thaler.remove"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
            } catch (BadBalanceException e) {
                plugin.sendMessage(sender, MessageKey.of("economy.error"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
            }
        }).exceptionally((ex) -> {
            plugin.sendMessage(sender, MessageKey.GENERIC_ERROR);
            return null;
        });
    }

    @Subcommand("add")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "thaler.add")
    @CommandCompletion("@akaniofflineplayers:limit=8 0")
    @Syntax("<player> <amount>")
    public void addBalance(Player sender, AkaniOfflinePlayer target, long coins) {
        try {
            target.coins().addBalance(coins);
            plugin.sendMessage(sender, MessageKey.of("thaler.add"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
        } catch (BadBalanceException e) {
            plugin.sendMessage(sender, MessageKey.of("economy.error"), (s) -> s.replaceAll("%player%", target.name()).replaceAll("%balance%", String.valueOf(coins)));
        }
    }


}

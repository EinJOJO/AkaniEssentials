package it.einjojo.akani.essentials.command.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.economy.BadBalanceException;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("pay")
public class PayCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public PayCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);

    }


    @Default
    @CommandCompletion("@akaniplayers 1|20|50")
    @Syntax("<player> <amount>")
    public void pay(Player sender, AkaniOfflinePlayer target, int amount) {
        if (sender.getUniqueId().equals(target.uuid())) {
            plugin.sendMessage(sender, "essentials.pay.self");
            return;
        }
        AkaniPlayer akaniPlayer = plugin.core().playerManager().onlinePlayer(sender.getUniqueId()).orElseThrow(IllegalArgumentException::new);
        pay(akaniPlayer, target, amount);
    }

    public void pay(AkaniPlayer sender, AkaniOfflinePlayer receiver, int amount) {
        if (amount <= 0) {
            plugin.sendMessage(sender, EssentialKey.of("coins.bad-value"));
            return;
        }
        try {
            sender.coins().removeBalance(amount);
            receiver.coins().addBalance(amount);
        } catch (BadBalanceException ex) {
            plugin.sendMessage(sender, EssentialKey.NOT_ENOUGH_COINS);
            return;
        }
        plugin.sendMessage(sender, EssentialKey.of("pay.success"), (s) -> s.replaceAll("%amount%", String.valueOf(amount)).replaceAll("%player%", receiver.name()));
        if (receiver instanceof AkaniPlayer online) {
            plugin.sendMessage(online, EssentialKey.of("pay.success.other"), (s) -> s.replaceAll("%amount%", String.valueOf(amount)).replaceAll("%player%", sender.name()));
        }


    }

    @Subcommand("*")
    @CommandCompletion("1|20|50")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "pay.all")
    public void payAll(Player sender, int amount) {
        List<AkaniPlayer> akaniPlayerList = plugin.core().playerManager().onlinePlayers();
        int totalAmount = amount * akaniPlayerList.size();
        AkaniPlayer akaniSender = plugin.core().playerManager().onlinePlayer(sender.getUniqueId()).orElseThrow(IllegalArgumentException::new);
        if (totalAmount > akaniSender.coins().balance()) {
            plugin.sendMessage(sender, EssentialKey.NOT_ENOUGH_COINS);
            return;
        }
        for (AkaniPlayer receiver : akaniPlayerList) {
            if (receiver.equals(akaniSender)) {
                continue;
            }
            pay(akaniSender, receiver, amount);
        }

    }

    @CatchUnknown
    public void unknown(Player sender) {
        plugin.sendCommandUsageMessage(sender, "/pay <player> <amount>");
    }

}

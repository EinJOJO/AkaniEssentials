package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import io.th0rgal.oraxen.api.OraxenItems;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.MessageKey;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;

@CommandAlias("give")
public class GiveCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public GiveCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().getCommandCompletions().registerStaticCompletion("items",
                Arrays.stream(Material.values()).map(Enum::name).toList());
        plugin.commandManager().getCommandCompletions().registerAsyncCompletion("oraxenitem",
                c -> OraxenItems.entryStream().map(Map.Entry::getKey).toList());
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @CommandCompletion("@items @range:1-64 @akaniplayers:includeSender")
    @Syntax("<item> [amount] [player]")
    public void giveItem(CommandSender sender, Material material, @co.aikar.commands.annotation.Optional String amount, @co.aikar.commands.annotation.Optional Player target) {
        if (target == null) {
            target = (Player) sender;
        }
        if (amount == null) {
            amount = "1";
        }
        target.getInventory().addItem(new org.bukkit.inventory.ItemStack(material, Integer.parseInt(amount)));
        Player finalTarget = target;
        String finalAmount = amount;
        plugin.sendMessage(sender, MessageKey.of("give.success"), s -> s.replaceAll("%player%", finalTarget.getName())
                .replaceAll("%amount%", finalAmount).replaceAll("%item%", material.name()));
    }
/*
    @CommandCompletion("@oraxenitem @akaniplayers:includeSender @range:1-64")
    @Syntax("<oraxenitem> [amount] [player]")
    public void giveOraxenItem(Player player, String itemId, @co.aikar.commands.annotation.Optional int amount) {
        if (amount == 0) {
            amount = 1;
        }
        int finalAmount = amount;
        Optional<ItemStack> oraxenItem = OraxenItems.getOptionalItemById(itemId).map(item -> item.setAmount(finalAmount).build());
        if (oraxenItem.isPresent()) {
            player.getInventory().addItem(oraxenItem.get());
            plugin.sendMessage(player, MessageKey.of("give.success"), s -> s.replaceAll("%player%", player.getName())
                    .replaceAll("%amount%", String.valueOf(finalAmount)).replaceAll("%item%", MiniMessage.miniMessage().serialize(oraxenItem.get().displayName())));
        } else {
            plugin.sendMessage(player, MessageKey.of("give.item_not_found"), s -> s.replaceAll("%item%", itemId));
        }
    }
*/
}

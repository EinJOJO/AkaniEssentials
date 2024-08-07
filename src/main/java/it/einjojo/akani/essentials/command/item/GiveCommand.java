package it.einjojo.akani.essentials.command.item;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;


@CommandAlias("give|giveall")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "give")
public class GiveCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public GiveCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().getCommandCompletions().registerStaticCompletion("items",
                Arrays.stream(Material.values()).map(Enum::name).toList());
        plugin.commandManager().registerCommand(this);
    }


    @Default
    @CommandCompletion("@items @range:1-64 @players")
    @Syntax("<item> [amount] [player]")
    public void giveItem(CommandSender sender, Material material, @Default(value = "1") String amount, @co.aikar.commands.annotation.Optional String target) {
        if (getExecCommandLabel().equalsIgnoreCase("giveall")) {
            giveAllItemInHand((Player) sender);
            return;
        }
        try {
            int amountInt = Integer.parseInt(amount);
            if (amountInt < 1 || amountInt > 64) {
                sender.sendMessage(plugin.miniMessage().deserialize("<red>Die Anzahl muss zwischen <yellow>1 <red>und <yellow>64 <red>liegen."));
                return;
            }
            processGiveItem(sender, material, amountInt, target);
            sendSuccessMessage((Player) sender, material, amountInt, Optional.ofNullable(target));
        } catch (NumberFormatException e) {
            if (target == null && amount != null) {
                processGiveItem(sender, material, 1, amount);
                sendSuccessMessage((Player) sender, material, 1, Optional.of(amount));
            } else if (sender instanceof Player) {
                processGiveItem(sender, material, 1, null);
            }
        }
    }

    @Subcommand("all|*")
    @CommandPermission("akani.essentials.give.all")
    @CommandCompletion("@items @range:1-64")
    @Syntax("<item> [amount]")
    public void giveAll(CommandSender sender, Material material, @Default(value = "1") int amount) {
        sendSuccessMessage((Player) sender, material, amount, Optional.of("ALL"));
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            giveItemToPlayer(onlinePlayer, material, amount);
        }
    }

    @Subcommand("all|* itemInHand")
    public void giveAllItemInHand(Player sender) {
        ItemStack is = sender.getInventory().getItemInMainHand();
        if (is.getType().isAir()) {
            plugin.sendMessage(sender, EssentialKey.of("give.no-item-in-hand"));
            return;
        }
        //TODO CrossServer
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.getInventory().addItem(is);
        }
        sendSuccessMessage(sender, is.getType(), 1, Optional.of("ALL"));
    }

    private void processGiveItem(CommandSender sender, Material material, int amount, String targetName) {
        Player player = targetName != null ? plugin.getServer().getPlayer(targetName) : sender instanceof Player ? (Player) sender : null;
        if (player != null) {
            giveItemToPlayer(player, material, amount);
        } else {
            plugin.sendMessage(sender, EssentialKey.PLAYER_NOT_ONLINE);
        }
    }

    @CatchUnknown
    public void onUnknownCommand(CommandSender sender) {
        sender.sendMessage("Give catcher");
    }

    public void sendSuccessMessage(Player player, Material material, int amount, Optional<String> targetName) {
        if (targetName.isPresent())
            plugin.sendMessage(player, EssentialKey.of("give.success"), s -> s.replaceAll("%player%", targetName.get())
                    .replaceAll("%amount%", String.valueOf(amount)).replaceAll("%item%", material.name()));
        else
            plugin.sendMessage(player, EssentialKey.of("give.success"), s -> s.replaceAll("%player%", player.getName())
                    .replaceAll("%amount%", String.valueOf(amount)).replaceAll("%item%", material.name()));
    }

    public void giveItemToPlayer(Player player, Material material, int amount) {
        player.getInventory().addItem(new org.bukkit.inventory.ItemStack(material, amount));
    }


}

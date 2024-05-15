package it.einjojo.akani.essentials.command.item;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "hat")
@CommandAlias("hat|hut")
public class HatCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public HatCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    public void setHat(Player sender, @Optional OnlinePlayer target) {
        ItemStack hatItem = sender.getInventory().getItemInMainHand().clone();
        if (target == null) {
            setHat(sender, hatItem);
            plugin.sendMessage(sender, EssentialKey.of("hat.success"));
        } else {
            setHat(target.player, hatItem);
            plugin.sendMessage(sender, EssentialKey.of("hat.success.other"), (s) -> s.replaceAll("%player%", target.player.getName()));
        }
    }

    private void setHat(Player player, ItemStack item) {
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet != null && !helmet.getType().isAir()) {
            player.getInventory().addItem(helmet);
        }
        player.getInventory().setHelmet(item);
    }
}

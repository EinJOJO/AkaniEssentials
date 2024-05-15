package it.einjojo.akani.essentials.command.item;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import it.einjojo.akani.core.paper.util.TextUtil;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("rename|unrename")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "rename")
public class RenameCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public RenameCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    public void renameItem(Player player, String rename) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType().equals(Material.AIR)) {
            plugin.sendMessage(player, EssentialKey.of("rename.no-item-in-hand"));
            return;
        }
        if (getExecCommandLabel().equalsIgnoreCase("rename")) {
            itemInHand.getItemMeta().displayName(plugin.miniMessage().deserialize(TextUtil.transformAmpersandToMiniMessage(rename)));
            plugin.sendMessage(player, EssentialKey.of("rename.success"));
        } else {
            itemInHand.getItemMeta().displayName(null);
            plugin.sendMessage(player, EssentialKey.of("rename.remove-success"));
        }
    }

}

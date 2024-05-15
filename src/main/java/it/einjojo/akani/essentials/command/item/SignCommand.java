package it.einjojo.akani.essentials.command.item;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import it.einjojo.akani.core.paper.util.TextUtil;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

@CommandAlias("sign|signature")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "sign")
public class SignCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public SignCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    public void signItem(Player player, @Optional String message) {
        if (player.getInventory().getItemInMainHand().getType().isAir()) {
            plugin.sendMessage(player, "item-signature.no-item-in-hand");
            return;
        }
        applySignature(player, java.util.Optional.ofNullable(message), player.getInventory().getItemInMainHand());
        plugin.sendMessage(player, "item-signature.success");
    }

    private void applySignature(Player player, java.util.Optional<String> message, org.bukkit.inventory.ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        List<Component> lore = meta.lore();
        List<Component> newLore = new LinkedList<>(lore != null ? lore : List.of());
        plugin.core().luckPermsHook().prefix(player.getUniqueId()).thenAccept((playerPrefix) -> {
            for (String line : plugin.getConfig().getStringList("item-signature")) {
                Component l = plugin.miniMessage().deserialize(
                        TextUtil.transformAmpersandToMiniMessage(line
                                .replaceAll("%player%", playerPrefix + " " + player.getName())
                                .replaceAll("%message%", (message.orElse("")))));
                newLore.add(l);
            }
            meta.lore(newLore);
            item.setItemMeta(meta);
        });

    }


}

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

@CommandAlias("sign|unsign")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "sign")
public class SignCommand extends BaseCommand {
    private static final String HIDDEN_SIGNATURE_PREFIX = "<click:suggest_command:'/hidden'></click>";
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
        if (getExecCommandLabel().equals("sign")) {
            applySignature(player, java.util.Optional.ofNullable(message), player.getInventory().getItemInMainHand());
            plugin.sendMessage(player, "item-signature.success");
        } else {
            unsignItem(player.getInventory().getItemInMainHand());
            plugin.sendMessage(player, "item-signature.remove-success");
        }
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
                        HIDDEN_SIGNATURE_PREFIX + TextUtil.transformAmpersandToMiniMessage(line
                                .replaceAll("%player%", playerPrefix + " " + player.getName())
                                .replaceAll("%message%", (message.orElse("")))));
                newLore.add(l);
            }
            meta.lore(newLore);
            item.setItemMeta(meta);
        });
    }

    public void unsignItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }
        List<Component> lore = meta.lore();
        if (lore == null) {
            return;
        }
        List<Component> newLore = new LinkedList<>();
        for (Component line : lore) {
            String minimessage = plugin.miniMessage().serialize(line);
            if (!minimessage.startsWith(HIDDEN_SIGNATURE_PREFIX)) {
                newLore.add(line);
            }
        }
        meta.lore(newLore);
        itemStack.setItemMeta(meta);
    }


}

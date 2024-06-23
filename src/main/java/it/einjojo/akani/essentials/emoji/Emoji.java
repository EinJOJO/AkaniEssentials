package it.einjojo.akani.essentials.emoji;

import it.einjojo.akani.core.paper.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Stream;

public class Emoji {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    public static String[] NO_ALIASES = new String[0];
    private final String name;
    private final String emoji;
    private final String permission;
    private final String[] aliases;
    private final EmojiRarity rarity;
    private final int customModelData;
    private final ItemStack guiItem;


    public Emoji(String name, String permission, String[] aliases, EmojiRarity rarity, String emoji, int customModelData) {
        this.customModelData = customModelData;
        this.permission = permission;
        this.name = name;
        this.aliases = aliases;
        this.rarity = rarity;
        this.emoji = emoji;
        guiItem = createGuiItem();
    }

    public String name() {
        return name;
    }

    public String[] aliases() {
        return aliases;
    }

    private String aliasMiniMessage() {
        if (aliases.length == 0) {
            return "<red>Keine";
        }
        return String.join("<dark_gray>,</dark_gray> ", aliases);

    }

    public ItemStack createGuiItem() {
        return new ItemBuilder(Material.RED_DYE).customModelData(customModelData)
                .displayName(Component.text(name, rarity().color()))
                .lore(Stream.of(
                        "",
                        "<gray>Seltenheit<dark_gray>: <rarity>",
                        "<gray>Alias<dark_gray>: <gray><alias></gray>",
                        ""
                ).map(s -> miniMessage().deserialize(s,
                        Placeholder.component("rarity", rarity().component()),
                        Placeholder.parsed("alias", aliasMiniMessage())
                )).toList())
                .build();
    }

    public boolean isUnlocked(Player player) {
        return player.hasPermission(permission);
    }

    public EmojiRarity rarity() {
        return rarity;
    }

    public String emoji() {
        return emoji;
    }

    public String permission() {
        return permission;
    }

    public int customModelData() {
        return customModelData;
    }

    public ItemStack guiItem() {
        return guiItem;
    }


    public static MiniMessage miniMessage() {
        return miniMessage;
    }
}

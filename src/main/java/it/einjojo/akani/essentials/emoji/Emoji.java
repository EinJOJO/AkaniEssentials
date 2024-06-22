package it.einjojo.akani.essentials.emoji;

import org.bukkit.inventory.ItemStack;

public class Emoji {
    public static String[] NO_ALIASES = new String[0];
    private final String name;
    private final String emoji;
    private final String permission;
    private final String[] aliases;
    private final EmojiRarity rarity;
    private final int customModelData;


    public Emoji(String name, String permission, String[] aliases, EmojiRarity rarity, String emoji, int customModelData) {
        this.customModelData = customModelData;
        this.permission = permission;
        this.name = name;
        this.aliases = aliases;
        this.rarity = rarity;
        this.emoji = emoji;
    }

    public String name() {
        return name;
    }

    public String[] aliases() {
        return aliases;
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
        ItemBuilder
    }
}

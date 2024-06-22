package it.einjojo.akani.essentials.emoji;

public class Emoji {
    public static String[] NO_ALIASES = new String[0];
    private final String name;
    private final String permission;
    private final String[] aliases;
    private final EmojiRarity rarity;
    private final String emoji;


    public Emoji(String name, String permission, String[] aliases, EmojiRarity rarity, String emoji) {
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
}

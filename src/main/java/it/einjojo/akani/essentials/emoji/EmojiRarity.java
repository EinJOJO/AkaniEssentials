package it.einjojo.akani.essentials.emoji;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum EmojiRarity {
    BASIC(Component.text("Basic"), NamedTextColor.GRAY),
    RARE(Component.text("Rare"), NamedTextColor.BLUE),
    EPIC(Component.text("Epic"), NamedTextColor.DARK_PURPLE),
    LEGENDARY(Component.text("Legendary"), NamedTextColor.GOLD);
    private final Component component;
    private final TextColor color;

    EmojiRarity(Component component, TextColor color) {
        this.component = component;
        this.color = color;
    }

    public TextColor color() {
        return color;
    }

    public Component component() {
        return component.color(color);
    }

}

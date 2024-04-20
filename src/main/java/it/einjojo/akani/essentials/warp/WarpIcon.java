package it.einjojo.akani.essentials.warp;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public record WarpIcon(ItemStack itemStack) {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    public static final WarpIcon DEFAULT = builder().material(Material.STONE).displayNameMiniMessage("<gray>default").descriptionMiniMessage("description").build();

    public static Builder builder() {
        return new Builder();
    }

    public static WarpIcon fromJsonObject(JsonObject j) {
        return builder()
                .material(Material.valueOf(j.get("m").getAsString()))
                .displayNameMiniMessage(j.get("n").getAsString())
                .descriptionMiniMessage(j.get("d").getAsString())
                .customModelData(j.get("c").getAsInt())
                .build();
    }

    public JsonObject toJsonObject() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("m", itemStack.getType().name());
        jsonObject.addProperty("n", miniMessage.serialize(itemMeta.displayName()));
        jsonObject.addProperty("d", miniMessage.serialize(itemStack.getItemMeta().lore().get(0)));
        jsonObject.addProperty("c", itemStack.getItemMeta().getCustomModelData());
        return jsonObject;
    }

    public static class Builder {
        private Material material = Material.STONE;
        private Component displayName = Component.empty();
        private Component description;
        private int customModelData = 0;

        public Builder(Material material, Component displayName, Component description, int customModelData) {
            this.material = material;
            this.displayName = displayName;
            this.description = description;
            this.customModelData = customModelData;
        }

        public Builder() {
        }

        public Builder material(Material material) {
            this.material = material;
            return this;
        }

        public Builder displayNameMiniMessage(String displayName) {
            this.displayName = miniMessage.deserialize(displayName);
            return this;
        }

        public Builder displayName(Component displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder descriptionMiniMessage(String description) {
            this.description = miniMessage.deserialize(description);
            return this;
        }

        public Builder description(Component description) {
            this.description = description;
            return this;
        }

        public Builder customModelData(int customModelData) {
            this.customModelData = customModelData;
            return this;
        }

        public WarpIcon build() {
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(displayName);
            itemMeta.lore(List.of(description));
            itemMeta.setCustomModelData(customModelData);
            itemStack.setItemMeta(itemMeta);
            return new WarpIcon(itemStack);

        }
    }

}

package it.einjojo.akani.essentials.emoji;

import com.google.common.collect.ImmutableList;
import it.einjojo.akani.essentials.util.EmojiConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EmojiManager {
    private static final Logger log = LoggerFactory.getLogger(EmojiManager.class);
    private final List<Emoji> allEmojis = new LinkedList<>();
    private final EmojiConfig config;
    private boolean dirty;

    public EmojiManager(EmojiConfig emojiConfig) {
        this.config = emojiConfig;
    }

    public Emoji emoji(String nameOrAlias) {
        for (Emoji emoji : allEmojis) {
            if (emoji.name().equalsIgnoreCase(nameOrAlias)) {
                return emoji;
            }
            for (String alias : emoji.aliases()) {
                if (alias.equalsIgnoreCase(nameOrAlias)) {
                    return emoji;
                }
            }
        }
        return null;
    }

    public List<Emoji> allEmojis() {
        return ImmutableList.copyOf(allEmojis);
    }

    public void addEmoji(Emoji emoji) {
        allEmojis.add(emoji);
        dirty = true;
    }

    public void removeEmoji(Emoji emoji) {
        allEmojis.remove(emoji);
        dirty = true;
    }

    public void load() {
        config.load();
        load(config.config());
    }

    public void save() {
        save(config.config());
        config.save();
    }

    public void load(FileConfiguration fileConfiguration) {
        allEmojis.clear();
        dirty = false;
        var mapList = fileConfiguration.getMapList("emojis");
        for (Map<?, ?> map : mapList) {
            try {
                String name = (String) map.get("name");
                String permission = (String) map.get("permission");
                String emoji = (String) map.get("emoji");
                String[] aliases = ((List<String>) map.get("aliases")).toArray(new String[0]);
                EmojiRarity rarity = EmojiRarity.valueOf((String) map.get("rarity"));
                int customModelData = (int) map.get("customModelData");
                Emoji emoji1 = new Emoji(name, permission, aliases, rarity, emoji, customModelData);
                allEmojis.add(emoji1);
            } catch (Exception ex) {
                log.error("Failed to load emoji", ex);
            }
        }

    }

    public void save(FileConfiguration fileConfiguration) {
        dirty = false;
        List<Map<String, Object>> mapList = new LinkedList<>();
        for (Emoji emoji : allEmojis) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", emoji.name());
            map.put("permission", emoji.permission());
            map.put("emoji", emoji.emoji());
            map.put("aliases", Arrays.asList(emoji.aliases()));
            map.put("rarity", emoji.rarity().name());
            map.put("customModelData", emoji.customModelData());
            mapList.add(map);
        }
        fileConfiguration.set("emojis", mapList);
    }

    public boolean isDirty() {
        return dirty;
    }


}

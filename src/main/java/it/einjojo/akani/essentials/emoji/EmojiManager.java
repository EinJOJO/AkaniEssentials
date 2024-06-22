package it.einjojo.akani.essentials.emoji;

import java.util.*;

public class EmojiManager {
    private final List<Emoji> allEmojis = new LinkedList<>();
    private final Map<UUID, PlayerEmojiContainer> playerContainers = new HashMap<>();


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
        return allEmojis;
    }

    public void addEmoji(Emoji emoji) {
        allEmojis.add(emoji);
    }

    public void removeEmoji(Emoji emoji) {
        allEmojis.remove(emoji);
    }

    public void updateContainers() {

    }

    public PlayerEmojiContainer container(UUID playerUuid) {
        return playerContainers.computeIfAbsent(playerUuid, (uuid) -> new PlayerEmojiContainer(playerUuid));
    }





}

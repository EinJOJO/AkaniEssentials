package it.einjojo.akani.essentials.emoji;

import it.einjojo.akani.core.util.LuckPermsHook;

import java.util.*;

public class PlayerEmojiContainer {
    private final Set<Emoji> emojis = new HashSet<>();
    private final UUID playerUuid;

    public PlayerEmojiContainer(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }


    /**
     * @param emoji emoji to check
     * @return true if the player has the emoji
     */
    public boolean hasEmoji(Emoji emoji) {
        return emojis.contains(emoji);
    }

    /**
     * @return The Set of available emojis for the player
     */
    public Set<Emoji> emojis() {
        return emojis;
    }

    /**
     * @return Player's UUID
     */
    public UUID playerUuid() {
        return playerUuid;
    }
}

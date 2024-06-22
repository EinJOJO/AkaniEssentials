package it.einjojo.akani.essentials.emoji;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiMessageProcessor {
    private static final Pattern EMOJI_PATTERN = Pattern.compile(":[a-zA-Z0-9_]+:");
    private final EmojiManager emojiManager;

    public EmojiMessageProcessor(EmojiManager emojiManager) {
        this.emojiManager = emojiManager;
    }

    public String process(UUID sender, String message) {
        Matcher matcher = EMOJI_PATTERN.matcher(message);
        StringBuilder processedMessage = new StringBuilder();

        while (matcher.find()) {

            String placeholder = matcher.group();
            String emojiName = placeholder.substring(1, placeholder.length() - 1); // Remove the surrounding colons
            Emoji emoji = emojiManager.emoji(emojiName);

            if (emoji != null && emojiManager.container(sender).hasEmoji(emoji)) {
                matcher.appendReplacement(processedMessage, emoji.emoji());
            } else {
                matcher.appendReplacement(processedMessage, placeholder);
            }
        }

        matcher.appendTail(processedMessage);
        return processedMessage.toString();
    }

}

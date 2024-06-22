import it.einjojo.akani.essentials.emoji.Emoji;
import it.einjojo.akani.essentials.emoji.EmojiManager;
import it.einjojo.akani.essentials.emoji.EmojiMessageProcessor;
import it.einjojo.akani.essentials.emoji.EmojiRarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmojiMessageProcessorTest {
    private EmojiManager emojiManager;
    private EmojiMessageProcessor emojiMessageProcessor;

    @BeforeEach
    public void setup() {
        emojiManager = new EmojiManager();
        emojiMessageProcessor = new EmojiMessageProcessor(emojiManager);
        Emoji emoji = new Emoji("smile", "sm", new String[]{"sm"}, EmojiRarity.BASIC, "😄");
        emojiManager.addEmoji(emoji);
    }

    @Test
    public void replacesEmojiPlaceholdersWithEmojisWhenAvailable() {
        UUID sender = UUID.randomUUID();
        emojiManager.container(sender).emojis().add(emojiManager.emoji("smile"));
        String result = emojiMessageProcessor.process(sender, ":smile: Hello World :sm:");
        assertEquals("😄 Hello World 😄", result);
    }

    @Test
    public void doesNotReplaceEmojiPlaceholdersWhenEmojiDoesNotExist() {
        UUID sender = UUID.randomUUID();
        String result = emojiMessageProcessor.process(sender, ":nonexistent: Hello World :nonexistent:");
        assertEquals(":nonexistent: Hello World :nonexistent:", result);
    }

    @Test
    public void doesNotReplaceEmojiPlaceholdersWhenSenderDoesNotHaveEmoji() {
        UUID sender = UUID.randomUUID();
        String result = emojiMessageProcessor.process(sender, ":smile: Hello World :smile:");
        assertEquals(":smile: Hello World :smile:", result);
    }

    @Test
    public void handlesMessageWithoutEmojis() {
        UUID sender = UUID.randomUUID();

        String result = emojiMessageProcessor.process(sender, "Hello World");

        assertEquals("Hello World", result);
    }
}
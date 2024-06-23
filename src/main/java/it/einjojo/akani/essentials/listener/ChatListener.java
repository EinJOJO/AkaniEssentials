package it.einjojo.akani.essentials.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import it.einjojo.akani.core.paper.util.TextUtil;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.emoji.EmojiMessageProcessor;
import it.einjojo.akani.essentials.service.MessageService;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    private static final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();
    private final static String COLORED_PERMISSION = AkaniEssentialsPlugin.PERMISSION_BASE + "chatcolor";

    private final MessageService messageService;
    private final EmojiMessageProcessor emojiMessageProcessor;

    public ChatListener(AkaniEssentialsPlugin plugin, EmojiMessageProcessor emojiMessageProcessor) {
        this.messageService = plugin.messageService();
        this.emojiMessageProcessor = emojiMessageProcessor;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onMessage(AsyncChatEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(true);
        // Serialize the message to plain text
        var message = plainTextComponentSerializer.serialize(event.message());
        // prevent minimessage-message usage
        message = MessageService.sanitizeMessage(message);
        if (event.getPlayer().hasPermission(COLORED_PERMISSION)) {
            message = TextUtil.transformAmpersandToMiniMessage(message);
        }
        message = emojiMessageProcessor.process(event.getPlayer(), message);
        messageService.publishPublicChatMessage(event.getPlayer(), message);
    }

}

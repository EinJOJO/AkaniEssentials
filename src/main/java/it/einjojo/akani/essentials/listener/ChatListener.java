package it.einjojo.akani.essentials.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.service.MessageService;
import it.einjojo.akani.essentials.util.TextUtil;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    private static final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();
    private final static String COLORED_PERMISSION = AkaniEssentialsPlugin.PERMISSION_BASE + "chatcolor";

    private final MessageService messageService;
    private final AkaniEssentialsPlugin plugin;

    public ChatListener(AkaniEssentialsPlugin plugin) {
        this.messageService = plugin.messageService();
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onMessage(AsyncChatEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(true);
        // Serialize the message to plain text
        var message = plainTextComponentSerializer.serialize(event.message());
        // prevent minimessage-message usage
        message = message.replaceAll("<", "‹");
        message = message.replaceAll(">", "›");

        if (event.getPlayer().hasPermission(COLORED_PERMISSION)) {
            message = TextUtil.translateColor(message);
        }
        messageService.publishPublicChatMessage(event.getPlayer(), message);
    }

}

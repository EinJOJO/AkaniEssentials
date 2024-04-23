package it.einjojo.akani.essentials.listener;

import com.google.common.io.ByteStreams;
import io.papermc.paper.event.player.AsyncChatEvent;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;
import java.util.regex.Pattern;

public class ChatListener implements Listener, MessageProcessor {
    private static final String TYPE = "c";
    private final static String COLORED_PERMISSION = AkaniEssentialsPlugin.PERMISSION_BASE + "chatcolor";
    private final static Pattern COLOR_PATTERN = Pattern.compile("&[0-9a-fk-or]");
    private final BrokerService brokerService;
    private final AkaniEssentialsPlugin plugin;

    public ChatListener(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        brokerService = plugin.core().brokerService();
        brokerService.registerMessageProcessor(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onMessage(AsyncChatEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(true);
        // Serialize the message to plain text
        var message = PlainTextComponentSerializer.plainText().serialize(event.message());
        // prevent message injection
        message = message.replaceAll("<", "‹");
        message = message.replaceAll(">", "›");
        if (event.getPlayer().hasPermission(COLORED_PERMISSION)) {
            message = translateColor(message);
        }
        var payload = ByteStreams.newDataOutput();
        payload.writeUTF(event.getPlayer().getUniqueId().toString());
        payload.writeUTF(message);
        brokerService.publish(
                ChannelMessage.builder()
                        .channel(processingChannel())
                        .content(payload.toByteArray())
                        .messageTypeID(TYPE)
                        .recipients(ChannelReceiver.all())
                        .build()
        );
    }

    public String translateColor(String s) {
        if (!COLOR_PATTERN.matcher(s).matches()) return s;
        String t = s.replaceAll("&a", "<green>");
        t = t.replaceAll("&b", "<aqua>");
        t = t.replaceAll("&c", "<red>");
        t = t.replaceAll("&d", "<light_purple>");
        t = t.replaceAll("&e", "<yellow>");
        t = t.replaceAll("&f", "<white>");
        t = t.replaceAll("&0", "<black>");
        t = t.replaceAll("&1", "<dark_blue>");
        t = t.replaceAll("&2", "<dark_green>");
        t = t.replaceAll("&3", "<dark_aqua>");
        t = t.replaceAll("&4", "<dark_red>");
        t = t.replaceAll("&5", "<dark_purple>");
        t = t.replaceAll("&6", "<gold>");
        t = t.replaceAll("&7", "<gray>");
        t = t.replaceAll("&8", "<dark_gray>");
        t = t.replaceAll("&9", "<blue>");
        t = t.replaceAll("&l", "<bold>");
        t = t.replaceAll("&m", "<strikethrough>");
        t = t.replaceAll("&n", "<underlined>");
        t = t.replaceAll("&o", "<italic>");
        t = t.replaceAll("&r", "<reset>");
        return t;

    }

    @Override
    public String processingChannel() {
        return "chat";
    }

    @Override
    public void processMessage(ChannelMessage channelMessage) {
        plugin.getLogger().info("Processing chat message");
        var payload = ByteStreams.newDataInput(channelMessage.contentBytes());
        var uuid = UUID.fromString(payload.readUTF());
        var optionalPlayer = plugin.core().playerManager().onlinePlayer(uuid);
        if (optionalPlayer.isEmpty()) return;
        var plainMessage = payload.readUTF();
        Component message = plugin.miniMessage().deserialize("<gray>%s» <white>%s".formatted(optionalPlayer.get().name(), plainMessage));
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }
}

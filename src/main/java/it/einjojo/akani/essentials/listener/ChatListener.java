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

public class ChatListener implements Listener, MessageProcessor {
    private static final String TYPE = "c";
    private final BrokerService brokerService;
    private final AkaniEssentialsPlugin plugin;

    public ChatListener(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        brokerService = plugin.core().brokerService();
        brokerService.registerMessageProcessor(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMessage(AsyncChatEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(true);
        var message = PlainTextComponentSerializer.plainText().serialize(event.message());
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

    @Override
    public String processingChannel() {
        return "chat";
    }

    @Override
    public void processMessage(ChannelMessage channelMessage) {
        var payload = ByteStreams.newDataInput(channelMessage.contentBytes());
        var uuid = UUID.fromString(payload.readUTF());
        var optionalPlayer = plugin.core().playerManager().onlinePlayer(uuid);
        if (optionalPlayer.isEmpty()) return;
        var plainMessage = payload.readUTF();
        Component message = plugin.miniMessage().deserialize("<gray>%s : <white>%s".formatted(optionalPlayer.get().name(), plainMessage));
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }
}

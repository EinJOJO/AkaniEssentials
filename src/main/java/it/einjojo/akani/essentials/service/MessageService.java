package it.einjojo.akani.essentials.service;

import com.google.common.io.ByteStreams;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public record MessageService(BrokerService brokerService, AkaniEssentialsPlugin plugin) implements MessageProcessor {
    private static final String PUBLIC_MESSAGE_TYPE = "c";
    private static final Logger logger = LoggerFactory.getLogger("Essentials-ChatService");


    public MessageService {
        brokerService.registerMessageProcessor(this);
        logger.info("Chat service initialized");
    }

    public void publishPublicChatMessage(Player sender, String serializedMessage) {
        var payload = ByteStreams.newDataOutput();
        payload.writeUTF(sender.getUniqueId().toString());
        payload.writeUTF(serializedMessage);
        logger().info("[PUBLIC CHAT] {} wrote: {}", sender.getName(), serializedMessage);
        brokerService.publish(
                ChannelMessage.builder()
                        .channel(processingChannel())
                        .content(payload.toByteArray())
                        .messageTypeID(PUBLIC_MESSAGE_TYPE)
                        .recipients(ChannelReceiver.all())
                        .build()
        );
    }

    @Override
    public void processMessage(ChannelMessage channelMessage) {
        try {
            if (channelMessage.messageTypeID().equals(PUBLIC_MESSAGE_TYPE)) {
                var payload = ByteStreams.newDataInput(channelMessage.contentBytes());
                var uuid = payload.readUTF();
                var plainMessage = payload.readUTF();
                AkaniPlayer player = core().playerManager().onlinePlayer(UUID.fromString(uuid)).orElseThrow();
                Component message = plugin.miniMessage().deserialize(plugin.config().chatFormat()
                        .replaceAll("%player%", player.name())
                        .replaceAll("%message%", plainMessage)
                        .replaceAll("%prefix%", player.plainPrefix().join()
                        ));
                for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
                    bukkitPlayer.sendMessage(message);
                }
            }
        } catch (Exception ex) {
            logger().warn("Could not process chat message: {}", channelMessage, ex);
        }
    }

    public Logger logger() {
        return logger;
    }

    public AkaniCore core() {
        return plugin.core();
    }

    @Override
    public String processingChannel() {
        return "chat";
    }
}

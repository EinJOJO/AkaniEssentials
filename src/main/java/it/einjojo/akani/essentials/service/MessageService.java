package it.einjojo.akani.essentials.service;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.AkaniBukkitAdapter;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public record MessageService(BrokerService brokerService, AkaniEssentialsPlugin plugin,
                             JedisPool pool) implements MessageProcessor {
    public static final String SOCIALSPY_WILDCARD = "*";
    private static final String PUBLIC_MESSAGE_TYPE = "c";
    private static final String REDIS_PREFIX = "akani:essentials:chat:";
    private static final String REDIS_LAST_CONVERSATION = REDIS_PREFIX + "last_conversation";
    private static final String REDIS_SOCIALSPY_LIST = REDIS_PREFIX + "socialspyer";
    private static final String PRIVATE_MESSAGE_TYPE = "msg";
    private static final Logger logger = LoggerFactory.getLogger("Essentials-ChatService");


    public MessageService {
        brokerService.registerMessageProcessor(this);
        logger.info("Chat service initialized");
    }

    public static String sanitizeMessage(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '<':
                    sb.append("‹");
                    break;
                case '>':
                    sb.append("›");
                    break;
                default:
                    sb.append(c);

            }
        }
        return sb.toString();
    }

    /**
     * Get the last conversation partner of a player from redis
     *
     * @param player the player to get the last conversation partner of
     * @return the last conversation partner of the player, if any
     */
    public Optional<UUID> lastConversation(UUID player) {
        try (Jedis jedis = pool.getResource()) {
            var uuid = jedis.get(REDIS_LAST_CONVERSATION + player.toString());
            return Optional.ofNullable(uuid).map(UUID::fromString);
        }
    }

    public Set<String> socialSpyer() {
        try (Jedis jedis = pool.getResource()) {
            return jedis.keys(REDIS_SOCIALSPY_LIST + "*");
        }
    }

    public List<String> socialSpy(UUID socialSpyer) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.lrange(REDIS_SOCIALSPY_LIST + socialSpyer.toString(), 0, -1);
        }
    }

    public void addSocialSpy(UUID socialSpyer, String target) {
        try (Jedis jedis = pool.getResource()) {
            jedis.lpush(REDIS_SOCIALSPY_LIST + socialSpyer.toString(), target);
        }
    }

    public void removeSocialSpy(UUID socialSpyer, String target) {
        try (Jedis jedis = pool.getResource()) {
            jedis.lrem(REDIS_SOCIALSPY_LIST + socialSpyer.toString(), 0, target);
        }
    }

    public long clearSocialSpy(UUID socialSpyer) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.del(REDIS_SOCIALSPY_LIST + socialSpyer.toString());
        }
    }

    /**
     * Set the last conversation partner of a player in redis with a 10-minute expiration
     *
     * @param player the player to set the last conversation partner of
     * @param target the last conversation partner of the player
     */
    public void setLastConversation(UUID player, UUID target) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(REDIS_LAST_CONVERSATION + player.toString(), target.toString(), SetParams.setParams().ex(60 * 10));
        }
    }

    public void publishPublicChatMessage(@NotNull Player sender, @NotNull String serializedMessage) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(serializedMessage, "Message cannot be null");
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

    public void publishPrivateChatMessage(@NotNull Player sender, @NotNull AkaniPlayer receiver, @NotNull String serializedMessage) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(receiver, "Receiver cannot be null");
        Preconditions.checkNotNull(serializedMessage, "Message cannot be null");
        var payload = ByteStreams.newDataOutput();
        payload.writeUTF(sender.getUniqueId().toString());
        payload.writeUTF(receiver.uuid().toString());
        payload.writeUTF(serializedMessage);
        setLastConversation(receiver.uuid(), sender.getUniqueId());
        setLastConversation(sender.getUniqueId(), receiver.uuid());
        logger().info("[Private-Chat] {} msg to {}: {}", sender.getName(), receiver.name(), serializedMessage);
        brokerService.publish(
                ChannelMessage.builder()
                        .channel(processingChannel())
                        .content(payload.toByteArray())
                        .messageTypeID(PRIVATE_MESSAGE_TYPE)
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
            } else if (channelMessage.messageTypeID().equals(PRIVATE_MESSAGE_TYPE)) {
                var payload = ByteStreams.newDataInput(channelMessage.contentBytes());
                var senderUUID = UUID.fromString(payload.readUTF());
                var receiverUUID = UUID.fromString(payload.readUTF());
                var plainMessage = payload.readUTF();
                AkaniPlayer sender = core().playerManager().onlinePlayer((senderUUID)).orElseThrow();
                AkaniPlayer receiver = core().playerManager().onlinePlayer((receiverUUID)).orElseThrow();
                Component message = plugin.miniMessage().deserialize(plugin.config().privateChatFormat()
                        .replaceAll("%sender%", sender.name())
                        .replaceAll("%receiver%", receiver.name())
                        .replaceAll("%message%", plainMessage)
                );
                AkaniBukkitAdapter.bukkitPlayer(senderUUID).ifPresent((bp) -> {
                    bp.sendMessage(message);
                });
                AkaniBukkitAdapter.bukkitPlayer(receiverUUID).ifPresent((bp) -> {
                    bp.sendMessage(message);
                });
                for (String socialSpyerUUIDString : socialSpyer()) {
                    Player onlineBukkitSpyer = Bukkit.getPlayer(UUID.fromString(socialSpyerUUIDString));
                    if (onlineBukkitSpyer == null) {
                        continue;
                    }
                    List<String> beeingSyed = socialSpy(onlineBukkitSpyer.getUniqueId());
                    if (beeingSyed.contains(receiver.uuid().toString()) || beeingSyed.contains(SOCIALSPY_WILDCARD) || beeingSyed.contains(sender.uuid().toString()) || beeingSyed.contains(SOCIALSPY_WILDCARD)) {
                        onlineBukkitSpyer.sendMessage(message);
                    }
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

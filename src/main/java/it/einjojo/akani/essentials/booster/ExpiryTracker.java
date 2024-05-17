package it.einjojo.akani.essentials.booster;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.function.Consumer;

public class ExpiryTracker {

    private final Consumer<UUID> onExpire;
    private final PriorityQueue<Expiry> expiryQueue = new PriorityQueue<>();

    public ExpiryTracker(Consumer<UUID> onExpire) {
        this.onExpire = onExpire;
    }

    public void track(UUID playerUuid, Duration duration) {
        long expiryTime = System.currentTimeMillis() + duration.toMillis();
        Expiry expiry = new Expiry(playerUuid, expiryTime);
        expiryQueue.add(expiry);
    }

    public void remove(UUID playerUuid) {
        expiryQueue.removeIf(expiry -> expiry.playerUuid.equals(playerUuid));
    }

    public void runCheck() {
        long currentTime = System.currentTimeMillis();
        while (!expiryQueue.isEmpty() && expiryQueue.peek().expiryTime <= currentTime) {
            Expiry expiry = expiryQueue.poll();
            onExpire.accept(expiry.playerUuid);
        }
    }

    private record Expiry(UUID playerUuid, long expiryTime) implements Comparable<Expiry> {
        @Override
        public int compareTo(@NotNull ExpiryTracker.Expiry o) {
            return Long.compare(expiryTime, o.expiryTime);
        }
    }

}

package it.einjojo.akani.essentials.cooldown;

import java.time.Duration;
import java.util.UUID;

public record Cooldown(UUID player, long end) {
    public static Cooldown of(UUID player, Duration duration) {
        return new Cooldown(player, System.currentTimeMillis() + duration.toMillis());
    }

}

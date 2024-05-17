package it.einjojo.akani.essentials.util;

public interface EssentialKey {
    String PREFIX = "essentials.";
    String NO_PERMISSION = "no_permission";
    String GENERIC_ERROR = "generic_error";
    String SPECIFY_PLAYER = "specify_player";
    String NOT_ENOUGH_COINS = "essentials.coins.not_enough";
    String PLAYER_NOT_FOUND = "player_not_found";
    String PLAYER_NOT_ONLINE = "player_not_online";
    String HEAL_SELF = "heal_self";
    String HEAL_OTHER = "heal_other";
    String FEED_SELF = "feed_self";
    String FEED_OTHER = "feed_other";
    String FLY_DISABLED = "fly_disabled";
    String FLY_ENABLED = "fly_enabled";
    String FLY_DISABLED_OTHER = "fly_disabled_other";
    String FLY_ENABLED_OTHER = "fly_enabled_other";

    static String of(String key) {
        return PREFIX + key;
    }

}

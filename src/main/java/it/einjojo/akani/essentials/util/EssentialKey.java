package it.einjojo.akani.essentials.util;

import it.einjojo.akani.core.api.message.AkaniMessageKey;

public interface EssentialKey extends AkaniMessageKey {
    String PREFIX = "essentials.";


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

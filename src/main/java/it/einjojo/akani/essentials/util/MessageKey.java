package it.einjojo.akani.essentials.util;

public interface MessageKey {
    String PREFIX = "essentials.";
    String GENERIC_ERROR = "essentials.generic_error";
    String SPECIFY_PLAYER = "essentials.specify_player";

    static String of(String key) {
        return PREFIX + key;
    }

}

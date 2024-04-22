package it.einjojo.akani.essentials.util;

public interface MessageKey {
    String PREFIX = "essentials.";
    String GENERIC_ERROR = "generic_error";
    String SPECIFY_PLAYER = "specify_player";
    String PLAYER_NOT_FOUND = "player_not_found";
    String PLAYER_NOT_ONLINE = "player_not_online";


    static String of(String key) {
        return PREFIX + key;
    }

}

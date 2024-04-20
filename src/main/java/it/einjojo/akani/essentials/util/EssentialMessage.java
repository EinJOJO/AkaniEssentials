package it.einjojo.akani.essentials.util;

public enum EssentialMessage {
    TELEPORT_SUCCESS("teleport.success"),
    TELEPORT_CANCEL("teleport.cancel"),
    TELEPORT_REQUEST("teleport.request");
    private final String key;

    EssentialMessage(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}

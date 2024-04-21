package it.einjojo.akani.essentials.util;

@Deprecated
public enum Message {
    GENERIC_ERROR("generic.error"),
    TELEPORT_SUCCESS("teleport.success"),
    TELEPORT_NOT_SELF("teleport.not_self"),
    TELEPORT_CANCEL("teleport.cancel"),
    TELEPORT_REQUEST("teleport.request");
    private final String key;

    Message(String key) {
        this.key = "essentials." + key;
    }

    public String key() {
        return key;
    }
}

package it.einjojo.akani.essentials.util;

import it.einjojo.akani.core.api.message.MessageProvider;
import it.einjojo.akani.core.api.message.MessageStorage;

public class EssentialsMessageProvider implements MessageProvider {
    private MessageStorage storage;

    @Override
    public String providerName() {
        return "Essentials";
    }

    @Override
    public boolean shouldInsert(MessageStorage messageStorage) {
        return true;
    }

    @Override
    public void insertMessages(MessageStorage messageStorage) {
        storage = messageStorage;
        registerMessage(EssentialMessage.GENERIC_ERROR, "%prefix% <red>Ein Fehler ist aufgetreten");
        registerMessage(EssentialMessage.TELEPORT_NOT_SELF, "%prefix% <red>Du kannst dich nicht zu dir selbst teleportieren");
        registerMessage(EssentialMessage.TELEPORT_SUCCESS, "%prefix% <green>Teleport erfolgreich");
        registerMessage(EssentialMessage.TELEPORT_CANCEL, "%prefix% <red>Teleport abgebrochen");
    }

    void registerMessage(EssentialMessage key, String message) {
        storage.registerMessage("de", key.key(), message);
    }
}

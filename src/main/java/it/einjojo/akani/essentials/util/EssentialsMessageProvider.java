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
        registerMessage(EssentialMessage.TELEPORT_SUCCESS, "<green>Teleport erfolgreich");
        registerMessage(EssentialMessage.TELEPORT_CANCEL, "<red>Teleport abgebrochen");
    }

    void registerMessage(EssentialMessage key, String message) {
        storage.registerMessage("de", key.key(), message);
    }
}

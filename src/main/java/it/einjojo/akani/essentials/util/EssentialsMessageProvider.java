package it.einjojo.akani.essentials.util;

import it.einjojo.akani.core.api.message.MessageProvider;
import it.einjojo.akani.core.api.message.MessageStorage;

public record EssentialsMessageProvider() implements MessageProvider {
    @Override
    public String providerName() {
        return "Essentials";
    }

    @Override
    public boolean shouldInsert(MessageStorage messageStorage) {
        return true;
    }
    @Override
    public void insertMessages(MessageStorage s) {
        s.registerMessage("de", MessageKey.GENERIC_ERROR, "%prefix% <red>Ein Fehler ist aufgetreten!");
        s.registerMessage("de", MessageKey.SPECIFY_PLAYER, "%prefix% <red>Du musst einen Spieler angeben!");
        //teleport
        s.registerMessage("de", MessageKey.of("teleport.not_self"), "%prefix% <red>Du kannst dich nicht zu dir selbst teleportieren!");
        s.registerMessage("de", MessageKey.of("teleport.teleporting"), "%prefix% <yellow>Du wirst zu %player% teleportiert!");
        // gamemode
        s.registerMessage("de", MessageKey.of("gamemode.invalid"), "%prefix% <red>Ungültiger Spielmodus!");
        s.registerMessage("de", MessageKey.of("gamemode.success"), "%prefix% <yellow>Dein Spielmodus wurde geändert!");
        s.registerMessage("de", MessageKey.of("gamemode.success.other"), "%prefix% <yellow>%player% Spielmodus wurde geändert!");
        // warp
        s.registerMessage("de", MessageKey.of("warp.not_found"), "%prefix% <red>Der Warp wurde nicht gefunden!");
        s.registerMessage("de", MessageKey.of("warp.teleporting"), "%prefix% <yellow>Du wirst zum Warp %warp% teleportiert!");
        s.registerMessage("de", MessageKey.of("warp.success"), "%prefix% <yellow>Du wurdest zum Warp teleportiert!");
    }
}

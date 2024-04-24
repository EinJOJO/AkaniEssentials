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
        s.registerMessage("de", MessageKey.PLAYER_NOT_FOUND, "%prefix% <red>Der Spieler wurde nicht gefunden!");
        s.registerMessage("de", MessageKey.PLAYER_NOT_ONLINE, "%prefix% <red>Der Spieler ist nicht online!");

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
        // economy
        s.registerMessage("de", MessageKey.of("economy.error"), "%prefix% <red>Der Kontostand von %player% konnte nicht auf %balance% Coins gesetzt werden!");
        //coins
        s.registerMessage("de", MessageKey.of("coins.not_enough"), "%prefix% <red>Du hast nicht genügend Coins!");
        s.registerMessage("de", MessageKey.of("coins.balance.own"), "%prefix% <yellow>Du hast %balance% Coins!");
        s.registerMessage("de", MessageKey.of("coins.balance.other"), "%prefix% <yellow>%name% hat %balance% Coins!");
        s.registerMessage("de", MessageKey.of("coins.set"), "%prefix% <yellow>Der Kontostand von %player% wurde auf %balance% Coins gesetzt!");
        s.registerMessage("de", MessageKey.of("coins.add"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Coins hinzugefügt!");
        s.registerMessage("de", MessageKey.of("coins.remove"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Coins abgezogen!");
        // thaler
        s.registerMessage("de", MessageKey.of("thaler.not_enough"), "%prefix% <red>Du hast nicht genug Thaler!");
        s.registerMessage("de", MessageKey.of("thaler.balance.own"), "%prefix% <yellow>Dein Kontostand beträgt %balance% Thaler!");
        s.registerMessage("de", MessageKey.of("thaler.balance.other"), "%prefix% <yellow>Dein Kontostand beträgt %balance% Thaler!");
        s.registerMessage("de", MessageKey.of("thaler.set"), "%prefix% <yellow>Der Kontostand von %player% wurde auf %balance% Thaler gesetzt!");
        s.registerMessage("de", MessageKey.of("thaler.add"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Thaler hinzugefügt!");
        s.registerMessage("de", MessageKey.of("thaler.remove"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Thaler abgezogen!");


        //heal/feed
        s.registerMessage("de", MessageKey.HEAL_SELF, "%prefix% <yellow>Du wurdest geheilt!");
        s.registerMessage("de", MessageKey.HEAL_OTHER, "%prefix% <yellow>%player% wurde geheilt!");
        s.registerMessage("de", MessageKey.FEED_SELF, "%prefix% <yellow>Dein Hunger wurde gestillt!");
        s.registerMessage("de", MessageKey.FEED_OTHER, "%prefix% <yellow>%player% Hunger wurde gestillt!");

        //fly
        s.registerMessage("de", MessageKey.FLY_DISABLED, "%prefix% <yellow>Flugmodus deaktiviert!");
        s.registerMessage("de", MessageKey.FLY_ENABLED, "%prefix% <yellow>Flugmodus aktiviert!");
        s.registerMessage("de", MessageKey.FLY_DISABLED_OTHER, "%prefix% <yellow>Flugmodus von %player% deaktiviert!");
        s.registerMessage("de", MessageKey.FLY_ENABLED_OTHER, "%prefix% <yellow>Flugmodus von %player% aktiviert!");
    }
}

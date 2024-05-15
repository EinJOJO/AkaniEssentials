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
        s.registerMessage("de", EssentialKey.GENERIC_ERROR, "%prefix% <red>Ein Fehler ist aufgetreten!");
        s.registerMessage("de", EssentialKey.SPECIFY_PLAYER, "%prefix% <red>Du musst einen Spieler angeben!");
        s.registerMessage("de", EssentialKey.PLAYER_NOT_FOUND, "%prefix% <red>Der Spieler wurde nicht gefunden!");
        s.registerMessage("de", EssentialKey.PLAYER_NOT_ONLINE, "%prefix% <red>Der Spieler ist nicht online!");
        s.registerMessage("de", EssentialKey.NO_PERMISSION, "%prefix% <red>Du hast keine Berechtigung für diesen Befehl!");
        s.registerMessage("de", EssentialKey.NOT_ENOUGH_MONEY, "%prefix% <red>Du hast nicht genügend Coins!");

        //teleport
        s.registerMessage("de", EssentialKey.of("teleport.not_self"), "%prefix% <red>Du kannst dich nicht zu dir selbst teleportieren!");
        s.registerMessage("de", EssentialKey.of("teleport.teleporting"), "%prefix% <yellow>Du wirst zu %player% teleportiert!");
        // gamemode
        s.registerMessage("de", EssentialKey.of("gamemode.invalid"), "%prefix% <red>Ungültiger Spielmodus!");
        s.registerMessage("de", EssentialKey.of("gamemode.success"), "%prefix% <yellow>Dein Spielmodus wurde geändert!");
        s.registerMessage("de", EssentialKey.of("gamemode.success.other"), "%prefix% <yellow>%player% Spielmodus wurde geändert!");
        // warp
        s.registerMessage("de", EssentialKey.of("warp.not_found"), "%prefix% <red>Der Warp wurde nicht gefunden!");
        s.registerMessage("de", EssentialKey.of("warp.teleporting"), "%prefix% <yellow>Du wirst zum Warp %warp% teleportiert!");
        s.registerMessage("de", EssentialKey.of("warp.success"), "%prefix% <yellow>Du wurdest zum Warp teleportiert!");
        // economy
        s.registerMessage("de", EssentialKey.of("economy.error"), "%prefix% <red>Der Kontostand von %player% konnte nicht auf %balance% Coins gesetzt werden!");
        //coins
        s.registerMessage("de", EssentialKey.of("coins.not_enough"), "%prefix% <red>Du hast nicht genügend Coins!");
        s.registerMessage("de", EssentialKey.of("coins.balance.own"), "%prefix% <yellow>Du hast %balance% Coins!");
        s.registerMessage("de", EssentialKey.of("coins.balance.other"), "%prefix% <yellow>%player% hat %balance% Coins!");
        s.registerMessage("de", EssentialKey.of("coins.set"), "%prefix% <yellow>Der Kontostand von %player% wurde auf %balance% Coins gesetzt!");
        s.registerMessage("de", EssentialKey.of("coins.add"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Coins hinzugefügt!");
        s.registerMessage("de", EssentialKey.of("coins.remove"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Coins abgezogen!");
        // thaler
        s.registerMessage("de", EssentialKey.of("thaler.not_enough"), "%prefix% <red>Du hast nicht genug Thaler!");
        s.registerMessage("de", EssentialKey.of("thaler.balance.own"), "%prefix% <yellow>Dein Kontostand beträgt %balance% Thaler!");
        s.registerMessage("de", EssentialKey.of("thaler.balance.other"), "%prefix% <yellow>Dein Kontostand beträgt %balance% Thaler!");
        s.registerMessage("de", EssentialKey.of("thaler.set"), "%prefix% <yellow>Der Kontostand von %player% wurde auf %balance% Thaler gesetzt!");
        s.registerMessage("de", EssentialKey.of("thaler.add"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Thaler hinzugefügt!");
        s.registerMessage("de", EssentialKey.of("thaler.remove"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Thaler abgezogen!");

        //back
        s.registerMessage("de", EssentialKey.of("back.announce-costs"), "<newline>%prefix% <gray>Das teleportieren kostet <yellow>300 Coins.<newline>%prefix% <yellow>Klicke um zum <u><green><click:run_command:'/back confirm'>[Bestätigen]</click></green></u><newline>");
        s.registerMessage("de", EssentialKey.of("back.success"), "%prefix% <yellow>Du wurdest zurück teleportiert!");
        s.registerMessage("de", EssentialKey.of("back.no_location"), "%prefix% <red>Kein Rückteleportationsort gefunden!");

        //heal/feed
        s.registerMessage("de", EssentialKey.HEAL_SELF, "%prefix% <yellow>Du wurdest geheilt!");
        s.registerMessage("de", EssentialKey.HEAL_OTHER, "%prefix% <yellow>%player% wurde geheilt!");
        s.registerMessage("de", EssentialKey.FEED_SELF, "%prefix% <yellow>Dein Hunger wurde gestillt!");
        s.registerMessage("de", EssentialKey.FEED_OTHER, "%prefix% <yellow>%player% Hunger wurde gestillt!");

        //fly
        s.registerMessage("de", EssentialKey.FLY_DISABLED, "%prefix% <yellow>Flugmodus deaktiviert!");
        s.registerMessage("de", EssentialKey.FLY_ENABLED, "%prefix% <yellow>Flugmodus aktiviert!");
        s.registerMessage("de", EssentialKey.FLY_DISABLED_OTHER, "%prefix% <yellow>Flugmodus von %player% deaktiviert!");
        s.registerMessage("de", EssentialKey.FLY_ENABLED_OTHER, "%prefix% <yellow>Flugmodus von %player% aktiviert!");

        //give
        s.registerMessage("de", EssentialKey.of("give.success"), "%prefix% <yellow>Das Item %item% wurde %amount%x an %player% gegeben!");
        s.registerMessage("de", EssentialKey.of("give.item_not_found"), "%prefix% <red>Das OraxenItem mit der ID %item% wurde nicht gefunden!");

        //scoreboard
        s.registerMessage("de", EssentialKey.of("sb.default.title"), "<red><bold>ᴀᴋᴀɴɪ.ɴᴇᴛ <white>ɴᴇᴛᴢᴡᴇʀᴋ");
        s.registerMessage("de", EssentialKey.of("sb.default.server_key"), "<red>Server");

        // server
        s.registerMessage("de", EssentialKey.of("server.connect"), "%prefix% <yellow>%player% wird auf %server% verbunden!");
        s.registerMessage("de", EssentialKey.of("server.stop"), "%prefix% <yellow>Der Server %server% wird gestoppt!");
        s.registerMessage("de", EssentialKey.of("server.not_found"), "%prefix% <red>Der Server wurde nicht gefunden!");

        // msg
        s.registerMessage("de", EssentialKey.of("msg.no_message"), "%prefix% <yellow>Bitte gib eine nachricht an!");
        s.registerMessage("de", EssentialKey.of("msg.no_conversation"), "%prefix% <yellow>Du kannst niemandem Antworten!");

        // socialspy
        s.registerMessage("de", EssentialKey.of("socialspy.enabled"), "%prefix% <yellow>Socialspy für %player% aktiviert!");
        s.registerMessage("de", EssentialKey.of("socialspy.disabled"), "%prefix% <yellow>Socialspy für %player% deaktiviert!");

        //cmdspy
        s.registerMessage("de", EssentialKey.of("cmdspy.all"), "%prefix% <yellow>Commandspy (ALLE SPIELER) aktiviert!");
        s.registerMessage("de", EssentialKey.of("cmdspy.target"), "%prefix% <yellow>Commandspy für %player% aktiviert!");
        s.registerMessage("de", EssentialKey.of("cmdspy.disabled"), "%prefix% <yellow>Commandspy deaktiviert!");

        // hat
        s.registerMessage("de", EssentialKey.of("hat.success"), "%prefix% <yellow>Hut aufgesetzt!");
        s.registerMessage("de", EssentialKey.of("hat.success.other"), "%prefix% <yellow>Hut %player% aufgesetzt!");

        // sign
        s.registerMessage("de", EssentialKey.of("item-signature.no-item-in-hand"), "%prefix% <red>Es wurde kein Item in der Hand gefunden!");
        s.registerMessage("de", EssentialKey.of("item-signature.success"), "%prefix <yellow>Item signiert!");
        s.registerMessage("de", EssentialKey.of("item-signature.removed-success"), "%prefix <yellow>Item entsigniert!");


        // rename
        s.registerMessage("de", EssentialKey.of("rename.success"), "%prefix% <yellow>Item umbenannt!");
        s.registerMessage("de", EssentialKey.of("rename.remove-success"), "%prefix% <yellow>Item zurück umbenannt!");
        s.registerMessage("de", EssentialKey.of("rename.no-item-in-hand"), "%prefix% <red>Es wurde kein Item in der Hand gefunden!");

    }
}

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
        // economy
        s.registerMessage("de", EssentialKey.of("economy.error"), "%prefix% <red>Der Kontostand von %player% konnte nicht auf %balance% Coins gesetzt werden!");
        //pay
        s.registerMessage("de", EssentialKey.of("pay.self"), "%prefix% <red>Du hast das Geld bereits!");
        s.registerMessage("de", EssentialKey.of("pay.success"), "%prefix% <yellow>%player%<gray> hat <yellow>%amount% Coins<gray> erhalten!");
        s.registerMessage("de", EssentialKey.of("pay.success.other"), "%prefix% <gray>Du hast <yellow>%amount% Coins</yellow> von <yellow>%player%</yellow> erhalten!");

        //coins
        s.registerMessage("de", EssentialKey.of("coins.not_enough"), "%prefix% <red>Du hast nicht genügend Coins!");
        s.registerMessage("de", EssentialKey.of("coins.balance.own"), "%prefix% <gray>Du hast <yellow>%balance% Coins</yellow>!");
        s.registerMessage("de", EssentialKey.of("coins.balance.other"), "%prefix% <gray><yellow>%player%</yellow> hat <yellow>%balance% Coins!</yellow>");
        s.registerMessage("de", EssentialKey.of("coins.set"), "%prefix% <yellow>Der Kontostand von %player% wurde auf %balance% Coins gesetzt!");
        s.registerMessage("de", EssentialKey.of("coins.add"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Coins hinzugefügt!");
        s.registerMessage("de", EssentialKey.of("coins.remove"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Coins abgezogen!");
        s.registerMessage("de", EssentialKey.of("coins.bad-value"), "%prefix% <red>Ungültiger Wert!");
        // thaler
        s.registerMessage("de", EssentialKey.of("thaler.not_enough"), "%prefix% <red>Du hast nicht genug Thaler!");
        s.registerMessage("de", EssentialKey.of("thaler.balance.own"), "%prefix% <yellow>Dein Kontostand beträgt %balance% Thaler!");
        s.registerMessage("de", EssentialKey.of("thaler.balance.other"), "%prefix% <yellow>Dein Kontostand beträgt %balance% Thaler!");
        s.registerMessage("de", EssentialKey.of("thaler.set"), "%prefix% <yellow>Der Kontostand von %player% wurde auf %balance% Thaler gesetzt!");
        s.registerMessage("de", EssentialKey.of("thaler.add"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Thaler hinzugefügt!");
        s.registerMessage("de", EssentialKey.of("thaler.remove"), "%prefix% <yellow>Dem Konto von %player% wurden %balance% Thaler abgezogen!");

        //back
        s.registerMessage("de", EssentialKey.of("back.death"), "%prefix% <gray>Nutze <yellow><u><click:run_command:'/back'>/back</click></u></yellow> um zurückzukehren!");
        s.registerMessage("de", EssentialKey.of("back.announce-costs"), "<newline>%prefix% <gray>Das teleportieren kostet <yellow>300 Coins.<newline>%prefix% <yellow>Klicke um zum <u><green><click:run_command:'/back confirm'>[Bestätigen]</click></green></u><newline>");
        s.registerMessage("de", EssentialKey.of("back.success"), "%prefix% <yellow>Du wurdest zurück teleportiert!");
        s.registerMessage("de", EssentialKey.of("back.no_location"), "%prefix% <red>Kein Rückteleportationsort gefunden!");

        //heal/feed
        s.registerMessage("de", EssentialKey.HEAL_SELF, "%prefix% <yellow>Du wurdest geheilt!"); //TODO: consistency!
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
        s.registerMessage("de", EssentialKey.of("cmdspy.format"), "<yellow>[CMDSPY] <gray>%player%: <red>%command%");
        s.registerMessage("de", EssentialKey.of("cmdspy.all"), "%prefix% <yellow>Commandspy (ALLE SPIELER) aktiviert!");
        s.registerMessage("de", EssentialKey.of("cmdspy.target"), "%prefix% <yellow>Commandspy für %player% aktiviert!");
        s.registerMessage("de", EssentialKey.of("cmdspy.off"), "%prefix% <yellow>Commandspy deaktiviert!");

        // hat
        s.registerMessage("de", EssentialKey.of("hat.success"), "%prefix% <yellow>Hut aufgesetzt!");
        s.registerMessage("de", EssentialKey.of("hat.success.other"), "%prefix% <yellow>Hut %player% aufgesetzt!");

        // sign
        s.registerMessage("de", EssentialKey.of("item-signature.no-item-in-hand"), "%prefix% <red>Es wurde kein Item in der Hand gefunden!");
        s.registerMessage("de", EssentialKey.of("item-signature.success"), "%prefix <yellow>Item signiert!");
        s.registerMessage("de", EssentialKey.of("item-signature.remove-success"), "%prefix <yellow>Item entsigniert!");


        // rename
        s.registerMessage("de", EssentialKey.of("rename.success"), "%prefix% <yellow>Item umbenannt!");
        s.registerMessage("de", EssentialKey.of("rename.remove-success"), "%prefix% <yellow>Item zurück umbenannt!");
        s.registerMessage("de", EssentialKey.of("rename.no-item-in-hand"), "%prefix% <red>Es wurde kein Item in der Hand gefunden!");

        // tpa
        s.registerMessage("de", EssentialKey.of("tpa.not-available"), "%prefix% <red>Der Befehl ist auf diesem Server nicht verfügbar!");
        s.registerMessage("de", EssentialKey.of("tpa.no-request"), "%prefix% <red>Du hast keine Anfragen!");
        s.registerMessage("de", EssentialKey.of("tpa.request-sent"), "%prefix% <yellow>Anfrage an %player% gesendet!");
        s.registerMessage("de", EssentialKey.of("tpa.request-received"), "%prefix% <yellow>%player% möchte sich zu dir teleportieren!");
        s.registerMessage("de", EssentialKey.of("tpa.request-accepted"), "%prefix% <yellow>Du hast die Anfrage von %player% angenommen!");
        s.registerMessage("de", EssentialKey.of("tpa.request-denied"), "%prefix% <yellow>Du hast die Anfrage von %player% abgelehnt!");
        // tpahere
        s.registerMessage("de", EssentialKey.of("tpahere.request-sent"), "%prefix% <yellow>Anfrage an %player% gesendet!");
        s.registerMessage("de", EssentialKey.of("tpahere.request-received"), "%prefix% <yellow>%player% möchte, dass du dich zu ihm teleportierst!");

        // speed
        s.registerMessage("de", EssentialKey.of("speed.invalid"), "%prefix% <red>Ungültige Geschwindigkeit!");
        s.registerMessage("de", EssentialKey.of("speed.success"), "%prefix% <yellow>Deine Geschwindigkeit wurde geändert!");
        s.registerMessage("de", EssentialKey.of("speed.fly-changed"), "%prefix% <yellow>Deine Fluggeschwindigkeit wurde geändert!");
        s.registerMessage("de", EssentialKey.of("speed.walk-changed"), "%prefix% <yellow>Deine Geschwindigkeit wurde geändert!");

        // time
        s.registerMessage("de", EssentialKey.of("time.set"), "%prefix% <yellow>Die Zeit wurde auf %time% gesetzt!");
        s.registerMessage("de", EssentialKey.of("time.invalid"), "%prefix% <red>Ungültige Zeit!");

        // home
        s.registerMessage("de", EssentialKey.of("home.exists"), "%prefix% <red>Das Zuhause %home% existiert bereits!");
        s.registerMessage("de", EssentialKey.of("home.name-too-long"), "%prefix% <red>Der Name des Zuhauses ist zu lang!");
        s.registerMessage("de", EssentialKey.of("home.teleport"), "%prefix% <yellow>Du wurdest zum Zuhause %home% teleportiert!");
        s.registerMessage("de", EssentialKey.of("home.not-found"), "%prefix% <red>Das Zuhause %home% wurde nicht gefunden!");
        s.registerMessage("de", EssentialKey.of("home.list.title"), "%prefix% <yellow>Deine Zuhause:");
        s.registerMessage("de", EssentialKey.of("home.list.entry"), "<gray>› <yellow>%home% <click:run_command:/home %home%><hover:show_text:'<gray>Teleportiere dich zu deinem Zuhause</gray>'><dark_gray>[<yellow>➤</yellow>]</dark_gray></hover></click>");
        s.registerMessage("de", EssentialKey.of("home.set"), "%prefix% <yellow>Das Zuhause %home% wurde gesetzt!");
        s.registerMessage("de", EssentialKey.of("home.remove"), "%prefix% <yellow>Das Zuhause %home% wurde entfernt!");
        s.registerMessage("de", EssentialKey.of("home.limit"), "%prefix% <red>Das Limit von %limit% wurde erreicht!");

        // plot adapter command
        s.registerMessage("de", EssentialKey.of("plots.connecting.title"), "<yellow>Verbinde...");
        s.registerMessage("de", EssentialKey.of("plots.connecting.subtitle"), "<gray>Bitte warte einen Moment...");
        s.registerMessage("de", EssentialKey.of("plots.connecting.failed"), "%prefix% <red>Verbindung zum Citybuild-Server fehlgeschlagen!");

        // emoji
        s.registerMessage("de", EssentialKey.of("emoji.reload"), "%prefix% <yellow>Emoji-Konfiguration neu geladen!");
        s.registerMessage("de", EssentialKey.of("emoji.save"), "%prefix% <yellow>Emoji-Konfiguration gespeichert!");
        s.registerMessage("de", EssentialKey.of("emoji.create"), "%prefix% <yellow>Emoji %emojiName% erstellt! <gray>(%rarity%, %permission%, %customModelData%, %aliases%, %emoji%)");
        s.registerMessage("de", EssentialKey.of("emoji.remove"), "%prefix% <yellow>Emoji %emoji% entfernt!");
        s.registerMessage("de", EssentialKey.of("emoji.notFound"), "%prefix% <red>Emoji nicht gefunden!");

    }
}

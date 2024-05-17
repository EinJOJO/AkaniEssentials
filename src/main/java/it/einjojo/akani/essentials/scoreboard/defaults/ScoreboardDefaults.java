package it.einjojo.akani.essentials.scoreboard.defaults;

import it.einjojo.akani.core.api.player.AkaniPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.time.Duration;
import java.util.List;

public class ScoreboardDefaults {
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    public static final Component BAR = deserialize("  <dark_gray>◆<st>                                  </st>◆  ");

    public static List<Component> playerSection(AkaniPlayer akaniPlayer) {
        return List.of(
                deserialize("   <#f8c1a1><b>ᴘʟᴀʏᴇʀ"),
                deserialize("    <dark_gray>▪ <gray>ʀᴀɴɢ: <white>" + akaniPlayer.plainPrefix().join()),
                deserialize("    <dark_gray>▪ <gray>ᴄᴏɪɴs: <white>" + akaniPlayer.coins().balance()),
                deserialize("    <dark_gray>▪ <gray>ꜱᴘɪᴇʟᴢᴇɪᴛ: <white>" + playtimeString(akaniPlayer))
        );
    }

    public static Component deserialize(String message) {
        return MINI_MESSAGE.deserialize(message);
    }

    public static String playtimeString(AkaniPlayer player) {
        long millis = player.playtime().playtimeMillis();
        if (millis == 0) {
            return "0m";
        }
        Duration duration = Duration.ofMillis(millis);
        long hours = duration.toHours();
        if (hours <= 2) {
            return duration.toMinutes() + "m";
        } else {
            return hours + "h";
        }
    }

}

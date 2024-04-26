package it.einjojo.akani.essentials.util;

import java.util.regex.Pattern;

public class TextUtil {
    public final static Pattern COLOR_PATTERN = Pattern.compile("&[0-9a-fk-or]");

    public static String translateColor(String s) {
        if (!COLOR_PATTERN.matcher(s).find()) return s;
        String t = s.replaceAll("&a", "<green>");
        t = t.replaceAll("&b", "<aqua>");
        t = t.replaceAll("&c", "<red>");
        t = t.replaceAll("&d", "<light_purple>");
        t = t.replaceAll("&e", "<yellow>");
        t = t.replaceAll("&f", "<white>");
        t = t.replaceAll("&0", "<black>");
        t = t.replaceAll("&1", "<dark_blue>");
        t = t.replaceAll("&2", "<dark_green>");
        t = t.replaceAll("&3", "<dark_aqua>");
        t = t.replaceAll("&4", "<dark_red>");
        t = t.replaceAll("&5", "<dark_purple>");
        t = t.replaceAll("&6", "<gold>");
        t = t.replaceAll("&7", "<gray>");
        t = t.replaceAll("&8", "<dark_gray>");
        t = t.replaceAll("&9", "<blue>");
        t = t.replaceAll("&l", "<bold>");
        t = t.replaceAll("&m", "<strikethrough>");
        t = t.replaceAll("&n", "<underlined>");
        t = t.replaceAll("&o", "<italic>");
        t = t.replaceAll("&r", "<reset>");
        return t;

    }
}

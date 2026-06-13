/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 */
package com.pedrojm96.playeroptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class HEXChaColor {
    private static Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]){6}>");

    public static String colorCodes(String string) {
        if (string == null) {
            return string;
        }
        if (string.isEmpty()) {
            return string;
        }
        if (string.length() <= 0) {
            return string;
        }
        if (string == "") {
            return string;
        }
        if (string == " ") {
            return string;
        }
        String string2 = HEXChaColor.color(string);
        return string2;
    }

    private static String color(String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        while (matcher.find()) {
            ChatColor chatColor = ChatColor.of((String)matcher.group().substring(1, matcher.group().length() - 1));
            String string2 = string.substring(0, matcher.start());
            String string3 = string.substring(matcher.end());
            string = String.valueOf(String.valueOf(string2)) + chatColor + string3;
            matcher = HEX_PATTERN.matcher(string);
        }
        return string;
    }
}


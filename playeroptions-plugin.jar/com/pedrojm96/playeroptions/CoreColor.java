/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions;

import com.pedrojm96.playeroptions.CoreVersion;
import com.pedrojm96.playeroptions.HEXChaColor;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoreColor {
    public static String colorCodes(String nonColoredText) {
        if (nonColoredText == null) {
            return nonColoredText;
        }
        if (nonColoredText.isEmpty()) {
            return nonColoredText;
        }
        if (nonColoredText.length() <= 0) {
            return nonColoredText;
        }
        if (nonColoredText == "") {
            return nonColoredText;
        }
        if (nonColoredText == " ") {
            return nonColoredText;
        }
        String coloredText = nonColoredText;
        if (CoreVersion.getVersion().esMayorIgual(CoreVersion.v1_16)) {
            coloredText = HEXChaColor.colorCodes(coloredText);
        }
        coloredText = ChatColor.translateAlternateColorCodes((char)'&', (String)coloredText);
        return coloredText;
    }

    public static void line(Player player, String color) {
        player.sendMessage(color.length() > 0 ? CoreColor.colorCodes(String.valueOf(color) + "---------------------------------------------------") : color);
    }

    public static void message(Player player, String paramString) {
        player.sendMessage(CoreColor.colorCodes(paramString));
    }

    public static void message(CommandSender player, String paramString) {
        player.sendMessage(CoreColor.colorCodes(paramString));
    }

    public static String coloriseTextComponentString(String string) {
        if (string == null || string.length() == 0) {
            return " ";
        }
        String localString = CoreColor.colorCodes(string.trim());
        String newString = "";
        String last = "\u00a77";
        if (localString.contains(" ")) {
            String[] frases = localString.split(" ");
            int i = 0;
            while (i < frases.length) {
                String frase = frases[i].trim();
                newString = frase.startsWith("\u00a7") ? String.valueOf(newString) + " " + frase : String.valueOf(newString) + " " + last + frase;
                int j = 0;
                while (j < frase.length()) {
                    int m;
                    char c = frase.charAt(j);
                    int n = m = frase.length() > 2 ? (int)frase.charAt(j + 2) : 32;
                    if (c == '\u00a7' && m == 167) {
                        last = "\u00a7" + frase.charAt(j + 1) + "\u00a7" + frase.charAt(j + 3);
                        j = 3;
                    } else if (c == '\u00a7') {
                        last = "\u00a7" + frase.charAt(j + 1);
                        j = 1;
                    }
                    ++j;
                }
                ++i;
            }
            return newString.trim();
        }
        return localString;
    }

    public static List<String> rColorList(List<String> paramList) {
        ArrayList<String> s = new ArrayList<String>();
        s.addAll(paramList);
        int i = 0;
        while (i < s.size()) {
            String p = CoreColor.colorCodes((String)s.get(i));
            s.set(i, p);
            ++i;
        }
        return s;
    }

    public static List<String> getAlternateColorList() {
        ArrayList<String> c = new ArrayList<String>();
        c.add("&0");
        c.add("&1");
        c.add("&2");
        c.add("&3");
        c.add("&4");
        c.add("&5");
        c.add("&6");
        c.add("&7");
        c.add("&8");
        c.add("&9");
        c.add("&a");
        c.add("&b");
        c.add("&c");
        c.add("&d");
        c.add("&e");
        c.add("&f");
        c.add("&k");
        c.add("&l");
        c.add("&m");
        c.add("&n");
        c.add("&o");
        c.add("&r");
        c.add("&A");
        c.add("&B");
        c.add("&C");
        c.add("&D");
        c.add("&E");
        c.add("&F");
        c.add("&K");
        c.add("&L");
        c.add("&M");
        c.add("&N");
        c.add("&O");
        c.add("&R");
        return c;
    }

    public static List<String> getNativeColorList() {
        ArrayList<String> c = new ArrayList<String>();
        c.add("\u00a70");
        c.add("\u00a71");
        c.add("\u00a72");
        c.add("\u00a73");
        c.add("\u00a74");
        c.add("\u00a75");
        c.add("\u00a76");
        c.add("\u00a77");
        c.add("\u00a78");
        c.add("\u00a79");
        c.add("\u00a7a");
        c.add("\u00a7b");
        c.add("\u00a7c");
        c.add("\u00a7d");
        c.add("\u00a7e");
        c.add("\u00a7f");
        c.add("\u00a7k");
        c.add("\u00a7l");
        c.add("\u00a7m");
        c.add("\u00a7n");
        c.add("\u00a7o");
        c.add("\u00a7r");
        c.add("\u00a7A");
        c.add("\u00a7B");
        c.add("\u00a7C");
        c.add("\u00a7D");
        c.add("\u00a7E");
        c.add("\u00a7F");
        c.add("\u00a7K");
        c.add("\u00a7L");
        c.add("\u00a7M");
        c.add("\u00a7N");
        c.add("\u00a7O");
        c.add("\u00a7R");
        return c;
    }

    public static String clearColor(String coloredText) {
        String nonColoredText = coloredText;
        for (String color : CoreColor.getAlternateColorList()) {
            nonColoredText = nonColoredText.replaceAll(color, "");
        }
        for (String color : CoreColor.getNativeColorList()) {
            nonColoredText = nonColoredText.replaceAll(color, "");
        }
        return nonColoredText.trim();
    }
}


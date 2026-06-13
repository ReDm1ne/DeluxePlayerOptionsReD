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
        String string2 = string;
        if (CoreVersion.getVersion().esMayorIgual(CoreVersion.v1_16)) {
            string2 = HEXChaColor.colorCodes(string2);
        }
        string2 = ChatColor.translateAlternateColorCodes((char)'&', (String)string2);
        return string2;
    }

    public static void line(Player player, String string) {
        player.sendMessage(string.length() > 0 ? CoreColor.colorCodes(String.valueOf(string) + "---------------------------------------------------") : string);
    }

    public static void message(Player player, String string) {
        player.sendMessage(CoreColor.colorCodes(string));
    }

    public static void message(CommandSender commandSender, String string) {
        commandSender.sendMessage(CoreColor.colorCodes(string));
    }

    public static String coloriseTextComponentString(String string) {
        if (string == null || string.length() == 0) {
            return " ";
        }
        String string2 = CoreColor.colorCodes(string.trim());
        String string3 = "";
        String string4 = "\u00a77";
        if (string2.contains(" ")) {
            String[] stringArray = string2.split(" ");
            int n = 0;
            while (n < stringArray.length) {
                String string5 = stringArray[n].trim();
                string3 = string5.startsWith("\u00a7") ? String.valueOf(string3) + " " + string5 : String.valueOf(string3) + " " + string4 + string5;
                int n2 = 0;
                while (n2 < string5.length()) {
                    int n3;
                    char c = string5.charAt(n2);
                    int n4 = n3 = string5.length() > 2 ? (int)string5.charAt(n2 + 2) : 32;
                    if (c == '\u00a7' && n3 == 167) {
                        string4 = "\u00a7" + string5.charAt(n2 + 1) + "\u00a7" + string5.charAt(n2 + 3);
                        n2 = 3;
                    } else if (c == '\u00a7') {
                        string4 = "\u00a7" + string5.charAt(n2 + 1);
                        n2 = 1;
                    }
                    ++n2;
                }
                ++n;
            }
            return string3.trim();
        }
        return string2;
    }

    public static List<String> rColorList(List<String> list) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(list);
        int n = 0;
        while (n < arrayList.size()) {
            String string = CoreColor.colorCodes((String)arrayList.get(n));
            arrayList.set(n, string);
            ++n;
        }
        return arrayList;
    }

    public static List<String> getAlternateColorList() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("&0");
        arrayList.add("&1");
        arrayList.add("&2");
        arrayList.add("&3");
        arrayList.add("&4");
        arrayList.add("&5");
        arrayList.add("&6");
        arrayList.add("&7");
        arrayList.add("&8");
        arrayList.add("&9");
        arrayList.add("&a");
        arrayList.add("&b");
        arrayList.add("&c");
        arrayList.add("&d");
        arrayList.add("&e");
        arrayList.add("&f");
        arrayList.add("&k");
        arrayList.add("&l");
        arrayList.add("&m");
        arrayList.add("&n");
        arrayList.add("&o");
        arrayList.add("&r");
        arrayList.add("&A");
        arrayList.add("&B");
        arrayList.add("&C");
        arrayList.add("&D");
        arrayList.add("&E");
        arrayList.add("&F");
        arrayList.add("&K");
        arrayList.add("&L");
        arrayList.add("&M");
        arrayList.add("&N");
        arrayList.add("&O");
        arrayList.add("&R");
        return arrayList;
    }

    public static List<String> getNativeColorList() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("\u00a70");
        arrayList.add("\u00a71");
        arrayList.add("\u00a72");
        arrayList.add("\u00a73");
        arrayList.add("\u00a74");
        arrayList.add("\u00a75");
        arrayList.add("\u00a76");
        arrayList.add("\u00a77");
        arrayList.add("\u00a78");
        arrayList.add("\u00a79");
        arrayList.add("\u00a7a");
        arrayList.add("\u00a7b");
        arrayList.add("\u00a7c");
        arrayList.add("\u00a7d");
        arrayList.add("\u00a7e");
        arrayList.add("\u00a7f");
        arrayList.add("\u00a7k");
        arrayList.add("\u00a7l");
        arrayList.add("\u00a7m");
        arrayList.add("\u00a7n");
        arrayList.add("\u00a7o");
        arrayList.add("\u00a7r");
        arrayList.add("\u00a7A");
        arrayList.add("\u00a7B");
        arrayList.add("\u00a7C");
        arrayList.add("\u00a7D");
        arrayList.add("\u00a7E");
        arrayList.add("\u00a7F");
        arrayList.add("\u00a7K");
        arrayList.add("\u00a7L");
        arrayList.add("\u00a7M");
        arrayList.add("\u00a7N");
        arrayList.add("\u00a7O");
        arrayList.add("\u00a7R");
        return arrayList;
    }

    public static String clearColor(String string) {
        String string2 = string;
        for (String string3 : CoreColor.getAlternateColorList()) {
            string2 = string2.replaceAll(string3, "");
        }
        for (String string3 : CoreColor.getNativeColorList()) {
            string2 = string2.replaceAll(string3, "");
        }
        return string2.trim();
    }
}


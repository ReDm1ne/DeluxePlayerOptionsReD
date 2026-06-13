/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.pedrojm96.superstats.SuperStatsAPI
 *  me.clip.placeholderapi.PlaceholderAPI
 *  net.milkbowl.vault.chat.Chat
 *  net.milkbowl.vault.permission.Permission
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions;

import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.superstats.SuperStatsAPI;
import java.util.ArrayList;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoreVariables {
    private static Permission permission = null;
    private static Chat chat = null;
    public static List<String> vu = new ArrayList<String>();
    private static boolean placeholderAPI = false;
    private static SuperStatsAPI superstatsAPI;

    public static void superstats(SuperStatsAPI sstats) {
        superstatsAPI = sstats;
    }

    public static void permission(Permission per) {
        permission = per;
    }

    public static void chat(Chat cha) {
        chat = cha;
    }

    public static void placeholderAPI(boolean api) {
        placeholderAPI = api;
    }

    public static String replace(String string, Player p) {
        String g;
        if (string == null) {
            return "";
        }
        String newString = string;
        if (newString.contains("<player>")) {
            newString = newString.replaceAll("<player>", p.getName());
        }
        if (newString.contains("<server>")) {
            newString = newString.replaceAll("<server>", p.getServer().getName());
        }
        if (newString.contains("<displayname>")) {
            newString = newString.replaceAll("<displayname>", p.getDisplayName());
        }
        if (newString.contains("<world>")) {
            newString = newString.replaceAll("<world>", p.getWorld().getName());
        }
        if (newString.contains("<online>")) {
            newString = newString.replaceAll("<online>", String.valueOf(Bukkit.getOnlinePlayers().size()));
        }
        if (newString.contains("<player-x>")) {
            newString = newString.replaceAll("<player-x>", String.valueOf(p.getLocation().getBlockX()));
        }
        if (newString.contains("<player-y>")) {
            newString = newString.replaceAll("<player-y>", String.valueOf(p.getLocation().getBlockY()));
        }
        if (newString.contains("<player-z>")) {
            newString = newString.replaceAll("<player-z>", String.valueOf(p.getLocation().getBlockZ()));
        }
        if (newString.contains("<rank>") && permission != null && !permission.getName().equalsIgnoreCase("SuperPerms") && p.isOnline() && ((g = permission.getPrimaryGroup(p)) != null || g != "")) {
            newString = newString.replaceAll("<rank>", g);
        }
        if (chat != null) {
            if (newString.contains("<prefix>")) {
                newString = newString.replaceAll("<prefix>", chat.getPlayerPrefix(p));
            }
            if (newString.contains("<suffix>")) {
                newString = newString.replaceAll("<suffix>", chat.getPlayerSuffix(p));
            }
        }
        newString = CoreVariables.replaceUcode(newString);
        if (placeholderAPI) {
            try {
                newString = PlaceholderAPI.setPlaceholders((Player)p, (String)newString);
            }
            catch (Exception var11) {
                var11.printStackTrace();
            }
        }
        if (superstatsAPI != null) {
            newString = superstatsAPI.replaceStats(newString, p);
            newString = superstatsAPI.replaceTopStats(newString);
        }
        newString = CoreColor.colorCodes(newString);
        return newString;
    }

    public static String replace(String string, OfflinePlayer p) {
        if (string == null) {
            return "";
        }
        String newString = string;
        if (newString.contains("<player>")) {
            newString = newString.replaceAll("<player>", p.getName());
        }
        newString = CoreVariables.replaceUcode(newString);
        if (placeholderAPI) {
            try {
                newString = PlaceholderAPI.setPlaceholders((OfflinePlayer)p, (String)newString);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (superstatsAPI != null) {
            newString = superstatsAPI.replaceStats(newString, p.getUniqueId().toString());
            newString = superstatsAPI.replaceTopStats(newString);
        }
        newString = CoreColor.colorCodes(newString);
        return newString;
    }

    public static List<String> replaceList(List<String> list, Player p) {
        ArrayList<String> localList = new ArrayList<String>();
        localList.addAll(list);
        int i = 0;
        while (i < localList.size()) {
            localList.set(i, CoreVariables.replace((String)localList.get(i), p));
            ++i;
        }
        return localList;
    }

    public static List<String> replaceList(List<String> list, OfflinePlayer p) {
        ArrayList<String> localList = new ArrayList<String>();
        localList.addAll(list);
        int i = 0;
        while (i < localList.size()) {
            localList.set(i, CoreVariables.replace((String)localList.get(i), p));
            ++i;
        }
        return localList;
    }

    public static String replaceConsole(String string, CommandSender p) {
        String newString = string;
        if (newString.contains("<player>")) {
            newString = newString.replaceAll("<player>", p.getName());
        }
        newString = CoreVariables.replaceUcode(newString);
        return newString;
    }

    public static String replaceUcode(String string) {
        String newString = string;
        while (newString.contains("<ucode")) {
            String code = newString.split("<ucode")[1].split(">")[0];
            newString = newString.replaceAll("<ucode" + code + ">", String.valueOf((char)Integer.parseInt(code, 16)));
        }
        if (newString.contains("<a>")) {
            newString = newString.replaceAll("<a>", "\u00e1");
        }
        if (newString.contains("<e>")) {
            newString = newString.replaceAll("<e>", "\u00e9");
        }
        if (newString.contains("<i>")) {
            newString = newString.replaceAll("<i>", "\u00ed");
        }
        if (newString.contains("<o>")) {
            newString = newString.replaceAll("<o>", "\u00f3");
        }
        if (newString.contains("<u>")) {
            newString = newString.replaceAll("<u>", "\u00fa");
        }
        int s = 1;
        for (String v : vu) {
            if (newString.contains("<" + s + ">")) {
                newString = newString.replaceAll("<" + s + ">", v);
            }
            ++s;
        }
        return newString;
    }

    public static void iniUcode() {
        vu.add("\u2716");
        vu.add("\u2665");
        vu.add("\u2605");
        vu.add("\u25cf");
        vu.add("\u2666");
        vu.add("\u263b");
        vu.add("\u25ba");
        vu.add("\u25c4");
        vu.add("\u25ac");
        vu.add("\u272a");
        vu.add("\u2714");
        vu.add("\u2749");
        vu.add("\u25a0");
        vu.add("\u2580");
        vu.add("\u2584");
        vu.add("\u2620");
        vu.add("\u262d");
        vu.add("\u2122");
        vu.add("\u25e2");
        vu.add("\u25e3");
        vu.add("\u2763");
        vu.add("\u2600");
        vu.add("\u2740");
        vu.add("\u262a");
        vu.add("\u2623");
        vu.add("\u2612");
        vu.add("\u260e");
        vu.add("\u2591");
        vu.add("\u2611");
        vu.add("\u00bb");
        vu.add("\u00ab");
        vu.add("\u00bf");
    }
}


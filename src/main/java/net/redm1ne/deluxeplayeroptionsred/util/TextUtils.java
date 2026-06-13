package net.redm1ne.deluxeplayeroptionsred.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for text replacement and placeholder handling.
 */
public final class TextUtils {

    private static boolean placeholderAPIEnabled = false;

    private TextUtils() {}

    /**
     * Initialize PlaceholderAPI integration.
     */
    public static void initPlaceholderAPI() {
        placeholderAPIEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    /**
     * Replace placeholders and variables in text for a player.
     *
     * @param text The text to process
     * @param player The player for context
     * @return The processed text
     */
    public static String replace(String text, Player player) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        String result = text;

        // Built-in placeholders
        result = result.replace("<player>", player.getName());
        result = result.replace("<displayname>", player.getDisplayName());
        result = result.replace("<world>", player.getWorld().getName());
        result = result.replace("<server>", Bukkit.getServer().getName());
        result = result.replace("<online>", String.valueOf(Bukkit.getOnlinePlayers().size()));
        result = result.replace("<player-x>", String.valueOf(player.getLocation().getBlockX()));
        result = result.replace("<player-y>", String.valueOf(player.getLocation().getBlockY()));
        result = result.replace("<player-z>", String.valueOf(player.getLocation().getBlockZ()));
        result = result.replace("<uuid>", player.getUniqueId().toString());

        // Replace unicode codes
        result = replaceUnicodeCodes(result);

        // PlaceholderAPI integration
        if (placeholderAPIEnabled) {
            try {
                result = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, result);
            } catch (Exception e) {
                // PlaceholderAPI not available or error
            }
        }

        return result;
    }

    /**
     * Replace placeholders for an offline player.
     *
     * @param text The text to process
     * @param player The offline player
     * @return The processed text
     */
    public static String replace(String text, OfflinePlayer player) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        String result = text;

        result = result.replace("<player>", player.getName() != null ? player.getName() : "Unknown");
        result = result.replace("<uuid>", player.getUniqueId().toString());

        result = replaceUnicodeCodes(result);

        if (placeholderAPIEnabled) {
            try {
                result = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, result);
            } catch (Exception e) {
                // PlaceholderAPI not available or error
            }
        }

        return result;
    }

    /**
     * Process a list of strings for a player.
     *
     * @param list The list to process
     * @param player The player for context
     * @return A new list with all strings processed
     */
    public static List<String> replaceList(List<String> list, Player player) {
        if (list == null) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>(list.size());
        for (String line : list) {
            result.add(replace(line, player));
        }
        return result;
    }

    /**
     * Replace unicode codes in the format <ucodeXXXX>.
     *
     * @param text The text containing unicode codes
     * @return The text with unicode characters
     */
    private static String replaceUnicodeCodes(String text) {
        String result = text;

        while (result.contains("<ucode")) {
            int start = result.indexOf("<ucode");
            int end = result.indexOf(">", start);
            if (end == -1) break;

            String code = result.substring(start + 6, end);
            try {
                char unicodeChar = (char) Integer.parseInt(code, 16);
                result = result.substring(0, start) + unicodeChar + result.substring(end + 1);
            } catch (NumberFormatException e) {
                break;
            }
        }

        // Common special characters
        result = result.replace("<a>", "\u00e1"); // á
        result = result.replace("<e>", "\u00e9"); // é
        result = result.replace("<i>", "\u00ed"); // í
        result = result.replace("<o>", "\u00f3"); // ó
        result = result.replace("<u>", "\u00fa"); // ú

        return result;
    }

    /**
     * Check if PlaceholderAPI is available.
     *
     * @return true if PlaceholderAPI is enabled
     */
    public static boolean isPlaceholderAPIEnabled() {
        return placeholderAPIEnabled;
    }
}

package net.redm1ne.deluxeplayeroptionsred.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import net.redm1ne.deluxeplayeroptionsred.data.DatabaseManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * PlaceholderAPI expansion for DeluxePlayerOptionsReD.
 * Provides placeholders for player option states.
 */
public class DPOExpansion extends PlaceholderExpansion {

    private final DeluxePlayerOptions plugin;

    public DPOExpansion(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "dpo";
    }

    @Override
    @NotNull
    public String getAuthor() {
        return "ReDM1ne";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "3.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        // Handle placeholders
        String lowercaseParams = params.toLowerCase();

        // Simple option status placeholders
        switch (lowercaseParams) {
            case "speed":
                return getOptionStatus(player, "speed");
            case "fly":
                return getOptionStatus(player, "fly");
            case "jump":
                return getOptionStatus(player, "jump");
            case "doublejump":
                return getOptionStatus(player, "doublejump");
            case "stacker":
                return getOptionStatus(player, "stacker");
            case "visibility":
                return getOptionStatus(player, "visibility");
            case "chat":
                return getOptionStatus(player, "chat");
            case "radio":
                return getOptionStatus(player, "radio");
            case "pvp":
                return getOptionStatus(player, "pvp");

            // Boolean placeholders
            case "speed_boolean":
                return String.valueOf(isOptionEnabled(player, "speed"));
            case "fly_boolean":
                return String.valueOf(isOptionEnabled(player, "fly"));
            case "jump_boolean":
                return String.valueOf(isOptionEnabled(player, "jump"));
            case "doublejump_boolean":
                return String.valueOf(isOptionEnabled(player, "doublejump"));
            case "stacker_boolean":
                return String.valueOf(isOptionEnabled(player, "stacker"));
            case "visibility_boolean":
                return String.valueOf(isOptionEnabled(player, "visibility"));
            case "chat_boolean":
                return String.valueOf(isOptionEnabled(player, "chat"));
            case "radio_boolean":
                return String.valueOf(isOptionEnabled(player, "radio"));
            case "pvp_boolean":
                return String.valueOf(isOptionEnabled(player, "pvp"));

            default:
                return null;
        }
    }

    private String getOptionStatus(OfflinePlayer player, String optionId) {
        boolean enabled = isOptionEnabled(player, optionId);
        return plugin.getMessageManager().get(enabled ? "enabled" : "disabled");
    }

    private boolean isOptionEnabled(OfflinePlayer player, String optionId) {
        if (player.isOnline() && player instanceof Player onlinePlayer) {
            return plugin.getOptionManager().isOptionEnabled(onlinePlayer, optionId);
        }

        // For offline players, we'd need to load from database
        // This is a simplified version
        DatabaseManager.PlayerData data = plugin.getOptionManager().getPlayerData((Player) player);
        if (data == null) {
            return false;
        }

        return switch (optionId.toLowerCase()) {
            case "speed" -> data.speedEnabled;
            case "fly" -> data.flyEnabled;
            case "jump" -> data.jumpEnabled;
            case "doublejump" -> data.doubleJumpEnabled;
            case "stacker" -> data.stackerEnabled;
            case "visibility" -> !data.visibilityEnabled; // inverted
            case "chat" -> !data.chatEnabled; // inverted
            case "radio" -> !data.radioEnabled; // inverted
            case "pvp" -> !data.pvpEnabled; // inverted
            default -> false;
        };
    }
}

package net.redm1ne.deluxeplayeroptionsred.config;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import net.redm1ne.deluxeplayeroptionsred.util.ColorUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages language-specific messages for the plugin.
 */
public class MessageManager {

    private final DeluxePlayerOptions plugin;
    private final Map<String, String> messages = new HashMap<>();
    private File messagesFile;
    private FileConfiguration messagesConfig;

    public MessageManager(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    public void load() {
        String language = plugin.getConfigManager().getLanguage();
        String fileName = "messages_" + language + ".yml";

        messagesFile = new File(plugin.getDataFolder(), fileName);

        // If the file doesn't exist, try to save default from resources
        if (!messagesFile.exists()) {
            // Try to save from resources
            InputStream resource = plugin.getResource(fileName);
            if (resource != null) {
                plugin.saveResource(fileName, false);
            } else {
                // Fallback to EN
                plugin.saveResource("messages_EN.yml", false);
                messagesFile = new File(plugin.getDataFolder(), "messages_EN.yml");
            }
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        // Load all messages
        loadMessages();
    }

    private void loadMessages() {
        messages.clear();

        // Permission messages
        messages.put("no-permission-speed", loadMessage("no-permission-speed", "&7You do not have permission to use speed mode."));
        messages.put("no-permission-jump", loadMessage("no-permission-jump", "&7You do not have permission to use the high jump."));
        messages.put("no-permission-doublejump", loadMessage("no-permission-doublejump", "&7You do not have permission to use the double jump."));
        messages.put("no-permission-fly", loadMessage("no-permission-fly", "&7You do not have permission to use flight mode."));
        messages.put("no-permission-pvp", loadMessage("no-permission-pvp", "&7You do not have permission to use PvP mode."));
        messages.put("no-permission-stacker", loadMessage("no-permission-stacker", "&7You do not have permission to use the stacker."));
        messages.put("no-permission-visibility", loadMessage("no-permission-visibility", "&7You do not have permission to hide players."));
        messages.put("no-permission-chat", loadMessage("no-permission-chat", "&7You do not have permission to hide the chat."));
        messages.put("no-permission-radio", loadMessage("no-permission-radio", "&7You do not have permission to use the radio."));
        messages.put("no-permission", loadMessage("no-permission", "&7You not have permission to use this command."));

        // Status messages
        messages.put("no-world", loadMessage("no-world", "&cNot available in this world."));
        messages.put("no-console", loadMessage("no-console", "&cNot available in the console."));
        messages.put("command-use", loadMessage("command-use", "&7Use &e/playeroptions help | ?"));
        messages.put("description-use", loadMessage("description-use", "&7Displays the help page."));
        messages.put("config-loaded", loadMessage("config-loaded", "&7config, messages, menu and items loaded"));

        messages.put("player-stacker-disabled", loadMessage("player-stacker-disabled", "&7The player has stacker disabled"));
        messages.put("player-pvp-disabled", loadMessage("player-pvp-disabled", "&7The player has PvP disabled"));
        messages.put("you-stacker-disabled", loadMessage("you-stacker-disabled", "&7You have stacker disabled"));
        messages.put("you-pvp-disabled", loadMessage("you-pvp-disabled", "&7You have PvP disabled"));
        messages.put("chat-disabled", loadMessage("chat-disabled", "&7Chat disabled, enable it at the preferences."));

        messages.put("disabled", loadMessage("disabled", "&cDisabled"));
        messages.put("enabled", loadMessage("enabled", "&aEnabled"));
        messages.put("command-error", loadMessage("command-error", "&7Invalid argument."));

        // Command toggle messages
        messages.put("command-speed-enable", loadMessage("command-speed-enable", "&7Speed mode Enabled"));
        messages.put("command-speed-disabled", loadMessage("command-speed-disabled", "&7Speed mode disabled"));
        messages.put("command-jump-enable", loadMessage("command-jump-enable", "&7Jump mode enabled"));
        messages.put("command-jump-disabled", loadMessage("command-jump-disabled", "&7Jump mode disabled"));
        messages.put("command-doublejump-enable", loadMessage("command-doublejump-enable", "&7Double jump mode activated"));
        messages.put("command-doublejump-disabled", loadMessage("command-doublejump-disabled", "&7Double jump mode disabled"));
        messages.put("command-fly-enable", loadMessage("command-fly-enable", "&7Flight mode enabled"));
        messages.put("command-fly-disabled", loadMessage("command-fly-disabled", "&7Flight mode disabled"));
        messages.put("command-pvp-enable", loadMessage("command-pvp-enable", "&7PVP enabled"));
        messages.put("command-pvp-disabled", loadMessage("command-pvp-disabled", "&7PVP disabled"));
        messages.put("command-stacker-enable", loadMessage("command-stacker-enable", "&7Stacker mode enabled"));
        messages.put("command-stacker-disabled", loadMessage("command-stacker-disabled", "&7Stacker mode disabled"));
        messages.put("command-visibility-enable", loadMessage("command-visibility-enable", "&7Visibility enabled"));
        messages.put("command-visibility-disabled", loadMessage("command-visibility-disabled", "&7Visibility disabled"));
        messages.put("command-chat-enable", loadMessage("command-chat-enable", "&7Chat enabled"));
        messages.put("command-chat-disabled", loadMessage("command-chat-disabled", "&7Chat disabled"));
        messages.put("command-radio-enable", loadMessage("command-radio-enable", "&7Radio enabled"));
        messages.put("command-radio-disabled", loadMessage("command-radio-disabled", "&7Radio disabled"));

        // Help messages
        messages.put("help-command-reload", loadMessage("help-command-reload", "&7[&5&l>&7] &5/playeroptions reload"));
        messages.put("help-description-reload", loadMessage("help-description-reload", "&7[&5&l-&7] &7Reload the configuration and messages file."));
        messages.put("help-command-options", loadMessage("help-command-options", "&7[&5&l>&7] &5/options"));
        messages.put("help-description-options", loadMessage("help-description-options", "&7[&5&l-&7] &7Open the player options."));
        messages.put("help-command-jump", loadMessage("help-command-jump", "&7[&5&l>&7] &5/jump"));
        messages.put("help-description-jump", loadMessage("help-description-jump", "&7[&5&l-&7] &7Change the players jumping height."));
        messages.put("help-command-speed", loadMessage("help-command-speed", "&7[&5&l>&7] &5/speed"));
        messages.put("help-description-speed", loadMessage("help-description-speed", "&7[&5&l-&7] &7Change the players movement speed."));
        messages.put("help-command-doublejump", loadMessage("help-command-doublejump", "&7[&5&l>&7] &5/doublejump"));
        messages.put("help-description-doublejump", loadMessage("help-description-doublejump", "&7[&5&l-&7] &7Change the players ability to Double Jump."));
        messages.put("help-command-fly", loadMessage("help-command-fly", "&7[&5&l>&7] &5/fly"));
        messages.put("help-description-fly", loadMessage("help-description-fly", "&7[&5&l-&7] &7Change the players ability to fly."));
        messages.put("help-command-stacker", loadMessage("help-command-stacker", "&7[&5&l>&7] &5/stacker"));
        messages.put("help-description-stacker", loadMessage("help-description-stacker", "&7[&5&l-&7] &7Allows to mount on the head of a player."));
        messages.put("help-command-visibility", loadMessage("help-command-visibility", "&7[&5&l>&7] &5/visibility"));
        messages.put("help-description-visibility", loadMessage("help-description-visibility", "&7[&5&l-&7] &7Changes the visibility to see players."));
        messages.put("help-command-chat", loadMessage("help-command-chat", "&7[&5&l>&7] &5/chat"));
        messages.put("help-description-chat", loadMessage("help-description-chat", "&7[&5&l-&7] &7Change the ability to see and use chat."));
        messages.put("help-command-radio", loadMessage("help-command-radio", "&7[&5&l>&7] &5/radio"));
        messages.put("help-description-radio", loadMessage("help-description-radio", "&7[&5&l-&7] &7Enables or disables the radio player."));
        messages.put("help-command-pvp", loadMessage("help-command-pvp", "&7[&5&l>&7] &5/pvp"));
        messages.put("help-description-pvp", loadMessage("help-description-pvp", "&7[&5&l-&7] &7Change the players ability to pvp."));
    }

    private String loadMessage(String key, String defaultValue) {
        String value = messagesConfig.getString(key, defaultValue);
        return ColorUtils.translate(value);
    }

    /**
     * Get a message by key.
     *
     * @param key The message key
     * @return The translated message
     */
    public String get(String key) {
        return messages.getOrDefault(key, "");
    }

    /**
     * Get a message by key with prefix.
     *
     * @param key The message key
     * @return The translated message with prefix
     */
    public String getWithPrefix(String key) {
        return plugin.getPrefix() + get(key);
    }

    /**
     * Check if a message exists.
     *
     * @param key The message key
     * @return true if the message exists
     */
    public boolean hasMessage(String key) {
        return messages.containsKey(key);
    }
}

package net.redm1ne.deluxeplayeroptionsred.config;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages plugin configuration loading and access.
 */
public class ConfigManager {

    private final DeluxePlayerOptions plugin;
    private FileConfiguration config;

    // Config values
    private boolean updateCheck;
    private String language;
    private List<String> enabledWorlds;
    private String prefix;
    private boolean disabledPvPMessages;
    private boolean doubleJumpParticlesEnabled;
    private String doubleJumpParticlesEffect;
    private boolean doubleJumpSoundEnabled;
    private String doubleJumpSound;
    private String doubleJumpSoundOld;

    // Database config
    private String databaseType;
    private String databaseHost;
    private int databasePort;
    private String databaseName;
    private String databaseUsername;
    private String databasePassword;

    public ConfigManager(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();

        updateCheck = config.getBoolean("update-check", true);
        language = config.getString("messages", "EN");
        enabledWorlds = config.getStringList("worlds");
        if (enabledWorlds.isEmpty()) {
            enabledWorlds = new ArrayList<>();
            enabledWorlds.add("world");
        }
        prefix = config.getString("prefix", "&8&lPlayerOptions &f>> &7");
        disabledPvPMessages = config.getBoolean("disabled-pvp-messages", false);

        // DoubleJump config
        if (config.contains("doublejump-particles")) {
            doubleJumpParticlesEnabled = config.getBoolean("doublejump-particles.enable", true);
            doubleJumpParticlesEffect = config.getString("doublejump-particles.effect", "MOBSPAWNER_FLAMES");
        } else {
            doubleJumpParticlesEnabled = true;
            doubleJumpParticlesEffect = "MOBSPAWNER_FLAMES";
        }

        if (config.contains("doublejump-sound")) {
            doubleJumpSoundEnabled = config.getBoolean("doublejump-sound.enable", true);
            doubleJumpSound = config.getString("doublejump-sound.sound", "ENTITY_GENERIC_EXPLODE");
            doubleJumpSoundOld = config.getString("doublejump-sound.sound-old", "EXPLODE");
        } else {
            doubleJumpSoundEnabled = true;
            doubleJumpSound = "ENTITY_GENERIC_EXPLODE";
            doubleJumpSoundOld = "EXPLODE";
        }

        // Database config
        if (config.contains("data-storage")) {
            databaseType = config.getString("data-storage.type", "SQLite");
            databaseHost = config.getString("data-storage.host", "localhost");
            databasePort = config.getInt("data-storage.port", 3306);
            databaseName = config.getString("data-storage.database", "minecraft");
            databaseUsername = config.getString("data-storage.username", "root");
            databasePassword = config.getString("data-storage.password", "1234");
        } else {
            databaseType = "SQLite";
            databaseHost = "localhost";
            databasePort = 3306;
            databaseName = "minecraft";
            databaseUsername = "root";
            databasePassword = "1234";
        }
    }

    public boolean isUpdateCheckEnabled() {
        return updateCheck;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getEnabledWorlds() {
        return enabledWorlds;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isPvPMessagesDisabled() {
        return disabledPvPMessages;
    }

    public boolean isDoubleJumpParticlesEnabled() {
        return doubleJumpParticlesEnabled;
    }

    public String getDoubleJumpParticlesEffect() {
        return doubleJumpParticlesEffect;
    }

    public boolean isDoubleJumpSoundEnabled() {
        return doubleJumpSoundEnabled;
    }

    public String getDoubleJumpSound() {
        return doubleJumpSound;
    }

    public String getDoubleJumpSoundOld() {
        return doubleJumpSoundOld;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Get nested configuration section as string list
     */
    public List<String> getStringList(String path, List<String> def) {
        if (config.contains(path)) {
            return config.getStringList(path);
        }
        return def;
    }

    /**
     * Get nested configuration value
     */
    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    /**
     * Get nested configuration value
     */
    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    /**
     * Get nested configuration value
     */
    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }
}

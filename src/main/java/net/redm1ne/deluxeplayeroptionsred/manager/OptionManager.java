package net.redm1ne.deluxeplayeroptionsred.manager;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import net.redm1ne.deluxeplayeroptionsred.data.DatabaseManager;
import net.redm1ne.deluxeplayeroptionsred.option.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages player option states and option implementations.
 */
public class OptionManager {

    private final DeluxePlayerOptions plugin;

    // Options map by ID
    private final Map<String, Option> options = new ConcurrentHashMap<>();

    // Player data cache
    private final Map<UUID, DatabaseManager.PlayerData> playerDataCache = new ConcurrentHashMap<>();

    // Radio option availability
    private boolean radioAvailable = false;

    public OptionManager(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        // Register all options
        registerOption(new OptionSpeed(plugin));
        registerOption(new OptionFly(plugin));
        registerOption(new OptionJump(plugin));
        registerOption(new OptionDoubleJump(plugin));
        registerOption(new OptionStacker(plugin));
        registerOption(new OptionVisibility(plugin));
        registerOption(new OptionChat(plugin));
        registerOption(new OptionRadio(plugin));
        registerOption(new OptionPvP(plugin));

        plugin.getLogger().info("Registered " + options.size() + " options");
    }

    private void registerOption(Option option) {
        options.put(option.getId(), option);
    }

    /**
     * Get an option by ID.
     */
    public Option getOption(String id) {
        return options.get(id);
    }

    /**
     * Get all registered options.
     */
    public Collection<Option> getAllOptions() {
        return options.values();
    }

    /**
     * Load player data from database.
     */
    public void loadPlayerData(Player player) {
        plugin.getDatabaseManager().loadPlayerData(player.getUniqueId())
                .thenAccept(data -> {
                    playerDataCache.put(player.getUniqueId(), data);

                    // Apply loaded options on main thread
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        applyLoadedOptions(player, data);
                    });
                });
    }

    /**
     * Apply loaded options to a player.
     */
    private void applyLoadedOptions(Player player, DatabaseManager.PlayerData data) {
        if (data.speedEnabled) {
            getOption("speed").enable(player);
        }
        if (data.flyEnabled && player.hasPermission("deluxeplayeroptions.fly")) {
            getOption("fly").enable(player);
        }
        if (data.jumpEnabled) {
            getOption("jump").enable(player);
        }
        if (data.doubleJumpEnabled) {
            getOption("doublejump").enable(player);
        }
        if (data.stackerEnabled) {
            getOption("stacker").enable(player);
        }
        // Visibility is inverted - true means visible, false means hidden
        if (!data.visibilityEnabled) {
            getOption("visibility").enable(player); // This hides players
        }
        // Chat is inverted - true means chat enabled, false means hidden
        if (!data.chatEnabled) {
            getOption("chat").enable(player); // This hides chat
        }
        if (!data.radioEnabled) {
            getOption("radio").enable(player); // This disables radio
        }
        if (!data.pvpEnabled) {
            getOption("pvp").enable(player); // This disables PvP
        }
    }

    /**
     * Save player data to database.
     */
    public void savePlayerData(Player player) {
        DatabaseManager.PlayerData data = playerDataCache.get(player.getUniqueId());
        if (data == null) {
            data = new DatabaseManager.PlayerData();
            data.uuid = player.getUniqueId();
        }

        data.playerName = player.getName();
        data.speedEnabled = isOptionEnabled(player, "speed");
        data.flyEnabled = isOptionEnabled(player, "fly");
        data.jumpEnabled = isOptionEnabled(player, "jump");
        data.doubleJumpEnabled = isOptionEnabled(player, "doublejump");
        data.stackerEnabled = isOptionEnabled(player, "stacker");
        data.visibilityEnabled = !isOptionEnabled(player, "visibility"); // inverted
        data.chatEnabled = !isOptionEnabled(player, "chat"); // inverted
        data.radioEnabled = !isOptionEnabled(player, "radio"); // inverted
        data.pvpEnabled = !isOptionEnabled(player, "pvp"); // inverted

        plugin.getDatabaseManager().savePlayerData(data);
    }

    /**
     * Save all online players' data.
     */
    public void saveAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayerData(player);
        }
    }

    /**
     * Unload player data from cache.
     */
    public void unloadPlayerData(Player player) {
        playerDataCache.remove(player.getUniqueId());
    }

    /**
     * Get player data from cache.
     */
    public DatabaseManager.PlayerData getPlayerData(Player player) {
        return playerDataCache.get(player.getUniqueId());
    }

    /**
     * Check if the player data is loaded.
     */
    public boolean isPlayerDataLoaded(Player player) {
        DatabaseManager.PlayerData data = playerDataCache.get(player.getUniqueId());
        return data != null && data.loaded;
    }

    /**
     * Check if an option is enabled for a player.
     */
    public boolean isOptionEnabled(Player player, String optionId) {
        DatabaseManager.PlayerData data = playerDataCache.get(player.getUniqueId());
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

    /**
     * Set an option state for a player.
     */
    public void setOptionEnabled(Player player, String optionId, boolean enabled) {
        DatabaseManager.PlayerData data = playerDataCache.get(player.getUniqueId());
        if (data == null) {
            return;
        }

        switch (optionId.toLowerCase()) {
            case "speed" -> data.speedEnabled = enabled;
            case "fly" -> data.flyEnabled = enabled;
            case "jump" -> data.jumpEnabled = enabled;
            case "doublejump" -> data.doubleJumpEnabled = enabled;
            case "stacker" -> data.stackerEnabled = enabled;
            case "visibility" -> data.visibilityEnabled = !enabled; // inverted
            case "chat" -> data.chatEnabled = !enabled; // inverted
            case "radio" -> data.radioEnabled = !enabled; // inverted
            case "pvp" -> data.pvpEnabled = !enabled; // inverted
        }
    }

    /**
     * Enable the radio option (called when radio integration is available).
     */
    public void enableRadioOption() {
        this.radioAvailable = true;
    }

    /**
     * Check if radio option is available.
     */
    public boolean isRadioAvailable() {
        return radioAvailable;
    }

    /**
     * Toggle an option for a player (with permission and world checks).
     *
     * @return true if toggled on, false if toggled off, null if not allowed
     */
    public Boolean toggleOption(Player player, String optionId) {
        Option option = getOption(optionId);
        if (option == null) {
            return null;
        }

        if (!option.hasPermission(player)) {
            option.sendNoPermissionMessage(player);
            return null;
        }

        if (!option.canUseInWorld(player)) {
            option.sendWorldNotAllowedMessage(player);
            return null;
        }

        boolean newState = option.toggle(player);

        if (newState) {
            option.sendEnabledMessage(player);
        } else {
            option.sendDisabledMessage(player);
        }

        return newState;
    }

    /**
     * Get players within range of a location (for visibility).
     */
    public List<Player> getNearbyPlayers(Player player, double radius) {
        List<Player> nearby = new ArrayList<>();
        for (Player other : player.getWorld().getPlayers()) {
            if (!other.equals(player) && other.getLocation().distanceSquared(player.getLocation()) <= radius * radius) {
                nearby.add(other);
            }
        }
        return nearby;
    }
}

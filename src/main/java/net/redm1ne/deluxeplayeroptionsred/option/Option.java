package net.redm1ne.deluxeplayeroptionsred.option;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import net.redm1ne.deluxeplayeroptionsred.manager.OptionManager;
import org.bukkit.entity.Player;

/**
 * Abstract base class for all toggle options.
 */
public abstract class Option {

    protected final DeluxePlayerOptions plugin;
    protected final OptionManager optionManager;

    public Option(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
        this.optionManager = plugin.getOptionManager();
    }

    /**
     * Get the unique identifier for this option.
     */
    public abstract String getId();

    /**
     * Get the display name for this option.
     */
    public abstract String getName();

    /**
     * Get the permission node required to use this option.
     */
    public abstract String getPermission();

    /**
     * Check if this option requires per-world functionality.
     */
    public boolean isWorldDependent() {
        return true;
    }

    /**
     * Enable this option for a player.
     *
     * @param player The player
     * @return true if successfully enabled
     */
    public abstract boolean enable(Player player);

    /**
     * Disable this option for a player.
     *
     * @param player The player
     * @return true if successfully disabled
     */
    public abstract boolean disable(Player player);

    /**
     * Check if this option is enabled for a player.
     *
     * @param player The player
     * @return true if enabled
     */
    public boolean isEnabled(Player player) {
        return optionManager.isOptionEnabled(player, getId());
    }

    /**
     * Toggle this option for a player.
     *
     * @param player The player
     * @return true if now enabled, false if now disabled
     */
    public boolean toggle(Player player) {
        if (isEnabled(player)) {
            disable(player);
            return false;
        } else {
            enable(player);
            return true;
        }
    }

    /**
     * Check if a player has permission to use this option.
     *
     * @param player The player
     * @return true if has permission
     */
    public boolean hasPermission(Player player) {
        return player.hasPermission(getPermission());
    }

    /**
     * Get the message key for enabling this option.
     */
    public String getEnableMessageKey() {
        return "command-" + getId() + "-enable";
    }

    /**
     * Get the message key for disabling this option.
     */
    public String getDisableMessageKey() {
        return "command-" + getId() + "-disabled";
    }

    /**
     * Get the message key for no permission.
     */
    public String getNoPermissionMessageKey() {
        return "no-permission-" + getId();
    }

    /**
     * Send the enabled message to a player.
     */
    public void sendEnabledMessage(Player player) {
        String message = plugin.getMessageManager().get(getEnableMessageKey());
        if (message != null && !message.isEmpty()) {
            player.sendMessage(plugin.getPrefix() + message);
        }
    }

    /**
     * Send the disabled message to a player.
     */
    public void sendDisabledMessage(Player player) {
        String message = plugin.getMessageManager().get(getDisableMessageKey());
        if (message != null && !message.isEmpty()) {
            player.sendMessage(plugin.getPrefix() + message);
        }
    }

    /**
     * Send the no permission message to a player.
     */
    public void sendNoPermissionMessage(Player player) {
        String message = plugin.getMessageManager().get(getNoPermissionMessageKey());
        if (message != null && !message.isEmpty()) {
            player.sendMessage(plugin.getPrefix() + message);
        }
    }

    /**
     * Check if the option can be used in the player's current world.
     *
     * @param player The player
     * @return true if usable in current world
     */
    public boolean canUseInWorld(Player player) {
        if (!isWorldDependent()) {
            return true;
        }
        return plugin.isWorldEnabled(player.getWorld().getName());
    }

    /**
     * Send the world-not-allowed message to a player.
     */
    public void sendWorldNotAllowedMessage(Player player) {
        player.sendMessage(plugin.getPrefix() + plugin.getMessageManager().get("no-world"));
    }
}

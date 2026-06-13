package net.redm1ne.deluxeplayeroptionsred.integration;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Integration for JukeBox plugin (alternative radio plugin).
 */
public class JukeBoxIntegration {

    private final DeluxePlayerOptions plugin;
    private boolean enabled = false;

    public JukeBoxIntegration(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        // Check if JukeBox plugin is present (handled by RadioIntegration too)
        if (Bukkit.getPluginManager().isPluginEnabled("JukeBox")) {
            enabled = true;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}

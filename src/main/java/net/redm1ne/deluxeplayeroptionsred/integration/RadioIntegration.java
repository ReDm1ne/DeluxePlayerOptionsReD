package net.redm1ne.deluxeplayeroptionsred.integration;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Abstract radio integration for JukeBox plugins.
 * Supports both icJukeBox and JukeBox plugins.
 */
public class RadioIntegration {

    private final DeluxePlayerOptions plugin;
    private boolean enabled = false;
    private RadioAdapter adapter;

    public RadioIntegration(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        // Try to find and initialize radio plugins
        if (Bukkit.getPluginManager().isPluginEnabled("icJukeBox")) {
            adapter = new IcJukeBoxAdapter();
            enabled = true;
            plugin.getLogger().info("Integrated with icJukeBox");
        } else if (Bukkit.getPluginManager().isPluginEnabled("JukeBox")) {
            adapter = new JukeBoxAdapter();
            enabled = true;
            plugin.getLogger().info("Integrated with JukeBox");
        } else {
            plugin.getLogger().info("No radio plugin found - radio option disabled");
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Toggle radio for a player.
     *
     * @param player The player
     * @return true if enabled after toggle, false if disabled
     */
    public boolean toggleRadio(Player player) {
        if (!enabled || adapter == null) return false;
        return adapter.toggleRadio(player);
    }

    /**
     * Enable radio for a player.
     */
    public void enableRadio(Player player) {
        if (!enabled || adapter == null) return;
        adapter.enableRadio(player);
    }

    /**
     * Disable radio for a player.
     */
    public void disableRadio(Player player) {
        if (!enabled || adapter == null) return;
        adapter.disableRadio(player);
    }

    /**
     * Check if radio is playing for a player.
     */
    public boolean isRadioEnabled(Player player) {
        if (!enabled || adapter == null) return false;
        return adapter.isRadioEnabled(player);
    }

    /**
     * Interface for radio plugin adapters.
     */
    private interface RadioAdapter {
        boolean toggleRadio(Player player);
        void enableRadio(Player player);
        void disableRadio(Player player);
        boolean isRadioEnabled(Player player);
    }

    /**
     * Adapter for icJukeBox plugin.
     */
    private static class IcJukeBoxAdapter implements RadioAdapter {
        @Override
        public boolean toggleRadio(Player player) {
            try {
                return net.darkium.forge.icJukeBox.api.JukeBoxAPI.toggleRadio(player);
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void enableRadio(Player player) {
            try {
                net.darkium.forge.icJukeBox.api.JukeBoxAPI.enableRadio(player);
            } catch (Exception ignored) {}
        }

        @Override
        public void disableRadio(Player player) {
            try {
                net.darkium.forge.icJukeBox.api.JukeBoxAPI.disableRadio(player);
            } catch (Exception ignored) {}
        }

        @Override
        public boolean isRadioEnabled(Player player) {
            try {
                return net.darkium.forge.icJukeBox.api.JukeBoxAPI.hasRadioEnabled(player);
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * Adapter for JukeBox plugin.
     */
    private static class JukeBoxAdapter implements RadioAdapter {
        @Override
        public boolean toggleRadio(Player player) {
            try {
                me.ele.plugin.jukebox.api.JukeboxAPI.toggleRadio(player);
                return me.ele.plugin.jukebox.api.JukeboxAPI.hasRadioEnabled(player);
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void enableRadio(Player player) {
            try {
                me.ele.plugin.jukebox.api.JukeboxAPI.enableRadio(player);
            } catch (Exception ignored) {}
        }

        @Override
        public void disableRadio(Player player) {
            try {
                me.ele.plugin.jukebox.api.JukeboxAPI.disableRadio(player);
            } catch (Exception ignored) {}
        }

        @Override
        public boolean isRadioEnabled(Player player) {
            try {
                return me.ele.plugin.jukebox.api.JukeboxAPI.hasRadioEnabled(player);
            } catch (Exception e) {
                return false;
            }
        }
    }
}

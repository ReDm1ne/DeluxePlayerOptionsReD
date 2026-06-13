package net.redm1ne.deluxeplayeroptionsred.integration;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

/**
 * Abstract radio integration for JukeBox plugins.
 * Supports both icJukeBox and JukeBox plugins using reflection.
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
     * Adapter for icJukeBox plugin using reflection.
     */
    private static class IcJukeBoxAdapter implements RadioAdapter {
        private Class<?> apiClass;
        private Method toggleMethod;
        private Method enableMethod;
        private Method disableMethod;
        private Method hasRadioMethod;

        public IcJukeBoxAdapter() {
            try {
                apiClass = Class.forName("net.darkium.forge.icJukeBox.api.JukeBoxAPI");
                toggleMethod = apiClass.getMethod("toggleRadio", Player.class);
                enableMethod = apiClass.getMethod("enableRadio", Player.class);
                disableMethod = apiClass.getMethod("disableRadio", Player.class);
                hasRadioMethod = apiClass.getMethod("hasRadioEnabled", Player.class);
            } catch (Exception e) {
                // API not available
            }
        }

        @Override
        public boolean toggleRadio(Player player) {
            if (toggleMethod == null) return false;
            try {
                Object result = toggleMethod.invoke(null, player);
                return result instanceof Boolean && (Boolean) result;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void enableRadio(Player player) {
            if (enableMethod == null) return;
            try {
                enableMethod.invoke(null, player);
            } catch (Exception ignored) {}
        }

        @Override
        public void disableRadio(Player player) {
            if (disableMethod == null) return;
            try {
                disableMethod.invoke(null, player);
            } catch (Exception ignored) {}
        }

        @Override
        public boolean isRadioEnabled(Player player) {
            if (hasRadioMethod == null) return false;
            try {
                Object result = hasRadioMethod.invoke(null, player);
                return result instanceof Boolean && (Boolean) result;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * Adapter for JukeBox plugin using reflection.
     */
    private static class JukeBoxAdapter implements RadioAdapter {
        private Class<?> apiClass;
        private Method toggleMethod;
        private Method enableMethod;
        private Method disableMethod;
        private Method hasRadioMethod;

        public JukeBoxAdapter() {
            try {
                apiClass = Class.forName("me.ele.plugin.jukebox.api.JukeboxAPI");
                toggleMethod = apiClass.getMethod("toggleRadio", Player.class);
                enableMethod = apiClass.getMethod("enableRadio", Player.class);
                disableMethod = apiClass.getMethod("disableRadio", Player.class);
                hasRadioMethod = apiClass.getMethod("hasRadioEnabled", Player.class);
            } catch (Exception e) {
                // API not available
            }
        }

        @Override
        public boolean toggleRadio(Player player) {
            if (toggleMethod == null) return false;
            try {
                toggleMethod.invoke(null, player);
                return isRadioEnabled(player);
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void enableRadio(Player player) {
            if (enableMethod == null) return;
            try {
                enableMethod.invoke(null, player);
            } catch (Exception ignored) {}
        }

        @Override
        public void disableRadio(Player player) {
            if (disableMethod == null) return;
            try {
                disableMethod.invoke(null, player);
            } catch (Exception ignored) {}
        }

        @Override
        public boolean isRadioEnabled(Player player) {
            if (hasRadioMethod == null) return false;
            try {
                Object result = hasRadioMethod.invoke(null, player);
                return result instanceof Boolean && (Boolean) result;
            } catch (Exception e) {
                return false;
            }
        }
    }
}

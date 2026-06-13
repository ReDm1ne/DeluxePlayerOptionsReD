package net.redm1ne.deluxeplayeroptionsred;

import net.redm1ne.deluxeplayeroptionsred.api.DPOExpansion;
import net.redm1ne.deluxeplayeroptionsred.command.CommandManager;
import net.redm1ne.deluxeplayeroptionsred.config.ConfigManager;
import net.redm1ne.deluxeplayeroptionsred.config.MessageManager;
import net.redm1ne.deluxeplayeroptionsred.data.DatabaseManager;
import net.redm1ne.deluxeplayeroptionsred.integration.JukeBoxIntegration;
import net.redm1ne.deluxeplayeroptionsred.integration.RadioIntegration;
import net.redm1ne.deluxeplayeroptionsred.listener.PlayerListener;
import net.redm1ne.deluxeplayeroptionsred.manager.MenuManager;
import net.redm1ne.deluxeplayeroptionsred.manager.OptionManager;
import net.redm1ne.deluxeplayeroptionsred.manager.ToggleItemManager;
import net.redm1ne.deluxeplayeroptionsred.util.ColorUtils;
import net.redm1ne.deluxeplayeroptionsred.util.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

/**
 * DeluxePlayerOptionsReD - Advanced player options for lobby servers.
 * Unified JAR version supporting Minecraft 1.16 - 1.21.11
 *
 * @author ReDM1ne (based on original by PedroJM96)
 * @version 3.0.0
 */
public final class DeluxePlayerOptions extends JavaPlugin {

    private static DeluxePlayerOptions instance;

    // Managers
    private ConfigManager configManager;
    private MessageManager messageManager;
    private DatabaseManager databaseManager;
    private OptionManager optionManager;
    private MenuManager menuManager;
    private ToggleItemManager toggleItemManager;
    private CommandManager commandManager;

    // Integrations
    private RadioIntegration radioIntegration;
    private JukeBoxIntegration jukeBoxIntegration;

    // Cached config values
    private List<String> enabledWorlds;
    private String prefix;

    @Override
    public void onEnable() {
        instance = this;

        // Print startup banner
        printBanner();

        // Check server version compatibility
        if (!checkVersionCompatibility()) {
            getLogger().severe("Incompatible server version! This plugin requires Minecraft 1.16 or higher.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize managers
        initializeManagers();

        // Register listeners
        registerListeners();

        // Register commands
        registerCommands();

        // Setup integrations
        setupIntegrations();

        // Register PlaceholderAPI expansion if available
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new DPOExpansion(this).register();
            getLogger().info("Hooked into PlaceholderAPI");
        }

        // Start metrics
        startMetrics();

        // Schedule update check
        scheduleUpdateCheck();

        getLogger().info("DeluxePlayerOptionsReD v" + getDescription().getVersion() + " enabled successfully!");
    }

    @Override
    public void onDisable() {
        // Save all player data
        if (optionManager != null) {
            optionManager.saveAllPlayers();
        }

        // Shutdown database
        if (databaseManager != null) {
            databaseManager.shutdown();
        }

        // Clear managers
        instance = null;

        getLogger().info("DeluxePlayerOptionsReD disabled.");
    }

    private void printBanner() {
        getLogger().info("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        getLogger().info("  ____       _                  _   _   _        _   ");
        getLogger().info(" |  _ \\ _ __(_)_ __ ___  _   _  | | | | | |_ __ | |_ ");
        getLogger().info(" | | | | '__| | '_ ` _ \\| | | | | | | | | | '_ \\| __|");
        getLogger().info(" | |_| | |  | | | | | | | |_| | | | |_| | | | | | |_ ");
        getLogger().info(" |____/|_|  |_|_| |_| |_|\\__, | |_|\\___/|_|_| |_|\\__|");
        getLogger().info("                         |___/                        ");
        getLogger().info("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("Server Version: " + Bukkit.getBukkitVersion());
        getLogger().info("Java Version: " + System.getProperty("java.version"));
        getLogger().info("Author: ReDM1ne (based on work by PedroJM96)");
    }

    private boolean checkVersionCompatibility() {
        return VersionUtils.isAtLeast(16);
    }

    private void initializeManagers() {
        // Config Manager
        configManager = new ConfigManager(this);
        configManager.loadConfigs();

        // Message Manager
        messageManager = new MessageManager(this);
        messageManager.load();

        // Cache frequently used config values
        enabledWorlds = configManager.getEnabledWorlds();
        prefix = ColorUtils.translate(configManager.getPrefix());

        // Database Manager
        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();

        // Option Manager
        optionManager = new OptionManager(this);
        optionManager.initialize();

        // Menu Manager
        menuManager = new MenuManager(this);
        menuManager.initialize();

        // Toggle Item Manager
        toggleItemManager = new ToggleItemManager(this);
        toggleItemManager.initialize();

        // Command Manager
        commandManager = new CommandManager(this);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void registerCommands() {
        commandManager.registerCommands();
    }

    private void setupIntegrations() {
        // Setup radio integration (JukeBox plugins)
        radioIntegration = new RadioIntegration(this);
        radioIntegration.initialize();

        // Setup JukeBox integration
        jukeBoxIntegration = new JukeBoxIntegration(this);
        jukeBoxIntegration.initialize();

        if (radioIntegration.isEnabled() || jukeBoxIntegration.isEnabled()) {
            getLogger().info("Radio integration enabled");
            optionManager.enableRadioOption();
        }
    }

    private void startMetrics() {
        try {
            org.bstats.bukkit.Metrics metrics = new org.bstats.bukkit.Metrics(this, 444);
            getLogger().info("bStats metrics started");
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Failed to start metrics", e);
        }
    }

    private void scheduleUpdateCheck() {
        if (!configManager.isUpdateCheckEnabled()) {
            return;
        }

        getServer().getScheduler().runTaskLaterAsynchronously(this, () -> {
            checkForUpdates();
        }, 100L);
    }

    private void checkForUpdates() {
        // Simplified update check without external dependencies
        getLogger().info("Checking for updates...");
        // Implementation would go here
    }

    /**
     * Reload all configurations and managers
     */
    public void reload() {
        configManager.loadConfigs();
        messageManager.load();
        enabledWorlds = configManager.getEnabledWorlds();
        prefix = ColorUtils.translate(configManager.getPrefix());
        toggleItemManager.reload();
        getLogger().info("Configuration reloaded successfully");
    }

    // Getters

    public static DeluxePlayerOptions getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public OptionManager getOptionManager() {
        return optionManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public ToggleItemManager getToggleItemManager() {
        return toggleItemManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public RadioIntegration getRadioIntegration() {
        return radioIntegration;
    }

    public JukeBoxIntegration getJukeBoxIntegration() {
        return jukeBoxIntegration;
    }

    public List<String> getEnabledWorlds() {
        return enabledWorlds;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isWorldEnabled(String worldName) {
        return enabledWorlds.contains(worldName);
    }
}

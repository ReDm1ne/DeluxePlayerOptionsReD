package net.redm1ne.deluxeplayeroptionsred.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Manages database connections and player data persistence.
 * Supports both SQLite and MySQL using HikariCP connection pooling.
 */
public class DatabaseManager {

    private final DeluxePlayerOptions plugin;
    private HikariDataSource dataSource;
    private boolean initialized = false;

    public DatabaseManager(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        String dbType = plugin.getConfigManager().getDatabaseType().toLowerCase();

        try {
            if (dbType.equals("mysql")) {
                setupMySQL();
            } else {
                setupSQLite();
            }

            createTables();
            initialized = true;
            plugin.getLogger().info("Database initialized (" + dbType + ")");
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupMySQL() {
        HikariConfig config = new HikariConfig();

        String host = plugin.getConfigManager().getDatabaseHost();
        int port = plugin.getConfigManager().getDatabasePort();
        String database = plugin.getConfigManager().getDatabaseName();
        String username = plugin.getConfigManager().getDatabaseUsername();
        String password = plugin.getConfigManager().getDatabasePassword();

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true");
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setMaxLifetime(1800000);

        config.setPoolName("DeluxePlayerOptions-Pool");

        dataSource = new HikariDataSource(config);
    }

    private void setupSQLite() {
        HikariConfig config = new HikariConfig();

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File dbFile = new File(dataFolder, "player_data.db");
        String dbPath = dbFile.getAbsolutePath();

        config.setJdbcUrl("jdbc:sqlite:" + dbPath);
        config.setDriverClassName("org.sqlite.JDBC");

        config.setMaximumPoolSize(1);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(10000);

        config.setPoolName("DeluxePlayerOptions-SQLite");

        dataSource = new HikariDataSource(config);
    }

    private void createTables() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS player_options (
                uuid VARCHAR(36) PRIMARY KEY,
                player_name VARCHAR(16),
                speed_enabled INTEGER DEFAULT 0,
                fly_enabled INTEGER DEFAULT 0,
                jump_enabled INTEGER DEFAULT 0,
                doublejump_enabled INTEGER DEFAULT 0,
                stacker_enabled INTEGER DEFAULT 0,
                visibility_enabled INTEGER DEFAULT 1,
                chat_enabled INTEGER DEFAULT 1,
                radio_enabled INTEGER DEFAULT 1,
                pvp_enabled INTEGER DEFAULT 1,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        try (Connection conn = getConnection();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create tables: " + e.getMessage());
        }
    }

    /**
     * Get a database connection from the pool.
     */
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource not initialized");
        }
        return dataSource.getConnection();
    }

    /**
     * Load player data from database asynchronously.
     */
    public CompletableFuture<PlayerData> loadPlayerData(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM player_options WHERE uuid = ?";

            try (Connection conn = getConnection();
                 var stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, uuid.toString());

                try (var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        PlayerData data = new PlayerData();
                        data.uuid = uuid;
                        data.playerName = rs.getString("player_name");
                        data.speedEnabled = rs.getBoolean("speed_enabled");
                        data.flyEnabled = rs.getBoolean("fly_enabled");
                        data.jumpEnabled = rs.getBoolean("jump_enabled");
                        data.doubleJumpEnabled = rs.getBoolean("doublejump_enabled");
                        data.stackerEnabled = rs.getBoolean("stacker_enabled");
                        data.visibilityEnabled = rs.getBoolean("visibility_enabled");
                        data.chatEnabled = rs.getBoolean("chat_enabled");
                        data.radioEnabled = rs.getBoolean("radio_enabled");
                        data.pvpEnabled = rs.getBoolean("pvp_enabled");
                        data.loaded = true;
                        return data;
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().warning("Failed to load player data for " + uuid + ": " + e.getMessage());
            }

            // Return default data
            PlayerData data = new PlayerData();
            data.uuid = uuid;
            data.visibilityEnabled = true;
            data.chatEnabled = true;
            data.radioEnabled = true;
            data.pvpEnabled = true;
            data.loaded = true;
            return data;
        });
    }

    /**
     * Save player data to database asynchronously.
     */
    public CompletableFuture<Void> savePlayerData(PlayerData data) {
        return CompletableFuture.runAsync(() -> {
            String sql = """
                INSERT OR REPLACE INTO player_options
                (uuid, player_name, speed_enabled, fly_enabled, jump_enabled,
                 doublejump_enabled, stacker_enabled, visibility_enabled,
                 chat_enabled, radio_enabled, pvp_enabled)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

            try (Connection conn = getConnection();
                 var stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, data.uuid.toString());
                stmt.setString(2, data.playerName);
                stmt.setBoolean(3, data.speedEnabled);
                stmt.setBoolean(4, data.flyEnabled);
                stmt.setBoolean(5, data.jumpEnabled);
                stmt.setBoolean(6, data.doubleJumpEnabled);
                stmt.setBoolean(7, data.stackerEnabled);
                stmt.setBoolean(8, data.visibilityEnabled);
                stmt.setBoolean(9, data.chatEnabled);
                stmt.setBoolean(10, data.radioEnabled);
                stmt.setBoolean(11, data.pvpEnabled);
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().warning("Failed to save player data for " + data.uuid + ": " + e.getMessage());
            }
        });
    }

    /**
     * Check if database is initialized.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Shutdown the database connection pool.
     */
    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
        initialized = false;
    }

    /**
     * Holds player option data.
     */
    public static class PlayerData {
        public UUID uuid;
        public String playerName;
        public boolean speedEnabled;
        public boolean flyEnabled;
        public boolean jumpEnabled;
        public boolean doubleJumpEnabled;
        public boolean stackerEnabled;
        public boolean visibilityEnabled = true;
        public boolean chatEnabled = true;
        public boolean radioEnabled = true;
        public boolean pvpEnabled = true;
        public boolean loaded = false;
    }
}

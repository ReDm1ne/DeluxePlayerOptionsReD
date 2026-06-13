/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariConfig
 *  com.zaxxer.hikari.HikariDataSource
 */
package com.pedrojm96.playeroptions.data;

import com.pedrojm96.playeroptions.CoreLog;
import com.pedrojm96.playeroptions.CorePlugin;
import com.pedrojm96.playeroptions.CoreVersion;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class CoreMySQLConnection {
    private CoreLog log;
    private String host;
    private String database;
    private String username;
    private String password;
    private int port;
    private HikariDataSource dataSource;
    private boolean useSSL = false;

    public CoreMySQLConnection(CorePlugin plugin, String host, int port, String database, String username, String password, boolean useSSL) {
        this.log = plugin.getLog();
        this.log.info("Data set to MySQL");
        this.host = host;
        if (this.host == null || this.host.equals("")) {
            this.log.alert("DMYSQL() - host nulo");
        }
        this.port = port;
        this.database = database;
        if (this.database == null || this.database.equals("")) {
            this.log.alert("DMYSQL() - database nulo");
        }
        this.username = username;
        if (this.username == null || this.username.equals("")) {
            this.log.alert("DMYSQL() - username nulo");
        }
        this.password = password;
        if (this.password == null || this.password.equals("")) {
            this.log.alert("DMYSQL() - password nulo");
        }
        this.useSSL = useSSL;
        HikariConfig config = new HikariConfig();
        config.setPoolName(String.valueOf(plugin.getInstance().getName()) + "-MySQLPool");
        config.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useSSL=" + (this.useSSL ? "true" : "false"));
        if (CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_17_x)) {
            config.setDriverClassName("com.mysql.jdbc.Driver");
        } else {
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        }
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setMaxLifetime(180000L);
        config.setIdleTimeout(60000L);
        config.setMinimumIdle(2);
        config.setMaximumPoolSize(15);
        config.setConnectionTimeout(10000L);
        config.addDataSourceProperty("cachePrepStmts", (Object)"true");
        config.addDataSourceProperty("prepStmtCacheSize", (Object)"250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", (Object)"2048");
        config.addDataSourceProperty("useServerPrepStmts", (Object)"true");
        config.addDataSourceProperty("useLocalSessionState", (Object)"true");
        config.addDataSourceProperty("cacheResultSetMetadata", (Object)"true");
        config.addDataSourceProperty("cacheServerConfiguration", (Object)"true");
        config.addDataSourceProperty("elideSetAutoCommits", (Object)"true");
        config.addDataSourceProperty("maintainTimeStats", (Object)"false");
        config.addDataSourceProperty("characterEncoding", (Object)"utf8");
        config.addDataSourceProperty("encoding", (Object)"UTF-8");
        config.addDataSourceProperty("useUnicode", (Object)"true");
        config.setLeakDetectionThreshold(10000L);
        this.dataSource = new HikariDataSource(config);
    }

    public CoreLog getLog() {
        return this.log;
    }

    public void close() {
        this.dataSource.close();
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}


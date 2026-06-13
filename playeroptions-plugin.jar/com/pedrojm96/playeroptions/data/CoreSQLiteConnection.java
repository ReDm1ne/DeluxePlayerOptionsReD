/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.data;

import com.pedrojm96.playeroptions.CoreLog;
import com.pedrojm96.playeroptions.CorePlugin;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CoreSQLiteConnection {
    private CoreLog log;
    private CorePlugin plugin;
    private Connection connection;
    private File dataFolder;

    public CoreSQLiteConnection(CorePlugin cplugin) {
        this.log = cplugin.getLog();
        this.log.info("Data set to SQLite");
        this.plugin = cplugin;
        this.dataFolder = new File(this.plugin.getInstance().getDataFolder(), "sqlite.db");
        if (!this.dataFolder.exists()) {
            try {
                this.dataFolder.createNewFile();
            }
            catch (IOException e) {
                this.log.error("File write error: data.db", e);
            }
        }
        this.connection = this.getConnection();
    }

    public CorePlugin getPlugin() {
        return this.plugin;
    }

    public void close() {
        try {
            this.getConnection().close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                return this.connection;
            }
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.dataFolder);
            return this.connection;
        }
        catch (SQLException e) {
            this.log.error("SQLite exception on initialize.", e);
        }
        catch (ClassNotFoundException e) {
            this.log.error("You need the SQLite JBDC library. Google it. Put it in /lib folder.", e);
        }
        return null;
    }
}


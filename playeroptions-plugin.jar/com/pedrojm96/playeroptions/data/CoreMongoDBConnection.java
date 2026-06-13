/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mongodb.MongoClient
 *  com.mongodb.MongoClientURI
 *  com.mongodb.client.MongoDatabase
 */
package com.pedrojm96.playeroptions.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.pedrojm96.playeroptions.CoreLog;
import com.pedrojm96.playeroptions.CorePlugin;

public class CoreMongoDBConnection {
    private CoreLog log;
    private CorePlugin plugin;
    private String database;
    private MongoClient client;
    private MongoDatabase datastore;

    public CoreMongoDBConnection(CorePlugin plugin, String host, int port, String authenticationDatabase, String username, String password, String database) {
        this.log = plugin.getLog();
        this.log.info("Data set to MongoDB");
        this.plugin = plugin;
        if (host == null || host.equals("")) {
            this.log.alert("DMYSQL() - host nulo");
        }
        this.database = database;
        if (this.database == null || this.database.equals("")) {
            this.log.alert("DMYSQL() - database nulo");
        }
        if (username == null || username.equals("")) {
            this.log.alert("DMYSQL() - username nulo");
        }
        if (password == null || password.equals("")) {
            this.log.alert("DMYSQL() - password nulo");
        }
        System.setProperty("jdk.tls.trustNameService", "true");
        this.client = this.getClient(host, port, authenticationDatabase, username, password);
    }

    public CoreMongoDBConnection(CorePlugin plugin, String host, int port, String database) {
        this.log = plugin.getLog();
        this.log.info("Data set to MongoDB");
        this.plugin = plugin;
        if (host == null || host.equals("")) {
            this.log.alert("DMYSQL() - host nulo");
        }
        this.database = database;
        if (this.database == null || this.database.equals("")) {
            this.log.alert("DMYSQL() - database nulo");
        }
        System.setProperty("jdk.tls.trustNameService", "true");
        this.client = this.getClient(host, port);
    }

    public CoreMongoDBConnection(CorePlugin plugin, String connectionString, String database) {
        this.log = plugin.getLog();
        this.log.info("Data set to MongoDB");
        this.plugin = plugin;
        this.database = database;
        if (this.database == null || this.database.equals("")) {
            this.log.alert("DMYSQL() - database nulo");
        }
        System.setProperty("jdk.tls.trustNameService", "true");
        this.client = this.getClient(connectionString);
    }

    public CorePlugin getPlugin() {
        return this.plugin;
    }

    public CoreLog getLog() {
        return this.log;
    }

    public MongoDatabase getDataStore() {
        if (this.datastore != null) {
            return this.datastore;
        }
        this.datastore = this.client.getDatabase(this.database);
        return this.datastore;
    }

    private MongoClient getClient(String host, int port, String authenticationDatabase, String username, String password) {
        if (this.client != null) {
            return this.client;
        }
        String uri = "mongodb://";
        uri = String.valueOf(uri) + username + ":" + password + "@";
        uri = String.valueOf(uri) + host + ":";
        uri = String.valueOf(uri) + port + "/";
        uri = String.valueOf(uri) + authenticationDatabase;
        uri = String.valueOf(uri) + "?ssl=false&connectTimeoutMS=10000&socketTimeoutMS=10000";
        this.log.println(uri);
        MongoClientURI connectionString = new MongoClientURI(uri);
        MongoClient localClient = new MongoClient(connectionString);
        return localClient;
    }

    private MongoClient getClient(String host, int port) {
        if (this.client != null) {
            return this.client;
        }
        String uri = "mongodb://";
        uri = String.valueOf(uri) + host + ":";
        uri = String.valueOf(uri) + port + "/";
        uri = String.valueOf(uri) + "?ssl=false&connectTimeoutMS=500&socketTimeoutMS=500";
        this.log.println(uri);
        MongoClientURI connectionString = new MongoClientURI(uri);
        MongoClient localClient = new MongoClient(connectionString);
        return localClient;
    }

    private MongoClient getClient(String connectionString) {
        if (this.client != null) {
            return this.client;
        }
        MongoClient localClient = new MongoClient(new MongoClientURI(connectionString));
        return localClient;
    }
}


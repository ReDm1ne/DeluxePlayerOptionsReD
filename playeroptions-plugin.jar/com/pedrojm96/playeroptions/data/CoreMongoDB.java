/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mongodb.client.FindIterable
 *  com.mongodb.client.MongoCollection
 *  org.bson.Document
 *  org.bson.conversions.Bson
 */
package com.pedrojm96.playeroptions.data;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.pedrojm96.playeroptions.data.CoreMongoDBConnection;
import com.pedrojm96.playeroptions.data.CoreWHERE;
import java.util.HashMap;
import org.bson.Document;
import org.bson.conversions.Bson;

public class CoreMongoDB {
    private CoreMongoDBConnection connection;
    private String colletion;

    public CoreMongoDB(CoreMongoDBConnection connection, String colletion) {
        this.connection = connection;
        this.colletion = colletion;
    }

    public String get(CoreWHERE where, String data) {
        MongoCollection collection = this.connection.getDataStore().getCollection(this.colletion);
        Document searchQuery = new Document();
        searchQuery.put(where.get(0)[0], (Object)where.get(0)[1]);
        FindIterable cursor = collection.find((Bson)searchQuery);
        return (String)((Document)cursor.first()).get((Object)data);
    }

    public HashMap<String, String> get(CoreWHERE where, String ... datas) {
        MongoCollection collection = this.connection.getDataStore().getCollection(this.colletion);
        Document searchQuery = new Document();
        searchQuery.put(where.get(0)[0], (Object)where.get(0)[1]);
        FindIterable cursor = collection.find((Bson)searchQuery);
        HashMap<String, String> value = new HashMap<String, String>();
        int i = 0;
        while (i < datas.length) {
            value.put(datas[i], (String)((Document)cursor.first()).get((Object)datas[i]));
            ++i;
        }
        return value;
    }

    public void insert(Document data) {
        MongoCollection collection = this.connection.getDataStore().getCollection(this.colletion);
        collection.insertOne((Object)data);
    }

    public void update(CoreWHERE where, Document data) {
        MongoCollection collection = this.connection.getDataStore().getCollection(this.colletion);
        Document query = new Document();
        query.put(where.get(0)[0], (Object)where.get(0)[1]);
        Document updateObject = new Document();
        updateObject.put("$set", (Object)data);
        collection.updateOne((Bson)query, (Bson)updateObject);
    }

    public boolean checkData(CoreWHERE where) {
        MongoCollection collection = this.connection.getDataStore().getCollection(this.colletion);
        Document searchQuery = new Document();
        searchQuery.put(where.get(0)[0], (Object)where.get(0)[1]);
        FindIterable iterable = collection.find((Bson)searchQuery);
        return iterable.first() != null;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.data;

import com.pedrojm96.playeroptions.data.CoreField;
import com.pedrojm96.playeroptions.data.CoreWHERE;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface CoreSQL {
    public boolean checkStorage();

    public Connection getConnection() throws SQLException;

    public void build(String ... var1);

    public void insert(String ... var1);

    public void table(String var1);

    public void executeUpdate(String var1);

    public void update(CoreWHERE var1, String ... var2);

    public void update(String ... var1);

    public void delete(CoreWHERE var1);

    public boolean columnExists(String var1);

    public String getColumnType(String var1);

    public void changeColumnType(String var1, String var2);

    public void addColumn(String var1, String var2);

    public String get(CoreWHERE var1, String var2);

    public HashMap<String, String> get(CoreWHERE var1, String ... var2);

    public List<HashMap<String, String>> getAll(CoreWHERE var1, String var2, String ... var3);

    public List<HashMap<String, String>> getAll(String var1, String ... var2);

    public boolean checkData(CoreWHERE var1, String var2);

    public static CoreWHERE WHERE(String ... args) {
        return new CoreWHERE(args);
    }

    public void close();

    public static CoreField FIELD(String name, String type, Class<?> classtype) {
        return new CoreField(name, type, classtype);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.data;

import com.pedrojm96.playeroptions.CoreLog;
import com.pedrojm96.playeroptions.data.CoreSQL;
import com.pedrojm96.playeroptions.data.CoreSQLiteConnection;
import com.pedrojm96.playeroptions.data.CoreWHERE;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreSQLite
implements CoreSQL {
    private CoreLog log;
    private String tabla;
    private CoreSQLiteConnection coreconnection;
    public Map<String, String> columns = new HashMap<String, String>();

    public CoreSQLite(CoreSQLiteConnection connection, String tabla) {
        this.log = connection.getPlugin().getLog();
        this.tabla = tabla;
        this.coreconnection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.coreconnection.getConnection();
    }

    protected void cleanup(Statement statement, ResultSet result) {
        if (statement != null) {
            try {
                statement.close();
            }
            catch (SQLException e) {
                this.log.error("SQLException on cleanup [statement].");
            }
        }
        if (result != null) {
            try {
                result.close();
            }
            catch (SQLException e) {
                this.log.error("SQLException on cleanup [result].");
            }
        }
    }

    @Override
    public boolean checkStorage() {
        boolean existe;
        block6: {
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            try {
                try {
                    String tableExists = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + this.tabla + "';";
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    result = statement.executeQuery(tableExists);
                    String exS = result.getString(1);
                    this.log.info("Table " + exS + " exists.");
                    this.log.info("Loaded database");
                    existe = true;
                    this.cleanup(statement, result);
                    String get = "SELECT * FROM " + this.tabla + ";";
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    result = statement.executeQuery(get);
                    String listcolumns = "";
                    ResultSetMetaData metaData = result.getMetaData();
                    int rowCount = metaData.getColumnCount();
                    int i = 1;
                    while (i <= rowCount) {
                        this.columns.put(metaData.getColumnName(i).toLowerCase(), metaData.getColumnTypeName(i));
                        listcolumns = String.valueOf(listcolumns) + "[" + metaData.getColumnName(i).toLowerCase() + "," + metaData.getColumnTypeName(i) + "] ";
                        ++i;
                    }
                    this.log.info(listcolumns);
                }
                catch (SQLException e) {
                    existe = false;
                    this.log.alert("The table " + this.tabla + " does not exist");
                    this.cleanup(statement, result);
                    break block6;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
        return existe;
    }

    @Override
    public boolean columnExists(String columnname) {
        return this.columns.containsKey(columnname.toLowerCase());
    }

    @Override
    public String getColumnType(String columnname) {
        return this.columns.get(columnname.toLowerCase());
    }

    @Override
    public void addColumn(String columnname, String columntype) {
        block5: {
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    String query = "ALTER TABLE " + this.tabla + " ADD " + columnname + " " + columntype;
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                    this.log.info("Added column: " + columnname + " " + columntype);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    this.log.alert("The table " + this.tabla + " does not exist");
                    this.cleanup(statement, result);
                    break block5;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
    }

    @Override
    public void changeColumnType(String columnname, String columntype) {
        block5: {
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    String query = "ALTER TABLE " + this.tabla + " MODIFY " + columnname + " " + columntype;
                    statement = connection.createStatement();
                    result = statement.executeQuery(query);
                    this.log.info("Change column Type of " + columnname + " for: " + columntype);
                }
                catch (SQLException e) {
                    this.log.alert("The table " + this.tabla + " does not exist");
                    this.cleanup(statement, result);
                    break block5;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
    }

    @Override
    public void build(String ... paramsString) {
        block6: {
            Connection connection = null;
            Statement statement = null;
            this.log.info("Creating table " + this.tabla);
            String query = "";
            int i = 0;
            while (i < paramsString.length) {
                query = i == paramsString.length - 1 ? String.valueOf(query) + paramsString[i] + ");" : String.valueOf(query) + paramsString[i] + ", ";
                ++i;
            }
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + this.tabla + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + query);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    this.cleanup(statement, null);
                    break block6;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, null);
                throw throwable;
            }
            this.cleanup(statement, null);
        }
    }

    @Override
    public boolean checkData(CoreWHERE paranWhere, String paranString) {
        boolean existe;
        block5: {
            existe = false;
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            try {
                try {
                    String query = "SELECT '" + paranString + "' FROM " + this.tabla + " WHERE " + paranWhere.get() + ";";
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    result = statement.executeQuery(query);
                    existe = result.next();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    existe = true;
                    this.cleanup(statement, result);
                    break block5;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
        return existe;
    }

    @Override
    public void insert(String ... args) {
        block9: {
            String data = "";
            String value = "";
            int i = 0;
            while (i < args.length) {
                String[] local;
                if (args[i].trim().contains(":") && (local = args[i].trim().split(":")).length >= 2) {
                    String locaData = local[0].trim();
                    String locaValue = local[1].trim();
                    if (i == args.length - 1) {
                        data = String.valueOf(data) + locaData;
                        value = String.valueOf(value) + "'" + locaValue + "'";
                    } else {
                        data = String.valueOf(data) + locaData + ",";
                        value = String.valueOf(value) + "'" + locaValue + "',";
                    }
                }
                ++i;
            }
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            String query = "INSERT INTO " + this.tabla + " (" + data + ") VALUES (" + value + ");";
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                }
                catch (SQLException e) {
                    this.log.error("Could not create getter statement on insert: " + query);
                    e.printStackTrace();
                    this.cleanup(statement, result);
                    break block9;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
    }

    @Override
    public String get(CoreWHERE paranWhere, String paranString) {
        String value;
        block6: {
            value = "";
            String query = "SELECT " + paranString + " FROM " + this.tabla + " WHERE " + paranWhere.get() + ";";
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    result = statement.executeQuery(query);
                    if (result != null && result.next()) {
                        value = result.getString(paranString);
                    }
                }
                catch (SQLException e) {
                    this.log.error("Could not create getter statement on get: " + query);
                    e.printStackTrace();
                    this.cleanup(statement, result);
                    break block6;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
        return value;
    }

    @Override
    public HashMap<String, String> get(CoreWHERE paranWhere, String ... paranString) {
        HashMap<String, String> value;
        block8: {
            value = new HashMap<String, String>();
            String getData = "";
            int i = 0;
            while (i < paranString.length) {
                getData = i == paranString.length - 1 ? String.valueOf(getData) + paranString[i].trim() : String.valueOf(getData) + paranString[i].trim() + ",";
                ++i;
            }
            String query = "SELECT " + getData + " FROM " + this.tabla + " WHERE " + paranWhere.get() + ";";
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    result = statement.executeQuery(query);
                    if (result != null && result.next()) {
                        int i2 = 0;
                        while (i2 < paranString.length) {
                            value.put(paranString[i2], result.getString(paranString[i2]));
                            ++i2;
                        }
                    }
                }
                catch (SQLException e) {
                    this.log.fatalError("Could not create getter statement on get: " + query, e);
                    e.printStackTrace();
                    this.cleanup(statement, result);
                    break block8;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
        return value;
    }

    @Override
    public List<HashMap<String, String>> getAll(CoreWHERE paranWhere, String criterio, String ... paranString) {
        ArrayList<HashMap<String, String>> lista;
        block8: {
            String getData = "";
            int i = 0;
            while (i < paranString.length) {
                getData = i == paranString.length - 1 ? String.valueOf(getData) + paranString[i].trim() : String.valueOf(getData) + paranString[i].trim() + ",";
                ++i;
            }
            lista = new ArrayList<HashMap<String, String>>();
            String query = criterio != null && criterio.isEmpty() && criterio == " " ? "SELECT " + getData + " FROM " + this.tabla + " WHERE " + paranWhere.get() + " " + criterio + ";" : "SELECT " + getData + " FROM " + this.tabla + " WHERE " + paranWhere.get() + ";";
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    result = statement.executeQuery(query);
                    while (result != null && result.next()) {
                        HashMap<String, String> values = new HashMap<String, String>();
                        int i2 = 0;
                        while (i2 < paranString.length) {
                            values.put(paranString[i2], result.getString(paranString[i2]));
                            ++i2;
                        }
                        lista.add(values);
                    }
                }
                catch (SQLException e) {
                    this.log.fatalError("Could not create getter statement on get: " + query, e);
                    e.printStackTrace();
                    this.cleanup(statement, result);
                    break block8;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
        return lista;
    }

    @Override
    public List<HashMap<String, String>> getAll(String criterio, String ... paranString) {
        ArrayList<HashMap<String, String>> lista;
        block8: {
            String getData = "";
            int i = 0;
            while (i < paranString.length) {
                getData = i == paranString.length - 1 ? String.valueOf(getData) + paranString[i].trim() : String.valueOf(getData) + paranString[i].trim() + ",";
                ++i;
            }
            lista = new ArrayList<HashMap<String, String>>();
            String query = "SELECT " + getData + " FROM " + this.tabla + " " + criterio + ";";
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    result = statement.executeQuery(query);
                    while (result != null && result.next()) {
                        HashMap<String, String> values = new HashMap<String, String>();
                        int i2 = 0;
                        while (i2 < paranString.length) {
                            values.put(paranString[i2], result.getString(paranString[i2]));
                            ++i2;
                        }
                        lista.add(values);
                    }
                }
                catch (SQLException e) {
                    this.log.fatalError("Could not create getter statement on get: " + query, e);
                    e.printStackTrace();
                    this.cleanup(statement, result);
                    break block8;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
        return lista;
    }

    @Override
    public void update(CoreWHERE paranWhere, String ... args) {
        block7: {
            String data = "";
            int i = 0;
            while (i < args.length) {
                String[] local;
                if (args[i].trim().contains(":") && (local = args[i].trim().split(":")).length >= 2) {
                    String locaData = local[0].trim();
                    String locaValue = local[1].trim();
                    data = i == args.length - 1 ? String.valueOf(data) + locaData + "='" + locaValue + "'" : String.valueOf(data) + locaData + "='" + locaValue + "',";
                }
                ++i;
            }
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            String query = "UPDATE " + this.tabla + " SET " + data + " WHERE " + paranWhere.get() + ";";
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                }
                catch (SQLException e) {
                    this.log.fatalError("Could not create getter statement on update: " + query, e);
                    this.cleanup(statement, result);
                    break block7;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
    }

    @Override
    public void update(String ... args) {
        block7: {
            String data = "";
            int i = 0;
            while (i < args.length) {
                String[] local;
                if (args[i].trim().contains(":") && (local = args[i].trim().split(":")).length >= 2) {
                    String locaData = local[0].trim();
                    String locaValue = local[1].trim();
                    data = i == args.length - 1 ? String.valueOf(data) + locaData + "='" + locaValue + "'" : String.valueOf(data) + locaData + "='" + locaValue + "',";
                }
                ++i;
            }
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            String query = "UPDATE " + this.tabla + " SET " + data + ";";
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                }
                catch (SQLException e) {
                    this.log.fatalError("Could not create getter statement on update: " + query, e);
                    this.cleanup(statement, result);
                    break block7;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
    }

    @Override
    public void delete(CoreWHERE paranWhere) {
        block5: {
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            String query = "DELETE FROM " + this.tabla + " WHERE " + paranWhere.get() + ";";
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                }
                catch (SQLException e) {
                    this.log.fatalError("Could not create getter statement on delete: " + query, e);
                    this.cleanup(statement, result);
                    break block5;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
    }

    @Override
    public void close() {
        this.coreconnection.close();
    }

    @Override
    public void table(String table) {
        this.tabla = table;
    }

    @Override
    public void executeUpdate(String query) {
        block5: {
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                }
                catch (SQLException e) {
                    this.log.fatalError("Could not create getter statement on insert: " + query, e);
                    e.printStackTrace();
                    this.cleanup(statement, result);
                    break block5;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(statement, result);
                throw throwable;
            }
            this.cleanup(statement, result);
        }
    }
}


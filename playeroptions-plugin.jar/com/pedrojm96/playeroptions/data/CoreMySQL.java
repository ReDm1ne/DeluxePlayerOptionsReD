/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.data;

import com.pedrojm96.playeroptions.CoreLog;
import com.pedrojm96.playeroptions.data.CoreMySQLConnection;
import com.pedrojm96.playeroptions.data.CoreSQL;
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

public class CoreMySQL
implements CoreSQL {
    private CoreLog log;
    private String tabla;
    private CoreMySQLConnection coreconnection;
    public Map<String, String> columns = new HashMap<String, String>();

    public CoreMySQL(CoreMySQLConnection connection, String tabla) {
        this.log = connection.getLog();
        this.tabla = tabla;
        this.coreconnection = connection;
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
                    connection = this.coreconnection.getConnection();
                    String query = "SELECT * FROM " + this.tabla;
                    statement = connection.createStatement();
                    result = statement.executeQuery(query);
                    existe = true;
                    this.log.info("Loaded database");
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
                    this.cleanup(connection, statement, result);
                    break block6;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, result);
                throw throwable;
            }
            this.cleanup(connection, statement, result);
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
                    this.cleanup(connection, statement, null);
                    break block5;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, null);
                throw throwable;
            }
            this.cleanup(connection, statement, null);
        }
    }

    @Override
    public void changeColumnType(String columnname, String columntype) {
        block5: {
            Connection connection = null;
            Statement statement = null;
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    String query = "ALTER TABLE " + this.tabla + " MODIFY " + columnname + " " + columntype;
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                    this.log.info("Change column Type of " + columnname + " for: " + columntype);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    this.log.alert("The table " + this.tabla + " does not exist");
                    this.cleanup(connection, statement, null);
                    break block5;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, null);
                throw throwable;
            }
            this.cleanup(connection, statement, null);
        }
    }

    protected void cleanup(Connection connection, Statement statement, ResultSet result) {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException e) {
                this.log.error("SQLException on cleanup [connection].");
            }
        }
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
    public void build(String ... paramsString) {
        block6: {
            this.log.info("Creating table " + this.tabla);
            Connection connection = null;
            Statement statement = null;
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
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + this.tabla + " (id int(64) NOT NULL AUTO_INCREMENT PRIMARY KEY, " + query);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    this.cleanup(connection, statement, null);
                    break block6;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, null);
                throw throwable;
            }
            this.cleanup(connection, statement, null);
        }
    }

    @Override
    public boolean checkData(CoreWHERE paranWhere, String paranString) {
        boolean existe;
        block5: {
            Connection connection = null;
            existe = false;
            Statement statement = null;
            ResultSet result = null;
            try {
                try {
                    connection = this.coreconnection.getConnection();
                    String query = "SELECT '" + paranString + "' FROM " + this.tabla + " WHERE " + paranWhere.get() + ";";
                    statement = connection.createStatement();
                    result = statement.executeQuery(query);
                    existe = result.next();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    existe = true;
                    this.cleanup(connection, statement, result);
                    break block5;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, result);
                throw throwable;
            }
            this.cleanup(connection, statement, result);
        }
        return existe;
    }

    @Override
    public void insert(String ... args) {
        block9: {
            Connection connection = null;
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
                    this.log.fatalError("Could not create getter statement on insert: " + query, e);
                    e.printStackTrace();
                    this.cleanup(connection, statement, result);
                    break block9;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, result);
                throw throwable;
            }
            this.cleanup(connection, statement, result);
        }
    }

    @Override
    public String get(CoreWHERE paranWhere, String paranString) {
        String value;
        block6: {
            Connection connection = null;
            value = "";
            String query = "SELECT " + paranString + " FROM " + this.tabla + " WHERE " + paranWhere.get() + ";";
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
                    this.log.fatalError("Could not create getter statement on get: " + query, e);
                    e.printStackTrace();
                    this.cleanup(connection, statement, result);
                    break block6;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, result);
                throw throwable;
            }
            this.cleanup(connection, statement, result);
        }
        return value;
    }

    @Override
    public HashMap<String, String> get(CoreWHERE paranWhere, String ... paranString) {
        HashMap<String, String> value;
        block8: {
            Connection connection = null;
            value = new HashMap<String, String>();
            String getData = "";
            int i = 0;
            while (i < paranString.length) {
                getData = i == paranString.length - 1 ? String.valueOf(getData) + paranString[i].trim() : String.valueOf(getData) + paranString[i].trim() + ",";
                ++i;
            }
            String query = "SELECT " + getData + " FROM " + this.tabla + " WHERE " + paranWhere.get() + ";";
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
                    this.cleanup(connection, statement, result);
                    break block8;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, result);
                throw throwable;
            }
            this.cleanup(connection, statement, result);
        }
        return value;
    }

    @Override
    public List<HashMap<String, String>> getAll(CoreWHERE paranWhere, String criterio, String ... paranString) {
        ArrayList<HashMap<String, String>> lista;
        block8: {
            Connection connection = null;
            String getData = "";
            int i = 0;
            while (i < paranString.length) {
                getData = i == paranString.length - 1 ? String.valueOf(getData) + paranString[i].trim() : String.valueOf(getData) + paranString[i].trim() + ",";
                ++i;
            }
            lista = new ArrayList<HashMap<String, String>>();
            String query = "";
            query = criterio != null && !criterio.isEmpty() && criterio != " " ? "SELECT " + getData + " FROM " + this.tabla + " WHERE " + paranWhere.get() + " " + criterio + ";" : "SELECT " + getData + " FROM " + this.tabla + " WHERE " + paranWhere.get() + ";";
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
                    this.cleanup(connection, statement, result);
                    break block8;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, result);
                throw throwable;
            }
            this.cleanup(connection, statement, result);
        }
        return lista;
    }

    @Override
    public List<HashMap<String, String>> getAll(String criterio, String ... paranString) {
        ArrayList<HashMap<String, String>> lista;
        block8: {
            Connection connection = null;
            String getData = "";
            int i = 0;
            while (i < paranString.length) {
                getData = i == paranString.length - 1 ? String.valueOf(getData) + paranString[i].trim() : String.valueOf(getData) + paranString[i].trim() + ",";
                ++i;
            }
            lista = new ArrayList<HashMap<String, String>>();
            String query = "SELECT " + getData + " FROM " + this.tabla + " " + criterio + ";";
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
                    this.cleanup(connection, statement, result);
                    break block8;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, result);
                throw throwable;
            }
            this.cleanup(connection, statement, result);
        }
        return lista;
    }

    @Override
    public void update(CoreWHERE paranWhere, String ... args) {
        block7: {
            Connection connection = null;
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
                    this.cleanup(connection, statement, result);
                    break block7;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, result);
                throw throwable;
            }
            this.cleanup(connection, statement, result);
        }
    }

    @Override
    public void update(String ... args) {
        block7: {
            Connection connection = null;
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
                    this.cleanup(connection, statement, result);
                    break block7;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, result);
                throw throwable;
            }
            this.cleanup(connection, statement, result);
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
                    this.cleanup(connection, statement, result);
                    break block5;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, result);
                throw throwable;
            }
            this.cleanup(connection, statement, result);
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
                    this.cleanup(connection, statement, result);
                    break block5;
                }
            }
            catch (Throwable throwable) {
                this.cleanup(connection, statement, result);
                throw throwable;
            }
            this.cleanup(connection, statement, result);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.coreconnection.getConnection();
    }
}


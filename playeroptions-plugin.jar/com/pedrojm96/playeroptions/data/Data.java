/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.data;

import com.pedrojm96.playeroptions.CoreUtils;
import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.data.CoreMySQL;
import com.pedrojm96.playeroptions.data.CoreMySQLConnection;
import com.pedrojm96.playeroptions.data.CoreSQL;
import com.pedrojm96.playeroptions.data.CoreSQLite;
import com.pedrojm96.playeroptions.data.CoreSQLiteConnection;
import com.pedrojm96.playeroptions.data.VarDatas;
import java.util.HashMap;

public class Data {
    public CoreSQL coreData;

    public Data(PlayerOptions plugin) {
        if (plugin.config.getString("data-storage.type").equalsIgnoreCase("mysql")) {
            CoreMySQLConnection coreConnection = new CoreMySQLConnection(plugin.loader, plugin.config.getString("data-storage.host"), plugin.config.getInt("data-storage.port"), plugin.config.getString("data-storage.database"), plugin.config.getString("data-storage.username"), plugin.config.getString("data-storage.password"), false);
            this.coreData = new CoreMySQL(coreConnection, "playeroptions");
        } else {
            CoreSQLiteConnection coreSQLiteConnection = new CoreSQLiteConnection(plugin.loader);
            this.coreData = new CoreSQLite(coreSQLiteConnection, "playeroptions");
        }
        if (!this.coreData.checkStorage()) {
            this.coreData.build("uuid varchar(40)", "visibility int(1)", "speed int(1)", "jump int(1)", "fly int(1)", "chat int(1)", "stacker int(1)", "doublejump int(1)", "radio int(1)", "pvp int(1)", "time bigint(20)");
        } else if (!this.coreData.columnExists("pvp")) {
            plugin.log.alert("The \"pvp\" column does not exist.");
            this.coreData.addColumn("pvp", "INT");
            this.coreData.update("pvp:0");
        } else if (!this.coreData.columnExists("visibility")) {
            plugin.log.alert("The \"visibility\" column does not exist.");
            this.coreData.addColumn("visibility", "INT");
            this.coreData.update("visibility:1");
        } else if (!this.coreData.columnExists("chat")) {
            plugin.log.alert("The \"chat\" column does not exist.");
            this.coreData.addColumn("chat", "INT");
            this.coreData.update("chat:1");
        }
    }

    public boolean checkData(String uuid) {
        return this.coreData.checkData(CoreSQL.WHERE("uuid:" + uuid), "uuid");
    }

    public void insert(String uuid, int visibility, int speed, int jump, int fly, int chat, int stacker, int doublejump, int radio, int pvp) {
        this.coreData.insert("uuid:" + uuid, "visibility:" + visibility, "speed:" + speed, "jump:" + jump, "fly:" + fly, "chat:" + chat, "stacker:" + stacker, "doublejump:" + doublejump, "radio:" + radio, "pvp:" + pvp, "time:" + System.currentTimeMillis());
    }

    public void udatePlayerData(String uuid, int visibility, int speed, int jump, int fly, int chat, int stacker, int doublejump, int radio, int pvp) {
        this.coreData.update(CoreSQL.WHERE("uuid:" + uuid), "visibility:" + visibility, "speed:" + speed, "jump:" + jump, "fly:" + fly, "chat:" + chat, "stacker:" + stacker, "doublejump:" + doublejump, "radio:" + radio, "pvp:" + pvp, "time:" + System.currentTimeMillis());
    }

    public VarDatas selePlayerData(String uuid) {
        VarDatas vd = new VarDatas();
        HashMap<String, String> datas = this.coreData.get(CoreSQL.WHERE("uuid:" + uuid), "visibility", "speed", "jump", "fly", "chat", "stacker", "doublejump", "radio", "pvp");
        vd.setHideplayer(CoreUtils.integerValue(datas.get("visibility")));
        vd.setSpeed(CoreUtils.integerValue(datas.get("speed")));
        vd.setJump(CoreUtils.integerValue(datas.get("jump")));
        vd.setFly(CoreUtils.integerValue(datas.get("fly")));
        vd.setPvp(CoreUtils.integerValue(datas.get("pvp")));
        vd.setHidechat(CoreUtils.integerValue(datas.get("chat")));
        vd.setStacker(CoreUtils.integerValue(datas.get("stacker")));
        vd.setDoublejump(CoreUtils.integerValue(datas.get("doublejump")));
        vd.setRadio(CoreUtils.integerValue(datas.get("radio")));
        return vd;
    }

    public boolean checkTable() {
        return this.coreData.checkStorage();
    }
}


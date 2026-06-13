/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.pedrojm96.playeroptions.data;

import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.data.VarDatas;
import com.pedrojm96.playeroptions.options.Option;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OptionsData {
    private String pn;
    private String uuid;
    private Player pp;
    private PlayerOptions plugin;
    public Map<String, Integer> datas = new HashMap<String, Integer>();

    public OptionsData(Player p, PlayerOptions plugin) {
        this.plugin = plugin;
        this.pp = p;
        this.pn = p.getName();
        this.uuid = p.getUniqueId().toString();
    }

    private void checkAll() {
        for (String nodo : this.plugin.options.keySet()) {
            this.resetData(nodo);
            if (!this.plugin.options.get(nodo).contains(this.pn)) continue;
            this.datas.replace(nodo, 1);
        }
    }

    public void resetData(String nodo) {
        if (this.datas.containsKey(nodo)) {
            this.datas.replace(nodo, 0);
        } else {
            this.datas.put(nodo, 0);
        }
    }

    public void prepare() {
        this.checkAll();
    }

    public void save() {
        this.prepare();
        if (!this.plugin.data.checkData(this.uuid)) {
            this.plugin.data.insert(this.uuid, this.datas.get("visibility"), this.datas.get("speed"), this.datas.get("jump"), this.datas.get("fly"), this.datas.get("chat"), this.datas.get("stacker"), this.datas.get("doublejump"), this.datas.get("radio"), this.datas.get("pvp"));
        } else {
            this.plugin.data.udatePlayerData(this.uuid, this.setData("visibility"), this.setData("speed"), this.setData("jump"), this.setData("fly"), this.setData("chat"), this.setData("stacker"), this.setData("doublejump"), this.setData("radio"), this.setData("pvp"));
        }
    }

    public int setData(String data) {
        if (this.datas.containsKey(data)) {
            return this.datas.get(data);
        }
        return 0;
    }

    public void clear() {
        this.checkAll();
    }

    public void Load() {
        if (this.plugin.data.checkData(this.uuid)) {
            VarDatas r = this.plugin.data.selePlayerData(this.uuid);
            this.datas.put("speed", r.getSpeed());
            this.datas.put("jump", r.getJump());
            this.datas.put("doublejump", r.getDoublejump());
            this.datas.put("fly", r.getFly());
            this.datas.put("stacker", r.getStacker());
            this.datas.put("visibility", r.getHideplayer());
            this.datas.put("chat", r.getHidechat());
            this.datas.put("radio", r.getRadio());
            this.datas.put("pvp", r.getPvp());
            new BukkitRunnable(){

                public void run() {
                    OptionsData.this.loadAll();
                    OptionsData.this.onJoinHide();
                }
            }.runTask((Plugin)this.plugin.getInstance());
        } else {
            this.datas.put("speed", 0);
            this.datas.put("jump", 0);
            this.datas.put("doublejump", 0);
            this.datas.put("fly", 0);
            this.datas.put("stacker", 0);
            this.datas.put("visibility", 1);
            this.datas.put("chat", 1);
            this.datas.put("radio", 0);
            this.datas.put("pvp", 0);
            new BukkitRunnable(){

                public void run() {
                    OptionsData.this.loadAll();
                    OptionsData.this.onJoinHide();
                }
            }.runTask((Plugin)this.plugin.getInstance());
        }
    }

    public void onJoinHide() {
        Option option = this.plugin.options.get("visibility");
        if (option.isEnable()) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (players.getName().equalsIgnoreCase(this.pp.getName()) || option.contains(players.getName()) || this.pp.hasPermission("playeroptions.visibility.vip")) continue;
                players.hidePlayer(this.pp);
            }
            if (!option.contains(this.pp.getName())) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.getName().equalsIgnoreCase(this.pp.getName()) || players.hasPermission("playeroptions.visibility.vip")) continue;
                    this.pp.hidePlayer(players);
                }
            }
        }
    }

    private void loadAll() {
        for (String nodo : this.plugin.options.keySet()) {
            Option option;
            if (nodo.equalsIgnoreCase("visibility") || !(option = this.plugin.options.get(nodo)).isEnable()) continue;
            if (this.datas.get(nodo) == 1) {
                if (!this.pp.hasPermission("playeroptions." + nodo)) continue;
                option.executeEnableAction(this.pp);
                continue;
            }
            option.executeDisableAction(this.pp);
        }
    }
}


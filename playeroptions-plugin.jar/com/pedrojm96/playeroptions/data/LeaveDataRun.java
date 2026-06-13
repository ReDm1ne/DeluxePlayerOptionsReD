/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.pedrojm96.playeroptions.data;

import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.data.OptionsData;
import org.bukkit.scheduler.BukkitRunnable;

public class LeaveDataRun
extends BukkitRunnable {
    private PlayerOptions plugin;
    private String playerName;
    private OptionsData od;

    public LeaveDataRun(String playerName, OptionsData od, PlayerOptions plugin) {
        this.plugin = plugin;
        this.playerName = playerName;
        this.od = od;
    }

    public void run() {
        this.od.save();
        for (String nodo : this.plugin.options.keySet()) {
            this.plugin.options.get(nodo).clear(this.playerName);
        }
    }
}


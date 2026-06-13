/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.pedrojm96.playeroptions.data;

import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.data.OptionsData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinDataRun
extends BukkitRunnable {
    private PlayerOptions plugin;
    private Player player;

    public JoinDataRun(Player player, PlayerOptions plugin) {
        this.plugin = plugin;
        this.player = player;
    }

    public void run() {
        if (this.player == null) {
            return;
        }
        if (!this.player.isOnline()) {
            return;
        }
        OptionsData od = new OptionsData(this.player, this.plugin);
        od.Load();
    }
}


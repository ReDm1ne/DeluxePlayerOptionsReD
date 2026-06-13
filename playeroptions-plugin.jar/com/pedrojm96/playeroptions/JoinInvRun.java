/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.pedrojm96.playeroptions;

import com.pedrojm96.playeroptions.managers.ManagerToggleItem;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinInvRun
extends BukkitRunnable {
    private Player player;
    private ManagerToggleItem toggle;

    public JoinInvRun(Player player, ManagerToggleItem toggle) {
        this.player = player;
        this.toggle = toggle;
    }

    public void run() {
        if (this.player == null) {
            return;
        }
        if (!this.player.isOnline()) {
            return;
        }
        this.toggle.udateinv(this.player);
    }
}


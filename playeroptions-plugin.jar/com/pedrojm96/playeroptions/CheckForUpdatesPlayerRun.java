/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.pedrojm96.playeroptions;

import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.CoreSpigotUpdater;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckForUpdatesPlayerRun
extends BukkitRunnable {
    private JavaPlugin plugin;
    private Player player;
    private String prefix;
    private int project = 0;

    public CheckForUpdatesPlayerRun(Player player, String prefix, JavaPlugin plugin, int projectID) {
        this.plugin = plugin;
        this.player = player;
        this.prefix = prefix;
        this.project = projectID;
    }

    public void run() {
        if (this.player == null) {
            return;
        }
        if (!this.player.isOnline()) {
            return;
        }
        CoreSpigotUpdater updater = new CoreSpigotUpdater(this.plugin, this.project);
        try {
            if (updater.checkForUpdates()) {
                CoreColor.message(this.player, String.valueOf(this.prefix) + "&7Update avaliable for " + this.plugin.getName() + ". Please update to recieve latest version " + updater.getLatestVersion() + ".");
                CoreColor.message(this.player, String.valueOf(this.prefix) + "&7" + updater.getResourceURL());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}


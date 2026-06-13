/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions.options;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.options.Option;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OptionVisibility
extends Option {
    private List<String> hideplayer = new ArrayList<String>();

    @Override
    public void executeEnableAction(Player p) {
        if (this.hideplayer.contains(p.getName().toLowerCase())) {
            this.hideplayer.remove(p.getName().toLowerCase());
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (players.getName().equalsIgnoreCase(p.getName()) || this.hideplayer.contains(players.getName().toLowerCase())) continue;
                p.showPlayer(players);
            }
        }
    }

    @Override
    public void executeDisableAction(Player p) {
        if (!this.hideplayer.contains(p.getName().toLowerCase())) {
            this.hideplayer.add(p.getName().toLowerCase());
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (players.getName().equalsIgnoreCase(p.getName()) || players.hasPermission("playeroptions.visibility.vip")) continue;
                p.hidePlayer(players);
            }
        }
    }

    @Override
    public boolean contains(String playername) {
        return !this.hideplayer.contains(playername.toLowerCase());
    }

    @Override
    public String noPermissionMessage() {
        return AllString.no_permission_visibility;
    }

    @Override
    public String enableMessage() {
        return AllString.command_visibility_enable;
    }

    @Override
    public String disableMessage() {
        return AllString.command_visibility_disabled;
    }

    @Override
    public void clear(String playername) {
        if (this.hideplayer.contains(playername.toLowerCase())) {
            this.hideplayer.remove(playername.toLowerCase());
        }
    }
}


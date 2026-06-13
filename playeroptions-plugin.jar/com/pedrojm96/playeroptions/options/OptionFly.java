/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions.options;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.options.Option;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class OptionFly
extends Option {
    private List<String> fly = new ArrayList<String>();

    @Override
    public void executeEnableAction(Player player) {
        this.fly.add(player.getName().toLowerCase());
        player.setAllowFlight(true);
    }

    @Override
    public void executeDisableAction(Player player) {
        this.fly.remove(player.getName().toLowerCase());
        player.setAllowFlight(false);
    }

    @Override
    public boolean contains(String playername) {
        return this.fly.contains(playername.toLowerCase());
    }

    @Override
    public String noPermissionMessage() {
        return AllString.no_permission_fly;
    }

    @Override
    public String enableMessage() {
        return AllString.command_fly_enable;
    }

    @Override
    public String disableMessage() {
        return AllString.command_fly_disabled;
    }

    @Override
    public void clear(String playername) {
        if (this.fly.contains(playername.toLowerCase())) {
            this.fly.remove(playername.toLowerCase());
        }
    }
}


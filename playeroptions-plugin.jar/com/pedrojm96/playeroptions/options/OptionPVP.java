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

public class OptionPVP
extends Option {
    private List<String> pvp = new ArrayList<String>();

    @Override
    public void executeEnableAction(Player p) {
        this.pvp.add(p.getName().toLowerCase());
    }

    @Override
    public void executeDisableAction(Player p) {
        this.pvp.remove(p.getName().toLowerCase());
    }

    @Override
    public boolean contains(String playername) {
        return this.pvp.contains(playername.toLowerCase());
    }

    @Override
    public String noPermissionMessage() {
        return AllString.no_permission_pvp;
    }

    @Override
    public String enableMessage() {
        return AllString.command_pvp_enable;
    }

    @Override
    public String disableMessage() {
        return AllString.command_pvp_disabled;
    }

    @Override
    public void clear(String playername) {
        if (this.pvp.contains(playername.toLowerCase())) {
            this.pvp.remove(playername.toLowerCase());
        }
    }
}


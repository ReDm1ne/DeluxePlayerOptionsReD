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

public class OptionDoubleJump
extends Option {
    private List<String> doublejump = new ArrayList<String>();

    @Override
    public void executeEnableAction(Player player) {
        this.doublejump.add(player.getName().toLowerCase());
    }

    @Override
    public void executeDisableAction(Player player) {
        this.doublejump.remove(player.getName().toLowerCase());
    }

    @Override
    public boolean contains(String playername) {
        return this.doublejump.contains(playername.toLowerCase());
    }

    @Override
    public String noPermissionMessage() {
        return AllString.no_permission_doublejump;
    }

    @Override
    public String enableMessage() {
        return AllString.command_doublejump_enable;
    }

    @Override
    public String disableMessage() {
        return AllString.command_doublejump_disabled;
    }

    @Override
    public void clear(String playername) {
        if (this.doublejump.contains(playername.toLowerCase())) {
            this.doublejump.remove(playername.toLowerCase());
        }
    }
}


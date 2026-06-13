/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions.options;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.options.Option;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class OptionRadio
extends Option {
    private List<String> radio = new ArrayList<String>();

    @Override
    public void executeEnableAction(Player p) {
        this.radio.add(p.getName().toLowerCase());
        if (PlayerOptions.radioPlugin.scJukeBoxPlugin != null) {
            PlayerOptions.radioPlugin.scJukeBoxPlugin.getRadio().addPlayer(p);
        } else if (PlayerOptions.radioPlugin.jukeBoxPlugin != null && !PlayerOptions.radioPlugin.jukeBoxPlugin.isListening(p)) {
            PlayerOptions.radioPlugin.jukeBoxPlugin.join(p);
        }
    }

    @Override
    public void executeDisableAction(Player p) {
        this.radio.remove(p.getName().toLowerCase());
        if (PlayerOptions.radioPlugin.scJukeBoxPlugin != null) {
            if (PlayerOptions.radioPlugin.scJukeBoxPlugin.getCurrentJukebox(p) != null) {
                PlayerOptions.radioPlugin.scJukeBoxPlugin.getCurrentJukebox(p).removePlayer(p);
            }
        } else if (PlayerOptions.radioPlugin.jukeBoxPlugin != null && PlayerOptions.radioPlugin.jukeBoxPlugin.isListening(p)) {
            PlayerOptions.radioPlugin.jukeBoxPlugin.leave(p);
        }
    }

    @Override
    public boolean contains(String playername) {
        return this.radio.contains(playername.toLowerCase());
    }

    @Override
    public String noPermissionMessage() {
        return AllString.no_permission_radio;
    }

    @Override
    public String enableMessage() {
        return AllString.command_radio_enable;
    }

    @Override
    public String disableMessage() {
        return AllString.command_radio_disabled;
    }

    @Override
    public void clear(String playername) {
        if (this.radio.contains(playername.toLowerCase())) {
            this.radio.remove(playername.toLowerCase());
        }
    }
}


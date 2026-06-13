/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.statiocraft.jukebox.JukeBox
 *  com.statiocraft.jukebox.Shuffle
 *  com.statiocraft.jukebox.scJukeBox
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions;

import com.statiocraft.jukebox.JukeBox;
import com.statiocraft.jukebox.Shuffle;
import com.statiocraft.jukebox.scJukeBox;
import org.bukkit.entity.Player;

public class scJukeBoxPlugin {
    public JukeBox getCurrentJukebox(Player player) {
        return scJukeBox.getCurrentJukebox((Player)player);
    }

    public Shuffle getRadio() {
        return scJukeBox.getRadio();
    }
}


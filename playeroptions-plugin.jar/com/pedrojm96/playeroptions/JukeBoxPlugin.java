/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fr.skytasul.music.JukeBox
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions;

import fr.skytasul.music.JukeBox;
import org.bukkit.entity.Player;

public class JukeBoxPlugin {
    public boolean isListening(Player player) {
        return JukeBox.radio.isListening(player);
    }

    public void leave(Player player) {
        JukeBox.radio.leave(player);
    }

    public void join(Player player) {
        JukeBox.radio.join(player);
    }
}


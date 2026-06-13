/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Effect
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions;

import com.pedrojm96.playeroptions.PlayerOptions;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

public class Util {
    public static void playerPlayEffect(Player p, PlayerOptions plugin) {
        Effect ef = Effect.valueOf((String)plugin.config.getString("doublejump-particles.effect"));
        if (ef == null) {
            ef = Effect.MOBSPAWNER_FLAMES;
        }
        int i = 0;
        while (i < 5) {
            p.playEffect(p.getLocation().add(0.9, 1.5, 0.0), ef, 1);
            p.playEffect(p.getLocation().add(0.6, 1.5, 0.0), ef, 1);
            p.playEffect(p.getLocation().add(0.0, 1.5, 0.0), ef, 1);
            p.playEffect(p.getLocation().add(0.9, 0.7, 0.0), ef, 1);
            p.playEffect(p.getLocation().add(0.0, 1.0, 0.6), ef, 1);
            p.playEffect(p.getLocation().add(0.0, 1.0, 1.3), ef, 1);
            ++i;
        }
    }
}


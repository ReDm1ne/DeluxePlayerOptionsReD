/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.pedrojm96.playeroptions.options;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.options.Option;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OptionSpeed
extends Option {
    private List<String> speed = new ArrayList<String>();

    @Override
    public void executeEnableAction(Player p) {
        this.speed.add(p.getName().toLowerCase());
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
    }

    @Override
    public void executeDisableAction(Player p) {
        this.speed.remove(p.getName().toLowerCase());
        p.removePotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public boolean contains(String playername) {
        return this.speed.contains(playername.toLowerCase());
    }

    @Override
    public String noPermissionMessage() {
        return AllString.no_permission_speed;
    }

    @Override
    public String enableMessage() {
        return AllString.command_speed_enable;
    }

    @Override
    public String disableMessage() {
        return AllString.command_speed_disabled;
    }

    @Override
    public void clear(String playername) {
        if (this.speed.contains(playername.toLowerCase())) {
            this.speed.remove(playername.toLowerCase());
        }
    }
}


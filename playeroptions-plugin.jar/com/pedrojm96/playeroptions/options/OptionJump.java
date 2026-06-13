/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 */
package com.pedrojm96.playeroptions.options;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.CoreReflection;
import com.pedrojm96.playeroptions.CoreVersion;
import com.pedrojm96.playeroptions.options.Option;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class OptionJump
extends Option {
    private List<String> jump = new ArrayList<String>();

    @Override
    public void executeEnableAction(Player player) {
        this.jump.add(player.getName().toLowerCase());
        if (CoreVersion.getVersion().esMayorIgual(CoreVersion.v1_21)) {
            player.addPotionEffect(new PotionEffect(CoreReflection.getPotionEffect("JUMP_BOOST"), 100000, 1));
        } else {
            player.addPotionEffect(new PotionEffect(CoreReflection.getPotionEffect("JUMP"), 100000, 1));
        }
    }

    @Override
    public void executeDisableAction(Player player) {
        this.jump.remove(player.getName().toLowerCase());
        if (CoreVersion.getVersion().esMayorIgual(CoreVersion.v1_21)) {
            player.removePotionEffect(CoreReflection.getPotionEffect("JUMP_BOOST"));
        } else {
            player.removePotionEffect(CoreReflection.getPotionEffect("JUMP"));
        }
    }

    @Override
    public boolean contains(String playername) {
        return this.jump.contains(playername.toLowerCase());
    }

    @Override
    public String noPermissionMessage() {
        return AllString.no_permission_jump;
    }

    @Override
    public String enableMessage() {
        return AllString.command_jump_enable;
    }

    @Override
    public String disableMessage() {
        return AllString.command_jump_disabled;
    }

    @Override
    public void clear(String playername) {
        if (this.jump.contains(playername.toLowerCase())) {
            this.jump.remove(playername.toLowerCase());
        }
    }
}


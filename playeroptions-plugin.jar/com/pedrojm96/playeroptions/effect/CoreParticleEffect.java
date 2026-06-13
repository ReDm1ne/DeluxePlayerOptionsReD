/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.util.Vector
 *  xyz.xenondevs.particle.ParticleEffect
 */
package com.pedrojm96.playeroptions.effect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

public enum CoreParticleEffect {
    EXPLOSION_NORMAL,
    EXPLOSION_HUGE,
    EXPLOSION_LARGE,
    FIREWORKS_SPARK,
    WATER_BUBBLE,
    WATER_SPLASH,
    WATER_WAKE,
    SUSPENDED,
    SUSPENDED_DEPTH,
    CRIT,
    CRIT_MAGIC,
    SMOKE_NORMAL,
    SMOKE_LARGE,
    SPELL,
    SPELL_INSTANT,
    SPELL_MOB,
    SPELL_MOB_AMBIENT,
    SPELL_WITCH,
    DRIP_WATER,
    DRIP_LAVA,
    VILLAGER_ANGRY,
    VILLAGER_HAPPY,
    TOWN_AURA,
    NOTE,
    PORTAL,
    ENCHANTMENT_TABLE,
    FLAME,
    LAVA,
    FOOTSTEP,
    CLOUD,
    REDSTONE,
    SNOWBALL,
    SNOW_SHOVEL,
    SLIME,
    HEART,
    BARRIER,
    ITEM_CRACK,
    BLOCK_CRACK,
    BLOCK_DUST,
    WATER_DROP,
    ITEM_TAKE,
    MOB_APPEARANCE;


    public void playAll(Location loc, float xOffset, float yOffset, float zOffset, float speed, int count, int ... extra) {
        ParticleEffect.valueOf((String)this.name()).display(loc, new Vector(xOffset, yOffset, zOffset), speed, count, null, Bukkit.getOnlinePlayers());
    }

    public void play(Player player, Location loc, float xOffset, float yOffset, float zOffset, float speed, int count, int ... extra) {
        ParticleEffect.valueOf((String)this.name()).display(loc, new Vector(xOffset, yOffset, zOffset), speed, count, null, new Player[]{player});
    }
}


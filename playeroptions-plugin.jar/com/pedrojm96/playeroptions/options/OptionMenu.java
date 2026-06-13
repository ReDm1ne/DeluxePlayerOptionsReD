/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package com.pedrojm96.playeroptions.options;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

public class OptionMenu {
    private boolean enable = false;
    private String name;
    private Material material;
    private short data;
    private int slot;
    private boolean bottom_enable;
    private int bottom_slot;
    private List<String> lore = new ArrayList<String>();

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getData() {
        return this.data;
    }

    public void setData(short data) {
        this.data = data;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public boolean isBottom_enable() {
        return this.bottom_enable;
    }

    public void setBottom_enable(boolean bottom_enable) {
        this.bottom_enable = bottom_enable;
    }

    public int getBottom_slot() {
        return this.bottom_slot;
    }

    public void setBottom_slot(int bottom_slot) {
        this.bottom_slot = bottom_slot;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}


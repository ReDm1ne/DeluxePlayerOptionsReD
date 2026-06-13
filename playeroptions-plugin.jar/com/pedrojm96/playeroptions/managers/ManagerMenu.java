/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package com.pedrojm96.playeroptions.managers;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.CoreUtils;
import com.pedrojm96.playeroptions.CoreVersion;
import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.inventory.CoreNBTAttribute;
import com.pedrojm96.playeroptions.options.Option;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ManagerMenu {
    private String name;
    private Inventory menu;
    private int slot;
    private PlayerOptions plugin;

    public ManagerMenu(PlayerOptions plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        if (CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x)) {
            this.name = CoreColor.colorCodes(this.name);
        } else {
            if (this.name.contains("&k")) {
                this.name = this.name.replaceAll("&k", "");
            }
            if (this.name.contains("&K")) {
                this.name = this.name.replaceAll("&K", "");
            }
            if (this.name.contains("\u00a7k")) {
                this.name = this.name.replaceAll("\u00a7k", "");
            }
            if (this.name.contains("\u00a7K")) {
                this.name = this.name.replaceAll("\u00a7K", "");
            }
            this.name = CoreColor.colorCodes(this.name);
        }
    }

    private void create(int Row) {
        this.slot = this.getSlot(Row);
        this.menu = Bukkit.createInventory(null, (int)this.slot, (String)this.name);
        if (this.plugin.menuConfig.getBoolean("glass-enable")) {
            int color = this.plugin.menuConfig.getInt("glass-color");
            int i = 0;
            while (i < this.slot) {
                ItemStack it = CoreUtils.createItem(" ", 160, color);
                this.menu.setItem(i, it);
                ++i;
            }
        }
    }

    private void setClose() {
        if (this.plugin.menuConfig.isSet("close.enable")) {
            if (this.plugin.menuConfig.getBoolean("close.enable")) {
                ItemStack i = CoreUtils.createItem(this.plugin.menuConfig.getString("close.name"), this.plugin.menuConfig.getStringList("close.lore"), this.plugin.closeMaterial, (int)this.plugin.closeData);
                i = CoreNBTAttribute.removeAllAttributes(i);
                this.menu.setItem(this.plugin.menuConfig.getInt("close.slot"), i);
            }
        } else if (this.plugin.menuConfig.isSet("close.name")) {
            ItemStack i = CoreUtils.createItem(this.plugin.menuConfig.getString("close.name"), this.plugin.menuConfig.getStringList("close.lore"), this.plugin.closeMaterial, (int)this.plugin.closeData);
            i = CoreNBTAttribute.removeAllAttributes(i);
            this.menu.setItem(this.plugin.menuConfig.getInt("close.slot"), i);
        }
    }

    private void setOptions(Player player) {
        for (String nodo : this.plugin.options.keySet()) {
            ItemStack i;
            Option option = this.plugin.options.get(nodo);
            if (!option.getMenuoption().isEnable()) continue;
            if (option.contains(player.getName())) {
                i = CoreUtils.createItem(option.getMenuoption().getName().replaceAll("<status>", AllString.enabled), option.getMenuoption().getLore(), option.getMenuoption().getMaterial(), (int)option.getMenuoption().getData());
                i = CoreNBTAttribute.addGlow(i);
                this.menu.setItem(option.getMenuoption().getSlot(), i);
                if (!option.getMenuoption().isBottom_enable()) continue;
                i = CoreUtils.createItem(this.plugin.menuConfig.getString("item-on.name"), this.plugin.menuConfig.getStringList("item-on.lore"), this.plugin.itemOnMaterial, (int)this.plugin.itemOnData);
                this.menu.setItem(option.getMenuoption().getBottom_slot(), i);
                continue;
            }
            i = CoreUtils.createItem(option.getMenuoption().getName().replaceAll("<status>", AllString.disabled), option.getMenuoption().getLore(), option.getMenuoption().getMaterial(), (int)option.getMenuoption().getData());
            i = CoreNBTAttribute.removeAllAttributes(i);
            this.menu.setItem(option.getMenuoption().getSlot(), i);
            if (!option.getMenuoption().isBottom_enable()) continue;
            i = CoreUtils.createItem(this.plugin.menuConfig.getString("item-off.name"), this.plugin.menuConfig.getStringList("item-off.lore"), this.plugin.itemOffMaterial, (int)this.plugin.itemOffData);
            this.menu.setItem(option.getMenuoption().getBottom_slot(), i);
        }
    }

    public void open(Player player) {
        int row = this.plugin.menuConfig.getInt("row");
        this.create(row);
        this.setOptions(player);
        this.setClose();
        player.openInventory(this.menu);
    }

    private int getSlot(int rows) {
        if (rows <= 0) {
            int s = 9;
            return s;
        }
        if (rows > 6) {
            int s = 54;
            return s;
        }
        int s = rows * 9;
        return s;
    }
}


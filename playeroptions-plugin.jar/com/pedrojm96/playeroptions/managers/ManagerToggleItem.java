/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.pedrojm96.playeroptions.managers;

import com.pedrojm96.playeroptions.CoreUtils;
import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.inventory.CoreNBTAttribute;
import com.pedrojm96.playeroptions.options.Option;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ManagerToggleItem {
    private PlayerOptions plugin;

    public ManagerToggleItem(PlayerOptions plugin) {
        this.plugin = plugin;
    }

    public void udateinv(Player p) {
        for (String nodo : this.plugin.options.keySet()) {
            ItemStack i;
            Option option = this.plugin.options.get(nodo);
            if (!option.getToggleItemOption().isEnable()) continue;
            if (option.contains(p.getName())) {
                i = CoreUtils.createItem(option.getToggleItemOption().getName().replaceAll("<action>", this.plugin.toggleItemsConfig.getString("action.disable")), option.getToggleItemOption().getLore(), option.getToggleItemOption().getMaterial(), (int)option.getToggleItemOption().getData());
                i = CoreNBTAttribute.addGlow(i);
                p.getInventory().setItem(option.getToggleItemOption().getSlot(), i);
                continue;
            }
            i = CoreUtils.createItem(option.getToggleItemOption().getName().replaceAll("<action>", this.plugin.toggleItemsConfig.getString("action.enable")), option.getToggleItemOption().getLore(), option.getToggleItemOption().getMaterial(), (int)option.getToggleItemOption().getData());
            i = CoreNBTAttribute.removeAllAttributes(i);
            p.getInventory().setItem(option.getToggleItemOption().getSlot(), i);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.pedrojm96.playeroptions.inventory;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CoreNBTAttribute {
    public static ItemStack removePotionAttributes(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_POTION_EFFECTS});
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack removeAllAttributes(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        ItemFlag[] itemFlagArray = ItemFlag.values();
        int n = itemFlagArray.length;
        int n2 = 0;
        while (n2 < n) {
            ItemFlag flag = itemFlagArray[n2];
            meta.addItemFlags(new ItemFlag[]{flag});
            ++n2;
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addGlow(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return CoreNBTAttribute.removeAllAttributes(item);
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions.options;

import com.pedrojm96.playeroptions.options.OptionMenu;
import com.pedrojm96.playeroptions.options.OptionToggleItem;
import org.bukkit.entity.Player;

public abstract class Option {
    private OptionMenu menuoption;
    private OptionToggleItem toggleItemOption;

    public abstract void executeEnableAction(Player var1);

    public abstract void executeDisableAction(Player var1);

    public abstract void clear(String var1);

    public abstract String noPermissionMessage();

    public abstract String enableMessage();

    public abstract String disableMessage();

    public abstract boolean contains(String var1);

    public OptionMenu getMenuoption() {
        return this.menuoption;
    }

    public void setMenuoption(OptionMenu menuoption) {
        this.menuoption = menuoption;
    }

    public boolean isEnable() {
        return this.menuoption.isEnable();
    }

    public OptionToggleItem getToggleItemOption() {
        return this.toggleItemOption;
    }

    public void setToggleItemOption(OptionToggleItem toggleItemOption) {
        this.toggleItemOption = toggleItemOption;
    }
}


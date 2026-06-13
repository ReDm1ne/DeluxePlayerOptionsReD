/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.pedrojm96.playeroptions;

import com.pedrojm96.playeroptions.CoreLog;
import org.bukkit.plugin.java.JavaPlugin;

public interface CorePlugin {
    public CoreLog getLog();

    public JavaPlugin getInstance();
}


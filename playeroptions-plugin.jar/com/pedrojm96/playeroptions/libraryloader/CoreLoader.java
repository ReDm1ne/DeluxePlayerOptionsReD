/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.libraryloader;

public interface CoreLoader {
    public void onLoad();

    default public void onEnable() {
    }

    default public void onDisable() {
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.pedrojm96.playeroptions;

import com.pedrojm96.playeroptions.CoreColor;
import org.bukkit.plugin.java.JavaPlugin;

public class CoreLog {
    private JavaPlugin plugin;
    private Color color;
    private String prefix;
    private boolean debug = false;

    public CoreLog(JavaPlugin javaPlugin, Color color, boolean bl) {
        this.plugin = javaPlugin;
        this.color = color;
        this.prefix = String.valueOf(this.color.get()) + "[" + Color.GRAY.get() + this.plugin.getName() + this.color.get() + "]";
        this.debug = bl;
    }

    public CoreLog(JavaPlugin javaPlugin, Color color) {
        this.plugin = javaPlugin;
        this.color = color;
        this.prefix = String.valueOf(this.color.get()) + "[" + Color.GRAY.get() + this.plugin.getName() + this.color.get() + "]";
    }

    public void seDebug(boolean bl) {
        this.debug = bl;
    }

    public void info(String string) {
        this.plugin.getServer().getConsoleSender().sendMessage(String.valueOf(this.prefix) + CoreColor.colorCodes("&7 " + string));
    }

    public void alert(String string) {
        this.plugin.getServer().getConsoleSender().sendMessage(String.valueOf(this.prefix) + CoreColor.colorCodes("&8 " + string));
    }

    public void fatalError(String string) {
        this.plugin.getServer().getConsoleSender().sendMessage(String.valueOf(this.prefix) + CoreColor.colorCodes("&4Fatal-Error:&c " + string));
    }

    public void fatalError(String string, Exception exception) {
        this.plugin.getServer().getConsoleSender().sendMessage(String.valueOf(this.prefix) + CoreColor.colorCodes("&4Fatal-Error:&c " + string));
        exception.printStackTrace();
    }

    public void error(String string) {
        this.plugin.getServer().getConsoleSender().sendMessage(String.valueOf(this.prefix) + CoreColor.colorCodes("&c " + string));
    }

    public void error(String string, Exception exception) {
        this.plugin.getServer().getConsoleSender().sendMessage(String.valueOf(this.prefix) + CoreColor.colorCodes("&c " + string));
        exception.printStackTrace();
    }

    public void debug(String string) {
        if (this.debug) {
            this.plugin.getServer().getConsoleSender().sendMessage(String.valueOf(this.prefix) + CoreColor.colorCodes("&8Debug:&c " + string));
        }
    }

    public void line() {
        this.plugin.getServer().getConsoleSender().sendMessage(String.valueOf(this.color.get()) + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }

    public void println(String string) {
        System.out.println(string);
    }

    public void print(String string) {
        System.out.print(string);
    }

    public void debug(String string, Exception exception) {
        if (this.debug) {
            this.plugin.getServer().getConsoleSender().sendMessage(String.valueOf(this.prefix) + CoreColor.colorCodes("&8Debug:&c " + string));
            exception.printStackTrace();
        }
    }

    public static enum Color {
        AQUA("\u00a7b"),
        BLACK("\u00a70"),
        BLUE("\u00a79"),
        DARK_AQUA("\u00a73"),
        DARK_BLUE("\u00a71"),
        DARK_GRAY("\u00a78"),
        DARK_GREEN("\u00a72"),
        DARK_PURPLE("\u00a75"),
        DARK_RED("\u00a74"),
        GOLD("\u00a76"),
        GRAY("\u00a77"),
        GREEN("\u00a7a"),
        LIGHT_PURPLE("\u00a7d"),
        RED("\u00a7c"),
        WHITE("\u00a7f"),
        YELLOW("\u00a7e");

        private String color;

        private Color(String string2) {
            this.color = string2;
        }

        public String get() {
            return this.color;
        }
    }
}


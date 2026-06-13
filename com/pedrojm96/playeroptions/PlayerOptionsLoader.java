/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.pedrojm96.playeroptions;

import com.pedrojm96.playeroptions.CoreLog;
import com.pedrojm96.playeroptions.CorePlugin;
import com.pedrojm96.playeroptions.libraryloader.CoreClassLoader;
import com.pedrojm96.playeroptions.libraryloader.CoreLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerOptionsLoader
extends JavaPlugin
implements CorePlugin {
    private final CoreLoader plugin;
    public CoreLog log = new CoreLog(this, CoreLog.Color.RED);

    public PlayerOptionsLoader() {
        CoreClassLoader coreClassLoader = new CoreClassLoader(this.getClass().getClassLoader(), "playeroptions-plugin.jarinjar", this.log);
        this.plugin = coreClassLoader.instantiatePlugin("com.pedrojm96.playeroptions.PlayerOptions", CorePlugin.class, this);
        if (this.plugin == null) {
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
        }
    }

    public void onLoad() {
        this.plugin.onLoad();
    }

    public void onEnable() {
        PlayerOptionsLoader.loadConfig0();
        this.plugin.onEnable();
    }

    public void onDisable() {
        this.plugin.onDisable();
    }

    @Override
    public CoreLog getLog() {
        return this.log;
    }

    @Override
    public JavaPlugin getInstance() {
        return this;
    }

    private static /* bridge */ /* synthetic */ void loadConfig0() {
        try {
            URLConnection con = new URL("https://api.spigotmc.org/legacy/premium.php?user_id=606074&resource_id=33033&nonce=877270803").openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            ((HttpURLConnection)con).setInstanceFollowRedirects(true);
            String response = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if ("false".equals(response)) {
                throw new RuntimeException("Access to this plugin has been disabled! Please contact the author!");
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.pedrojm96.playeroptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.plugin.java.JavaPlugin;

public class CoreSpigotUpdater {
    private int project = 0;
    private URL checkURL;
    private String newVersion = "";
    private String pluginVersion;

    public CoreSpigotUpdater(JavaPlugin plugin, int projectID) {
        this.pluginVersion = plugin.getDescription().getVersion();
        this.project = projectID;
        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectID);
        }
        catch (MalformedURLException malformedURLException) {
            // empty catch block
        }
    }

    public int getProjectID() {
        return this.project;
    }

    public String getLatestVersion() {
        return this.newVersion;
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + this.project;
    }

    public boolean checkForUpdates() throws Exception {
        URLConnection con = this.checkURL.openConnection();
        this.newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        return !this.pluginVersion.equals(this.newVersion);
    }
}


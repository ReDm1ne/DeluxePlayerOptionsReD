/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.expansion.PlaceholderExpansion
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.options.Option;
import java.util.Iterator;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderApiExpansion
extends PlaceholderExpansion {
    private PlayerOptions plugin;

    public PlaceholderApiExpansion(PlayerOptions plugin) {
        this.plugin = plugin;
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return this.plugin.getInstance().getDescription().getAuthors().toString();
    }

    public String getIdentifier() {
        return "deluxeplayeroptions";
    }

    public String getVersion() {
        return this.plugin.getInstance().getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player p, String ide) {
        String nodo;
        if (p == null) {
            return "";
        }
        String retorno = null;
        Iterator<String> iterator = this.plugin.options.keySet().iterator();
        if (iterator.hasNext() && ide.equalsIgnoreCase(nodo = iterator.next())) {
            Option option = this.plugin.options.get(nodo);
            retorno = option.contains(p.getName()) ? AllString.enabled : AllString.disabled;
        }
        return retorno;
    }
}


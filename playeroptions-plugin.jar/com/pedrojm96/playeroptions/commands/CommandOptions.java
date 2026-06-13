/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions.commands;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.command.CorePluginCommand;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOptions
extends CorePluginCommand {
    private PlayerOptions plugin;

    public CommandOptions(PlayerOptions plugin) {
        this.plugin = plugin;
        plugin.log.info("Register command /options");
    }

    @Override
    public boolean onCommand(CommandSender sender, String command, String[] args) {
        if (!(sender instanceof Player)) {
            CoreColor.message(sender, String.valueOf(AllString.prefix) + AllString.no_console);
            return true;
        }
        Player p = (Player)sender;
        if (!this.plugin.worlds.contains(p.getWorld().getName())) {
            CoreColor.message(sender, String.valueOf(AllString.prefix) + AllString.no_world);
            return true;
        }
        this.plugin.getMenu().open(p);
        return true;
    }

    @Override
    public String getErrorNoPermission() {
        return String.valueOf(AllString.prefix) + AllString.no_permission;
    }

    @Override
    public String getPerm() {
        return "playeroptions.use";
    }

    @Override
    public List<String> onCustomTabComplete(CommandSender sender, List<String> list, String[] args) {
        return null;
    }

    @Override
    public String getName() {
        return "options";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("opt");
    }

    @Override
    public String getUsage() {
        return "/options";
    }

    @Override
    public String getDescription() {
        return "PlayerOptions main options command";
    }
}


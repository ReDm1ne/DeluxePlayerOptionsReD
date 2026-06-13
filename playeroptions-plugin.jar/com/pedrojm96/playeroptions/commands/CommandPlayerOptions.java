/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package com.pedrojm96.playeroptions.commands;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.command.CorePluginCommand;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;

public class CommandPlayerOptions
extends CorePluginCommand {
    public PlayerOptions plugin;

    public CommandPlayerOptions(PlayerOptions plugin) {
        this.plugin = plugin;
        plugin.log.info("Register command /playeroptions");
    }

    @Override
    public boolean onCommand(CommandSender sender, String command, String[] args) {
        CoreColor.message(sender, String.valueOf(AllString.prefix) + AllString.command_use);
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
        return list;
    }

    @Override
    public String getName() {
        return "playeroptions";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("po");
    }

    @Override
    public String getUsage() {
        return "/playeroptions <arg>";
    }

    @Override
    public String getDescription() {
        return "PlayerOptions admin main command.";
    }
}


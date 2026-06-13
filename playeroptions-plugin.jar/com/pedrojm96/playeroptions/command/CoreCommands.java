/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.reflect.FieldUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandMap
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.SimplePluginManager
 */
package com.pedrojm96.playeroptions.command;

import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.CorePlugin;
import com.pedrojm96.playeroptions.command.CorePluginCommand;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

public class CoreCommands {
    public static void registerCommand(CorePluginCommand command, CorePlugin plugin) {
        PluginCommand plugincommands = CoreCommands.createPluginCommand(command.getName(), plugin);
        if (plugincommands != null) {
            plugincommands.setAliases(command.getAliases()).setUsage(command.getUsage()).setDescription(command.getDescription()).setPermissionMessage(CoreColor.colorCodes(command.getErrorNoPermission())).setPermission(command.getPerm());
            try {
                CommandMap commandMap = CoreCommands.getCommandMapInstance();
                if (commandMap != null) {
                    commandMap.register(plugin.getInstance().getDescription().getName(), (Command)plugincommands);
                    plugin.getInstance().getCommand(command.getName()).setExecutor((CommandExecutor)command);
                    plugin.getInstance().getCommand(command.getName()).setTabCompleter((TabCompleter)command);
                }
            }
            catch (RuntimeException e) {
                plugin.getLog().fatalError("Error on register plugin command with reflection in the bukkit Commans map.", e);
            }
        }
    }

    private static PluginCommand createPluginCommand(String name, CorePlugin plugin) {
        try {
            Constructor constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            return (PluginCommand)constructor.newInstance(name, plugin.getInstance());
        }
        catch (Exception e) {
            plugin.getLog().fatalError("Error on create plugin command with reflection", e);
            return null;
        }
    }

    private static CommandMap getCommandMapInstance() {
        if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager spm = (SimplePluginManager)Bukkit.getPluginManager();
            try {
                Field field = FieldUtils.getDeclaredField(spm.getClass(), (String)"commandMap", (boolean)true);
                return (CommandMap)field.get(spm);
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException("Can't get the Bukkit CommandMap instance.");
            }
        }
        return null;
    }
}


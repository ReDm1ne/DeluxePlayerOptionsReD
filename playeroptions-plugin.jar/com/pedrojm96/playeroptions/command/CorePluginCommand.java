/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabExecutor
 */
package com.pedrojm96.playeroptions.command;

import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.command.CoreSubCommand;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public abstract class CorePluginCommand
implements CommandExecutor,
TabExecutor {
    private HashMap<List<String>, CoreSubCommand> subcommands = new HashMap();

    public abstract boolean onCommand(CommandSender var1, String var2, String[] var3);

    public abstract List<String> onCustomTabComplete(CommandSender var1, List<String> var2, String[] var3);

    public abstract String getErrorNoPermission();

    public abstract String getPerm();

    public abstract String getName();

    public abstract List<String> getAliases();

    public abstract String getUsage();

    public abstract String getDescription();

    public boolean hasPerm(CommandSender sender) {
        if (this.getPerm() == null) {
            return true;
        }
        return sender.hasPermission(this.getPerm());
    }

    public void addSubCommand(List<String> subcmds, CoreSubCommand s) {
        this.subcommands.put(subcmds, s);
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String arg2, String[] arg3) {
        if (command.getName().equalsIgnoreCase(this.getName())) {
            if (!this.hasPerm(sender)) {
                return null;
            }
            List<String> result = new ArrayList<String>();
            if (arg3.length == 1) {
                for (List<String> subcommand : this.subcommands.keySet()) {
                    for (String subcommandAlliases : subcommand) {
                        if (!arg3[0].isEmpty() || arg3[0] != "") {
                            if (!subcommandAlliases.startsWith(arg3[0]) || !this.subcommands.get(subcommand).hasPerm(sender)) continue;
                            result.add(subcommandAlliases);
                            continue;
                        }
                        if (!this.subcommands.get(subcommand).hasPerm(sender)) continue;
                        result.add(subcommandAlliases);
                    }
                }
                return this.onCustomTabComplete(sender, result, arg3);
            }
            for (List<String> subcommand : this.subcommands.keySet()) {
                if (!subcommand.contains(arg3[0].toLowerCase())) continue;
                if (!this.subcommands.get(subcommand).hasPerm(sender)) break;
                String[] subargs = new String[arg3.length - 1];
                int i = 1;
                while (i < arg3.length) {
                    subargs[i - 1] = arg3[i];
                    ++i;
                }
                result = this.subcommands.get(subcommand).onCustomTabComplete(sender, result, subargs);
                break;
            }
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        }
        return null;
    }

    public boolean onCommand(CommandSender sender, Command command, String arg2, String[] arg3) {
        if (!this.hasPerm(sender)) {
            CoreColor.message(sender, this.getErrorNoPermission());
            return true;
        }
        boolean retorno = false;
        if (arg3.length == 0) {
            return this.onCommand(sender, command.getName(), arg3);
        }
        for (List<String> s : this.subcommands.keySet()) {
            if (!s.contains(arg3[0].toLowerCase())) continue;
            String[] args = new String[arg3.length - 1];
            int i = 1;
            while (i < arg3.length) {
                args[i - 1] = arg3[i];
                ++i;
            }
            retorno = this.subcommands.get(s).rum(sender, arg3[0].toLowerCase(), args);
            break;
        }
        if (!retorno) {
            return this.onCommand(sender, command.getName(), arg3);
        }
        return retorno;
    }
}


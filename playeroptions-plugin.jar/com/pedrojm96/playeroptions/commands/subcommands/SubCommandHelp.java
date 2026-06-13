/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package com.pedrojm96.playeroptions.commands.subcommands;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.command.CoreSubCommand;
import java.util.List;
import org.bukkit.command.CommandSender;

public class SubCommandHelp
extends CoreSubCommand {
    @Override
    public boolean onSubCommand(CommandSender sender, String[] args) {
        CoreColor.message(sender, "&5&m---------------------------------------------------");
        if (sender.hasPermission("playeroptions.admin")) {
            CoreColor.message(sender, AllString.help_command_reload);
            CoreColor.message(sender, AllString.help_description_reload);
        }
        CoreColor.message(sender, AllString.help_command_options);
        CoreColor.message(sender, AllString.help_description_options);
        CoreColor.message(sender, AllString.help_command_jump);
        CoreColor.message(sender, AllString.help_description_jump);
        CoreColor.message(sender, AllString.help_command_speed);
        CoreColor.message(sender, AllString.help_description_speed);
        CoreColor.message(sender, AllString.help_command_doublejump);
        CoreColor.message(sender, AllString.help_description_doublejump);
        CoreColor.message(sender, AllString.help_command_fly);
        CoreColor.message(sender, AllString.help_description_fly);
        CoreColor.message(sender, AllString.help_command_stacker);
        CoreColor.message(sender, AllString.help_description_stacker);
        CoreColor.message(sender, AllString.help_command_visibility);
        CoreColor.message(sender, AllString.help_description_visibility);
        CoreColor.message(sender, AllString.help_command_chat);
        CoreColor.message(sender, AllString.help_description_chat);
        CoreColor.message(sender, AllString.help_command_radio);
        CoreColor.message(sender, AllString.help_description_radio);
        CoreColor.message(sender, AllString.help_command_radio);
        CoreColor.message(sender, AllString.help_description_radio);
        CoreColor.message(sender, AllString.help_command_pvp);
        CoreColor.message(sender, AllString.help_description_pvp);
        CoreColor.message(sender, "&5&m---------------------------------------------------");
        return true;
    }

    @Override
    public String getErrorNoPermission() {
        return String.valueOf(AllString.prefix) + AllString.no_permission;
    }

    @Override
    public List<String> onCustomTabComplete(CommandSender sender, List<String> list, String[] args) {
        return null;
    }

    @Override
    public String getPerm() {
        return "playeroptions.use";
    }
}


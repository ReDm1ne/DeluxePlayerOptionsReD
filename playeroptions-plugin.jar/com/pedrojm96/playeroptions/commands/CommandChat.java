/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Instrument
 *  org.bukkit.Note
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.pedrojm96.playeroptions.commands;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.command.CorePluginCommand;
import com.pedrojm96.playeroptions.options.Option;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandChat
extends CorePluginCommand {
    private PlayerOptions plugin;

    public CommandChat(PlayerOptions plugin) {
        this.plugin = plugin;
        plugin.log.info("Register command /chat");
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
        Option options = this.plugin.options.get("chat");
        if (options.contains(p.getName())) {
            options.executeDisableAction(p);
            this.plugin.getToggleItem().udateinv(p);
            p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
            CoreColor.message(sender, String.valueOf(AllString.prefix) + options.disableMessage());
        } else {
            options.executeEnableAction(p);
            this.plugin.getToggleItem().udateinv(p);
            p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
            CoreColor.message(sender, String.valueOf(AllString.prefix) + options.enableMessage());
        }
        return true;
    }

    @Override
    public String getErrorNoPermission() {
        return String.valueOf(AllString.prefix) + AllString.no_permission_chat;
    }

    @Override
    public String getPerm() {
        return "playeroptions.chat";
    }

    @Override
    public List<String> onCustomTabComplete(CommandSender sender, List<String> list, String[] args) {
        return null;
    }

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("cha");
    }

    @Override
    public String getUsage() {
        return "/chat";
    }

    @Override
    public String getDescription() {
        return "PlayerOptions Chat Commands";
    }
}


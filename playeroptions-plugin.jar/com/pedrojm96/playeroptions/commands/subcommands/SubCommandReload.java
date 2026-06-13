/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package com.pedrojm96.playeroptions.commands.subcommands;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.CoreConfig;
import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.command.CoreSubCommand;
import java.util.List;
import org.bukkit.command.CommandSender;

public class SubCommandReload
extends CoreSubCommand {
    public PlayerOptions plugin;

    public SubCommandReload(PlayerOptions plugin) {
        this.plugin = plugin;
        plugin.log.info("Register command /playeroptions reload");
    }

    @Override
    public boolean onSubCommand(CommandSender sender, String[] args) {
        this.plugin.config = new CoreConfig(this.plugin.getInstance(), "config", this.plugin.log, this.plugin.getInstance().getResource("config.yml"), true);
        this.plugin.menuConfig = new CoreConfig(this.plugin.getInstance(), "menu", this.plugin.log, this.plugin.getInstance().getResource("menu.yml"), true);
        this.plugin.toggleItemsConfig = new CoreConfig(this.plugin.getInstance(), "toggleitems", this.plugin.log, this.plugin.getInstance().getResource("toggleitems.yml"), true);
        this.plugin.loadTogleItem();
        this.plugin.loadMessages();
        AllString.load(this.plugin.config, this.plugin.messages);
        CoreColor.message(sender, String.valueOf(AllString.prefix) + AllString.config_loaded);
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
        return "playeroptions.admin";
    }
}


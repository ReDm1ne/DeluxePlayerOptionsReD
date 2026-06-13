/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package com.pedrojm96.playeroptions.command;

import com.pedrojm96.playeroptions.CoreColor;
import java.util.List;
import org.bukkit.command.CommandSender;

public abstract class CoreSubCommand {
    public abstract boolean onSubCommand(CommandSender var1, String[] var2);

    public abstract String getErrorNoPermission();

    public abstract List<String> onCustomTabComplete(CommandSender var1, List<String> var2, String[] var3);

    public abstract String getPerm();

    public boolean hasPerm(CommandSender sender) {
        if (this.getPerm() == null) {
            return true;
        }
        return sender.hasPermission(this.getPerm());
    }

    public boolean rum(CommandSender sender, String cmd, String[] args) {
        if (!this.hasPerm(sender)) {
            CoreColor.message(sender, this.getErrorNoPermission());
            return true;
        }
        return this.onSubCommand(sender, args);
    }
}


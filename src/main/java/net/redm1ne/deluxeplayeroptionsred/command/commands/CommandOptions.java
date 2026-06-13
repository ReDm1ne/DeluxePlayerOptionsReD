package net.redm1ne.deluxeplayeroptionsred.command.commands;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to open the options menu.
 */
public class CommandOptions implements CommandExecutor {

    private final DeluxePlayerOptions plugin;

    public CommandOptions(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageManager().get("no-console"));
            return true;
        }

        Player player = (Player) sender;

        // Check permission
        if (!player.hasPermission("deluxeplayeroptions.menu")) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessageManager().get("no-permission"));
            return true;
        }

        // Open menu
        plugin.getMenuManager().openMenu(player);
        return true;
    }
}

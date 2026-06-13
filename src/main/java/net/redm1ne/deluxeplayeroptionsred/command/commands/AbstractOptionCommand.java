package net.redm1ne.deluxeplayeroptionsred.command.commands;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import net.redm1ne.deluxeplayeroptionsred.option.Option;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for option toggle commands.
 */
public abstract class AbstractOptionCommand implements CommandExecutor, TabCompleter {

    protected final DeluxePlayerOptions plugin;
    protected final String optionId;

    public AbstractOptionCommand(DeluxePlayerOptions plugin, String optionId) {
        this.plugin = plugin;
        this.optionId = optionId;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageManager().get("no-console"));
            return true;
        }

        Player player = (Player) sender;
        Option option = plugin.getOptionManager().getOption(optionId);

        if (option == null) {
            return true;
        }

        // Check permission
        if (!option.hasPermission(player)) {
            option.sendNoPermissionMessage(player);
            return true;
        }

        // Check world
        if (!option.canUseInWorld(player)) {
            option.sendWorldNotAllowedMessage(player);
            return true;
        }

        // Toggle option
        boolean newState = option.toggle(player);

        if (newState) {
            option.sendEnabledMessage(player);
        } else {
            option.sendDisabledMessage(player);
        }

        // Update toggle item if applicable
        plugin.getToggleItemManager().updateToggleItem(player, optionId);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}

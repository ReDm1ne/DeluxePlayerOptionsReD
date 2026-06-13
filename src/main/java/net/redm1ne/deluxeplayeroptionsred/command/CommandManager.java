package net.redm1ne.deluxeplayeroptionsred.command;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import net.redm1ne.deluxeplayeroptionsred.command.commands.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages command registration and execution.
 */
public class CommandManager implements CommandExecutor, TabCompleter {

    private final DeluxePlayerOptions plugin;

    public CommandManager(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        // Register main command
        PluginCommand mainCmd = plugin.getCommand("playeroptions");
        if (mainCmd != null) {
            mainCmd.setExecutor(this);
            mainCmd.setTabCompleter(this);
        }

        // Register individual option commands
        registerCommand("speed", new CommandSpeed(plugin));
        registerCommand("fly", new CommandFly(plugin));
        registerCommand("jump", new CommandJump(plugin));
        registerCommand("doublejump", new CommandDoubleJump(plugin));
        registerCommand("stacker", new CommandStacker(plugin));
        registerCommand("visibility", new CommandVisibility(plugin));
        registerCommand("chat", new CommandChat(plugin));
        registerCommand("radio", new CommandRadio(plugin));
        registerCommand("pvp", new CommandPvP(plugin));

        // Register options menu command
        registerCommand("options", new CommandOptions(plugin));
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand cmd = plugin.getCommand(name);
        if (cmd != null) {
            cmd.setExecutor(executor);
            if (executor instanceof TabCompleter tabCompleter) {
                cmd.setTabCompleter(tabCompleter);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageManager().get("no-console"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "reload":
                if (player.hasPermission("deluxeplayeroptions.admin")) {
                    plugin.reload();
                    player.sendMessage(plugin.getPrefix() + plugin.getMessageManager().get("config-loaded"));
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getMessageManager().get("no-permission"));
                }
                break;

            case "help":
            case "?":
                sendHelp(player);
                break;

            default:
                player.sendMessage(plugin.getPrefix() + plugin.getMessageManager().get("command-error"));
                break;
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(plugin.getPrefix() + plugin.getMessageManager().get("command-use"));
        player.sendMessage("");

        if (player.hasPermission("deluxeplayeroptions.reload")) {
            player.sendMessage(plugin.getMessageManager().get("help-command-reload"));
            player.sendMessage(plugin.getMessageManager().get("help-description-reload"));
            player.sendMessage("");
        }

        player.sendMessage(plugin.getMessageManager().get("help-command-options"));
        player.sendMessage(plugin.getMessageManager().get("help-description-options"));
        player.sendMessage("");

        if (player.hasPermission("deluxeplayeroptions.speed")) {
            player.sendMessage(plugin.getMessageManager().get("help-command-speed"));
            player.sendMessage(plugin.getMessageManager().get("help-description-speed"));
        }

        if (player.hasPermission("deluxeplayeroptions.fly")) {
            player.sendMessage(plugin.getMessageManager().get("help-command-fly"));
            player.sendMessage(plugin.getMessageManager().get("help-description-fly"));
        }

        if (player.hasPermission("deluxeplayeroptions.jump")) {
            player.sendMessage(plugin.getMessageManager().get("help-command-jump"));
            player.sendMessage(plugin.getMessageManager().get("help-description-jump"));
        }

        if (player.hasPermission("deluxeplayeroptions.doublejump")) {
            player.sendMessage(plugin.getMessageManager().get("help-command-doublejump"));
            player.sendMessage(plugin.getMessageManager().get("help-description-doublejump"));
        }

        if (player.hasPermission("deluxeplayeroptions.stacker")) {
            player.sendMessage(plugin.getMessageManager().get("help-command-stacker"));
            player.sendMessage(plugin.getMessageManager().get("help-description-stacker"));
        }

        if (player.hasPermission("deluxeplayeroptions.visibility")) {
            player.sendMessage(plugin.getMessageManager().get("help-command-visibility"));
            player.sendMessage(plugin.getMessageManager().get("help-description-visibility"));
        }

        if (player.hasPermission("deluxeplayeroptions.chat")) {
            player.sendMessage(plugin.getMessageManager().get("help-command-chat"));
            player.sendMessage(plugin.getMessageManager().get("help-description-chat"));
        }

        if (player.hasPermission("deluxeplayeroptions.radio")) {
            player.sendMessage(plugin.getMessageManager().get("help-command-radio"));
            player.sendMessage(plugin.getMessageManager().get("help-description-radio"));
        }

        if (player.hasPermission("deluxeplayeroptions.pvp")) {
            player.sendMessage(plugin.getMessageManager().get("help-command-pvp"));
            player.sendMessage(plugin.getMessageManager().get("help-description-pvp"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subcommands = new ArrayList<>(Arrays.asList("help", "?"));
            if (sender.hasPermission("deluxeplayeroptions.admin")) {
                subcommands.add("reload");
            }

            String prefix = args[0].toLowerCase();
            for (String sub : subcommands) {
                if (sub.startsWith(prefix)) {
                    completions.add(sub);
                }
            }
        }

        return completions;
    }
}

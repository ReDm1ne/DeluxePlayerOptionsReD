package net.redm1ne.deluxeplayeroptionsred.option;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.entity.Player;

/**
 * Chat option - allows hiding chat visibility.
 * Note: This option is inverted in storage - "enabled" means hidden.
 */
public class OptionChat extends Option {

    public OptionChat(DeluxePlayerOptions plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "chat";
    }

    @Override
    public String getName() {
        return "Chat";
    }

    @Override
    public String getPermission() {
        return "deluxeplayeroptions.chat";
    }

    @Override
    public boolean isWorldDependent() {
        return false; // Chat works everywhere
    }

    @Override
    public boolean enable(Player player) {
        optionManager.setOptionEnabled(player, getId(), true);
        return true;
    }

    @Override
    public boolean disable(Player player) {
        optionManager.setOptionEnabled(player, getId(), false);
        return true;
    }
}

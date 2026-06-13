package net.redm1ne.deluxeplayeroptionsred.option;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.entity.Player;

/**
 * Double jump option - allows players to double jump in the air.
 * Actual double jump handling is done in PlayerListener.
 */
public class OptionDoubleJump extends Option {

    public OptionDoubleJump(DeluxePlayerOptions plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "doublejump";
    }

    @Override
    public String getName() {
        return "Double Jump";
    }

    @Override
    public String getPermission() {
        return "deluxeplayeroptions.doublejump";
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

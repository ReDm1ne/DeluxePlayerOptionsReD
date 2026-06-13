package net.redm1ne.deluxeplayeroptionsred.option;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.entity.Player;

/**
 * Stacker option - allows players to stack on each other's heads.
 * Actual stacking logic is handled in PlayerListener.
 */
public class OptionStacker extends Option {

    public OptionStacker(DeluxePlayerOptions plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "stacker";
    }

    @Override
    public String getName() {
        return "Stacker";
    }

    @Override
    public String getPermission() {
        return "deluxeplayeroptions.stacker";
    }

    @Override
    public boolean enable(Player player) {
        optionManager.setOptionEnabled(player, getId(), true);
        return true;
    }

    @Override
    public boolean disable(Player player) {
        // Remove from any player's head
        if (player.getVehicle() instanceof Player carrier) {
            player.leaveVehicle();
        }
        optionManager.setOptionEnabled(player, getId(), false);
        return true;
    }

    /**
     * Check if a player can be stacked on another.
     */
    public boolean canStackOn(Player stacker, Player target) {
        return isEnabled(stacker) && isEnabled(target);
    }
}

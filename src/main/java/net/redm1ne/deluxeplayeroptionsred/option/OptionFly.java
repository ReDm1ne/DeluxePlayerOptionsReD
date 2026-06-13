package net.redm1ne.deluxeplayeroptionsred.option;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.entity.Player;

/**
 * Fly option - allows players to fly.
 */
public class OptionFly extends Option {

    public OptionFly(DeluxePlayerOptions plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "fly";
    }

    @Override
    public String getName() {
        return "Fly";
    }

    @Override
    public String getPermission() {
        return "deluxeplayeroptions.fly";
    }

    @Override
    public boolean enable(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
        optionManager.setOptionEnabled(player, getId(), true);
        return true;
    }

    @Override
    public boolean disable(Player player) {
        player.setFlying(false);
        player.setAllowFlight(false);
        optionManager.setOptionEnabled(player, getId(), false);
        return true;
    }
}

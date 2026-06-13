package net.redm1ne.deluxeplayeroptionsred.option;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.entity.Player;

/**
 * Speed option - allows players to move faster.
 */
public class OptionSpeed extends Option {

    public static final float SPEED_VALUE = 0.2f; // Normal walking speed

    public OptionSpeed(DeluxePlayerOptions plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "speed";
    }

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public String getPermission() {
        return "deluxeplayeroptions.speed";
    }

    @Override
    public boolean enable(Player player) {
        player.setWalkSpeed(SPEED_VALUE * 2); // Double speed
        optionManager.setOptionEnabled(player, getId(), true);
        return true;
    }

    @Override
    public boolean disable(Player player) {
        player.setWalkSpeed(SPEED_VALUE); // Normal speed
        optionManager.setOptionEnabled(player, getId(), false);
        return true;
    }
}

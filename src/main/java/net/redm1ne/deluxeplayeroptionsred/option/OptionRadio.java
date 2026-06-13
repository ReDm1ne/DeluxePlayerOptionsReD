package net.redm1ne.deluxeplayeroptionsred.option;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.entity.Player;

/**
 * Radio option - toggle for radio plugin integration.
 */
public class OptionRadio extends Option {

    public OptionRadio(DeluxePlayerOptions plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "radio";
    }

    @Override
    public String getName() {
        return "Radio";
    }

    @Override
    public String getPermission() {
        return "deluxeplayeroptions.radio";
    }

    @Override
    public boolean isWorldDependent() {
        return false;
    }

    @Override
    public boolean enable(Player player) {
        if (!optionManager.isRadioAvailable()) {
            return false;
        }
        optionManager.setOptionEnabled(player, getId(), true);
        return true;
    }

    @Override
    public boolean disable(Player player) {
        optionManager.setOptionEnabled(player, getId(), false);
        return true;
    }

    @Override
    public boolean hasPermission(Player player) {
        // Additional check for radio availability
        return super.hasPermission(player) && optionManager.isRadioAvailable();
    }
}

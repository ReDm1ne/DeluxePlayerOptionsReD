package net.redm1ne.deluxeplayeroptionsred.option;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.entity.Player;

/**
 * PvP option - allows players to toggle their PvP status.
 * Note: This option is inverted in storage - "enabled" means PvP is disabled.
 */
public class OptionPvP extends Option {

    public OptionPvP(DeluxePlayerOptions plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "pvp";
    }

    @Override
    public String getName() {
        return "PvP";
    }

    @Override
    public String getPermission() {
        return "deluxeplayeroptions.pvp";
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

    /**
     * Check if PvP is enabled for a player (actual PvP state, not option state).
     */
    public boolean isPvPEnabled(Player player) {
        return !isEnabled(player); // Inverted
    }

    /**
     * Check if two players can PvP each other.
     */
    public boolean canPvP(Player player1, Player player2) {
        OptionPvP pvpOption = (OptionPvP) plugin.getOptionManager().getOption("pvp");
        return pvpOption.isPvPEnabled(player1) && pvpOption.isPvPEnabled(player2);
    }
}

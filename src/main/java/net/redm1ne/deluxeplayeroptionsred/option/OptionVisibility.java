package net.redm1ne.deluxeplayeroptionsred.option;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Visibility option - hides other players from view.
 */
public class OptionVisibility extends Option {

    public OptionVisibility(DeluxePlayerOptions plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "visibility";
    }

    @Override
    public String getName() {
        return "Visibility";
    }

    @Override
    public String getPermission() {
        return "deluxeplayeroptions.visibility";
    }

    @Override
    public boolean enable(Player player) {
        // Hide all other players from this player
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.equals(player)) {
                player.hidePlayer(plugin, other);
            }
        }
        optionManager.setOptionEnabled(player, getId(), true);
        return true;
    }

    @Override
    public boolean disable(Player player) {
        // Show all players to this player
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.equals(player)) {
                player.showPlayer(plugin, other);
            }
        }
        optionManager.setOptionEnabled(player, getId(), false);
        return true;
    }

    /**
     * Hide a newly joined player from all players with visibility enabled.
     */
    public void hidePlayerFromOthers(Player toHide) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (!viewer.equals(toHide) && isEnabled(viewer)) {
                viewer.hidePlayer(plugin, toHide);
            }
        }
    }

    /**
     * Show a leaving player to all players with visibility enabled.
     */
    public void showPlayerToOthers(Player toShow) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (!viewer.equals(toShow)) {
                viewer.showPlayer(plugin, toShow);
            }
        }
    }
}

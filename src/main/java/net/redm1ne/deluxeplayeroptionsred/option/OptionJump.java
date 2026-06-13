package net.redm1ne.deluxeplayeroptionsred.option;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Jump option - gives players a jump boost effect.
 */
public class OptionJump extends Option {

    private static final int JUMP_AMPLIFIER = 4; // Jump Boost V
    private static final int EFFECT_DURATION = 999999; // Practically infinite

    public OptionJump(DeluxePlayerOptions plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "jump";
    }

    @Override
    public String getName() {
        return "Jump";
    }

    @Override
    public String getPermission() {
        return "deluxeplayeroptions.jump";
    }

    @Override
    public boolean enable(Player player) {
        PotionEffect jumpEffect = new PotionEffect(getJumpEffectType(), EFFECT_DURATION, JUMP_AMPLIFIER, false, false);
        player.addPotionEffect(jumpEffect);
        optionManager.setOptionEnabled(player, getId(), true);
        return true;
    }

    @Override
    public boolean disable(Player player) {
        player.removePotionEffect(getJumpEffectType());
        optionManager.setOptionEnabled(player, getId(), false);
        return true;
    }

    /**
     * Get the appropriate PotionEffectType for jump boost.
     * Handles version differences between JUMP (1.16-1.20) and JUMP_BOOST (1.21+).
     */
    private PotionEffectType getJumpEffectType() {
        PotionEffectType type = PotionEffectType.getByName("JUMP_BOOST");
        if (type != null) {
            return type; // 1.21+
        }
        return PotionEffectType.getByName("JUMP"); // 1.16-1.20
    }
}

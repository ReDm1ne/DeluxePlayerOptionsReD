package net.redm1ne.deluxeplayeroptionsred.manager;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import net.redm1ne.deluxeplayeroptionsred.option.Option;
import net.redm1ne.deluxeplayeroptionsred.util.ColorUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages toggle items in player hotbars.
 */
public class ToggleItemManager {

    private static final String ACTION_PLACEHOLDER = "<action>";

    private final DeluxePlayerOptions plugin;
    private FileConfiguration toggleConfig;
    private String enableText;
    private String disableText;
    private final Map<String, ToggleItem> toggleItems = new HashMap<>();

    public ToggleItemManager(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        loadConfig();
    }

    public void reload() {
        loadConfig();
    }

    private void loadConfig() {
        File toggleFile = new File(plugin.getDataFolder(), "toggleitems.yml");
        if (!toggleFile.exists()) {
            plugin.saveResource("toggleitems.yml", false);
        }

        toggleConfig = YamlConfiguration.loadConfiguration(toggleFile);

        // Load action text
        enableText = ColorUtils.translate(toggleConfig.getString("action.enable", "&7(Right click to enable)"));
        disableText = ColorUtils.translate(toggleConfig.getString("action.disable", "&7(Right click to disable)"));

        // Load toggle items
        toggleItems.clear();
        loadToggleItems();
    }

    private void loadToggleItems() {
        String[] optionKeys = {"speed", "jump", "doublejump", "fly", "stacker", "visibility", "chat", "radio", "pvp"};

        for (String key : optionKeys) {
            if (toggleConfig.contains(key) && toggleConfig.getBoolean(key + ".enable", false)) {
                ToggleItem item = new ToggleItem();
                item.id = key;
                item.name = toggleConfig.getString(key + ".name", "&3&l" + key);
                item.material = getMaterial(key);
                item.slot = toggleConfig.getInt(key + ".slot", 1);
                item.lore = toggleConfig.getStringList(key + ".lore", new ArrayList<>());
                item.enabled = true;
                toggleItems.put(key, item);
            }
        }
    }

    private Material getMaterial(String key) {
        String materialName = toggleConfig.getString(key + ".material", "STONE");
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            // Try old material name
            String oldName = toggleConfig.getString(key + ".material-old", "");
            if (!oldName.isEmpty()) {
                material = Material.matchMaterial(oldName);
            }
        }
        return material != null ? material : Material.STONE;
    }

    /**
     * Give toggle items to a player on join.
     */
    public void giveToggleItems(Player player) {
        for (ToggleItem item : toggleItems.values()) {
            if (!item.enabled) continue;

            Option option = plugin.getOptionManager().getOption(item.id);
            if (option == null || !option.hasPermission(player)) continue;

            ItemStack toggleItem = createToggleItem(item, option, player);
            int slot = Math.min(item.slot - 1, 8); // Convert to 0-based and clamp to hotbar
            player.getInventory().setItem(slot, toggleItem);
        }
    }

    /**
     * Update toggle item for a specific option after toggle.
     */
    public void updateToggleItem(Player player, String optionId) {
        ToggleItem item = toggleItems.get(optionId);
        if (item == null || !item.enabled) return;

        Option option = plugin.getOptionManager().getOption(optionId);
        if (option == null) return;

        int slot = Math.min(item.slot - 1, 8);
        ItemStack existingItem = player.getInventory().getItem(slot);
        if (existingItem != null && isToggleItem(existingItem, optionId)) {
            ItemStack newItem = createToggleItem(item, option, player);
            player.getInventory().setItem(slot, newItem);
        }
    }

    private ItemStack createToggleItem(ToggleItem item, Option option, Player player) {
        ItemStack stack = new ItemStack(item.material);
        ItemMeta meta = stack.getItemMeta();

        // Replace <action> placeholder in name
        boolean optionEnabled = option.isEnabled(player);
        String actionText = optionEnabled ? disableText : enableText;
        String name = item.name.replace(ACTION_PLACEHOLDER, actionText);
        meta.setDisplayName(ColorUtils.translate(name));

        // Set lore
        List<String> lore = new ArrayList<>();
        for (String line : item.lore) {
            lore.add(ColorUtils.translate(line));
        }
        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }

        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Check if an item is a toggle item for a specific option.
     */
    public boolean isToggleItem(ItemStack item, String optionId) {
        if (item == null || item.getType() == Material.AIR) return false;
        ToggleItem toggleItem = toggleItems.get(optionId);
        if (toggleItem == null) return false;
        return item.getType() == toggleItem.material;
    }

    /**
     * Check if an item is any toggle item and return its option ID.
     */
    public String getToggleItemOption(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;

        for (ToggleItem toggleItem : toggleItems.values()) {
            if (toggleItem.enabled && item.getType() == toggleItem.material) {
                return toggleItem.id;
            }
        }
        return null;
    }

    /**
     * Get the slot for a toggle item option.
     */
    public int getToggleItemSlot(String optionId) {
        ToggleItem item = toggleItems.get(optionId);
        if (item == null) return -1;
        return Math.min(item.slot - 1, 8);
    }

    /**
     * Represents a toggle item configuration.
     */
    private static class ToggleItem {
        String id;
        String name;
        Material material;
        int slot;
        List<String> lore;
        boolean enabled;
    }
}

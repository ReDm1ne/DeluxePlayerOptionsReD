package net.redm1ne.deluxeplayeroptionsred.manager;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import net.redm1ne.deluxeplayeroptionsred.option.Option;
import net.redm1ne.deluxeplayeroptionsred.util.ColorUtils;
import net.redm1ne.deluxeplayeroptionsred.util.MaterialCompat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the options GUI menu.
 */
public class MenuManager implements InventoryHolder {

    public static final String MENU_TITLE = "PlayerOptionsMenu";
    private static final String STATUS_PLACEHOLDER = "<status>";

    private final DeluxePlayerOptions plugin;
    private FileConfiguration menuConfig;
    private String menuName;
    private int menuRows;
    private boolean glassEnabled;
    private int glassColor;
    private final Map<String, MenuItem> menuItems = new HashMap<>();

    // Item templates
    private ItemStack itemOnTemplate;
    private ItemStack itemOffTemplate;

    public MenuManager(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        loadMenuConfig();
    }

    private void loadMenuConfig() {
        File menuFile = new File(plugin.getDataFolder(), "menu.yml");
        if (!menuFile.exists()) {
            plugin.saveResource("menu.yml", false);
        }

        menuConfig = YamlConfiguration.loadConfiguration(menuFile);

        // Load menu settings
        menuName = ColorUtils.translate(menuConfig.getString("name", "&b&lPlayer Options"));
        menuRows = menuConfig.getInt("row", 6);
        glassEnabled = menuConfig.getBoolean("glass-enable", true);
        glassColor = menuConfig.getInt("glass-color", 3);

        // Load item templates
        loadItemTemplates();

        // Load all menu items
        loadMenuItems();
    }

    private void loadItemTemplates() {
        // Load ON item template
        if (menuConfig.contains("item-on")) {
            itemOnTemplate = loadItemFromConfig("item-on");
        } else {
            itemOnTemplate = MaterialCompat.getOnIndicator();
            ItemMeta meta = itemOnTemplate.getItemMeta();
            meta.setDisplayName(ColorUtils.translate("&a&lOn"));
            itemOnTemplate.setItemMeta(meta);
        }

        // Load OFF item template
        if (menuConfig.contains("item-off")) {
            itemOffTemplate = loadItemFromConfig("item-off");
        } else {
            itemOffTemplate = MaterialCompat.getOffIndicator();
            ItemMeta meta = itemOffTemplate.getItemMeta();
            meta.setDisplayName(ColorUtils.translate("&c&lOff"));
            itemOffTemplate.setItemMeta(meta);
        }
    }

    private void loadMenuItems() {
        String[] optionKeys = {"speed", "jump", "doublejump", "fly", "stacker", "visibility", "chat", "radio", "pvp", "close"};

        for (String key : optionKeys) {
            if (menuConfig.contains(key)) {
                boolean enabled = menuConfig.getBoolean(key + ".enable", false);
                if (enabled) {
                    MenuItem item = new MenuItem();
                    item.id = key;
                    item.name = menuConfig.getString(key + ".name", "&3&l" + key);
                    item.material = getMaterial(key);
                    item.slot = menuConfig.getInt(key + ".slot", 0);
                    item.itemUseEnabled = menuConfig.getBoolean(key + ".itemuse-enable", false);
                    item.itemUseSlot = menuConfig.getInt(key + ".itemuse-slot", 0);
                    item.lore = menuConfig.getStringList(key + ".lore", new ArrayList<>());
                    item.enabled = enabled;
                    menuItems.put(key, item);
                }
            }
        }
    }

    private Material getMaterial(String key) {
        String materialName = menuConfig.getString(key + ".material", "STONE");
        return Material.matchMaterial(materialName);
    }

    private ItemStack loadItemFromConfig(String path) {
        String materialName = menuConfig.getString(path + ".material", "STONE");
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            material = Material.STONE;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        String name = menuConfig.getString(path + ".name", "");
        meta.setDisplayName(ColorUtils.translate(name));

        List<String> lore = menuConfig.getStringList(path + ".lore", new ArrayList<>());
        List<String> translatedLore = new ArrayList<>();
        for (String line : lore) {
            translatedLore.add(ColorUtils.translate(line));
        }
        meta.setLore(translatedLore);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Open the options menu for a player.
     */
    public void openMenu(Player player) {
        Inventory inventory = createMenuInventory(player);
        player.openInventory(inventory);
    }

    private Inventory createMenuInventory(Player player) {
        int size = menuRows * 9;
        Inventory inventory = Bukkit.createInventory(this, size, MENU_TITLE);

        // Fill with glass panes if enabled
        if (glassEnabled) {
            fillGlass(inventory);
        }

        // Add option items
        for (MenuItem item : menuItems.values()) {
            if (item.id.equals("close")) {
                // Close button
                ItemStack closeItem = createCloseItem();
                inventory.setItem(item.slot, closeItem);
            } else {
                // Option item
                Option option = plugin.getOptionManager().getOption(item.id);
                if (option == null) continue;

                // Check permission for display
                if (!option.hasPermission(player)) continue;

                // Main option item
                ItemStack optionItem = createOptionItem(item, option, player);
                inventory.setItem(item.slot, optionItem);

                // Status indicator item
                if (item.itemUseEnabled) {
                    ItemStack statusItem = createStatusItem(option, player);
                    inventory.setItem(item.itemUseSlot, statusItem);
                }
            }
        }

        return inventory;
    }

    private void fillGlass(Inventory inventory) {
        ItemStack glass = createGlassPane();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }
    }

    private ItemStack createGlassPane() {
        Material stainedGlass = getStainedGlassMaterial();
        ItemStack glass = new ItemStack(stainedGlass);
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(" ");
        glass.setItemMeta(meta);
        return glass;
    }

    private Material getStainedGlassMaterial() {
        // Try to get colored glass pane
        String glassName = switch (glassColor) {
            case 1 -> "LIGHT_BLUE_STAINED_GLASS_PANE";
            case 2 -> "MAGENTA_STAINED_GLASS_PANE";
            case 3 -> "LIGHT_GRAY_STAINED_GLASS_PANE";
            case 4 -> "YELLOW_STAINED_GLASS_PANE";
            case 5 -> "LIME_STAINED_GLASS_PANE";
            case 6 -> "PINK_STAINED_GLASS_PANE";
            case 7 -> "GRAY_STAINED_GLASS_PANE";
            case 8 -> "CYAN_STAINED_GLASS_PANE";
            case 9 -> "PURPLE_STAINED_GLASS_PANE";
            case 10 -> "BLUE_STAINED_GLASS_PANE";
            case 11 -> "BROWN_STAINED_GLASS_PANE";
            case 12 -> "GREEN_STAINED_GLASS_PANE";
            case 13 -> "RED_STAINED_GLASS_PANE";
            case 14 -> "BLACK_STAINED_GLASS_PANE";
            case 15 -> "WHITE_STAINED_GLASS_PANE";
            default -> "LIGHT_GRAY_STAINED_GLASS_PANE";
        };

        Material material = Material.matchMaterial(glassName);
        return material != null ? material : Material.GLASS_PANE;
    }

    private ItemStack createOptionItem(MenuItem item, Option option, Player player) {
        ItemStack stack = new ItemStack(item.material);
        ItemMeta meta = stack.getItemMeta();

        // Replace <status> placeholder in name
        String name = item.name.replace(STATUS_PLACEHOLDER, getStatusText(option, player));
        meta.setDisplayName(ColorUtils.translate(name));

        // Set lore
        List<String> lore = new ArrayList<>();
        for (String line : item.lore) {
            lore.add(ColorUtils.translate(line));
        }
        meta.setLore(lore);

        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack createStatusItem(Option option, Player player) {
        boolean enabled = option.isEnabled(player);
        ItemStack template = enabled ? itemOnTemplate.clone() : itemOffTemplate.clone();
        return template;
    }

    private ItemStack createCloseItem() {
        MenuItem closeItem = menuItems.get("close");
        if (closeItem == null) {
            return new ItemStack(Material.BARRIER);
        }

        ItemStack item = new ItemStack(closeItem.material != null ? closeItem.material : Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ColorUtils.translate(closeItem.name));

        List<String> lore = new ArrayList<>();
        for (String line : closeItem.lore) {
            lore.add(ColorUtils.translate(line));
        }
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private String getStatusText(Option option, Player player) {
        boolean enabled = option.isEnabled(player);
        String statusKey = enabled ? "enabled" : "disabled";
        return plugin.getMessageManager().get(statusKey);
    }

    /**
     * Check if a slot is an option slot.
     */
    public String getOptionAtSlot(int slot) {
        for (MenuItem item : menuItems.values()) {
            if (item.slot == slot) {
                return item.id;
            }
            if (item.itemUseEnabled && item.itemUseSlot == slot) {
                return item.id;
            }
        }
        return null;
    }

    /**
     * Check if a slot is the close button.
     */
    public boolean isCloseSlot(int slot) {
        MenuItem closeItem = menuItems.get("close");
        return closeItem != null && closeItem.slot == slot;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    /**
     * Represents a menu item configuration.
     */
    private static class MenuItem {
        String id;
        String name;
        Material material;
        int slot;
        boolean itemUseEnabled;
        int itemUseSlot;
        List<String> lore;
        boolean enabled;
    }
}

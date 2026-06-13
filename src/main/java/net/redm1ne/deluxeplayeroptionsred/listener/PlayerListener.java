package net.redm1ne.deluxeplayeroptionsred.listener;

import net.redm1ne.deluxeplayeroptionsred.DeluxePlayerOptions;
import net.redm1ne.deluxeplayeroptionsred.manager.MenuManager;
import net.redm1ne.deluxeplayeroptionsred.option.Option;
import net.redm1ne.deluxeplayeroptionsred.option.OptionPvP;
import net.redm1ne.deluxeplayeroptionsred.option.OptionStacker;
import net.redm1ne.deluxeplayeroptionsred.option.OptionVisibility;
import net.redm1ne.deluxeplayeroptionsred.util.SoundCompat;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Handles all player-related events.
 */
public class PlayerListener implements Listener {

    private final DeluxePlayerOptions plugin;

    // Track players in double jump
    private final Set<UUID> doubleJumpReady = new HashSet<>();
    private final Set<UUID> inAir = new HashSet<>();
    private final Map<UUID, Long> lastGroundTime = new HashMap<>();

    // Cooldown for interactions
    private final Map<UUID, Long> interactionCooldown = new HashMap<>();

    public PlayerListener(DeluxePlayerOptions plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Load player data asynchronously
        plugin.getOptionManager().loadPlayerData(player);

        // Give toggle items after a short delay
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    plugin.getToggleItemManager().giveToggleItems(player);
                }
            }
        }.runTaskLater(plugin, 20L);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Save player data
        plugin.getOptionManager().savePlayerData(player);

        // Unload from cache
        plugin.getOptionManager().unloadPlayerData(player);

        // Clean up tracking
        UUID uuid = player.getUniqueId();
        doubleJumpReady.remove(uuid);
        inAir.remove(uuid);
        lastGroundTime.remove(uuid);
        interactionCooldown.remove(uuid);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;

        // Check if this is our menu
        if (inventory.getHolder() instanceof MenuManager) {
            event.setCancelled(true);

            int slot = event.getRawSlot();
            MenuManager menuManager = plugin.getMenuManager();

            // Check for close button
            if (menuManager.isCloseSlot(slot)) {
                player.closeInventory();
                return;
            }

            // Check for option slot
            String optionId = menuManager.getOptionAtSlot(slot);
            if (optionId == null || optionId.equals("close")) return;

            // Toggle option
            Option option = plugin.getOptionManager().getOption(optionId);
            if (option == null) return;

            if (!option.hasPermission(player)) {
                option.sendNoPermissionMessage(player);
                return;
            }

            boolean newState = option.toggle(player);
            if (newState) {
                option.sendEnabledMessage(player);
            } else {
                option.sendDisabledMessage(player);
            }

            // Update toggle item
            plugin.getToggleItemManager().updateToggleItem(player, optionId);

            // Refresh menu
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isOnline()) {
                        plugin.getMenuManager().openMenu(player);
                    }
                }
            }.runTaskLater(plugin, 1L);
            return;
        }

        // Handle toggle item clicks in player inventory
        ItemStack clickedItem = event.getCurrentItem();
        String toggleOption = plugin.getToggleItemManager().getToggleItemOption(clickedItem);
        if (toggleOption != null) {
            event.setCancelled(true);

            // Check cooldown
            if (isOnCooldown(player)) return;

            Option option = plugin.getOptionManager().getOption(toggleOption);
            if (option == null || !option.hasPermission(player)) return;

            boolean newState = option.toggle(player);
            if (newState) {
                option.sendEnabledMessage(player);
            } else {
                option.sendDisabledMessage(player);
            }

            // Update toggle item
            plugin.getToggleItemManager().updateToggleItem(player, toggleOption);
            setCooldown(player);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() == Material.AIR) return;

        // Only handle right click
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        // Check if this is a toggle item
        String toggleOption = plugin.getToggleItemManager().getToggleItemOption(item);
        if (toggleOption != null) {
            if (isOnCooldown(player)) return;

            Option option = plugin.getOptionManager().getOption(toggleOption);
            if (option == null || !option.hasPermission(player)) return;

            event.setCancelled(true);

            boolean newState = option.toggle(player);
            if (newState) {
                option.sendEnabledMessage(player);
            } else {
                option.sendDisabledMessage(player);
            }

            // Update toggle item
            plugin.getToggleItemManager().updateToggleItem(player, toggleOption);
            setCooldown(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager)) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        // Check PvP status
        OptionPvP pvpOption = (OptionPvP) plugin.getOptionManager().getOption("pvp");
        if (pvpOption == null) return;

        // If either player has PvP disabled
        if (!pvpOption.isPvPEnabled(damager)) {
            if (!plugin.getConfigManager().isPvPMessagesDisabled()) {
                damager.sendMessage(plugin.getPrefix() + plugin.getMessageManager().get("you-pvp-disabled"));
            }
            event.setCancelled(true);
            return;
        }

        if (!pvpOption.isPvPEnabled(victim)) {
            if (!plugin.getConfigManager().isPvPMessagesDisabled()) {
                damager.sendMessage(plugin.getPrefix() + plugin.getMessageManager().get("player-pvp-disabled"));
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Double jump handling
        if (plugin.getOptionManager().isOptionEnabled(player, "doublejump")) {
            handleDoubleJump(player);
        }

        // Stacker handling - right-click to stack
        // This is handled in PlayerInteractEntityEvent
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player target)) return;
        Player player = event.getPlayer();

        // Stacker handling
        if (!plugin.getOptionManager().isOptionEnabled(player, "stacker")) return;

        // Check cooldown
        if (isOnCooldown(player)) return;

        // Check if target has stacker enabled
        if (!plugin.getOptionManager().isOptionEnabled(target, "stacker")) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessageManager().get("player-stacker-disabled"));
            return;
        }

        // Stack player on target's head
        if (!target.getPassengers().isEmpty()) {
            // Target already has someone stacked
            return;
        }

        // Add player as passenger
        target.addPassenger(player);
        setCooldown(player);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        // Dismount from stacker
        if (event.isSneaking() && player.isInsideVehicle()) {
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof Player) {
                player.leaveVehicle();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        // Check if player has chat hidden
        if (plugin.getOptionManager().isOptionEnabled(player, "chat")) {
            event.setCancelled(true);
            player.sendMessage(plugin.getPrefix() + plugin.getMessageManager().get("chat-disabled"));
            return;
        }

        // Check recipients for visibility
        Set<Player> toRemove = new HashSet<>();
        for (Player recipient : event.getRecipients()) {
            if (plugin.getOptionManager().isOptionEnabled(recipient, "chat")) {
                toRemove.add(recipient);
            }
        }
        event.getRecipients().removeAll(toRemove);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String toWorld = player.getWorld().getName();

        // Check if world is enabled
        if (!plugin.isWorldEnabled(toWorld)) {
            // Disable all options when moving to disabled world
            Option flyOption = plugin.getOptionManager().getOption("fly");
            if (flyOption != null && flyOption.isEnabled(player)) {
                flyOption.disable(player);
            }

            Option speedOption = plugin.getOptionManager().getOption("speed");
            if (speedOption != null && speedOption.isEnabled(player)) {
                speedOption.disable(player);
            }

            Option jumpOption = plugin.getOptionManager().getOption("jump");
            if (jumpOption != null && jumpOption.isEnabled(player)) {
                jumpOption.disable(player);
            }
        }
    }

    private void handleDoubleJump(Player player) {
        UUID uuid = player.getUniqueId();
        boolean onGround = player.isOnGround();

        if (onGround) {
            doubleJumpReady.add(uuid);
            inAir.remove(uuid);
            lastGroundTime.put(uuid, System.currentTimeMillis());
        } else {
            // Player is in air
            if (doubleJumpReady.contains(uuid) && !inAir.contains(uuid)) {
                // Check if player is falling (not just jumping up)
                Vector velocity = player.getVelocity();
                if (velocity.getY() < -0.1) {
                    inAir.add(uuid);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // If player has fly option enabled, let them fly normally
        if (plugin.getOptionManager().isOptionEnabled(player, "fly")) {
            return;
        }

        // Double jump handling
        if (plugin.getOptionManager().isOptionEnabled(player, "doublejump")
                && doubleJumpReady.contains(uuid)
                && !player.isFlying()
                && player.getGameMode() != GameMode.CREATIVE
                && player.getGameMode() != GameMode.SPECTATOR) {

            // Cancel default flight toggle
            event.setCancelled(true);

            // Set player flying false (they tried to toggle it on)
            player.setFlying(false);
            player.setAllowFlight(false);

            // Do double jump
            Vector velocity = player.getVelocity();
            velocity.setY(1.0); // Jump velocity
            player.setVelocity(velocity);

            // Play effects
            playDoubleJumpEffects(player);

            // Reset for next jump
            doubleJumpReady.remove(uuid);
            inAir.remove(uuid);

            // Allow flight detection again after landing
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isOnline() && player.isOnGround()) {
                        doubleJumpReady.add(uuid);
                        // Temporarily allow flight for detection
                        if (!plugin.getOptionManager().isOptionEnabled(player, "fly")) {
                            player.setAllowFlight(true);
                        }
                    }
                }
            }.runTaskLater(plugin, 10L);
        }
    }

    private void playDoubleJumpEffects(Player player) {
        // Play sound
        if (plugin.getConfigManager().isDoubleJumpSoundEnabled()) {
            String soundName = plugin.getConfigManager().getDoubleJumpSound();
            SoundCompat.playSound(player, soundName, 1.0f, 1.0f);
        }

        // Play particles
        if (plugin.getConfigManager().isDoubleJumpParticlesEnabled()) {
            String effectName = plugin.getConfigManager().getDoubleJumpParticlesEffect();
            try {
                Effect effect = Effect.valueOf(effectName);
                player.getWorld().playEffect(player.getLocation(), effect, 0);
            } catch (IllegalArgumentException ignored) {
                // Effect not found
            }
        }
    }

    private boolean isOnCooldown(Player player) {
        Long lastTime = interactionCooldown.get(player.getUniqueId());
        if (lastTime == null) return false;
        return System.currentTimeMillis() - lastTime < 500; // 500ms cooldown
    }

    private void setCooldown(Player player) {
        interactionCooldown.put(player.getUniqueId(), System.currentTimeMillis());
    }
}

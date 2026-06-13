/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Instrument
 *  org.bukkit.Material
 *  org.bukkit.Note
 *  org.bukkit.Sound
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.ThrownPotion
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityCombustByEntityEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerFishEvent
 *  org.bukkit.event.player.PlayerInteractAtEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerToggleFlightEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package com.pedrojm96.playeroptions;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.CheckForUpdatesPlayerRun;
import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.CoreUtils;
import com.pedrojm96.playeroptions.JoinInvRun;
import com.pedrojm96.playeroptions.PlayerOptions;
import com.pedrojm96.playeroptions.Util;
import com.pedrojm96.playeroptions.command.CoreExecuteComands;
import com.pedrojm96.playeroptions.data.JoinDataRun;
import com.pedrojm96.playeroptions.data.LeaveDataRun;
import com.pedrojm96.playeroptions.data.OptionsData;
import com.pedrojm96.playeroptions.options.Option;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class OptionsEvent
implements Listener {
    private PlayerOptions plugin;

    public OptionsEvent(PlayerOptions plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled=true)
    public void onHit(EntityDamageByEntityEvent e) {
        if (this.plugin.worlds.contains(e.getEntity().getWorld().getName())) {
            ThrownPotion potion;
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                Player damager = (Player)e.getDamager();
                Player attacked = (Player)e.getEntity();
                if (!this.plugin.options.get("pvp").contains(damager.getName())) {
                    e.setCancelled(true);
                    CoreColor.message(damager, String.valueOf(AllString.prefix) + AllString.player_pvp_disabled);
                }
                if (!this.plugin.options.get("pvp").contains(attacked.getName())) {
                    e.setCancelled(true);
                    CoreColor.message(damager, String.valueOf(AllString.prefix) + AllString.you_pvp_disabled);
                }
            } else if (e.getDamager() instanceof Projectile) {
                Projectile arrow = (Projectile)e.getDamager();
                if (arrow.getShooter() instanceof Player && e.getEntity() instanceof Player) {
                    Player attacked;
                    Player damager = (Player)arrow.getShooter();
                    if (damager == (attacked = (Player)e.getEntity())) {
                        return;
                    }
                    if (!this.plugin.options.get("pvp").contains(damager.getName())) {
                        e.setCancelled(true);
                        CoreColor.message(damager, String.valueOf(AllString.prefix) + AllString.player_pvp_disabled);
                    }
                    if (!this.plugin.options.get("pvp").contains(attacked.getName())) {
                        e.setCancelled(true);
                        CoreColor.message(damager, String.valueOf(AllString.prefix) + AllString.you_pvp_disabled);
                    }
                }
            } else if (e.getDamager() instanceof ThrownPotion && (potion = (ThrownPotion)e.getDamager()).getShooter() instanceof Player && e.getEntity() instanceof Player) {
                Player attacked;
                Player damager = (Player)potion.getShooter();
                if (damager == (attacked = (Player)e.getEntity())) {
                    return;
                }
                if (!this.plugin.options.get("pvp").contains(damager.getName())) {
                    e.setCancelled(true);
                    CoreColor.message(damager, String.valueOf(AllString.prefix) + AllString.player_pvp_disabled);
                }
                if (!this.plugin.options.get("pvp").contains(attacked.getName())) {
                    e.setCancelled(true);
                    CoreColor.message(damager, String.valueOf(AllString.prefix) + AllString.you_pvp_disabled);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onFlameArrow(EntityCombustByEntityEvent e) {
        Arrow arrow;
        if (this.plugin.worlds.contains(e.getEntity().getWorld().getName()) && e.getCombuster() instanceof Arrow && (arrow = (Arrow)e.getCombuster()).getShooter() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player)arrow.getShooter();
            Player attacked = (Player)e.getEntity();
            if (!this.plugin.options.get("pvp").contains(damager.getName())) {
                e.setCancelled(true);
            }
            if (!this.plugin.options.get("pvp").contains(attacked.getName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onPlayerFishing(PlayerFishEvent e) {
        if (e.getCaught() instanceof Player) {
            Player damager = e.getPlayer();
            Player attacked = (Player)e.getCaught();
            if (damager.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD || damager.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD) {
                if (!this.plugin.options.get("pvp").contains(damager.getName())) {
                    e.setCancelled(true);
                    CoreColor.message(damager, String.valueOf(AllString.prefix) + AllString.player_pvp_disabled);
                }
                if (!this.plugin.options.get("pvp").contains(attacked.getName())) {
                    e.setCancelled(true);
                    CoreColor.message(damager, String.valueOf(AllString.prefix) + AllString.you_pvp_disabled);
                }
            }
        }
    }

    @EventHandler
    public void onCcommand(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        if (e.getMessage().equalsIgnoreCase("/" + this.plugin.menuConfig.getString("command"))) {
            e.setCancelled(true);
            if (!this.plugin.worlds.contains(p.getWorld().getName())) {
                CoreColor.message(e.getPlayer(), String.valueOf(AllString.prefix) + AllString.no_world);
                return;
            }
            if (!p.hasPermission("playeroptions.use")) {
                CoreColor.message(e.getPlayer(), String.valueOf(AllString.prefix) + AllString.no_permission);
                return;
            }
            this.plugin.getMenu().open(p);
        }
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        if (CoreUtils.getTitleInventory((InventoryEvent)e).equals(CoreColor.colorCodes(this.plugin.getMenu().getName()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFly(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();
        if (this.plugin.worlds.contains(p.getWorld().getName()) && this.plugin.options.get("doublejump").contains(p.getName())) {
            if (p.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            e.setCancelled(true);
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setVelocity(p.getLocation().getDirection().multiply(1.5).setY(1));
            if (this.plugin.config.getBoolean("doublejump-particles.enable")) {
                Util.playerPlayEffect(p, this.plugin);
            }
            if (this.plugin.config.getBoolean("doublejump-sound.enable")) {
                Sound sound;
                try {
                    sound = Sound.valueOf((String)this.plugin.config.getString("doublejump-sound.sound").toUpperCase());
                }
                catch (IllegalArgumentException ignore2) {
                    sound = null;
                }
                if (sound == null) {
                    sound = Sound.valueOf((String)this.plugin.config.getString("doublejump-sound.sound-old").toUpperCase());
                }
                e.getPlayer().playSound(e.getPlayer().getLocation(), sound, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void changeWorldLobbyItem(PlayerChangedWorldEvent e) {
        if (this.plugin.worlds.contains(e.getPlayer().getWorld().getName())) {
            Player p = e.getPlayer();
            if (this.plugin.options.get("speed").contains(p.getName())) {
                this.plugin.options.get("speed").executeEnableAction(p);
            }
            if (this.plugin.options.get("jump").contains(p.getName())) {
                this.plugin.options.get("jump").executeEnableAction(p);
            }
            if (this.plugin.options.get("fly").contains(p.getName())) {
                this.plugin.options.get("fly").executeEnableAction(p);
            }
        } else {
            Player p = e.getPlayer();
            if (this.plugin.options.get("speed").contains(p.getName())) {
                this.plugin.options.get("speed").executeDisableAction(p);
            }
            if (this.plugin.options.get("jump").contains(p.getName())) {
                this.plugin.options.get("jump").executeDisableAction(p);
            }
            if (this.plugin.options.get("fly").contains(p.getName())) {
                this.plugin.options.get("fly").executeDisableAction(p);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player)e.getEntity();
        if (this.plugin.worlds.contains(p.getWorld().getName()) && this.plugin.options.get("doublejump").contains(p.getName()) && e.getCause().equals((Object)EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (this.plugin.worlds.contains(p.getWorld().getName())) {
            if (this.plugin.options.get("doublejump").contains(p.getName())) {
                if (p.getGameMode() != GameMode.CREATIVE && p.getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR && !p.isFlying()) {
                    p.setAllowFlight(true);
                }
            } else if (p.getGameMode() != GameMode.CREATIVE && p.getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR && !p.isFlying()) {
                if (this.plugin.options.get("fly").contains(p.getName())) {
                    p.setAllowFlight(true);
                } else {
                    p.setAllowFlight(false);
                }
            }
        }
    }

    @EventHandler
    public void checkForUpdates(PlayerJoinEvent e) {
        Player p;
        if (this.plugin.config.getBoolean("update-check") && (p = e.getPlayer()).isOp()) {
            new CheckForUpdatesPlayerRun(e.getPlayer(), AllString.prefix, this.plugin.getInstance(), 33033).runTaskAsynchronously((Plugin)this.plugin.getInstance());
        }
    }

    @EventHandler
    public void onStack(PlayerInteractAtEntityEvent e) {
        Player p;
        String world;
        if (e.isCancelled()) {
            return;
        }
        Entity entity = e.getRightClicked();
        boolean isNPC = entity.hasMetadata("NPC");
        if (isNPC) {
            return;
        }
        if (entity instanceof Player && this.plugin.worlds.contains(e.getPlayer().getWorld().getName()) && this.plugin.options.get("stacker").isEnable() && this.plugin.worlds.contains(world = (p = e.getPlayer()).getWorld().getName()) && p.hasPermission("playeroptions.stacker")) {
            if (this.plugin.options.get("stacker").contains(p.getName())) {
                if (p.getPassenger() != null) {
                    p.getPassenger().leaveVehicle();
                } else if (this.plugin.options.get("stacker").contains(entity.getName())) {
                    entity.setPassenger((Entity)p);
                } else {
                    CoreColor.message(p, String.valueOf(AllString.prefix) + AllString.player_stacker_disabled);
                }
            } else {
                CoreColor.message(p, String.valueOf(AllString.prefix) + AllString.you_stacker_disabled);
            }
        }
    }

    @EventHandler
    public void onLeaveSaveData(PlayerQuitEvent e) {
        OptionsData od = new OptionsData(e.getPlayer(), this.plugin);
        od.prepare();
        new LeaveDataRun(e.getPlayer().getName(), od, this.plugin).runTaskAsynchronously((Plugin)this.plugin.getInstance());
    }

    @EventHandler
    public void onJoinLoadeData(PlayerJoinEvent e) {
        new JoinDataRun(e.getPlayer(), this.plugin).runTaskAsynchronously((Plugin)this.plugin.getInstance());
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void ChatDisable(AsyncPlayerChatEvent e) {
        if (this.plugin.options.get("chat").isEnable()) {
            Player p = e.getPlayer();
            if (!this.plugin.options.get("chat").contains(p.getName())) {
                if (this.plugin.worlds.contains(e.getPlayer().getWorld().getName())) {
                    e.setCancelled(true);
                    CoreColor.message(p, String.valueOf(AllString.prefix) + AllString.chat_disabled);
                }
            } else {
                for (Player r : Bukkit.getOnlinePlayers()) {
                    if (this.plugin.options.get("chat").contains(r.getName()) || !this.plugin.worlds.contains(r.getWorld().getName())) continue;
                    e.getRecipients().remove(r);
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onJoinToggleItems(PlayerJoinEvent e) {
        new JoinInvRun(e.getPlayer(), this.plugin.getToggleItem()).runTaskLaterAsynchronously((Plugin)this.plugin.getInstance(), 20L);
    }

    @EventHandler
    public void onDropToggleItem(PlayerDropItemEvent e) {
        if (e.isCancelled()) {
            return;
        }
        ItemStack itemInHand = e.getItemDrop().getItemStack();
        if (itemInHand == null) {
            return;
        }
        if (itemInHand.getItemMeta() == null) {
            return;
        }
        if (itemInHand.getItemMeta().getDisplayName() == null) {
            return;
        }
        for (String nodo : this.plugin.options.keySet()) {
            Option option = this.plugin.options.get(nodo);
            if (!option.getToggleItemOption().isEnable()) continue;
            if (itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(CoreColor.colorCodes(option.getToggleItemOption().getName().replaceAll("<action>", this.plugin.toggleItemsConfig.getString("action.disable"))))) {
                e.setCancelled(true);
                break;
            }
            if (!itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(CoreColor.colorCodes(option.getToggleItemOption().getName().replaceAll("<action>", this.plugin.toggleItemsConfig.getString("action.enable"))))) continue;
            e.setCancelled(true);
            break;
        }
    }

    @EventHandler
    public void InventoryToggelItem(InventoryClickEvent e) {
        if (e.isCancelled()) {
            return;
        }
        ItemStack itemInHand = e.getCurrentItem();
        if (itemInHand == null) {
            return;
        }
        if (itemInHand.getItemMeta() == null) {
            return;
        }
        if (itemInHand.getItemMeta().getDisplayName() == null) {
            return;
        }
        for (String nodo : this.plugin.options.keySet()) {
            Option option = this.plugin.options.get(nodo);
            if (!option.getToggleItemOption().isEnable()) continue;
            if (itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(CoreColor.colorCodes(option.getToggleItemOption().getName().replaceAll("<action>", this.plugin.toggleItemsConfig.getString("action.disable"))))) {
                e.setCancelled(true);
                break;
            }
            if (!itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(CoreColor.colorCodes(option.getToggleItemOption().getName().replaceAll("<action>", this.plugin.toggleItemsConfig.getString("action.enable"))))) continue;
            e.setCancelled(true);
            break;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemInHand = e.getItem();
            if (itemInHand == null) {
                return;
            }
            if (itemInHand.getItemMeta() == null) {
                return;
            }
            if (itemInHand.getItemMeta().getDisplayName() == null) {
                return;
            }
            for (String nodo : this.plugin.options.keySet()) {
                Option option = this.plugin.options.get(nodo);
                if (!option.getToggleItemOption().isEnable()) continue;
                if (itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(CoreColor.colorCodes(option.getToggleItemOption().getName().replaceAll("<action>", this.plugin.toggleItemsConfig.getString("action.disable"))))) {
                    e.setCancelled(true);
                    option.executeDisableAction(p);
                    this.plugin.getToggleItem().udateinv(p);
                    p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
                    CoreColor.message(p, String.valueOf(AllString.prefix) + option.disableMessage());
                    break;
                }
                if (!itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(CoreColor.colorCodes(option.getToggleItemOption().getName().replaceAll("<action>", this.plugin.toggleItemsConfig.getString("action.enable"))))) continue;
                e.setCancelled(true);
                option.executeEnableAction(p);
                this.plugin.getToggleItem().udateinv(p);
                p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
                CoreColor.message(p, String.valueOf(AllString.prefix) + option.enableMessage());
                break;
            }
        }
    }

    @EventHandler
    public void menuClick(InventoryClickEvent e) {
        if (CoreUtils.getTitleInventory((InventoryEvent)e).equals(CoreColor.colorCodes(this.plugin.getMenu().getName()))) {
            Player p = (Player)e.getWhoClicked();
            String pn = p.getName();
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().getItemMeta() == null) {
                return;
            }
            boolean done = false;
            for (String nodo : this.plugin.options.keySet()) {
                Option option = this.plugin.options.get(nodo);
                if (!option.getMenuoption().isEnable() || e.getSlot() != option.getMenuoption().getSlot() && (e.getSlot() != option.getMenuoption().getBottom_slot() || !option.getMenuoption().isBottom_enable())) continue;
                if (p.hasPermission("playeroptions." + nodo)) {
                    OptionsData od;
                    if (option.contains(pn)) {
                        option.executeDisableAction(p);
                        this.plugin.getMenu().open(p);
                        this.plugin.getToggleItem().udateinv(p);
                        p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
                        od = new OptionsData(p, this.plugin);
                        od.save();
                    } else {
                        option.executeEnableAction(p);
                        this.plugin.getMenu().open(p);
                        this.plugin.getToggleItem().udateinv(p);
                        p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
                        od = new OptionsData(p, this.plugin);
                        od.save();
                    }
                } else {
                    p.closeInventory();
                    e.setCancelled(true);
                    CoreColor.message(p, String.valueOf(AllString.prefix) + option.noPermissionMessage());
                }
                done = true;
                break;
            }
            if (!done && e.getSlot() == this.plugin.menuConfig.getInt("close.slot")) {
                if (this.plugin.menuConfig.getBoolean("close.commands-enable")) {
                    if (this.plugin.menuConfig.isSet("close.commands") && this.plugin.menuConfig.isList("close.commands")) {
                        List<String> commands = this.plugin.menuConfig.getStringList("close.commands");
                        if (commands != null && commands.size() > 0) {
                            for (String s : commands) {
                                CoreExecuteComands c = new CoreExecuteComands(p, s, this.plugin.getInstance(), AllString.prefix);
                                c.execute();
                            }
                        }
                        if (!this.plugin.menuConfig.getBoolean("close.keep-open")) {
                            p.closeInventory();
                        }
                    }
                } else {
                    p.closeInventory();
                }
            }
        }
    }
}


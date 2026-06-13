/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  net.milkbowl.vault.economy.EconomyResponse
 *  org.black_ixx.playerpoints.PlayerPoints
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.pedrojm96.playeroptions.command;

import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.CoreUtils;
import com.pedrojm96.playeroptions.CoreVariables;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CoreExecuteComands {
    private TypeCMD commandtype;
    private static Economy economy = null;
    private static PlayerPoints playerPoints = null;
    private Player player;
    private JavaPlugin plugin;
    private String command;
    private String prefix;

    public CoreExecuteComands(Player p, String cm, JavaPlugin plugin, String prefix) {
        this.player = p;
        this.plugin = plugin;
        this.prefix = prefix;
        cm = cm.trim();
        this.commandtype = TypeCMD.CONSOLE;
        if (cm.startsWith("player:")) {
            this.command = cm.substring(7);
            this.commandtype = TypeCMD.PLAYER;
        } else if (cm.startsWith("op:")) {
            this.command = cm.substring(3);
            this.commandtype = TypeCMD.OP;
        } else if (cm.startsWith("broadcast:")) {
            this.command = cm.substring(10);
            this.commandtype = TypeCMD.BROADCAST;
        } else if (cm.startsWith("give:")) {
            this.command = cm.substring(5);
            this.commandtype = TypeCMD.GIVE;
        } else if (cm.startsWith("money:")) {
            this.command = cm.substring(6);
            this.commandtype = TypeCMD.MONEY;
        } else if (cm.startsWith("points:")) {
            this.command = cm.substring(7);
            this.commandtype = TypeCMD.POINTS;
        } else if (cm.startsWith("tell:")) {
            this.command = cm.substring(5);
            this.commandtype = TypeCMD.TELL;
        } else if (cm.startsWith("open:")) {
            this.command = cm.substring(5);
            this.commandtype = TypeCMD.OPEN;
        } else if (cm.startsWith("console:")) {
            this.command = cm.substring(8);
            this.commandtype = TypeCMD.CONSOLE;
        } else if (cm.startsWith("server:")) {
            this.command = cm.substring(7);
            this.commandtype = TypeCMD.SERVER;
        } else if (cm.startsWith("world:")) {
            this.command = cm.substring(6);
            this.commandtype = TypeCMD.WORLD;
        } else if (cm.contains(":")) {
            String[] cms = cm.split(":");
            String cmss = cms[0];
            this.command = cm.substring(cmss.length());
            p.sendMessage(CoreColor.colorCodes(String.valueOf(this.prefix) + "The type of command " + cmss + " is not valid please contact the administrator."));
        } else {
            this.command = cm;
        }
        this.command = this.command.trim();
    }

    public void bungee() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(this.command);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.player.sendPluginMessage((Plugin)this.plugin, "BungeeCord", b.toByteArray());
    }

    public void world() {
        World w = Bukkit.getWorld((String)this.command);
        if (w != null) {
            this.player.teleport(w.getSpawnLocation());
        }
    }

    public void cmOP() {
        this.command = this.command.replaceAll("<player>", this.player.getName());
        this.command = CoreVariables.replace(this.command, this.player);
        if (this.player.isOp()) {
            this.player.chat("/" + this.command);
        } else {
            this.player.setOp(true);
            this.player.chat("/" + this.command);
            this.player.setOp(false);
        }
    }

    public void cmBROADCAST() {
        this.command = CoreVariables.replace(this.command, this.player);
        Bukkit.broadcastMessage((String)this.command);
    }

    public void cmGIVE() {
        if (this.command == null || this.command.length() == 0) {
            this.player.sendMessage(CoreColor.colorCodes(String.valueOf(this.prefix) + "Item no valid contact an administrator"));
            return;
        }
        if (this.command.contains(":")) {
            String[] ds = this.command.split(":");
            String m = ds[0];
            if (ds.length != 1 && ds.length != 0) {
                if (ds[1].contains(" ")) {
                    String[] dv = ds[1].split(" ");
                    String d = dv[0];
                    String a = dv[1];
                    GiveItem i = new GiveItem(m, d, a);
                    i.give(this.player);
                } else {
                    String d = ds[1];
                    GiveItem i = new GiveItem(m, d, "1");
                    i.give(this.player);
                }
            } else {
                GiveItem i = new GiveItem(m, "0", "1");
                i.give(this.player);
            }
        } else if (this.command.contains(" ")) {
            String[] dv = this.command.split(" ");
            String m = dv[0];
            String a = dv[1];
            GiveItem i = new GiveItem(m, "0", a);
            i.give(this.player);
        } else {
            GiveItem i = new GiveItem(this.command, "0", "1");
            i.give(this.player);
        }
    }

    public void cmMONEY() {
        if (!CoreUtils.isdouble(this.command)) {
            this.player.sendMessage(CoreColor.colorCodes(String.valueOf(this.prefix) + "Invalid money amount: " + this.command + ". Please inform the staff."));
            return;
        }
        double moni = Double.parseDouble(this.command);
        if (moni <= 0.0) {
            this.player.sendMessage(CoreColor.colorCodes(String.valueOf(this.prefix) + "Invalid money amount: " + this.command + ". Please inform the staff."));
            return;
        }
        if (economy != null) {
            EconomyResponse response = economy.depositPlayer(this.player.getName(), this.player.getWorld().getName(), moni);
            if (!response.transactionSuccess()) {
                this.player.sendMessage(CoreColor.colorCodes(String.valueOf(this.prefix) + "An unexpected error completing payment. Please inform the staff."));
            }
        } else {
            this.player.sendMessage(CoreColor.colorCodes(String.valueOf(this.prefix) + "Vault with a compatible economy plugin not found. Please inform the staff."));
        }
    }

    public static void economy(Economy econ) {
        economy = econ;
    }

    public static void playerPoints(PlayerPoints point) {
        playerPoints = point;
    }

    public void cmPOINTS() {
        if (!CoreUtils.isint(this.command)) {
            this.player.sendMessage(CoreColor.colorCodes(String.valueOf(this.prefix) + "Invalid points amount: " + this.command + ". Please inform the staff."));
            return;
        }
        int poin = Integer.parseInt(this.command);
        if (poin <= 0) {
            this.player.sendMessage(CoreColor.colorCodes(String.valueOf(this.prefix) + "Invalid points amount: " + this.command + ". Please inform the staff."));
            return;
        }
        if (playerPoints != null) {
            if (!playerPoints.getAPI().give(this.player.getUniqueId(), poin)) {
                this.player.sendMessage(CoreColor.colorCodes(String.valueOf(this.prefix) + "An unexpected error completing payment. Please inform the staff."));
            }
        } else {
            this.player.sendMessage(CoreColor.colorCodes(String.valueOf(this.prefix) + "The plugin PlayerPoints was not found. Please inform the staff."));
        }
    }

    public void execute() {
        switch (this.commandtype) {
            case PLAYER: {
                this.command = this.command.replaceAll("<player>", this.player.getName());
                this.command = CoreVariables.replace(this.command, this.player);
                this.player.chat("/" + this.command);
                break;
            }
            case OP: {
                this.cmOP();
                break;
            }
            case BROADCAST: {
                this.cmBROADCAST();
                break;
            }
            case GIVE: {
                this.cmGIVE();
                break;
            }
            case TELL: {
                this.command = CoreVariables.replace(this.command, this.player);
                this.player.sendMessage(this.command);
                break;
            }
            case MONEY: {
                this.cmMONEY();
                break;
            }
            case OPEN: {
                this.player.chat("/slmenuopen#" + this.command);
                break;
            }
            case SERVER: {
                this.bungee();
                break;
            }
            case WORLD: {
                this.world();
                break;
            }
            case POINTS: {
                this.cmPOINTS();
                break;
            }
            default: {
                this.command = this.command.replaceAll("<player>", this.player.getName());
                new BukkitRunnable(){

                    public void run() {
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)CoreExecuteComands.this.command);
                    }
                }.runTask((Plugin)this.plugin);
            }
        }
    }

    private class GiveItem {
        private String ma;
        private Short da;
        private int ca;

        private GiveItem(String material, String data, String cantida) {
            this.ma = material.trim();
            this.da = data == null || data.length() == 0 || !CoreUtils.isint(data.trim()) ? Short.valueOf((short)0) : Short.valueOf(data.trim());
            this.ca = cantida == null || cantida.length() == 0 || !CoreUtils.isint(cantida.trim()) ? 1 : Integer.valueOf(cantida.trim());
        }

        public void give(Player p) {
            if (this.ma == null || this.ma.length() == 0 || Material.getMaterial((String)this.ma) == null) {
                p.sendMessage("Material " + this.ma + "is invalid");
                return;
            }
            Material mate = Material.getMaterial((String)this.ma);
            ItemStack i = new ItemStack(mate, this.ca, this.da.shortValue());
            p.getInventory().addItem(new ItemStack[]{new ItemStack(i)});
        }
    }

    private static enum TypeCMD {
        SERVER,
        PLAYER,
        CONSOLE,
        OP,
        BROADCAST,
        GIVE,
        MONEY,
        POINTS,
        TELL,
        OPEN,
        WORLD;

    }
}


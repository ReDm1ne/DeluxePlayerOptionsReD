/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.pedrojm96.playeroptions;

import com.pedrojm96.playeroptions.AllString;
import com.pedrojm96.playeroptions.CoreConfig;
import com.pedrojm96.playeroptions.CoreLog;
import com.pedrojm96.playeroptions.CoreMaterial;
import com.pedrojm96.playeroptions.CorePlugin;
import com.pedrojm96.playeroptions.CoreSpigotUpdater;
import com.pedrojm96.playeroptions.CoreVariables;
import com.pedrojm96.playeroptions.CoreVersion;
import com.pedrojm96.playeroptions.JukeBoxPlugin;
import com.pedrojm96.playeroptions.OptionsEvent;
import com.pedrojm96.playeroptions.PlaceholderApiExpansion;
import com.pedrojm96.playeroptions.RadioPlugin;
import com.pedrojm96.playeroptions.command.CoreCommands;
import com.pedrojm96.playeroptions.commands.CommandChat;
import com.pedrojm96.playeroptions.commands.CommandDoubleJump;
import com.pedrojm96.playeroptions.commands.CommandFly;
import com.pedrojm96.playeroptions.commands.CommandJump;
import com.pedrojm96.playeroptions.commands.CommandOptions;
import com.pedrojm96.playeroptions.commands.CommandPVP;
import com.pedrojm96.playeroptions.commands.CommandPlayerOptions;
import com.pedrojm96.playeroptions.commands.CommandRadio;
import com.pedrojm96.playeroptions.commands.CommandSpeed;
import com.pedrojm96.playeroptions.commands.CommandStacker;
import com.pedrojm96.playeroptions.commands.CommandVisibility;
import com.pedrojm96.playeroptions.commands.subcommands.SubCommandHelp;
import com.pedrojm96.playeroptions.commands.subcommands.SubCommandReload;
import com.pedrojm96.playeroptions.data.Data;
import com.pedrojm96.playeroptions.libraryloader.CoreLoader;
import com.pedrojm96.playeroptions.libraryloader.CoreURLClassLoader;
import com.pedrojm96.playeroptions.libraryloader.CoreURLClassLoaderHelper;
import com.pedrojm96.playeroptions.libraryloader.LibraryLoader;
import com.pedrojm96.playeroptions.libs.org.bstats.bukkit.Metrics;
import com.pedrojm96.playeroptions.managers.ManagerMenu;
import com.pedrojm96.playeroptions.managers.ManagerToggleItem;
import com.pedrojm96.playeroptions.options.Option;
import com.pedrojm96.playeroptions.options.OptionChat;
import com.pedrojm96.playeroptions.options.OptionDoubleJump;
import com.pedrojm96.playeroptions.options.OptionFly;
import com.pedrojm96.playeroptions.options.OptionJump;
import com.pedrojm96.playeroptions.options.OptionMenu;
import com.pedrojm96.playeroptions.options.OptionPVP;
import com.pedrojm96.playeroptions.options.OptionRadio;
import com.pedrojm96.playeroptions.options.OptionSpeed;
import com.pedrojm96.playeroptions.options.OptionStacker;
import com.pedrojm96.playeroptions.options.OptionToggleItem;
import com.pedrojm96.playeroptions.options.OptionVisibility;
import com.pedrojm96.playeroptions.scJukeBoxPlugin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerOptions
implements CoreLoader {
    public final JavaPlugin instance;
    public final CorePlugin loader;
    public static PlayerOptions plugin;
    private final CoreURLClassLoaderHelper classPathAppender;
    public CoreConfig messages;
    public CoreConfig config;
    public CoreConfig menuConfig;
    public CoreConfig toggleItemsConfig;
    public Data data;
    public List<String> worlds = new ArrayList<String>();
    public List<String> staff = new ArrayList<String>();
    public CoreLog log;
    public boolean JukeBox;
    public Map<String, Option> options = new HashMap<String, Option>();
    public ManagerMenu menu;
    public ManagerToggleItem toggleItem;
    public Material closeMaterial;
    public short closeData;
    public Material itemOnMaterial;
    public short itemOnData;
    public Material itemOffMaterial;
    public short itemOffData;
    public static RadioPlugin radioPlugin;

    public PlayerOptions(CorePlugin loader) {
        this.loader = loader;
        this.instance = loader.getInstance();
        this.log = loader.getLog();
        this.classPathAppender = new CoreURLClassLoader(this.getClass().getClassLoader());
    }

    public void loadDependencies() {
        this.log.info("Loading Libraries...");
        LibraryLoader lib = new LibraryLoader(this.classPathAppender, this.log, this.loader);
        try {
            lib.loadLib("commons-lang", "commons-lang", "2.6");
            lib.loadLib("commons-codec", "commons-codec", "1.15");
            lib.loadLib("com.google.code.gson", "gson", "2.9.0");
            lib.loadLib("org.slf4j", "slf4j-api", "1.7.25");
            lib.loadLib("org.slf4j", "slf4j-simple", "1.7.25");
            lib.loadLib("com.zaxxer", "HikariCP", "4.0.3");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void banner() {
        this.log.line();
        this.log.println(" __                 ___  __   __   __  ___    __        __  ");
        this.log.println("|__) |     /\\  \\ / |__  |__) /  \\ |__)  |  | /  \\ |\\ | /__` ");
        this.log.println("|    |___ /~~\\  |  |___ |  \\ \\__/ |     |  | \\__/ | \\| .__/ ");
        this.log.println("                                                           ");
        this.log.line();
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.log = this.loader.getLog();
        this.banner();
        this.log.info("Deluxe Version: " + this.getInstance().getDescription().getVersion());
        this.log.info("Plugin Create by PedroJM96.");
        this.loadDependencies();
        try {
            Thread.sleep(1000L);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        PluginManager pm = this.getInstance().getServer().getPluginManager();
        OptionsEvent listener = new OptionsEvent(this);
        pm.registerEvents((Listener)listener, (Plugin)this.getInstance());
        this.log.line();
        this.log.println(" __                 ___  __   __   __  ___    __        __  ");
        this.log.println("|__) |     /\\  \\ / |__  |__) /  \\ |__)  |  | /  \\ |\\ | /__` ");
        this.log.println("|    |___ /~~\\  |  |___ |  \\ \\__/ |     |  | \\__/ | \\| .__/ ");
        this.log.println("                                                           ");
        this.log.info("Loading configuration...");
        this.config = new CoreConfig(this.getInstance(), "config", this.log, this.getInstance().getResource("config.yml"), true);
        this.menuConfig = new CoreConfig(this.getInstance(), "menu", this.log, this.getInstance().getResource("menu.yml"), true);
        this.menu = new ManagerMenu(this);
        this.menu.setName(this.menuConfig.getString("name"));
        this.toggleItemsConfig = new CoreConfig(this.getInstance(), "toggleitems", this.log, this.getInstance().getResource("toggleitems.yml"), true);
        this.toggleItem = new ManagerToggleItem(this);
        this.loadOptions();
        this.loadMessages();
        AllString.load(this.config, this.messages);
        this.worlds = this.config.getStringList("worlds");
        CoreCommands.registerCommand(new CommandOptions(this), this.loader);
        CommandPlayerOptions playeroptions = new CommandPlayerOptions(this);
        playeroptions.addSubCommand(Arrays.asList("reload"), new SubCommandReload(this));
        playeroptions.addSubCommand(Arrays.asList("help", "ayuda", "?"), new SubCommandHelp());
        CoreCommands.registerCommand(playeroptions, this.loader);
        CoreCommands.registerCommand(new CommandSpeed(this), this.loader);
        CoreCommands.registerCommand(new CommandJump(this), this.loader);
        CoreCommands.registerCommand(new CommandDoubleJump(this), this.loader);
        CoreCommands.registerCommand(new CommandFly(this), this.loader);
        CoreCommands.registerCommand(new CommandStacker(this), this.loader);
        CoreCommands.registerCommand(new CommandVisibility(this), this.loader);
        CoreCommands.registerCommand(new CommandChat(this), this.loader);
        CoreCommands.registerCommand(new CommandPVP(this), this.loader);
        this.setupJukeBox();
        if (this.JukeBox) {
            CoreCommands.registerCommand(new CommandRadio(this), this.loader);
            this.log.alert("Hooked icJukeBox");
        }
        this.data = new Data(this);
        this.checkForUpdates();
        new Metrics(this.getInstance(), 444);
        CoreVariables.iniUcode();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderApiExpansion(this).register();
            CoreVariables.placeholderAPI(true);
            this.log.alert("Hooked PlaceholderAPI");
        }
        this.log.line();
    }

    public ManagerMenu getMenu() {
        return this.menu;
    }

    public ManagerToggleItem getToggleItem() {
        return this.toggleItem;
    }

    public void setupJukeBox() {
        radioPlugin = new RadioPlugin();
        if (Bukkit.getPluginManager().isPluginEnabled("icJukeBox")) {
            PlayerOptions.radioPlugin.scJukeBoxPlugin = new scJukeBoxPlugin();
            this.JukeBox = true;
        } else if (Bukkit.getPluginManager().isPluginEnabled("JukeBox")) {
            PlayerOptions.radioPlugin.jukeBoxPlugin = new JukeBoxPlugin();
            this.JukeBox = true;
        } else {
            this.JukeBox = false;
        }
    }

    public void loadMessages() {
        String m = this.config.getString("messages");
        switch (m.toUpperCase()) {
            case "EN": {
                this.messages = new CoreConfig(this.getInstance(), "messages_EN", this.log, this.getInstance().getResource("messages_EN.yml"), true);
                break;
            }
            case "ES": {
                this.messages = new CoreConfig(this.getInstance(), "messages_ES", this.log, this.getInstance().getResource("messages_ES.yml"), true);
                break;
            }
            case "ZHCN": {
                this.messages = new CoreConfig(this.getInstance(), "messages_ZHCN", this.log, this.getInstance().getResource("messages_ZHCN.yml"), true);
                break;
            }
            default: {
                this.messages = new CoreConfig(this.getInstance(), "messages_EN", this.log, this.getInstance().getResource("messages_EN.yml"), true);
            }
        }
    }

    private void loadOptions() {
        this.loadItemOn();
        this.loadItemOff();
        OptionSpeed speed = new OptionSpeed();
        speed.setMenuoption(this.loadMenuOption("speed"));
        speed.setToggleItemOption(this.loadToggleItemOption("speed"));
        this.options.put("speed", speed);
        OptionJump jump = new OptionJump();
        jump.setMenuoption(this.loadMenuOption("jump"));
        jump.setToggleItemOption(this.loadToggleItemOption("jump"));
        this.options.put("jump", jump);
        OptionDoubleJump doublejump = new OptionDoubleJump();
        doublejump.setMenuoption(this.loadMenuOption("doublejump"));
        doublejump.setToggleItemOption(this.loadToggleItemOption("doublejump"));
        this.options.put("doublejump", doublejump);
        OptionFly fly = new OptionFly();
        fly.setMenuoption(this.loadMenuOption("fly"));
        fly.setToggleItemOption(this.loadToggleItemOption("fly"));
        this.options.put("fly", fly);
        OptionStacker stacker = new OptionStacker();
        stacker.setMenuoption(this.loadMenuOption("stacker"));
        stacker.setToggleItemOption(this.loadToggleItemOption("stacker"));
        this.options.put("stacker", stacker);
        OptionVisibility visibility = new OptionVisibility();
        visibility.setMenuoption(this.loadMenuOption("visibility"));
        visibility.setToggleItemOption(this.loadToggleItemOption("visibility"));
        this.options.put("visibility", visibility);
        OptionChat chat = new OptionChat();
        chat.setMenuoption(this.loadMenuOption("chat"));
        chat.setToggleItemOption(this.loadToggleItemOption("chat"));
        this.options.put("chat", chat);
        OptionRadio radio = new OptionRadio();
        radio.setMenuoption(this.loadMenuOption("radio"));
        radio.setToggleItemOption(this.loadToggleItemOption("radio"));
        if (this.JukeBox) {
            radio.getMenuoption().setEnable(true);
            radio.getToggleItemOption().setEnable(true);
        } else {
            radio.getMenuoption().setEnable(false);
            radio.getToggleItemOption().setEnable(false);
        }
        this.options.put("radio", radio);
        OptionPVP pvp = new OptionPVP();
        pvp.setMenuoption(this.loadMenuOption("pvp"));
        pvp.setToggleItemOption(this.loadToggleItemOption("pvp"));
        this.options.put("pvp", pvp);
        this.loadClose();
    }

    private void loadItemOn() {
        if (!this.menuConfig.isSet("item-on.material") && !this.menuConfig.isSet("item-on.material-old")) {
            this.getLog().alert("OptionMenu: The item item-on does not have a defined material value, using id instead.!");
            if (!this.menuConfig.isSet("item-on.id")) {
                this.getLog().error("OptionMenu: The item item-on has no Material or ID!");
                if (CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x)) {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SACK'");
                    this.itemOnData = (short)10;
                    this.itemOnMaterial = CoreMaterial.getMaterial("INK_SACK");
                } else {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SAC'");
                    this.itemOnData = (short)10;
                    this.itemOnMaterial = Material.INK_SAC;
                }
            } else if (this.menuConfig.getInt("item-on.id") == 0 || CoreMaterial.getMaterial(this.menuConfig.getInt("item-on.id")) == null) {
                this.getLog().error("OptionMenu: The item item-on has an invalid item ID: " + this.menuConfig.getInt("item-on.id") + ".");
                if (CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x)) {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SACK'");
                    this.itemOnData = (short)10;
                    this.itemOnMaterial = CoreMaterial.getMaterial("INK_SACK");
                } else {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SAC'");
                    this.itemOnData = (short)10;
                    this.itemOnMaterial = Material.INK_SAC;
                }
            } else {
                this.getLog().alert("It is recommended to change the numeric id to a text value of the material. Check out the new settings here. https://pedrojm96.com/minecraft-plugin/deluxeplayeroptions/po-config-yml/  ");
                this.getLog().alert("The material id is not supported in 1.16+");
                this.itemOnData = (short)this.menuConfig.getInt("item-on.data");
                this.itemOnMaterial = CoreMaterial.getMaterial(this.menuConfig.getInt("item-on.id"));
            }
        } else {
            String mate_data = CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x) ? (this.menuConfig.isSet("item-on.material-old") ? this.menuConfig.getString("item-on.material-old") : this.menuConfig.getString("item-on.material")) : (this.menuConfig.isSet("item-on.material") ? this.menuConfig.getString("item-on.material") : this.menuConfig.getString("item-on.material-old"));
            if (Material.getMaterial((String)(mate_data.contains(":") ? mate_data.split(":")[0].trim().toLowerCase() : mate_data.toUpperCase())) == null) {
                this.getLog().error("OptionMenu: The item item-on has an invalid item Material: " + (mate_data.contains(":") ? mate_data.split(":")[0].trim().toUpperCase() : mate_data.toUpperCase()) + ".");
                if (CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x)) {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SACK'");
                    this.itemOnData = (short)10;
                    this.itemOnMaterial = CoreMaterial.getMaterial("INK_SACK");
                } else {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SAC'");
                    this.itemOnData = (short)10;
                    this.itemOnMaterial = Material.INK_SAC;
                }
            } else {
                short data;
                String mate;
                if (mate_data.contains(":")) {
                    mate = mate_data.split(":")[0].trim().toUpperCase();
                    data = Short.valueOf(mate_data.split(":")[1].trim());
                } else {
                    mate = mate_data.toUpperCase();
                    data = (short)this.menuConfig.getInt("item-on.data");
                }
                this.itemOnData = data;
                this.itemOnMaterial = Material.getMaterial((String)mate);
            }
        }
    }

    private void loadItemOff() {
        if (!this.menuConfig.isSet("item-off.material") && !this.menuConfig.isSet("item-off.material-old")) {
            this.getLog().alert("OptionMenu: The item item-off does not have a defined material value, using id instead.!");
            if (!this.menuConfig.isSet("item-off.id")) {
                this.getLog().error("OptionMenu: The item item-off has no Material or ID!");
                if (CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x)) {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SACk'");
                    this.itemOffData = (short)8;
                    this.itemOffMaterial = CoreMaterial.getMaterial("INK_SACK");
                } else {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SAC'");
                    this.itemOffData = (short)8;
                    this.itemOffMaterial = Material.INK_SAC;
                }
            } else if (this.menuConfig.getInt("item-off.id") == 0 || CoreMaterial.getMaterial(this.menuConfig.getInt("item-off.id")) == null) {
                this.getLog().error("OptionMenu: The item item-off has an invalid item ID: " + this.menuConfig.getInt("item-off.id") + ".");
                if (CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x)) {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SACk'");
                    this.itemOffData = (short)8;
                    this.itemOffMaterial = CoreMaterial.getMaterial("INK_SACK");
                } else {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SAC'");
                    this.itemOffData = (short)8;
                    this.itemOffMaterial = Material.INK_SAC;
                }
            } else {
                this.getLog().alert("It is recommended to change the numeric id to a text value of the material. Check out the new settings here. https://pedrojm96.com/minecraft-plugin/deluxeplayeroptions/po-config-yml/  ");
                this.getLog().alert("The material id is not supported in 1.16+");
                this.itemOffData = (short)this.menuConfig.getInt("item-off.data");
                this.itemOffMaterial = CoreMaterial.getMaterial(this.menuConfig.getInt("item-off.id"));
            }
        } else {
            String mate_data = CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x) ? (this.menuConfig.isSet("item-off.material-old") ? this.menuConfig.getString("item-off.material-old") : this.menuConfig.getString("item-off.material")) : (this.menuConfig.isSet("item-off.material") ? this.menuConfig.getString("item-off.material") : this.menuConfig.getString("item-off.material-old"));
            if (Material.getMaterial((String)(mate_data.contains(":") ? mate_data.split(":")[0].trim().toUpperCase() : mate_data.toUpperCase())) == null) {
                this.getLog().error("OptionMenu: The item item-off has an invalid item Material: " + (mate_data.contains(":") ? mate_data.split(":")[0].trim().toUpperCase() : mate_data.toUpperCase()) + ".");
                if (CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x)) {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SACk'");
                    this.itemOffData = (short)8;
                    this.itemOffMaterial = CoreMaterial.getMaterial("INK_SACK");
                } else {
                    this.getLog().error("OptionMenu: Use the default material 'INK_SAC'");
                    this.itemOffData = (short)8;
                    this.itemOffMaterial = Material.INK_SAC;
                }
            } else {
                short data;
                String mate;
                if (mate_data.contains(":")) {
                    mate = mate_data.split(":")[0].trim().toUpperCase();
                    data = Short.valueOf(mate_data.split(":")[1].trim());
                } else {
                    mate = mate_data.toUpperCase();
                    data = (short)this.menuConfig.getInt("item-off.data");
                }
                this.itemOffData = data;
                this.itemOffMaterial = Material.getMaterial((String)mate);
            }
        }
    }

    private void loadClose() {
        if (!this.menuConfig.isSet("close.material") && !this.menuConfig.isSet("close.material-old")) {
            this.getLog().alert("OptionMenu: The item close does not have a defined material value, using id instead.!");
            if (!this.menuConfig.isSet("close.id")) {
                this.getLog().error("OptionMenu: The item close has no Material or ID!");
                this.getLog().error("OptionMenu: Use the default material 'NETHER_STAR'");
                this.closeData = 0;
                this.closeMaterial = Material.NETHER_STAR;
            } else if (this.menuConfig.getInt("close.id") == 0 || CoreMaterial.getMaterial(this.menuConfig.getInt("close.id")) == null) {
                this.getLog().error("OptionMenu: The item close has an invalid item ID: " + this.menuConfig.getInt("close.id") + ".");
                this.getLog().error("OptionMenu: Use the default material 'NETHER_STAR'");
                this.closeData = 0;
                this.closeMaterial = Material.NETHER_STAR;
            } else {
                this.getLog().alert("It is recommended to change the numeric id to a text value of the material. Check out the new settings here. https://pedrojm96.com/minecraft-plugin/deluxeplayeroptions/po-config-yml/  ");
                this.getLog().alert("The material id is not supported in 1.16+");
                this.closeData = (short)this.menuConfig.getInt("close.data");
                this.closeMaterial = CoreMaterial.getMaterial(this.menuConfig.getInt("close.id"));
            }
        } else {
            String mate_data = CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x) ? (this.menuConfig.isSet("close.material-old") ? this.menuConfig.getString("close.material-old") : this.menuConfig.getString("close.material")) : (this.menuConfig.isSet("close.material") ? this.menuConfig.getString("close.material") : this.menuConfig.getString("close.material-old"));
            if (Material.getMaterial((String)(mate_data.contains(":") ? mate_data.split(":")[0].trim().toUpperCase() : mate_data.toUpperCase())) == null) {
                this.getLog().error("OptionMenu: The item close has an invalid item Material: " + (mate_data.contains(":") ? mate_data.split(":")[0].trim().toUpperCase() : mate_data.toUpperCase()) + ".");
                this.getLog().error("OptionMenu: Use the default material 'NETHER_STAR'");
                this.closeData = 0;
                this.closeMaterial = Material.NETHER_STAR;
            } else {
                short data;
                String mate;
                if (mate_data.contains(":")) {
                    mate = mate_data.split(":")[0].trim().toUpperCase();
                    data = Short.valueOf(mate_data.split(":")[1].trim());
                } else {
                    mate = mate_data.toUpperCase();
                    data = (short)this.menuConfig.getInt("close.data");
                }
                this.closeData = data;
                this.closeMaterial = Material.getMaterial((String)mate);
            }
        }
    }

    private OptionMenu loadMenuOption(String nodo) {
        short data;
        String mate;
        OptionMenu menuoption = new OptionMenu();
        if (!this.menuConfig.isSet(String.valueOf(nodo) + ".material") && !this.menuConfig.isSet(String.valueOf(nodo) + ".material-old")) {
            this.getLog().alert("OptionMenu: The item " + nodo + " does not have a defined material value, using id instead.!");
            if (!this.menuConfig.isSet(String.valueOf(nodo) + ".id")) {
                this.getLog().error("OptionMenu: The item " + nodo + " has no Material or ID!");
                menuoption.setEnable(false);
                return menuoption;
            }
            if (this.menuConfig.getInt(String.valueOf(nodo) + ".id") == 0 || CoreMaterial.getMaterial(this.menuConfig.getInt(String.valueOf(nodo) + ".id")) == null) {
                this.getLog().error("OptionMenu: The item " + nodo + " has an invalid item ID: " + this.menuConfig.getInt(String.valueOf(nodo) + ".id") + ".");
                menuoption.setEnable(false);
                return menuoption;
            }
            this.getLog().alert("It is recommended to change the numeric id to a text value of the material. Check out the new settings here. https://pedrojm96.com/minecraft-plugin/deluxeplayeroptions/po-config-yml/  ");
            this.getLog().alert("The material id is not supported in 1.16+");
            menuoption.setEnable(this.menuConfig.getBoolean(String.valueOf(nodo) + ".enable"));
            menuoption.setName(this.menuConfig.getString(String.valueOf(nodo) + ".name"));
            menuoption.setMaterial(CoreMaterial.getMaterial(this.menuConfig.getInt(String.valueOf(nodo) + ".id")));
            menuoption.setData((short)this.menuConfig.getInt(String.valueOf(nodo) + ".data"));
            menuoption.setLore(this.menuConfig.getStringList(String.valueOf(nodo) + ".lore"));
            menuoption.setSlot(this.menuConfig.getInt(String.valueOf(nodo) + ".slot"));
            menuoption.setBottom_enable(this.menuConfig.getBoolean(String.valueOf(nodo) + ".itemuse-enable"));
            menuoption.setBottom_slot(this.menuConfig.getInt(String.valueOf(nodo) + ".itemuse-slot"));
            return menuoption;
        }
        String mate_data = CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x) ? (this.menuConfig.isSet(String.valueOf(nodo) + ".material-old") ? this.menuConfig.getString(String.valueOf(nodo) + ".material-old") : this.menuConfig.getString(String.valueOf(nodo) + ".material")) : (this.menuConfig.isSet(String.valueOf(nodo) + ".material") ? this.menuConfig.getString(String.valueOf(nodo) + ".material") : this.menuConfig.getString(String.valueOf(nodo) + ".material-old"));
        if (Material.getMaterial((String)(mate_data.contains(":") ? mate_data.split(":")[0].trim().toUpperCase() : mate_data.toUpperCase())) == null) {
            this.getLog().error("OptionMenu: The item " + nodo + " has an invalid item Material: " + (mate_data.contains(":") ? mate_data.split(":")[0].trim().toUpperCase() : mate_data.toUpperCase()) + ".");
            menuoption.setEnable(false);
            return menuoption;
        }
        if (mate_data.contains(":")) {
            mate = mate_data.split(":")[0].trim().toUpperCase();
            data = Short.valueOf(mate_data.split(":")[1].trim());
        } else {
            mate = mate_data.toUpperCase();
            data = (short)this.menuConfig.getInt(String.valueOf(nodo) + ".data");
        }
        menuoption.setEnable(this.menuConfig.getBoolean(String.valueOf(nodo) + ".enable"));
        menuoption.setName(this.menuConfig.getString(String.valueOf(nodo) + ".name"));
        menuoption.setMaterial(Material.getMaterial((String)mate));
        menuoption.setData(data);
        menuoption.setLore(this.menuConfig.getStringList(String.valueOf(nodo) + ".lore"));
        menuoption.setSlot(this.menuConfig.getInt(String.valueOf(nodo) + ".slot"));
        menuoption.setBottom_enable(this.menuConfig.getBoolean(String.valueOf(nodo) + ".itemuse-enable"));
        menuoption.setBottom_slot(this.menuConfig.getInt(String.valueOf(nodo) + ".itemuse-slot"));
        return menuoption;
    }

    private OptionToggleItem loadToggleItemOption(String nodo) {
        short data;
        String mate;
        OptionToggleItem toggleItemOption = new OptionToggleItem();
        if (!this.toggleItemsConfig.isSet(String.valueOf(nodo) + ".material") && !this.toggleItemsConfig.isSet(String.valueOf(nodo) + ".material-old")) {
            this.getLog().alert("ToggleItemOption: The item " + nodo + " does not have a defined material value, using id instead.!");
            if (!this.toggleItemsConfig.isSet(String.valueOf(nodo) + ".id")) {
                this.getLog().error("ToggleItemOption: The item " + nodo + " has no Material or ID!");
                toggleItemOption.setEnable(false);
                return toggleItemOption;
            }
            if (this.toggleItemsConfig.getInt(String.valueOf(nodo) + ".id") == 0 || CoreMaterial.getMaterial(this.toggleItemsConfig.getInt(String.valueOf(nodo) + ".id")) == null) {
                this.getLog().error("ToggleItemOption: The item " + nodo + " has an invalid item ID: " + this.toggleItemsConfig.getInt(String.valueOf(nodo) + ".id") + ".");
                toggleItemOption.setEnable(false);
                return toggleItemOption;
            }
            this.getLog().alert("It is recommended to change the numeric id to a text value of the material. Check out the new settings here. https://pedrojm96.com/minecraft-plugin/deluxeplayeroptions/po-toggleitems-yml/    ");
            this.getLog().alert("The material id is not supported in 1.16+");
            toggleItemOption.setEnable(this.toggleItemsConfig.getBoolean(String.valueOf(nodo) + ".enable"));
            toggleItemOption.setName(this.toggleItemsConfig.getString(String.valueOf(nodo) + ".name"));
            toggleItemOption.setMaterial(CoreMaterial.getMaterial(this.toggleItemsConfig.getInt(String.valueOf(nodo) + ".id")));
            toggleItemOption.setData((short)this.toggleItemsConfig.getInt(String.valueOf(nodo) + ".data"));
            toggleItemOption.setLore(this.toggleItemsConfig.getStringList(String.valueOf(nodo) + ".lore"));
            toggleItemOption.setSlot(this.toggleItemsConfig.getInt(String.valueOf(nodo) + ".slot"));
            return toggleItemOption;
        }
        String mate_data = CoreVersion.getVersion().esMenorIgual(CoreVersion.v1_12_x) ? (this.toggleItemsConfig.isSet(String.valueOf(nodo) + ".material-old") ? this.toggleItemsConfig.getString(String.valueOf(nodo) + ".material-old") : this.toggleItemsConfig.getString(String.valueOf(nodo) + ".material")) : (this.toggleItemsConfig.isSet(String.valueOf(nodo) + ".material") ? this.toggleItemsConfig.getString(String.valueOf(nodo) + ".material") : this.toggleItemsConfig.getString(String.valueOf(nodo) + ".material-old"));
        if (Material.getMaterial((String)(mate_data.contains(":") ? mate_data.split(":")[0].trim().toUpperCase() : mate_data.toUpperCase())) == null) {
            this.getLog().error("ToggleItemOption: The item " + nodo + " has an invalid item Material: " + (mate_data.contains(":") ? mate_data.split(":")[0].trim().toUpperCase() : mate_data.toUpperCase()) + ".");
            toggleItemOption.setEnable(false);
            return toggleItemOption;
        }
        if (mate_data.contains(":")) {
            mate = mate_data.split(":")[0].trim().toUpperCase();
            data = Short.valueOf(mate_data.split(":")[1].trim());
        } else {
            mate = mate_data.toUpperCase();
            data = (short)this.toggleItemsConfig.getInt(String.valueOf(nodo) + ".data");
        }
        toggleItemOption.setEnable(this.toggleItemsConfig.getBoolean(String.valueOf(nodo) + ".enable"));
        toggleItemOption.setName(this.toggleItemsConfig.getString(String.valueOf(nodo) + ".name"));
        toggleItemOption.setMaterial(Material.getMaterial((String)mate));
        toggleItemOption.setData(data);
        toggleItemOption.setLore(this.toggleItemsConfig.getStringList(String.valueOf(nodo) + ".lore"));
        toggleItemOption.setSlot(this.toggleItemsConfig.getInt(String.valueOf(nodo) + ".slot"));
        return toggleItemOption;
    }

    public void loadTogleItem() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            this.toggleItem.udateinv(p);
        }
    }

    public void checkForUpdates() {
        if (this.config.getBoolean("update-check")) {
            new BukkitRunnable(){

                public void run() {
                    CoreSpigotUpdater updater = new CoreSpigotUpdater(PlayerOptions.this.instance, 33033);
                    try {
                        if (updater.checkForUpdates()) {
                            PlayerOptions.this.log.alert("An update was found! for DeluxePlayerOptions. Please update to recieve latest version. download: " + updater.getResourceURL());
                        }
                    }
                    catch (Exception e) {
                        PlayerOptions.this.log.error("Failed to check for a update on spigot.");
                    }
                }
            }.runTask((Plugin)this.getInstance());
        }
    }

    public CoreLog getLog() {
        return this.log;
    }

    public JavaPlugin getInstance() {
        return this.instance;
    }

    @Override
    public void onLoad() {
    }
}


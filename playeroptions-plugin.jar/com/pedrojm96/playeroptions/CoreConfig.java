/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.CharStreams
 *  org.apache.commons.codec.binary.StringUtils
 *  org.apache.commons.lang.Validate
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.InvalidConfigurationException
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.pedrojm96.playeroptions;

import com.google.common.io.CharStreams;
import com.pedrojm96.playeroptions.CoreLog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CoreConfig {
    private FileConfiguration config;
    private File file;
    private String configFileName;
    private String header;
    private CoreLog log;

    public CoreConfig(JavaPlugin plugin, String configfile, CoreLog log, InputStream dafaultData, boolean update) {
        try {
            Validate.notNull((Object)plugin);
            Validate.notNull((Object)configfile);
            Validate.notNull((Object)log);
            Validate.notNull((Object)dafaultData);
        }
        catch (IllegalArgumentException e) {
            log.fatalError("Error on create Object CoreConfig");
            log.fatalError("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin((Plugin)plugin);
        }
        this.config = new YamlConfiguration();
        this.file = new File(plugin.getDataFolder(), String.valueOf(configfile) + ".yml");
        this.configFileName = configfile;
        this.log = log;
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        String inpu = this.getInputStreamToString(dafaultData);
        YamlConfiguration defaultConfig = new YamlConfiguration();
        byte[] bytes = StringUtils.getBytesUtf8((String)inpu);
        String utf8EncodedString = StringUtils.newStringUtf8((byte[])bytes);
        try {
            defaultConfig.loadFromString(utf8EncodedString);
        }
        catch (InvalidConfigurationException e) {
            log.fatalError("Error on loaded default config for " + this.configFileName + ".yml.");
            log.fatalError("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin((Plugin)plugin);
        }
        String[] inpuArray = inpu.split("\n");
        String header = "";
        int i = 0;
        while (i < inpuArray.length) {
            if (!inpuArray[i].trim().startsWith("#")) break;
            header = String.valueOf(header) + inpuArray[i] + "\n";
            ++i;
        }
        this.header = header;
        if (this.exists()) {
            this.load();
            if (update) {
                for (String nodo : defaultConfig.getKeys(true)) {
                    this.add(nodo, defaultConfig.get(nodo));
                }
                this.silenSave();
            }
        } else {
            for (String nodo : defaultConfig.getKeys(true)) {
                this.add(nodo, defaultConfig.get(nodo));
            }
            this.create();
        }
    }

    private String getInputStreamToString(InputStream dataIn) {
        String result = "";
        if (dataIn == null) {
            return result;
        }
        try {
            result = CharStreams.toString((Readable)new InputStreamReader(dataIn, Charset.forName("UTF-8")));
        }
        catch (IOException e) {
            this.log.fatalError("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
        return result;
    }

    private boolean exists() {
        return this.file.exists();
    }

    public String getYMLtoString() {
        return this.config.saveToString();
    }

    public void save() {
        try {
            String filestring = this.config.saveToString();
            String[] inpuArray = filestring.split("\n");
            String configStringLimpia = "";
            int i = 0;
            while (i < inpuArray.length) {
                if (!inpuArray[i].trim().startsWith("#")) {
                    configStringLimpia = String.valueOf(configStringLimpia) + "\n" + inpuArray[i];
                }
                ++i;
            }
            BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(this.file), Charset.forName("UTF-8")));
            fileWriter.write(String.valueOf(this.header) + configStringLimpia);
            ((Writer)fileWriter).close();
            this.log.alert(String.valueOf(this.configFileName) + ".yml  save.");
        }
        catch (IOException e) {
            this.log.fatalError("Error on save " + this.configFileName + ".yml.");
            this.log.fatalError("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
    }

    public void silenSave() {
        try {
            String filestring = this.config.saveToString();
            String[] inpuArray = filestring.split("\n");
            String configStringLimpia = "";
            int i = 0;
            while (i < inpuArray.length) {
                if (!inpuArray[i].trim().startsWith("#")) {
                    configStringLimpia = String.valueOf(configStringLimpia) + "\n" + inpuArray[i];
                }
                ++i;
            }
            BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(this.file), Charset.forName("UTF-8")));
            fileWriter.write(String.valueOf(this.header) + configStringLimpia);
            ((Writer)fileWriter).close();
        }
        catch (IOException e) {
            this.log.fatalError("Error on save " + this.configFileName + ".yml.");
            this.log.fatalError("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
    }

    public void load() {
        this.log.info("Load " + this.configFileName + ".yml");
        try {
            FileInputStream fileinputstream = new FileInputStream(this.file);
            this.config.load((Reader)new InputStreamReader((InputStream)fileinputstream, Charset.forName("UTF-8")));
            this.log.alert(String.valueOf(this.configFileName) + ".yml loaded.");
        }
        catch (IOException | InvalidConfigurationException e) {
            this.log.fatalError("Error on loaded " + this.configFileName + ".yml.");
            this.log.fatalError("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
    }

    public CoreConfig loadFromString(String text) {
        try {
            this.config.loadFromString(text);
            this.log.alert("String to " + this.configFileName + " loaded.");
            return this;
        }
        catch (InvalidConfigurationException e) {
            this.log.fatalError("Error on loaded Master " + this.configFileName);
            this.log.fatalError("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
            return null;
        }
    }

    private void create() {
        this.log.alert("The " + this.configFileName + ".yml file does not exist yet.");
        this.log.info("Creating and loading file " + this.configFileName + ".yml.");
        try {
            String filestring = this.config.saveToString();
            String[] inpuArray = filestring.split("\n");
            String configStringLimpia = "";
            int i = 0;
            while (i < inpuArray.length) {
                if (!inpuArray[i].trim().startsWith("#")) {
                    configStringLimpia = String.valueOf(configStringLimpia) + "\n" + inpuArray[i];
                }
                ++i;
            }
            BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(this.file), Charset.forName("UTF-8")));
            fileWriter.write(String.valueOf(this.header) + configStringLimpia);
            ((Writer)fileWriter).close();
            this.log.alert(String.valueOf(this.configFileName) + ".yml  create.");
        }
        catch (IOException e) {
            this.log.fatalError("Error on create " + this.configFileName + ".yml.");
            this.log.fatalError("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
    }

    public void add(String path, String value) {
        if (!this.config.isSet(path)) {
            this.config.set(path, (Object)value);
        }
    }

    public void add(String path, long value) {
        if (!this.config.isSet(path)) {
            this.config.set(path, (Object)value);
        }
    }

    public void add(String path, boolean value) {
        if (!this.config.isSet(path)) {
            this.config.set(path, (Object)value);
        }
    }

    public void add(String path, List<String> value) {
        if (!this.config.isSet(path)) {
            this.config.set(path, value);
        }
    }

    public void add(String path, int value) {
        if (!this.config.isSet(path)) {
            this.config.set(path, (Object)value);
        }
    }

    public void add(String path, double value) {
        if (!this.config.isSet(path)) {
            this.config.set(path, (Object)value);
        }
    }

    public void add(String path, Object value) {
        if (!this.config.isSet(path)) {
            this.config.set(path, value);
        }
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public long getLong(String path) {
        return this.config.getLong(path);
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public Double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public float getFloat(String path) {
        return (float)this.config.getDouble(path);
    }

    public Set<String> getKeys(Boolean bo) {
        return this.config.getKeys(bo.booleanValue());
    }

    public void set(String path, String value) {
        this.config.set(path, (Object)value);
    }

    public void setNull(String path) {
        this.config.set(path, null);
    }

    public void set(String path, double value) {
        this.config.set(path, (Object)value);
    }

    public void set(String path, float value) {
        this.config.set(path, (Object)Float.valueOf(value));
    }

    public void set(String path, long value) {
        this.config.set(path, (Object)value);
    }

    public void set(String path, List<String> value) {
        this.config.set(path, value);
    }

    public void set(String path, int value) {
        this.config.set(path, (Object)value);
    }

    public void set(String path, boolean value) {
        this.config.set(path, (Object)value);
    }

    public boolean isSet(String path) {
        return this.config.isSet(path);
    }

    public boolean isList(String path) {
        return this.config.isList(path);
    }

    public boolean contains(String path) {
        return this.config.contains(path);
    }

    public void header(String h) {
        this.config.options().header(h);
    }
}


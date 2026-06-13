/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.reflect.TypeToken
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.pedrojm96.playeroptions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.pedrojm96.playeroptions.CoreColor;
import com.pedrojm96.playeroptions.CoreLog;
import com.pedrojm96.playeroptions.CoreMaterial;
import com.pedrojm96.playeroptions.CorePlugin;
import com.pedrojm96.playeroptions.CoreReflection;
import com.pedrojm96.playeroptions.CoreVariables;
import com.pedrojm96.playeroptions.CoreVersion;
import com.pedrojm96.playeroptions.UUIDFetcher;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CoreUtils {
    public static Map<String, String[]> playerTexturesCache = new HashMap<String, String[]>();

    public static String[] getPlayerTextureLocal(Player playerBukkit, CoreLog log) {
        String[] a = null;
        Class<?> strClass = CoreReflection.getCraftClass("entity.CraftPlayer");
        try {
            GameProfile profile = (GameProfile)strClass.cast(playerBukkit).getClass().getMethod("getProfile", new Class[0]).invoke(strClass.cast(playerBukkit), new Object[0]);
            Property colle = (Property)profile.getProperties().get((Object)"textures").iterator().next();
            String texture = CoreUtils.getPropertyValue(colle);
            String signature = colle.getSignature();
            a = new String[]{texture, signature};
            log.debug("Textura obtenida localmente para: " + playerBukkit.getName());
        }
        catch (Exception e) {
            log.debug("Error al intentar obtener texturas localmente para " + playerBukkit.getName(), e);
        }
        return a;
    }

    public static String getPropertyValue(Property property) {
        if (CoreVersion.getVersion().esMayorIgual(CoreVersion.v1_20_2)) {
            try {
                Object value = property.getClass().getMethod("value", new Class[0]).invoke(property, new Object[0]);
                return (String)value;
            }
            catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        return property.getValue();
    }

    public static String[] getTextureFromMojang(String name, CoreLog log) {
        try {
            if (playerTexturesCache.containsKey(name)) {
                log.debug("Textura obtenida desde el cache de mojan para: " + name);
                return playerTexturesCache.get(name);
            }
            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            String uuid = new JsonParser().parse((Reader)reader_0).getAsJsonObject().get("id").getAsString();
            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject textureProperty = new JsonParser().parse((Reader)reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();
            log.debug("Textura obtenida desde la web de mojan para: " + name);
            String[] playerTexture = new String[]{texture, signature};
            playerTexturesCache.put(name, playerTexture);
            return playerTexture;
        }
        catch (IOException | IllegalStateException e) {
            log.debug("Error al intentar obtener texturas desde la web mojan para " + name, e);
            return null;
        }
    }

    public static boolean isdouble(String s) {
        try {
            double i = Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException er) {
            return false;
        }
    }

    public static boolean isint(String s) {
        try {
            int i = Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException er) {
            return false;
        }
    }

    public static int toint(String s) {
        try {
            int i = Integer.parseInt(s);
            return i;
        }
        catch (NumberFormatException er) {
            return 0;
        }
    }

    public static int toint(String s, int error) {
        try {
            int i = Integer.parseInt(s);
            return i;
        }
        catch (NumberFormatException er) {
            return error;
        }
    }

    public static long toLong(String s) {
        try {
            long i = Long.parseLong(s);
            return i;
        }
        catch (NumberFormatException er) {
            return 0L;
        }
    }

    public static boolean isEnum(Class<?> class1, String value) {
        try {
            Object obj = Enum.valueOf(class1, value);
            return true;
        }
        catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean isinteger(String s) {
        try {
            Integer i = Integer.valueOf(s);
            return true;
        }
        catch (NumberFormatException er) {
            return false;
        }
    }

    public static Integer integerValue(String s) {
        try {
            Integer i = Integer.valueOf(s);
            return i;
        }
        catch (NumberFormatException er) {
            return 0;
        }
    }

    public static Double doubleValue(String s) {
        try {
            Double i = Double.valueOf(s);
            return i;
        }
        catch (NumberFormatException er) {
            return 0.0;
        }
    }

    public static String formatime(long segundos) {
        String format = "<dd>d:<hh>h:<mm>m:<ss>s";
        int d = 0;
        int h = 0;
        int m = 0;
        while (segundos > 60L) {
            if (h > 12) {
                ++d;
                h = 0;
            }
            if (m > 60) {
                ++h;
                m = 0;
            }
            segundos -= 60L;
            ++m;
        }
        if (d == 0 && h == 0 && m == 0) {
            String forma = format.replaceAll("<dd>", String.valueOf(d)).replaceAll("<hh>", String.valueOf(h)).replaceAll("<mm>", String.valueOf(m)).replaceAll("<ss>", String.valueOf(segundos));
            return forma;
        }
        if (d == 0 && h == 0) {
            String forma = format.replaceAll("<dd>", String.valueOf(d)).replaceAll("<hh>", String.valueOf(h)).replaceAll("<mm>", String.valueOf(m)).replaceAll("<ss>", String.valueOf(segundos));
            return forma;
        }
        if (d == 0) {
            String forma = format.replaceAll("<dd>", String.valueOf(d)).replaceAll("<hh>", String.valueOf(h)).replaceAll("<mm>", String.valueOf(m)).replaceAll("<ss>", String.valueOf(segundos));
            return forma;
        }
        String forma = format.replaceAll("<dd>", String.valueOf(d)).replaceAll("<hh>", String.valueOf(h)).replaceAll("<mm>", String.valueOf(m)).replaceAll("<ss>", String.valueOf(segundos));
        return forma;
    }

    public static String formatimeClear(long segundos) {
        int d = 0;
        int h = 0;
        int m = 0;
        while (segundos > 60L) {
            if (h > 12) {
                ++d;
                h = 0;
            }
            if (m > 60) {
                ++h;
                m = 0;
            }
            segundos -= 60L;
            ++m;
        }
        if (d == 0 && h == 0 && m == 0) {
            String forma = String.valueOf(segundos) + "s";
            return forma;
        }
        if (d == 0 && h == 0) {
            String forma = String.valueOf(m) + "m " + segundos + "s";
            return forma;
        }
        if (d == 0) {
            String forma = String.valueOf(h) + "h " + m + "m " + segundos + "s";
            return forma;
        }
        String forma = String.valueOf(d) + "d " + h + "h " + m + "m " + segundos + "s";
        return forma;
    }

    public static String timeLeft(int timeoutSeconds) {
        int days = timeoutSeconds / 86400;
        int hours = timeoutSeconds / 3600 % 24;
        int minutes = timeoutSeconds / 60 % 60;
        int seconds = timeoutSeconds % 60;
        return String.valueOf(days > 0 ? " " + days + " d" : "") + (hours > 0 ? " " + hours + " h" : "") + (minutes > 0 ? " " + minutes + " m" : "") + (seconds > 0 ? " " + seconds + " s" : "");
    }

    public static ArrayList<Location> getCircleLocation(Location center, double radius, int amount) {
        World world = center.getWorld();
        double increment = Math.PI * 2 / (double)amount;
        ArrayList<Location> locations = new ArrayList<Location>();
        int i = 0;
        while (i < amount) {
            double angle = (double)i * increment;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            locations.add(new Location(world, x, center.getY(), z));
            ++i;
        }
        return locations;
    }

    public static List<String> jsonArrayStringToList(String json) {
        JsonArray jsonArry = new JsonParser().parse(json).getAsJsonArray();
        Type listType = new TypeToken<List<String>>(){}.getType();
        List yourList = (List)new Gson().fromJson((JsonElement)jsonArry, listType);
        return yourList;
    }

    public static String listToJsonArrayString(List<String> list) {
        JsonArray ss = new JsonArray();
        for (String s : list) {
            ss.add((JsonElement)new JsonPrimitive(s));
        }
        return ss.toString();
    }

    public static ItemStack createItem(Player player, String name, List<String> lore, int id, int shrt) {
        ItemStack item = new ItemStack(CoreMaterial.getMaterial(id), 1, (short)shrt);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(CoreVariables.replace(name, player));
        itemMeta.setLore(CoreColor.rColorList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createItem(String name, List<String> lore, int id, int shrt) {
        ItemStack item = new ItemStack(CoreMaterial.getMaterial(id), 1, (short)shrt);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(CoreColor.colorCodes(name));
        itemMeta.setLore(CoreColor.rColorList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createItem(String name, List<String> lore, Material mate, int shrt) {
        ItemStack item = new ItemStack(mate, 1, (short)shrt);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(CoreColor.colorCodes(name));
        itemMeta.setLore(CoreColor.rColorList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createItem(Player player, String name, List<String> lore, Material mate, int shrt) {
        ItemStack item = new ItemStack(mate, 1, (short)shrt);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(CoreColor.colorCodes(name));
        itemMeta.setLore(CoreColor.rColorList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createItem(String name, int mate, int shrt) {
        ItemStack item = new ItemStack(CoreMaterial.getMaterial(mate), 1, (short)shrt);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(CoreColor.colorCodes(name));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static UUID translateNameToUUID(String name, CorePlugin plugin) {
        UUID id = null;
        plugin.getLog().debug("translateNameToUUID(" + name + ")");
        if (name == null) {
            plugin.getLog().debug("translateNameToUUID() - bad ID");
            return id;
        }
        plugin.getLog().debug("translateNameToUUID() - Looking through online players: " + Bukkit.getServer().getOnlinePlayers().size());
        Collection players = Bukkit.getServer().getOnlinePlayers();
        for (Player p : players) {
            if (!p.getName().equalsIgnoreCase(name)) continue;
            id = p.getUniqueId();
            plugin.getLog().debug("translateNameToUUID() online player UUID found: " + id.toString());
            break;
        }
        if (id == null && Bukkit.getServer().getOnlineMode()) {
            plugin.getLog().debug("translateNameToUUID() - Attempting online lookup");
            UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(name));
            try {
                Object map = fetcher.call();
                for (Map.Entry entry : map.entrySet()) {
                    if (!name.equalsIgnoreCase((String)entry.getKey())) continue;
                    id = (UUID)entry.getValue();
                    plugin.getLog().debug("translateNameToUUID() web player UUID found: " + (id == null ? id : id.toString()));
                    break;
                }
            }
            catch (Exception e) {
                plugin.getLog().error("Exception on online UUID fetch", e);
            }
        } else if (id == null && !Bukkit.getServer().getOnlineMode()) {
            id = Bukkit.getServer().getOfflinePlayer(name).getUniqueId();
            plugin.getLog().debug("translateNameToUUID() offline player UUID found: " + (id == null ? id : id.toString()));
        }
        return id;
    }

    public void setItemInHand(ItemStack item, Player player) {
        if (CoreVersion.getVersion().esMayorIgual(CoreVersion.v1_9)) {
            this.setItemInHand_1_9(item, player);
        } else {
            this.setItemInHand_1_8(item, player);
        }
    }

    public ItemStack getItemInHand(Player player) {
        if (CoreVersion.getVersion().esMayorIgual(CoreVersion.v1_9)) {
            return this.getItemInHand_1_9(player);
        }
        return this.getItemInHand_1_8(player);
    }

    public void setItemInHand_1_8(ItemStack item, Player player) {
        try {
            player.getInventory().getClass().getMethod("setItemInHand", item.getClass()).invoke(player.getInventory(), item);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setItemInHand_1_9(ItemStack item, Player player) {
        try {
            player.getInventory().getClass().getMethod("setItemInMainHand", item.getClass()).invoke(player.getInventory(), item);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ItemStack getItemInHand_1_8(Player player) {
        try {
            Object item = player.getInventory().getClass().getMethod("getItemInHand", new Class[0]).invoke(player.getInventory(), new Object[0]);
            return (ItemStack)item;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ItemStack getItemInHand_1_9(Player player) {
        try {
            Object item = player.getInventory().getClass().getMethod("getItemInMainHand", new Class[0]).invoke(player.getInventory(), new Object[0]);
            return (ItemStack)item;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTitleInventory_1_21(InventoryEvent event) {
        return CoreReflection.getTitleInventory(event);
    }

    public static String getTitleInventory_1_20_6(InventoryEvent event) {
        return event.getView().getTitle();
    }

    public static String getTitleInventory(InventoryEvent event) {
        if (CoreVersion.getVersion().esMayorIgual(CoreVersion.v1_21)) {
            return CoreUtils.getTitleInventory_1_21(event);
        }
        return CoreUtils.getTitleInventory_1_20_6(event);
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryEvent
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.potion.PotionEffectType
 */
package com.pedrojm96.playeroptions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.potion.PotionEffectType;

public class CoreReflection {
    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return CoreReflection.getClass("net.minecraft.server." + version + "." + name);
    }

    public static Class<?> getNMSClassArray(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return CoreReflection.getClass("[Lnet.minecraft.server." + version + "." + name + ";");
    }

    public static Class<?> getCraftClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return CoreReflection.getClass("org.bukkit.craftbukkit." + version + "." + name);
    }

    public static Class<?> getBukkitClass(String name) {
        return CoreReflection.getClass("org.bukkit." + name);
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", CoreReflection.getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
    }

    public static void sendPacketPos_1_17_Pre_1_18(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("b").get(handle);
            Object networkManager = playerConnection.getClass().getField("a").get(playerConnection);
            networkManager.getClass().getMethod("sendPacket", CoreReflection.getClass("net.minecraft.network.protocol.Packet")).invoke(networkManager, packet);
        }
        catch (Exception e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
    }

    public static void sendPacketPos_1_18_Pre_1_19(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("b").get(handle);
            Object networkManager = playerConnection.getClass().getField("a").get(playerConnection);
            networkManager.getClass().getMethod("a", CoreReflection.getClass("net.minecraft.network.protocol.Packet")).invoke(networkManager, packet);
        }
        catch (Exception e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
    }

    public static void sendPacketPos_1_19(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("b").get(handle);
            playerConnection.getClass().getMethod("a", CoreReflection.getClass("net.minecraft.network.protocol.Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
    }

    public static void sendPacketPos_1_20(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("c").get(handle);
            playerConnection.getClass().getMethod("a", CoreReflection.getClass("net.minecraft.network.protocol.Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
    }

    public static Object getStaticField(Class<?> c, String name) {
        Object retorno = null;
        Field f = null;
        try {
            f = c.getDeclaredField(name);
        }
        catch (NoSuchFieldException e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
        catch (SecurityException e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
        if (f == null) {
            throw new IllegalArgumentException("Error while getting the field '" + name + "'");
        }
        f.setAccessible(true);
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(f, modifiersField.getInt(f) - 16);
        }
        catch (SecurityException e) {
            System.out.println("A security manager may be preventing you from setting this field.");
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
        catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
        try {
            retorno = f.get(null);
        }
        catch (IllegalAccessException | IllegalArgumentException e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
        return retorno;
    }

    public static Object getFieldValue(Object obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static void setFieldValue(Object obj, String name, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void setStaticField(Class<?> c, String name, Object set) {
        Field f = null;
        try {
            f = c.getDeclaredField(name);
        }
        catch (NoSuchFieldException e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
        catch (SecurityException e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
        if (f == null) {
            throw new IllegalArgumentException("Error while getting the field '" + name + "'");
        }
        f.setAccessible(true);
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(f, modifiersField.getInt(f) - 16);
        }
        catch (SecurityException e) {
            System.out.println("A security manager may be preventing you from setting this field.");
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
        }
        catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            f.set(null, set);
        }
        catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static Enum<?> getEnum(String enumFullName) {
        String[] x = enumFullName.split("\\.(?=[^\\.]+$)");
        if (x.length == 2) {
            String enumClassName = x[0];
            String enumName = x[1];
            Class<?> cl = CoreReflection.getClass(enumClassName);
            return Enum.valueOf(cl, enumName);
        }
        return null;
    }

    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Please report the bug at: https://github.com/PedroJM96/CoreLibBukkit");
            e.printStackTrace();
            return null;
        }
    }

    public static String getTitleInventory(InventoryEvent event) {
        try {
            InventoryView view = event.getView();
            Method getTitle = view.getClass().getMethod("getTitle", new Class[0]);
            getTitle.setAccessible(true);
            return (String)getTitle.invoke(view, new Object[0]);
        }
        catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static PotionEffectType getPotionEffect(String name) {
        Class<?> clase = CoreReflection.getBukkitClass("potion.PotionEffectType");
        PotionEffectType type = (PotionEffectType)CoreReflection.getStaticField(clase, name);
        return type;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.libraryloader;

import com.pedrojm96.playeroptions.CoreLog;
import com.pedrojm96.playeroptions.libraryloader.CoreLoader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;

public class CoreClassLoader
extends URLClassLoader {
    private static CoreLog log;

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public CoreClassLoader(ClassLoader loaderClassLoader, String jarResourcePath, CoreLog paramlog) {
        super(new URL[]{CoreClassLoader.extractJar(loaderClassLoader, jarResourcePath, paramlog)}, loaderClassLoader);
    }

    public void addJarToClasspath(URL url) {
        this.addURL(url);
    }

    public void deleteJarResource() {
        URL[] urls = this.getURLs();
        if (urls.length == 0) {
            return;
        }
        try {
            Path path = Paths.get(urls[0].toURI());
            Files.deleteIfExists(path);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public <T> CoreLoader instantiatePlugin(String bootstrapClass, Class<T> loaderPluginType, T loaderPlugin) {
        Constructor<CoreLoader> constructor;
        Class<CoreLoader> plugin;
        try {
            plugin = this.loadClass(bootstrapClass).asSubclass(CoreLoader.class);
        }
        catch (ReflectiveOperationException e) {
            log.error("Unable to load the plugin bootstrap class", e);
            return null;
        }
        try {
            constructor = plugin.getConstructor(loaderPluginType);
        }
        catch (ReflectiveOperationException e) {
            log.error("Unable to get the plugin bootstrap constructor", e);
            return null;
        }
        try {
            return constructor.newInstance(loaderPlugin);
        }
        catch (ReflectiveOperationException e) {
            log.error("Unable to create bootstrap plugin instance", e);
            return null;
        }
    }

    private static URL extractJar(ClassLoader loaderClassLoader, String jarResourcePath, CoreLog paramlog) {
        Path path;
        log = paramlog;
        URL jarInJar = loaderClassLoader.getResource(jarResourcePath);
        if (jarInJar == null) {
            log.error("Could not locate the plugin bootstrap files");
            return null;
        }
        try {
            path = Files.createTempFile("coreplugin-jarinjar", ".jar.tmp", new FileAttribute[0]);
        }
        catch (IOException e) {
            log.error("Unable to create a temporary file to plugin bootstrap", e);
            return null;
        }
        path.toFile().deleteOnExit();
        try {
            Throwable e = null;
            Object var6_10 = null;
            try (InputStream in = jarInJar.openStream();){
                Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (Throwable throwable) {
                if (e == null) {
                    e = throwable;
                } else if (e != throwable) {
                    e.addSuppressed(throwable);
                }
                throw e;
            }
        }
        catch (IOException e) {
            log.error("Unable to copy the plugin bootstrap files to temporary path", e);
            return null;
        }
        try {
            return path.toUri().toURL();
        }
        catch (MalformedURLException e) {
            log.error("Unable to get URL of the plugin bootstrap from path", e);
            return null;
        }
    }
}


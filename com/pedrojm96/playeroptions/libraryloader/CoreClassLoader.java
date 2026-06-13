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

    public CoreClassLoader(ClassLoader classLoader, String string, CoreLog coreLog) {
        super(new URL[]{CoreClassLoader.extractJar(classLoader, string, coreLog)}, classLoader);
    }

    public void addJarToClasspath(URL uRL) {
        this.addURL(uRL);
    }

    public void deleteJarResource() {
        URL[] uRLArray = this.getURLs();
        if (uRLArray.length == 0) {
            return;
        }
        try {
            Path path = Paths.get(uRLArray[0].toURI());
            Files.deleteIfExists(path);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public <T> CoreLoader instantiatePlugin(String string, Class<T> clazz, T t) {
        Constructor<CoreLoader> constructor;
        Class<CoreLoader> clazz2;
        try {
            clazz2 = this.loadClass(string).asSubclass(CoreLoader.class);
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            log.error("Unable to load the plugin bootstrap class", reflectiveOperationException);
            return null;
        }
        try {
            constructor = clazz2.getConstructor(clazz);
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            log.error("Unable to get the plugin bootstrap constructor", reflectiveOperationException);
            return null;
        }
        try {
            return constructor.newInstance(t);
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            log.error("Unable to create bootstrap plugin instance", reflectiveOperationException);
            return null;
        }
    }

    private static URL extractJar(ClassLoader classLoader, String string, CoreLog coreLog) {
        Path path;
        log = coreLog;
        URL uRL = classLoader.getResource(string);
        if (uRL == null) {
            log.error("Could not locate the plugin bootstrap files");
            return null;
        }
        try {
            path = Files.createTempFile("coreplugin-jarinjar", ".jar.tmp", new FileAttribute[0]);
        }
        catch (IOException iOException) {
            log.error("Unable to create a temporary file to plugin bootstrap", iOException);
            return null;
        }
        path.toFile().deleteOnExit();
        try {
            Throwable throwable = null;
            Object var6_10 = null;
            try (InputStream inputStream = uRL.openStream();){
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (Throwable throwable2) {
                if (throwable == null) {
                    throwable = throwable2;
                } else if (throwable != throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
        }
        catch (IOException iOException) {
            log.error("Unable to copy the plugin bootstrap files to temporary path", iOException);
            return null;
        }
        try {
            return path.toUri().toURL();
        }
        catch (MalformedURLException malformedURLException) {
            log.error("Unable to get URL of the plugin bootstrap from path", malformedURLException);
            return null;
        }
    }
}


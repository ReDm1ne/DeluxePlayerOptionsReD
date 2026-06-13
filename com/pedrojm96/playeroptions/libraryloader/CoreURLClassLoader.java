/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.libraryloader;

import com.pedrojm96.playeroptions.libraryloader.CoreClassLoader;
import com.pedrojm96.playeroptions.libraryloader.CoreURLClassLoaderHelper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public class CoreURLClassLoader
implements CoreURLClassLoaderHelper {
    private final CoreClassLoader classLoader;

    public CoreURLClassLoader(ClassLoader classLoader) {
        if (!(classLoader instanceof CoreClassLoader)) {
            throw new IllegalArgumentException("Loader is not a CoreClassLoader: " + classLoader.getClass().getName());
        }
        this.classLoader = (CoreClassLoader)classLoader;
    }

    @Override
    public void addJarToClasspath(Path path) {
        try {
            this.classLoader.addJarToClasspath(path.toUri().toURL());
        }
        catch (MalformedURLException malformedURLException) {
            throw new RuntimeException(malformedURLException);
        }
    }

    @Override
    public void close() {
        this.classLoader.deleteJarResource();
        try {
            this.classLoader.close();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
}


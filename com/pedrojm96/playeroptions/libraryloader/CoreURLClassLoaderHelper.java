/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.libraryloader;

import java.nio.file.Path;

public interface CoreURLClassLoaderHelper
extends AutoCloseable {
    public void addJarToClasspath(Path var1);

    @Override
    default public void close() {
    }
}


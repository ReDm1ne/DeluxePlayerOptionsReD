/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.libraryloader;

import com.pedrojm96.playeroptions.CoreLog;
import com.pedrojm96.playeroptions.CorePlugin;
import com.pedrojm96.playeroptions.libraryloader.CoreURLClassLoaderHelper;
import com.pedrojm96.playeroptions.libraryloader.MavenArtifact;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Objects;

public class LibraryLoader {
    public static final String HTTP_USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
    private CoreLog log;
    private Path saveDirectory;
    private CoreURLClassLoaderHelper classLoader;

    public LibraryLoader(CoreURLClassLoaderHelper coreURLClassLoaderHelper, CoreLog coreLog, CorePlugin corePlugin) {
        this(coreURLClassLoaderHelper, coreLog, corePlugin.getInstance().getDataFolder().toPath());
    }

    public LibraryLoader(CoreURLClassLoaderHelper coreURLClassLoaderHelper, CoreLog coreLog, Path path) {
        this.log = coreLog;
        this.classLoader = coreURLClassLoaderHelper;
        this.saveDirectory = Objects.requireNonNull(path, "dataDirectory").toAbsolutePath().resolve("libs");
    }

    public void loadLib(String string, String string2, String string3) {
        this.loadLib(string, string2, string3, "https://repo1.maven.org/maven2");
    }

    public void loadLib(String string, String string2, String string3, String string4) {
        this.loadLib(new MavenArtifact(string, string2, string3, string4));
    }

    public void loadLib(MavenArtifact mavenArtifact) {
        String string = String.valueOf(mavenArtifact.getGroupId()) + "-" + mavenArtifact.getArtifactId() + "-" + mavenArtifact.getVersion();
        Path path = this.downloadLibrary(mavenArtifact, string);
        this.classLoader.addJarToClasspath(path);
        this.log.info(String.valueOf(string) + " library loaded successfully.");
    }

    public Path downloadLibrary(MavenArtifact mavenArtifact, String string) {
        Path path = this.saveDirectory.resolve(Objects.requireNonNull(mavenArtifact, "library").getPath());
        if (Files.exists(path, new LinkOption[0])) {
            this.log.info("Loading library " + string);
            return path;
        }
        Path path2 = path.resolveSibling(path.getFileName() + ".tmp");
        path2.toFile().deleteOnExit();
        try {
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
            byte[] byArray = this.downloadLibrary(mavenArtifact.getUrl(), string);
            if (byArray == null) {
                throw new RuntimeException("Failed to download library '" + string + "'");
            }
            Files.write(path2, byArray, new OpenOption[0]);
            Files.move(path2, path, new CopyOption[0]);
            Path path3 = path;
            return path3;
        }
        catch (IOException iOException) {
            throw new UncheckedIOException(iOException);
        }
        finally {
            try {
                Files.deleteIfExists(path2);
            }
            catch (IOException iOException) {}
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private byte[] downloadLibrary(URL uRL, String string) {
        try {
            URLConnection uRLConnection = uRL.openConnection();
            uRLConnection.setConnectTimeout(5000);
            uRLConnection.setReadTimeout(5000);
            uRLConnection.setRequestProperty("User-Agent", HTTP_USER_AGENT);
            this.log.info("Downloading librarie " + string);
            Throwable throwable = null;
            Object var5_8 = null;
            try (InputStream inputStream = uRLConnection.getInputStream();){
                byte[] byArray = new byte[8192];
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    int n;
                    while ((n = inputStream.read(byArray)) != -1) {
                        byteArrayOutputStream.write(byArray, 0, n);
                    }
                }
                catch (SocketTimeoutException socketTimeoutException) {
                    this.log.error("Download timed out: " + uRLConnection.getURL());
                    if (inputStream == null) return null;
                    inputStream.close();
                    return null;
                }
                this.log.info("Librarie '" + string + "' successfully downloaded.");
                return byteArrayOutputStream.toByteArray();
            }
            catch (Throwable throwable2) {
                if (throwable == null) {
                    throwable = throwable2;
                    throw throwable;
                }
                if (throwable == throwable2) throw throwable;
                throwable.addSuppressed(throwable2);
                throw throwable;
            }
        }
        catch (MalformedURLException malformedURLException) {
            throw new IllegalArgumentException(malformedURLException);
        }
        catch (IOException iOException) {
            if (iOException instanceof FileNotFoundException) {
                this.log.error("File not found: " + uRL);
                return null;
            }
            if (iOException instanceof SocketTimeoutException) {
                this.log.error("Connect timed out: " + uRL);
                return null;
            }
            if (iOException instanceof UnknownHostException) {
                this.log.error("Unknown host: " + uRL);
                return null;
            }
            this.log.error("Unexpected IOException", iOException);
            return null;
        }
    }
}


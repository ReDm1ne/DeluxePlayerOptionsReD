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

    public LibraryLoader(CoreURLClassLoaderHelper classLoader, CoreLog log, CorePlugin plugin) {
        this(classLoader, log, plugin.getInstance().getDataFolder().toPath());
    }

    public LibraryLoader(CoreURLClassLoaderHelper classLoader, CoreLog log, Path dataDirectory) {
        this.log = log;
        this.classLoader = classLoader;
        this.saveDirectory = Objects.requireNonNull(dataDirectory, "dataDirectory").toAbsolutePath().resolve("libs");
    }

    public void loadLib(String groupId, String artifactId, String version) throws IOException {
        this.loadLib(groupId, artifactId, version, "https://repo1.maven.org/maven2");
    }

    public void loadLib(String groupId, String artifactId, String version, String url) throws IOException {
        this.loadLib(new MavenArtifact(groupId, artifactId, version, url));
    }

    public void loadLib(MavenArtifact maven) throws IOException {
        String name = String.valueOf(maven.getGroupId()) + "-" + maven.getArtifactId() + "-" + maven.getVersion();
        Path file = this.downloadLibrary(maven, name);
        this.classLoader.addJarToClasspath(file);
        this.log.info(String.valueOf(name) + " library loaded successfully.");
    }

    public Path downloadLibrary(MavenArtifact library, String name) {
        Path file = this.saveDirectory.resolve(Objects.requireNonNull(library, "library").getPath());
        if (Files.exists(file, new LinkOption[0])) {
            this.log.info("Loading library " + name);
            return file;
        }
        Path out = file.resolveSibling(file.getFileName() + ".tmp");
        out.toFile().deleteOnExit();
        try {
            Files.createDirectories(file.getParent(), new FileAttribute[0]);
            byte[] bytes = this.downloadLibrary(library.getUrl(), name);
            if (bytes == null) {
                throw new RuntimeException("Failed to download library '" + name + "'");
            }
            Files.write(out, bytes, new OpenOption[0]);
            Files.move(out, file, new CopyOption[0]);
            Path path = file;
            return path;
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        finally {
            try {
                Files.deleteIfExists(out);
            }
            catch (IOException iOException) {}
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private byte[] downloadLibrary(URL url, String name) {
        try {
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", HTTP_USER_AGENT);
            this.log.info("Downloading librarie " + name);
            Throwable throwable = null;
            Object var5_8 = null;
            try (InputStream in = connection.getInputStream();){
                byte[] buf = new byte[8192];
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    int len;
                    while ((len = in.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                }
                catch (SocketTimeoutException e) {
                    this.log.error("Download timed out: " + connection.getURL());
                    if (in == null) return null;
                    in.close();
                    return null;
                }
                this.log.info("Librarie '" + name + "' successfully downloaded.");
                return out.toByteArray();
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
        catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
        catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                this.log.error("File not found: " + url);
                return null;
            }
            if (e instanceof SocketTimeoutException) {
                this.log.error("Connect timed out: " + url);
                return null;
            }
            if (e instanceof UnknownHostException) {
                this.log.error("Unknown host: " + url);
                return null;
            }
            this.log.error("Unexpected IOException", e);
            return null;
        }
    }
}


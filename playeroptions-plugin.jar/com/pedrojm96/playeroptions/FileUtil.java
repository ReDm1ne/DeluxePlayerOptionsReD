/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
 *  org.bukkit.Bukkit
 */
package com.pedrojm96.playeroptions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;

public class FileUtil {
    public static void extractFolder(String zipFile, String extractFolder) {
        try {
            int BUFFER = 2048;
            File file = new File(zipFile);
            ZipFile zip = new ZipFile(file);
            String newPath = extractFolder;
            new File(newPath).mkdir();
            Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();
            while (zipFileEntries.hasMoreElements()) {
                int currentByte;
                ZipEntry entry = zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                File destFile = new File(newPath, currentEntry);
                File destinationParent = destFile.getParentFile();
                destinationParent.mkdirs();
                if (entry.isDirectory()) continue;
                BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                byte[] data = new byte[BUFFER];
                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }
        }
        catch (Exception e) {
            Bukkit.getLogger().severe("ERROR: " + e.getMessage());
        }
    }

    public static void copy(InputStream in, File file) {
        try {
            int len;
            FileOutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) > 0) {
                ((OutputStream)out).write(buf, 0, len);
            }
            ((OutputStream)out).close();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zipFolder(File folder, File zipFile) throws IOException {
        FileUtil.zipFolder(folder, new FileOutputStream(zipFile));
    }

    public static void zipFolder(File folder, OutputStream outputStream) throws IOException {
        Throwable throwable = null;
        Object var3_4 = null;
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);){
            FileUtil.processFolder(folder, zipOutputStream, folder.getPath().length() + 1);
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

    private static void processFolder(File folder, ZipOutputStream zipOutputStream, int prefixLength) throws IOException {
        File[] fileArray = folder.listFiles();
        int n = fileArray.length;
        int n2 = 0;
        while (n2 < n) {
            File file = fileArray[n2];
            if (file.isFile()) {
                ZipEntry zipEntry = new ZipEntry(file.getPath().substring(prefixLength));
                zipOutputStream.putNextEntry(zipEntry);
                Throwable throwable = null;
                Object var9_10 = null;
                try (FileInputStream inputStream = new FileInputStream(file);){
                    IOUtils.copy((InputStream)inputStream, (OutputStream)zipOutputStream);
                }
                catch (Throwable throwable2) {
                    if (throwable == null) {
                        throwable = throwable2;
                    } else if (throwable != throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                    throw throwable;
                }
                zipOutputStream.closeEntry();
            } else if (file.isDirectory()) {
                FileUtil.processFolder(file, zipOutputStream, prefixLength);
            }
            ++n2;
        }
    }
}


package com.mw.easylib;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void unzip(String targetPath, String zipFilePath) {
        try {
            int BUFFER = 2048;
            String fileName = zipFilePath;
            String filePath = targetPath;
            ZipFile zipFile = getZipFile(new File(fileName));
            Enumeration emu = zipFile.entries();
            int i = 0;
            while (emu.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) emu.nextElement();
                if (entry.isDirectory()) {
                    new File(filePath + entry.getName()).mkdirs();
                    continue;
                }
                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                File file = new File(filePath + entry.getName());
                File parent = file.getParentFile();
                if (parent != null && (!parent.exists())) {
                    parent.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);

                int count;
                byte data[] = new byte[BUFFER];
                while ((count = bis.read(data, 0, BUFFER)) != -1) {
                    bos.write(data, 0, count);
                }
                bos.flush();
                bos.close();
                bis.close();
            }
            zipFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static ZipFile getZipFile(File zipFile) throws Exception {
        ZipFile zip = new ZipFile(zipFile, Charset.forName("UTF-8"));
        Enumeration entries = zip.entries();
        while (entries.hasMoreElements()) {
            try {
                entries.nextElement();
                zip.close();
                zip = new ZipFile(zipFile, Charset.forName("UTF-8"));
                return zip;
            } catch (Exception e) {
                zip = new ZipFile(zipFile, Charset.forName("GBK"));
                return zip;
            }
        }
        return zip;
    }
}

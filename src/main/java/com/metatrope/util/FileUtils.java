package com.metatrope.util;

import java.io.File;

public class FileUtils {
    public static String getExtension(File file) {
        String fileName = file.getPath();
        return getExtension(fileName);
    }

    public static String getExtension(String fileName) {
        int extensionStart = fileName.lastIndexOf('.');
        if (extensionStart < 0)
            return null;
        String extension = fileName.substring(extensionStart + 1, fileName.length()).toLowerCase();
        return extension;
    }
}

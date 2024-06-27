/*******************************************************************************
 * Copyright (c) 2024 Lawrence McAlpin
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Lawrence McAlpin - initial implementation
 *******************************************************************************/
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

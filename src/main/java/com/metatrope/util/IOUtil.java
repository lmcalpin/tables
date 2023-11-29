/*******************************************************************************
 * Copyright (c) 2015 Lawrence McAlpin
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Lawrence McAlpin - initial API and implementation
 *******************************************************************************/
package com.metatrope.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtil.class);

    public static void close(InputStream is) {
        if (is == null)
            return;
        try {
            is.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close InputStream", e);
        }
    }

    public static void close(OutputStream os) {
        if (os == null)
            return;
        try {
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException("Could not flush InputStream", e);
        }
        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close InputStream", e);
        }
    }

    public static void close(Reader r) {
        if (r == null)
            return;
        try {
            r.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close Reader", e);
        }
    }

    public static void close(Writer is) {
        if (is == null)
            return;
        try {
            is.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close InputStream", e);
        }
    }

    public static String readAsString(InputStream is) throws IOException {
        try {
            if (is == null)
                return null;
            return readAsString(new InputStreamReader(is));
        } finally {
            close(is);
        }
    }

    public static String readAsString(Reader r) throws IOException {
        BufferedReader br = new BufferedReader(r);
        try {
            StringWriter sw = new StringWriter();
            String line;
            while ((line = br.readLine()) != null) {
                sw.write(line + "\n");
            }
            return sw.toString();
        } finally {
            close(br);
        }
    }

    public static String readAsString(String fileName) {
        BufferedReader reader = null;
        StringBuffer contents = new StringBuffer();
        try {
            FileInputStream fistream = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fistream);
            reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                contents.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.close(reader);
        }
        return contents.toString();
    }

    public static String readFrom(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }
        is.close();
        return sb.toString();
    }
}

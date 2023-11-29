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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.common.base.Strings;

public class FormatUtils {
    private static final NumberFormat DECIMAL_NO_COMMAS_FMT = new DecimalFormat("###0.00");
    private static final NumberFormat DECIMAL_FMT = new DecimalFormat("#,##0.00");
    public static final String DATETIME_FORMAT = "MMM dd, yyyy h:mm a";

    public static String formatDate(Date date) {
        return formatDate(date, DATETIME_FORMAT, TimeZone.getDefault());
    }

    public static String formatDate(Date date, String format, TimeZone tz) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (tz != null)
            sdf.setTimeZone(tz);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String formatDate(Date date, TimeZone tz) {
        return formatDate(date, DATETIME_FORMAT, tz);
    }

    public static String formatDecimal(double d) {
        return DECIMAL_FMT.format(d);
    }

    public static String formatDecimal(double d, int decimalPlaces) {
        String decimalPlacesStr = Strings.repeat("#", decimalPlaces);
        NumberFormat format = new DecimalFormat("#,##0." + decimalPlacesStr);
        return format.format(d);
    }

    public static String formatDecimalNoCommas(BigDecimal d) {
        return DECIMAL_NO_COMMAS_FMT.format(d);
    }

    public static String formatDecimalNoCommas(double d) {
        return DECIMAL_NO_COMMAS_FMT.format(d);
    }
}

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
package com.metatrope.tables.model;

import com.metatrope.tables.exception.TableImporterException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import com.google.common.base.Strings;

/**
 * The value of a cell in a table.
 *
 * @author Lawrence McAlpin (admin@lmcalpin.com)
 */
public final class Value {
    private Row parent;
    private Object value;
    private Column column;

    public Value(Row parent, Column c, Object inValue) {
        if (c == null) {
            throw new NullPointerException("Can not have null column");
        }
        if (inValue instanceof Value) {
            throw new UnsupportedOperationException("Code error: values should not hold objects of class Value");
        }
        this.parent = parent;
        this.column = c;
        this.value = normalize(inValue);
    }

    public Column getColumn() {
        return column;
    }

    public Object getObject() {
        return value;
    }

    public Row getParent() {
        return parent;
    }

    // If the input is not provided in the format required for the column,
    // convert it to the expected form.
    private Object normalize(Object inValue) {
        if (inValue == null) {
            switch (column.getDataType()) {
            case STRING:
                return "";
            case INTEGER:
                return 0;
            case DECIMAL:
                return BigDecimal.ZERO;
            default:
                break;
            }
            return null;
        }
        Object returnMe = inValue;
        switch (column.getDataType()) {
        case STRING:
            {
                if (!(inValue instanceof String)) {
                    returnMe = inValue.toString();
                }
            }
            break;
        case INTEGER:
            {
                if (inValue instanceof String) {
                    returnMe = Integer.valueOf((String) inValue);
                } else if (inValue instanceof BigDecimal) {
                    int r = (((BigDecimal) inValue).intValueExact());
                    returnMe = r;
                } else if (inValue instanceof Double) {
                    int r = (int) (((Double) inValue).doubleValue());
                    returnMe = r;
                }
            }
            break;
        case DECIMAL:
            {
                if (inValue instanceof String) {
                    try {
                        String s = (String) inValue;
                        if (Strings.isNullOrEmpty(s)) {
                            return BigDecimal.ZERO;
                        }
                        if (s.indexOf(",") >= 0) {
                            java.text.DecimalFormat df = new java.text.DecimalFormat("#,###.###");
                            double doubleValue = df.parse(s).doubleValue();
                            return new BigDecimal(doubleValue);
                        } else {
                            returnMe = new BigDecimal((String) inValue);
                        }
                    } catch (Exception e) {
                        throw new TableImporterException("Could not parse: " + inValue + " as DECIMAL for column: " + column.getID(), e);
                    }
                } else if (inValue instanceof Double) {
                    Double doubleValue = (Double) inValue;
                    return new BigDecimal(doubleValue);
                }
            }
            break;
        case DATE:
            {
                if (inValue instanceof String) {
                    String metadata = getColumn().getMetadata();
                    if (!Strings.isNullOrEmpty(metadata)) {
                        SimpleDateFormat ratef = new SimpleDateFormat(metadata);
                        String strInValue = (String) inValue;
                        if (Strings.isNullOrEmpty(strInValue))
                            return null;
                        try {
                            returnMe = ratef.parse(strInValue);
                        } catch (Exception e) {
                            throw new TableImporterException("Could not parse: " + inValue + ", with format [" + metadata + "] for column: " + column.getID(), e);
                        }
                    }
                }
            }
            break;
        default:
            break;
        }
        return returnMe;
    }

    @Override
    public String toString() {
        if (value == null) {
            return column.getID() + ": {NULL}";
        }
        return column.getID() + ":" + value.toString();
    }
}

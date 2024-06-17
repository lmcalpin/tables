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

/**
 * A vertical arrangement of cells in a tabular representation of data. All cells in a column
 * should be of the same data type.
 *
 * @author Lawrence McAlpin (admin@lmcalpin.com)
 */
public class Column {
    private String id;
    private DataType dataType;
    private String metadata;

    public Column(Column other) {
        this.id = other.id;
        this.dataType = other.dataType;
        this.metadata = other.metadata;
    }

    public Column(String id, DataType dataType) {
        this.id = id;
        this.dataType = dataType;
    }

    public Column(String id, DataType dataType, String metadata) {
        this(id, dataType);
        this.metadata = metadata;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getID() {
        return id;
    }

    public String getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        String output = getID() + " [" + getDataType() + "]";
        if (metadata != null) {
            output += " (" + metadata + ")";
        }
        return output;
    }
}

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
package com.metatrope.tables.transformer;

import com.metatrope.tables.model.Row;

import java.util.function.Predicate;

public class RowFilter implements RowTransformer {
    private Predicate<Row> filter;
    
    public RowFilter(Predicate<Row> filter) {
        this.filter = filter;
    }

    @Override
    public Row transform(Row in) {
        if (filter.test(in)) {
            return in;
        }
        return null;
    }

}

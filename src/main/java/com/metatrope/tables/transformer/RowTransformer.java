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

/**
 * A RowTransformer is used with the <code>Etl</code> class to modify the data in rows before
 * sending the data to the output sink. If an implementation returns null, the Row will be
 * filtered out.
 */
@FunctionalInterface
public interface RowTransformer {
    public Row transform(Row in);
}

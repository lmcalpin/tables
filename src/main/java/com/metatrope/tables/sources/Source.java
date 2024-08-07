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
package com.metatrope.tables.sources;

import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

import java.io.Closeable;
import java.util.Iterator;

public interface Source extends Closeable, Iterator<Row> {
    public Format getFormat();

    @Override
    public Row next();
}

/*
 * Copyright 2018 MSet contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ochumak.mset.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ValueMSet<T> extends AbstractMSet<T> {

    private final Set<T> backSet;

    public ValueMSet(Collection<T> values) {
        backSet = Collections.unmodifiableSet(new HashSet<>(values));
        checkContainsNull(backSet);
    }

    @Override
    public boolean contains(final Object value) {
        return backSet.contains(value);
    }

    @Override
    public Iterator<T> iterator() {
        return backSet.iterator();
    }

    @Override
    protected long calcCardinality() {
        return backSet.size();
    }

    //a separate method to suppress sonar warning
    private static void checkContainsNull(Collection collection) {
        if (collection.contains(null)) {
            throw new NullPointerException("Cannot contain null");
        }
    }
}

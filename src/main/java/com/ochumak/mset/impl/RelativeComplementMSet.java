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

import java.util.Iterator;
import java.util.Objects;

import com.ochumak.mset.MSet;
import com.ochumak.mset.iterator.SimpleIterator;
import com.ochumak.mset.iterator.SimpleIteratorImpl;
import com.ochumak.mset.util.MSetUtils;

public class RelativeComplementMSet<T> extends AbstractMSet<T> {

    private final MSet<T> firstSet;
    private final MSet<T> secondSet;

    public RelativeComplementMSet(final MSet<T> firstSet, final MSet<T> secondSet) {
        Objects.requireNonNull(firstSet);
        Objects.requireNonNull(secondSet);
        this.firstSet = firstSet;
        this.secondSet = secondSet;
    }

    @Override
    public boolean contains(final Object value) {
        return firstSet.contains(value) && !secondSet.contains(value);
    }

    @Override
    public Iterator<T> iterator() {
        return new ComplementIterator();
    }

    private final class ComplementIterator extends SimpleIteratorImpl<T> {

        private final SimpleIterator<T> firstSetIterator;

        private ComplementIterator() {
            firstSetIterator = MSetUtils.toSimpleIterator(firstSet.iterator());
        }

        @Override
        public T nextValue() {
            T value;
            do {
                value = firstSetIterator.nextValue();
            } while (value != null && secondSet.contains(value));
            return value;
        }
    }
}
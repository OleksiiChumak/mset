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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.ochumak.mset.MSet;
import com.ochumak.mset.iterator.SimpleIterator;
import com.ochumak.mset.iterator.SimpleIteratorImpl;
import com.ochumak.mset.util.MSetUtils;

public class IntersectionMSet<T> extends AbstractMSet<T> {

    private final Collection<MSet<T>> sets;

    public IntersectionMSet(final Collection<MSet<T>> sets) {
        if (sets.isEmpty()) {
            throw new IllegalArgumentException("Sets cannot be empty");
        }
        this.sets = new ArrayList<>(sets);
    }

    @Override
    public boolean contains(final Object value) {
        for (MSet<T> set : sets) {
            if (!set.contains(value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new IntersectionIterator();
    }

    private final class IntersectionIterator extends SimpleIteratorImpl<T> {

        private final SimpleIterator<T> firstSetIterator;
        private final List<MSet<T>> otherSets;

        private IntersectionIterator() {
            final Iterator<MSet<T>> iterator = sets.iterator();
            firstSetIterator = MSetUtils.toSimpleIterator(iterator.next().iterator());
            otherSets = new ArrayList<>();
            iterator.forEachRemaining(otherSets::add);
        }

        @Override
        public T nextValue() {
            T value;
            do {
                value = firstSetIterator.nextValue();
            } while (value != null && !allOtherSetsContain(value));
            return value;
        }

        private boolean allOtherSetsContain(T value) {
            for (MSet<T> set : otherSets) {
                if (!set.contains(value)) {
                    return false;
                }
            }
            return true;
        }
    }
}

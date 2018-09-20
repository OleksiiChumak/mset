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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ochumak.mset.MSet;
import com.ochumak.mset.iterator.SimpleIterator;
import com.ochumak.mset.iterator.SimpleIteratorImpl;
import com.ochumak.mset.util.MSetUtils;

public class UnionMSet<T> extends AbstractMSet<T> {

    private final Collection<MSet<T>> sets;

    public UnionMSet(final Collection<MSet<T>> sets) {
        if (sets.isEmpty()) {
            throw new IllegalArgumentException("Sets cannot be empty");
        }
        this.sets = new ArrayList<>(sets);
    }

    @Override
    public boolean contains(final Object value) {
        for (MSet<T> set : sets) {
            if (set.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new UnionIterator();
    }

    private final class UnionIterator extends SimpleIteratorImpl<T> {

        private final List<SimpleIterator<T>> iterators;
        private final Set<T> returned = new HashSet<>();

        private UnionIterator() {
            this.iterators = sets.stream()
                    .map(Iterable::iterator)
                    .map(MSetUtils::toSimpleIterator)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        @Override
        public T nextValue() {
            Iterator<SimpleIterator<T>> setsIterator = iterators.iterator();
            while (setsIterator.hasNext()) {
                SimpleIterator<T> iterator = setsIterator.next();
                T value;
                do {
                    value = iterator.nextValue();
                }
                while (value != null && returned.contains(value));
                if (value == null) {
                    setsIterator.remove();
                } else {
                    returned.add(value);
                    return value;
                }
            }
            return null;
        }
    }
}

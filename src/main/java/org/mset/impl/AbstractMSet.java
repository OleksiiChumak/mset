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

package org.mset.impl;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mset.MSet;

public abstract class AbstractMSet<T> implements MSet<T> {

    private static final ThreadLocal<Object> UNIVERSAL_SET_THREAD_LOCAL = new ThreadLocal<>();
    private long cardinality = -1;

    protected long calcCardinality() {
        long count = 0;
        for (Iterator<T> iterator = iterator(); iterator.hasNext(); iterator.next()) {
            count++;
        }
        return count;
    }

    @Override
    public synchronized long cardinality() {
        if (cardinality < 0) {
            cardinality = calcCardinality();
        }
        return cardinality;
    }

    @Override
    public String toString() {
        return stream()
                .map(Object::toString)
                .collect(Collectors.joining(",", "{", "}"));
    }

    @Override
    public final MSet<T> getUniversalSet() {
        return new UniversalMSetProxy<>();
    }

    @Override
    public final boolean contains(final Object value, final MSet<T> universalSet) {
        return withUniversalSet(universalSet, set -> set.contains(value));
    }

    @Override
    public final Iterator<T> iterator(final MSet<T> universalSet) {
        return withUniversalSet(universalSet, Iterable::iterator);
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof MSet) {
            return equalsSet((MSet<?>) obj);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return (int) cardinality();
    }

    private <R> R withUniversalSet(MSet<T> universalSet, Function<MSet<T>, R> func) {
        Objects.requireNonNull(universalSet);
        UNIVERSAL_SET_THREAD_LOCAL.set(universalSet);
        final R result = func.apply(this);
        UNIVERSAL_SET_THREAD_LOCAL.remove();
        return result;
    }

    private static class UniversalMSetProxy<E> implements MSet<E> {

        @Override
        public boolean contains(final Object value) {
            return getLocalUniversalSet().contains(value);
        }

        @Override
        public boolean contains(final Object value, final MSet<E> universalSet) {
            return contains(value);
        }

        @Override
        public Iterator<E> iterator(final MSet<E> universalSet) {
            return iterator();
        }

        @Override
        public MSet<E> getUniversalSet() {
            return this;
        }

        @Override
        public long cardinality() {
            return getLocalUniversalSet().cardinality();
        }

        @Override
        public Iterator<E> iterator() {
            return UniversalMSetProxy.<E>getLocalUniversalSet().iterator();
        }

        @SuppressWarnings("unchecked")
        private static <E> MSet<E> getLocalUniversalSet() {
            Object universalSet = UNIVERSAL_SET_THREAD_LOCAL.get();
            if (universalSet == null) {
                throw new IllegalStateException("No universal set. Use universal set bound methods");
            }
            return (MSet<E>) universalSet;
        }

        @Override
        public int hashCode() {
            return getLocalUniversalSet().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return getLocalUniversalSet().equals(obj);
        }
    }
}

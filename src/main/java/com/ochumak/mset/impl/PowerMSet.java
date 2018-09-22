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

import com.ochumak.mset.MSet;
import com.ochumak.mset.iterator.SimpleIteratorImpl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

public class PowerMSet<T> extends AbstractMSet<MSet<T>> {

    private static final long MAX_SET_SIZE = 62;

    private final MSet<T> set;

    public PowerMSet(MSet<T> set) {
        this.set = set;
    }

    @Override
    public boolean contains(Object objectValue) {
        if (objectValue instanceof MSet) {
            MSet<?> setValue = (MSet<?>) objectValue;
            for (Object value : setValue) {
                if (!set.contains(value)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Iterator<MSet<T>> iterator() {
        return new PowerIterator();
    }

    @Override
    protected long calcCardinality() {
        long setCardinality = set.cardinality();
        if (setCardinality > MAX_SET_SIZE) {
            throw new IllegalStateException("Long cannot hold that big cardinality");
        }
        return Math.round(Math.pow(2, setCardinality));
    }

    private final class PowerIterator extends SimpleIteratorImpl<MSet<T>> {

        private final Iterator<T> valuesIterator = set.iterator();
        private final List<T> values = new ArrayList<>();
        private BitSet counter = new BitSet();
        private int nextSetSize;

        @Override
        public MSet<T> nextValue() {
            List<T> result = new ArrayList<>();
            for (int i = 0; i < nextSetSize; i++) {
                T value = get(i);
                if (value == null) {
                    return null;
                } else if (counter.get(i)) {
                    result.add(value);
                }
            }
            inc();
            return MSet.of(result);
        }

        private void inc() {
            int idx = 0;
            while (true) {
                if (counter.get(idx)) {
                    counter.clear(idx);
                    idx++;
                } else {
                    counter.set(idx);
                    nextSetSize = Math.max(nextSetSize, idx + 1);
                    return;
                }
            }
        }

        private T get(int index) {
            while (index == values.size()) {
                if (!valuesIterator.hasNext()) {
                    return null;
                }
                values.add(valuesIterator.next());
            }
            return values.get(index);
        }
    }
}

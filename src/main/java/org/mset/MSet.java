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

package org.mset;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.mset.impl.IntersectionMSet;
import org.mset.impl.PowerMSet;
import org.mset.impl.RelativeComplementMSet;
import org.mset.impl.UnionMSet;
import org.mset.impl.ValueMSet;
import org.mset.util.MSetUtils;

public interface MSet<T> extends Iterable<T> {

    @SafeVarargs
    static <T> MSet<T> of(T... values) {
        return new ValueMSet<>(Arrays.asList(values));
    }

    static <T> MSet<T> of(List<T> value) {
        return new ValueMSet<>(value);
    }

    @SafeVarargs
    static <E> MSet<E> union(MSet<E> firstSet, MSet<E>... sets) {
        return new UnionMSet<>(MSetUtils.combineToList(firstSet, sets));
    }

    @SafeVarargs
    static <E> MSet<E> intersection(MSet<E> firstSet, MSet<E>... sets) {
        return new IntersectionMSet<>(MSetUtils.combineToList(firstSet, sets));
    }

    static <I> Collector<I, List<I>, MSet<I>> toMSet() {
        return new MSetCollector<>();
    }

    boolean contains(Object value);

    boolean contains(Object value, MSet<T> universalSet);

    Iterator<T> iterator(MSet<T> universalSet);

    MSet<T> getUniversalSet();

    default MSet<MSet<T>> powerSet() {
        return new PowerMSet<>(this);
    }

    long cardinality();

    default MSet<T> union(MSet<T> set) {
        return MSet.union(this, set);
    }

    default MSet<T> intersection(MSet<T> set) {
        return MSet.intersection(this, set);
    }

    default MSet<T> relativeComplement(MSet<T> set) {
        return new RelativeComplementMSet<>(this, set);
    }

    default MSet<T> complement() {
        return getUniversalSet().relativeComplement(this);
    }

    default boolean isSubsetOf(MSet<?> set) {
        for (T value : this) {
            if (!set.contains(value)) {
                return false;
            }
        }
        return true;
    }

    default boolean isSuperSetOf(MSet<?> set) {
        return set.isSubsetOf(this);
    }

    default boolean equalsSet(MSet<?> set) {
        return isSubsetOf(set) && isSuperSetOf(set);
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}

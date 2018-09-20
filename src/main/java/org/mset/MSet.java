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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.mset.collector.MSetCollector;
import org.mset.impl.IntersectionMSet;
import org.mset.impl.PowerMSet;
import org.mset.impl.RelativeComplementMSet;
import org.mset.impl.UnionMSet;
import org.mset.impl.ValueMSet;
import org.mset.util.MSetUtils;

/**
 * A data structure that contains no duplicate elements. The intention of <tt>MSet</tt> is to more closely follow
 * operations allowed by the mathematical <i>Set theory</i> than {@link java.util.Set} does it. <tt>MSet</tt> doesn't allow
 * null as a value.
 *
 * <p>Note: Implementations are immutable and thread-safe (iterators are not thread-safe). Methods that return <tt>MSet</tt> don't do any computation other than
 * creating a new instance, so may be considerate cheep.</p>
 *
 * @param <T> the type of elements maintained by this set
 */
public interface MSet<T> extends Iterable<T> {

    /**
     * Constructs a new set containing the elements in the specified
     * array.
     *
     * @param <T>    the class of the objects in the array
     * @param values the array whose elements are to be placed into this set
     * @return a set of the specified array
     * @throws NullPointerException if the specified array is null or contains a null
     */
    @SafeVarargs
    static <T> MSet<T> of(T... values) {
        return new ValueMSet<>(Arrays.asList(values));
    }

    /**
     * Constructs a new set containing the elements in the specified
     * collection.
     *
     * @param <T>   the class of the objects in the collection
     * @param value the collection whose elements are to be placed into this set
     * @return a set of the specified collection
     * @throws NullPointerException if the specified collection is null or contains a null
     */
    static <T> MSet<T> of(Collection<T> value) {
        return new ValueMSet<>(value);
    }

    /**
     * Creates a new set that contains any element if it is present in at least one passed set.
     *
     * @param firstSet the first member of the union
     * @param sets     the other members of the union
     * @param <T>      the class of the objects in the sets
     * @return firstSet &#8746; sets[0] &#8746; ... &#8746; sets[n]
     */
    @SafeVarargs
    static <T> MSet<T> union(MSet<T> firstSet, MSet<T>... sets) {
        return new UnionMSet<>(MSetUtils.combineToList(firstSet, sets));
    }

    /**
     * Creates a new set that contains any element if it is present in all passed sets.
     *
     * @param firstSet the first member of the intersection
     * @param sets     the other members of the intersection
     * @param <T>      the class of the objects in the sets
     * @return firstSet &#8745; sets[0] &#8745; ... &#8745; sets[n]
     */
    @SafeVarargs
    static <T> MSet<T> intersection(MSet<T> firstSet, MSet<T>... sets) {
        return new IntersectionMSet<>(MSetUtils.combineToList(firstSet, sets));
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code MSet}.
     *
     * @param <I> the type of the input elements
     * @return a {@code Collector} which collects all the input elements into a
     * {@code MSet}
     */
    static <I> Collector<I, List<I>, MSet<I>> toMSet() {
        return new MSetCollector<>();
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element.
     *
     * @param value element whose presence in this set is to be tested
     * @return <tt>true</tt> if this set contains the specified element
     */
    boolean contains(Object value);

    /**
     * Returns <tt>true</tt> if this set contains the specified element.
     *
     * @param value        element whose presence in this set is to be tested
     * @param universalSet a universal set shared with all sets involved in computation. Required if on some level
     *                     a universal set bounded operation such as {@link #complement()} is used
     * @return <tt>true</tt> if this set contains the specified element
     */
    boolean contains(Object value, MSet<T> universalSet);

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @param universalSet a universal set shared with all sets involved in computation. Required if on some level
     *                     a universal set bounded operation such as {@link #complement()} is used
     * @return an Iterator.
     */
    Iterator<T> iterator(MSet<T> universalSet);

    /**
     * Returns a proxy of the universal set bounded to the current context. If no set is present, any operation on the proxy will fail.
     * The method is used internally by other sets.
     *
     * @return a proxy of the universal set bounded to current context
     */
    MSet<T> getUniversalSet();

    /**
     * Returns a set that contains all subsets of the current set
     *
     * @return P(this)
     */
    default MSet<MSet<T>> powerSet() {
        return new PowerMSet<>(this);
    }

    /**
     * Returns the number of elements in this set.
     *
     * @return | this |
     */
    long cardinality();

    /**
     * Creates a new set that contains any element if it is present in the current set or in the passed set
     *
     * @param set the second member of the union
     * @return this &#8746; set
     * @see MSet#union(MSet, MSet[])
     */
    default MSet<T> union(MSet<T> set) {
        return MSet.union(this, set);
    }

    /**
     * Creates a new set that contains any element if it is present in the current set and in the passed set
     *
     * @param set the second member of the intersection
     * @return this &#8745; set
     * @see MSet#intersection(MSet, MSet[])
     */
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

    /**
     * Returns a sequential {@code Stream} with this set as its source.
     *
     * <p>The default implementation creates a sequential {@code Stream} from the
     * sets's {@code Spliterator}.</p>
     *
     * @return a sequential {@code Stream} over the elements in this set
     */
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}

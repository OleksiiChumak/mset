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

package org.mset.util;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mset.MSet;
import org.mset.iterator.SimpleIterator;
import org.mset.iterator.SimpleIteratorAdapter;

public final class MSetUtils {

    private MSetUtils() {
    }

    @SafeVarargs
    public static <T> List<MSet<T>> combineToList(MSet<T> firstSet, MSet<T>... sets) {
        return Stream.concat(Stream.of(firstSet), Stream.of(sets)).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static <T> SimpleIterator<T> toSimpleIterator(Iterator<T> iterator) {
        if (iterator instanceof SimpleIterator) {
            return (SimpleIterator<T>) iterator;
        } else {
            return new SimpleIteratorAdapter<>(iterator);
        }
    }
}

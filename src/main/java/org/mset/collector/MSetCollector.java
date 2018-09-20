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

package org.mset.collector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.mset.MSet;

public class MSetCollector<I> implements Collector<I, List<I>, MSet<I>> {

    private static final Set<Characteristics> CHARACTERISTICS = Collections.singleton(Characteristics.UNORDERED);

    @Override
    public Supplier<List<I>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<I>, I> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<I>> combiner() {
        return (list1, list2) -> {
            list1.addAll(list2);
            return list2;
        };
    }

    @Override
    public Function<List<I>, MSet<I>> finisher() {
        return MSet::of;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return CHARACTERISTICS;
    }
}

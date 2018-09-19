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

import org.junit.jupiter.api.Test;
import org.mset.MSet;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PowerMSetTest {

    @Test
    void cardinality() {
        PowerMSet<Integer> set = new PowerMSet<>(MSet.of(1, 2));
        assertThat(set.cardinality()).isEqualTo(4);
    }

    @Test
    void cardinalityEmpty() {
        PowerMSet<Integer> set = new PowerMSet<>(MSet.of());
        assertThat(set.cardinality()).isEqualTo(1);
    }

    @Test
    void cardinalityExceeded() {
        PowerMSet<Integer> set = new PowerMSet<>(IntStream.range(0, 63).boxed().collect(MSet.toMSet()));
        assertThatThrownBy(set::cardinality).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void iterator() {
        PowerMSet<Integer> set = new PowerMSet<>(MSet.of(1, 2));
        assertThat(set)
                .containsExactlyInAnyOrder(
                        MSet.of(),
                        MSet.of(1),
                        MSet.of(2),
                        MSet.of(1, 2));
    }

    @Test
    void iteratorEmpty() {
        PowerMSet<Integer> set = new PowerMSet<>(MSet.of());
        assertThat(set)
                .containsExactlyInAnyOrder(
                        MSet.of());
    }

    @Test
    void iterator2() {
        PowerMSet<Integer> set = new PowerMSet<>(MSet.of(1, 2, 3));
        assertThat(set)
                .containsExactlyInAnyOrder(
                        MSet.of(),
                        MSet.of(1),
                        MSet.of(2),
                        MSet.of(1, 2),
                        MSet.of(3),
                        MSet.of(1, 3),
                        MSet.of(2, 3),
                        MSet.of(1, 2, 3));
    }


    @Test
    void contains() {
        PowerMSet<Integer> set = new PowerMSet<>(MSet.of(1, 2));
        assertTrue(set.contains(MSet.of()));
        assertTrue(set.contains(MSet.of(2, 1)));
    }

    @Test
    void containsFalse() {
        PowerMSet<Integer> set = new PowerMSet<>(MSet.of(1, 2));
        assertFalse(set.contains(MSet.of(1, 2, 3)));
    }

    @Test
    void containsEmpty() {
        PowerMSet<Integer> set = new PowerMSet<>(MSet.of());
        assertTrue(set.contains(MSet.of()));
    }
}
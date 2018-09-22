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

package com.ochumak.mset;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MSetTest {

    @Test
    void union() {
        assertThat(MSet.of(1, 2, 3).union(MSet.of(3, 4, 5))).containsExactlyInAnyOrder(1, 2, 3, 4, 5);
    }

    @Test
    void intersection() {
        assertThat(MSet.of(1, 2, 3).intersection(MSet.of(3, 4, 5))).containsExactlyInAnyOrder(3);
    }

    @Test
    void containsTrue() {
        assertTrue(MSet.of(1, 2, 3).contains(3));
    }

    @Test
    void containsFalse() {
        assertFalse(MSet.of(1, 2, 3).contains(4));
    }

    @Test
    void isSubsetOfSame() {
        assertTrue(MSet.of(1, 2, 3).isSubsetOf(MSet.of(1, 2, 3)));
    }

    @Test
    void isSubsetOfBigger() {
        assertTrue(MSet.of(1, 2, 3).isSubsetOf(MSet.of(1, 2, 3, 4)));
    }

    @Test
    void isSubsetFalse() {
        assertFalse(MSet.of(1, 2, 3).isSubsetOf(MSet.of(1, 2, 4)));
    }

    @Test
    void isSuperSetOfSame() {
        assertTrue(MSet.of(1, 2, 3).isSuperSetOf(MSet.of(1, 2, 3)));
    }

    @Test
    void isSuperSetOfSmaller() {
        assertTrue(MSet.of(1, 2, 3, 4).isSuperSetOf(MSet.of(1, 2, 3)));
    }

    @Test
    void isSuperSetOfFalse() {
        assertFalse(MSet.of(1, 2, 3, 4, 5).isSuperSetOf(MSet.of(1, 2, 7)));
    }

    @Test
    void equalsSame() {
        assertTrue(MSet.of(1, 2, 3).equalsSet(MSet.of(1, 2, 3)));
    }

    @Test
    void equalsSameUnion() {
        assertTrue(MSet.of(1).union(MSet.of(2)).union(MSet.of(3)).equalsSet(MSet.of(1, 2, 3)));
    }

    @Test
    void equalsSameIntersection() {
        assertTrue(MSet.of(1, 2, 3, 4, 5)
            .intersection(MSet.of(1, 2, 3, 6))
            .intersection(MSet.of(1, 2, 3, 8))
            .equalsSet(MSet.of(1, 2, 3)));
    }

    @Test
    void equalsDifferent() {
        assertFalse(MSet.of(1, 2, 4).equalsSet(MSet.of(1, 2, 3)));
    }

    @Test
    void of() {
        assertThat(MSet.of(1, 2, 2, 3, 3)).containsExactlyInAnyOrder(1, 2, 3);
    }

    @Test
    void ofNullValue() {
        assertThatThrownBy(() -> MSet.of((Object) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Cannot contain null");
    }

    @Test
    void ofNullList() {
        List<Integer> nullList = null;
        assertThatThrownBy(() -> MSet.of(nullList))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void ofNullValueInCollection() {
        assertThatThrownBy(() -> MSet.of(Collections.singleton(null))).isInstanceOf(NullPointerException.class);
    }

    @Test
    void cardinality() {
        assertThat(MSet.of(1, 2, 2, 2, 2, 3).cardinality()).isEqualTo(3);
    }

    @Test
    void cardinalityUnion() {
        assertThat(MSet.of(1, 2, 2, 2, 2, 3).union(MSet.of(4)).cardinality()).isEqualTo(4);
    }

    @Test
    void relativeComplement() {
        assertThat(MSet.of(1, 2, 3).relativeComplement(MSet.of(2))).containsExactlyInAnyOrder(1, 3);
    }

    @Test
    void relativeComplementEmpty() {
        assertThat(MSet.of(1, 2, 3).relativeComplement(MSet.of())).containsExactlyInAnyOrder(1, 2, 3);
    }

    @Test
    void toMSet() {
        MSet<Integer> set = Stream.of(1, 2, 3, 4, 5, 6, 4, 4, 4, 4)
            .collect(MSet.toMSet());
        assertThat(set).containsExactlyInAnyOrder(1, 2, 3, 4, 5, 6);
    }

    @Test
    void toStream() {
        List<Integer> res = MSet.of(1, 2, 3, 3, 3, 3, 4)
            .stream()
            .collect(Collectors.toList());
        assertThat(res).containsExactlyInAnyOrder(1, 2, 3, 4);
    }

    @Test
    void toStreamFilter() {
        List<Integer> res = MSet.of(1, 2, 3, 3, 3, 3, 4)
            .stream()
            .filter(i -> i != 2)
            .collect(Collectors.toList());
        assertThat(res).containsExactlyInAnyOrder(1, 3, 4);
    }

    @Test
    void complementIllegalStateException() {
        MSet<Boolean> set = MSet.of(true);
        MSet<Boolean> complement = set.complement();

        assertThatThrownBy(() -> complement.contains(true))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void complement() {
        MSet<Integer> universalSet = MSet.of(1, 2, 3, 4, 5, 6);
        MSet<Integer> a = MSet.of(1);
        MSet<Integer> b = a.complement();

        MSet<Integer> newUniversalSet = a.union(b);

        Iterator<Integer> integerIterator = newUniversalSet.iterator(universalSet);
        assertThat(integerIterator).containsExactlyInAnyOrder(1, 2, 3, 4, 5, 6);
    }

    @Test
    void complementContains() {
        MSet<Integer> universalSet = MSet.of(1, 2, 3);
        MSet<Integer> a = MSet.of(1);
        MSet<Integer> b = a.complement();

        MSet<Integer> newUniversalSet = a.union(b);

        assertTrue(newUniversalSet.contains(1, universalSet));
        assertTrue(newUniversalSet.contains(2, universalSet));
        assertTrue(newUniversalSet.contains(3, universalSet));
        assertFalse(newUniversalSet.contains(4, universalSet));
    }

    @Test
    void powerSet() {
        Assertions.assertThat(MSet.of(1, 2).powerSet())
            .containsExactlyInAnyOrder(
                MSet.of(),
                MSet.of(1),
                MSet.of(2),
                MSet.of(1, 2));
    }
}
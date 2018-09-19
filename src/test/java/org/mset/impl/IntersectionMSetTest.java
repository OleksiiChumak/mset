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

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mset.MSet.of;

import java.util.Collections;

import org.junit.jupiter.api.Test;

class IntersectionMSetTest {

    @Test
    void illegalArguments() {
        assertThatThrownBy(() -> new IntersectionMSet<Integer>(Collections.emptyList())).isInstanceOf(
            IllegalArgumentException.class);
    }

    @Test
    void iteratorEmpty() {
        final IntersectionMSet<Integer> set = new IntersectionMSet<>(singletonList(of()));
        assertThat(set).isEmpty();
    }

    @Test
    void iteratorSingle() {
        final IntersectionMSet<Integer> set = new IntersectionMSet<>(singletonList(of(1, 2, 3)));
        assertThat(set).containsExactlyInAnyOrder(1, 2, 3);
    }

    @Test
    void iterator() {
        final IntersectionMSet<Integer> set = new IntersectionMSet<>(asList(of(1, 2, 3), of(3, 4, 5)));
        assertThat(set).containsExactlyInAnyOrder(3);
    }

    @Test
    void iterator2() {
        final IntersectionMSet<Integer> set = new IntersectionMSet<>(asList(of(1, 2, 3), of(3, 4, 5), of(1, 2, 5, 3)));
        assertThat(set).containsExactlyInAnyOrder(3);
    }

    @Test
    void contains() {
        final IntersectionMSet<Integer> set = new IntersectionMSet<>(asList(of(1, 2, 3), of(3, 4, 5), of(1, 2, 5, 3)));
        assertTrue(set.contains(3));
        assertFalse(set.contains(4));
    }
}
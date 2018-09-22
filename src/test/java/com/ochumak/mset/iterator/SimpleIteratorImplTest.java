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

package com.ochumak.mset.iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleIteratorImplTest {

    @Spy
    private SimpleIteratorImpl<Integer> simpleIterator;

    @Test
    void simple() {
        when(simpleIterator.nextValue()).thenReturn(1, 2, 3, null);
        assertThat(simpleIterator).containsExactlyInAnyOrder(1, 2, 3);
    }

    @Test
    void empty() {
        when(simpleIterator.nextValue()).thenReturn(null);
        assertThat(simpleIterator).isEmpty();
    }

    @Test
    void noSuchElementException() {
        when(simpleIterator.nextValue()).thenReturn(1, 2, null);
        simpleIterator.next();
        simpleIterator.next();
        assertThatThrownBy(() -> simpleIterator.next()).isInstanceOf(NoSuchElementException.class);
    }
}
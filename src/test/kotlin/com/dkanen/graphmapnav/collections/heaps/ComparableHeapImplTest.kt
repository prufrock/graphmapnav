package com.dkanen.graphmapnav.collections.heaps

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ComparableHeapImplTest {
    @Test
    fun `remove elements in max priority order`() {
        val array = arrayListOf(1, 12, 3, 4, 1 , 6, 8, 7)
        val heap = ComparableHeapImpl.create(array)
        val ordered = mutableListOf<Int>()

        while(!heap.isEmpty) {
            heap.remove()?.let { ordered.add(it) }
        }

        assertEquals(arrayListOf(12, 8, 7, 6, 4, 3, 1, 1), ordered)
    }
}
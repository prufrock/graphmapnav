package com.dkanen.graphmapnav.collections.heaps

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ComparatorHeapImplTest {
    @Test
    fun `remove elements in min priority order`() {
        val array = arrayListOf(1, 12, 3, 4, 1, 6, 8, 7)
        val inverseComparator = Comparator<Int> { o1, o2 -> o2.compareTo(o1) }
        val minHeap = ComparatorHeapImpl.create(array, inverseComparator)
        val ordered = mutableListOf<Int>()

        while(!minHeap.isEmpty) {
            minHeap.remove()?.let { ordered.add(it) }
        }

        assertEquals(arrayListOf(1, 1, 3, 4, 6, 7, 8, 12), ordered)
    }

    @Test
    fun `heap merge`() {
        val inverseComparator = Comparator<Int> { o1, o2 -> o2.compareTo(o1) }
        val minHeapOne = ComparatorHeapImpl.create(arrayListOf(1, 12, 3), inverseComparator)
        val minHeapTwo = ComparatorHeapImpl.create(arrayListOf(4, 1, 6, 8, 7), inverseComparator)
        minHeapOne.merge(minHeapTwo)
        val ordered = mutableListOf<Int>()

        while(!minHeapOne.isEmpty) {
            minHeapOne.remove()?.let { ordered.add(it) }
        }

        assertEquals(arrayListOf(1, 1, 3, 4, 6, 7, 8, 12), ordered)
    }

    @Test
    fun `verify nthSmallest`() {
        assertEquals(18, arrayListOf(3, 10, 18, 5, 21, 100).nthSmallest(4))
    }

    @Test
    fun isMinHeap() {
        assertTrue(arrayListOf<Int>().isMinHeap())
        assertTrue(arrayListOf(1).isMinHeap())
        assertTrue(arrayListOf(1, 2).isMinHeap())
        assertFalse(arrayListOf(2, 1).isMinHeap())
        assertTrue(arrayListOf(1, 2, 3, 4).isMinHeap())
        assertTrue(arrayListOf(4, 5, 6, 9).isMinHeap())
        assertFalse(arrayListOf(4, 5, 1, 9).isMinHeap())
    }
}
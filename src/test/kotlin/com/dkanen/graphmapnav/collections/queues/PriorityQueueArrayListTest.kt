package com.dkanen.graphmapnav.collections.queues

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PriorityQueueArrayListTest {
    @Test
    fun `max priority array list based queue example`() {
        val priorityQueue = ComparablePriorityQueueImpl<Int>()
        arrayListOf(1, 12, 3, 4, 1, 6, 8, 7).forEach {
            priorityQueue.enqueue(it)
        }
        priorityQueue.enqueue(5)
        priorityQueue.enqueue(0)
        priorityQueue.enqueue(10)

        val result = mutableListOf<Int>()

        while (!priorityQueue.isEmpty) {
            priorityQueue.dequeue()?.let { result.add(it) }
        }

        assertEquals(listOf(12, 10, 8, 7, 6, 5, 4, 3, 1, 1, 0), result)
    }
}
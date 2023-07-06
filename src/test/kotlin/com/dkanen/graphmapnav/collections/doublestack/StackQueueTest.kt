package com.dkanen.graphmapnav.collections.doublestack

import com.dkanen.graphmapnav.collections.queues.StackQueue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StackQueueTest {
    @Test
    fun `add and remove items from the queue`() {
        val queue = StackQueue<String>().apply {
            enqueue("Ondro")
            enqueue("Ilya")
            enqueue("Tamiko")
        }

        assertEquals(listOf("Ondro", "Ilya", "Tamiko"), queue.toList())
        assertEquals("Ondro", queue.dequeue())
        assertEquals(listOf("Ilya", "Tamiko"), queue.toList())
        assertEquals("Ilya", queue.peek())
    }
}
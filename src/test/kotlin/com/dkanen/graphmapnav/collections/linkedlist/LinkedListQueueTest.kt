package com.dkanen.graphmapnav.collections.linkedlist

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class LinkedListQueueTest {
    @Test
    fun `add and remove items from the queue`() {
        val queue = LinkedListQueue<String>().apply {
            enqueue("Cid")
            enqueue("Veronica")
            enqueue("Celes")
        }
        assertEquals(listOf("Cid", "Veronica", "Celes"), queue.toList())
        assertEquals("Cid", queue.dequeue())
        assertEquals("Veronica", queue.peek())
    }
}
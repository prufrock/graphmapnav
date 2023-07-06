package com.dkanen.graphmapnav.collections.ringbuffer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RingBufferQueueTest {
    @Test
    fun `add and remove elements from the ring buffer`() {
        val ringBuffer = RingBufferQueue<String>(10).apply {
            enqueue("Binti")
            enqueue("Soya")
            enqueue("Fujiko")
        }
        assertEquals(listOf("Binti", "Soya", "Fujiko"), ringBuffer.toList())
        ringBuffer.dequeue()
        assertEquals(listOf("Soya", "Fujiko"), ringBuffer.toList())
        assertEquals("Soya", ringBuffer.peek())
    }
}
package com.dkanen.graphmapnav.collections.ringbuffer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ArrayListRingBufferTest {
    @Test
    fun `add elements to a ring buffer and read them`() {
        val ring = ArrayListRingBuffer<Int>(3)
        assertEquals("[]", ring.toString())
        assertTrue(ring.write(1))
        assertEquals("[1]", ring.toString())
        assertTrue(ring.write(2))
        assertEquals("[1, 2]", ring.toString())
        assertTrue(ring.write(3))
        assertEquals("[1, 2, 3]", ring.toString())
        assertFalse(ring.write(4), "You shouldn't be able to add more elements once the ring buffer is bigger than the size it was initialized with.")
        assertEquals("[1, 2, 3]", ring.toString())
        assertEquals(1, ring.read())
        assertEquals(2, ring.read())
        assertEquals(3, ring.read())
        assertNull(ring.read())
    }

    @Test
    fun `when the ring buffer is full`()
    {
        val ring = ArrayListRingBuffer<Int>(3)
        assertTrue(ring.write(1))
        assertTrue(ring.write(2))
        assertTrue(ring.write(3))
        assertEquals("[1, 2, 3]", ring.toString())
        assertEquals(3, ring.count)
    }
}
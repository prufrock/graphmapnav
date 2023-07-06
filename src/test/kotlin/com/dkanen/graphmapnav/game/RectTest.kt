package com.dkanen.graphmapnav.game

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class RectTest {

    @Test
    fun intersection() {
    }

    @Test
    fun contains() {
        val r = Rect(0.0, 0.0 ,5.0, 5.0)

        assertEquals(false, r.contains(6.0, 6.0))
        assertEquals(false, r.contains(6.0, 4.0))
        assertEquals(true, r.contains(1.0, 1.0))
    }

    @Test
    fun testContains() {
        val r = Rect(0.0, 0.0 ,5.0, 5.0)

        // assuming upper left origin
        assertFalse(r.contains(-1.0, -1.0, 1.0, 1.0)) // upper left
        assertFalse(r.contains(4.0, 1.0, 6.0, -1.0)) // upper right
        assertFalse(r.contains(-1.0, 4.0, 1.0, 6.0)) // lower left
        assertFalse(r.contains(4.0, 4.0, 6.0, 6.0)) // lower right

        assertTrue(r.contains(0.0, 0.0, 3.0, 3.0))
    }

    @Test
    fun testDivide() {
        val r = Rect(0.0, 0.0, 5.0, 5.0)

        val (ar, br) = r.divideVertical()
        assertEquals(0.0, ar.min.x)
        assertEquals(0.0, ar.min.y)
        assertEquals(2.5, ar.max.x)
        assertEquals(5.0, ar.max.y)
        assertEquals(2.5, br.min.x)
        assertEquals(0.0, br.min.y)
        assertEquals(5.0, br.max.x)
        assertEquals(5.0, br.max.y)

        val (cr, dr) = r.divideHorizontal()
        assertEquals(0.0, cr.min.y)
        assertEquals(2.5, cr.max.y)
        assertEquals(2.5, dr.min.y)
        assertEquals(5.0, dr.max.y)
    }
}
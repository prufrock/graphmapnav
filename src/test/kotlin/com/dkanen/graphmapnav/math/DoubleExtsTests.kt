package com.dkanen.graphmapnav.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class DoubleExtsTests {

    @Test
    fun doubleV2() {
        assertEquals(2.0, 2.0.v2.x, 0.0)
        assertEquals(0.0, 2.0.v2.y, 0.0)
    }

    @Test
    fun doubleV3Direction() {
        assertEquals(2.0, 2.0.v3Direction.x, 0.0)
        assertEquals(0.0, 2.0.v3Direction.y, 0.0)
        assertEquals(0.0, 2.0.v3Direction.z, 0.0)
    }

    @Test
    fun doubleV3Point() {
        assertEquals(2.0, 2.0.v3Point.x, 0.0)
        assertEquals(0.0, 2.0.v3Point.y, 0.0)
        assertEquals(1.0, 2.0.v3Point.z, 0.0)
    }

    @Test
    fun doubleInvert() {
        assertEquals(-1.0, 1.0.invert(true), 0.0)
        assertEquals(1.0, (-1.0).invert(true), 0.0)
        assertEquals(1.0, 1.0.invert(false), 0.0)
        assertEquals(-1.0, (-1.0).invert(false), 0.0)
    }

    @Test
    fun ndcToScreen() {
        assertEquals(0.0, (-1.0).ndcToScreen(100.0, flip = false), 0.0)
        assertEquals(25.0, (-0.5).ndcToScreen(100.0, flip = false), 0.0)
        assertEquals(50.0, 0.0.ndcToScreen(100.0, flip = false), 0.0)
        assertEquals(75.0, 0.5.ndcToScreen(100.0, flip = false), 0.0)
        assertEquals(100.0, 1.0.ndcToScreen(100.0, flip = false), 0.0)
    }

    @Test
    fun screenToNdc() {
        assertEquals(-1.0, 0.0.screenToNdc(100.0, flip = false), 0.0)
        assertEquals(-0.5, 25.0.screenToNdc(100.0, flip = false), 0.0)
        assertEquals(0.0, 50.0.screenToNdc(100.0, flip = false), 0.0)
        assertEquals(0.5, 75.0.screenToNdc(100.0, flip = false), 0.0)
        assertEquals(1.0, 100.0.screenToNdc(100.0, flip = false), 0.0)
    }
}
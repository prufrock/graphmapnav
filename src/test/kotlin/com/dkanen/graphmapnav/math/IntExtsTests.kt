package com.dkanen.graphmapnav.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector3

class IntExtsTests {

    @Test
    fun intV2() {
        assertEquals(2.0, 2.v2.x, 0.0)
        assertEquals(0.0, 2.v2.y, 0.0)
    }

    @Test
    fun v3Direction() {
        assertEquals(Vector3(1.0, 0.0, 0.0), 1.v3Direction)
    }

    @Test
    fun v3Point() {
        assertEquals(Vector3(1.0, 0.0, 1.0), 1.v3Point)
    }
}
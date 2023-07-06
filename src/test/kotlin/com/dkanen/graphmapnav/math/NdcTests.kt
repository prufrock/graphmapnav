package com.dkanen.graphmapnav.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3

class NdcTests {
    @Test
    fun create() {
        val ndc = Ndc(listOf(Vector2(1.0, 1.0)))
        assertEquals(Vector2(1.0, 1.0), ndc.points.first())
    }
}
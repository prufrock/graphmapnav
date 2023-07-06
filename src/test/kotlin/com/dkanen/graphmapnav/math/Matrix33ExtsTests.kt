package com.dkanen.graphmapnav.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Matrix33
import org.openrndr.math.Vector3

class Matrix33ExtsTests {

    @Test
    fun orthographic() {
        val left = 0.0
        val right = 5.0
        val top = 0.0
        val bottom = 5.0
        val o = orthographic(left = 0.0, right = 5.0, top = 0.0, bottom = 5.0)
        // 0,0 in world space lands in the upper left corner of NDC
        assertEquals(Vector3(-1.0, 1.0, 1.0), o * Vector3(x = 0.0, y = 0.0, z = 1.0))
        // the middle of world space is the middle of NDC
        assertEquals(Vector3(0.0, 0.0, 1.0), o * Vector3(x = ((right - left) / 2), y = ((bottom - top) / 2), z = 1.0))
        // the maximums of world space is the lower left corner of NDC
        assertEquals(Vector3(1.0, -1.0, 1.0), o * Vector3(x = right, y = bottom, z = 1.0))
    }
}
package com.dkanen.graphmapnav.math

import com.dkanen.graphmapnav.game.Camera
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3

class Vector3ExtsTests {

    @Test
    fun worldToNdc() {
        val left = 0.0
        val right = 5.0
        val top = 0.0
        val bottom = 5.0
        val camera = Camera(left, right, top, bottom)

        // upper left
        assertEquals(Vector2(-1.0, 1.0), Vector3(0.0, 0.0, 1.0).worldToNdc(left, right, top, bottom))
        assertEquals(Vector2(-1.0, 1.0), Vector3(0.0, 0.0, 1.0).worldToNdc(camera))

        // the middle
        assertEquals(Vector2(0.0, 0.0), Vector3((right - left) / 2, (bottom - top) / 2, 1.0).worldToNdc(left, right, top, bottom))
        assertEquals(Vector2(0.0, 0.0), Vector3((right - left) / 2, (bottom - top) / 2, 1.0).worldToNdc(camera))

        // lower right
        assertEquals(Vector2(1.0, -1.0), Vector3(right, bottom, 1.0).worldToNdc(left, right, top, bottom))
        assertEquals(Vector2(0.0, 0.0), Vector3((right - left) / 2, (bottom - top) / 2, 1.0).worldToNdc(camera))
    }

    @Test
    fun minus() {
        assertEquals(Vector2(1.0, 1.0), Vector3(2.0, 2.0, 1.0) - Vector2(1.0, 1.0))
    }

    @Test
    fun plus() {
        assertEquals(Vector2(3.0, 3.0), Vector3(2.0, 2.0, 1.0) + Vector2(1.0, 1.0))
    }

    @Test
    fun Vector3Point() {
        assertEquals(Vector3(2.0, 2.0, 1.0), Vector3Point(2.0, 2.0))
    }
}
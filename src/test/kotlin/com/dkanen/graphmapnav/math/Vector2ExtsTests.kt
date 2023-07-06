package com.dkanen.graphmapnav.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class Vector2ExtsTests {

    @Test
    fun ndcToScreen() {
        // lower left origin
        // upper left ndc to upper left screen
        assertEquals(Vector2(0.0, 100.0), Vector2(-1.0, 1.0).ndcToScreen(width = 100.0, height = 100.0, flipY = false))

        // the middle of ndc is the middle of the screen.
        assertEquals(Vector2(50.0, 50.0), Vector2(0.0, 0.0).ndcToScreen(width = 100.0, height = 100.0))
        assertEquals(Vector2(50.0, 50.0), Vector2(0.0, 0.0).ndcToScreen(width = 100.0, height = 100.0, flipY = true))

        // upper left origin
        // upper left ndc to upper left screen
        assertEquals(Vector2(0.0, 0.0), Vector2(-1.0, 1.0).ndcToScreen(width = 100.0, height = 100.0, flipY = true))
    }
}
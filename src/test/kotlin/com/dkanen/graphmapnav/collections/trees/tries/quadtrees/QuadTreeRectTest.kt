package com.dkanen.graphmapnav.collections.trees.tries.quadtrees

import com.dkanen.graphmapnav.game.Rect
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class QuadTreeRectTest {
    @Test
    fun addRectInBounds() {
       val t: QuadTree = QuadTreeRect(boundary = Rect(Vector2(0.0, 0.0), Vector2(5.0, 5.0)), maxObjects = 2)

        assertTrue(t.add(Rect(1.0, 1.0, 2.0, 2.0)))
        assertTrue(t.add(Rect(2.5, 2.5, 3.0, 3.0)))
        assertTrue(t.add(Rect(2.8, 2.8, 2.9, 2.9)))

        val result = t.search(Rect(0.9, 0.9, 2.1, 2.1))

        assertEquals(1, result.count())
        assertEquals(Rect(1.0, 1.0, 2.0, 2.0), result.firstOrNull())
    }

    @Test
    fun addRectNotInBounds() {
        val t: QuadTree = QuadTreeRect(boundary = Rect(Vector2(0.0, 0.0), Vector2(5.0, 5.0)), maxObjects = 2)

        assertFalse(t.add(Rect(6.0, 6.0, 6.1, 6.1)))
    }

    @Test
    fun searchForOneMatchingRect() {
        val t: QuadTree = QuadTreeRect(boundary = Rect(Vector2(0.0, 0.0), Vector2(5.0, 5.0)), maxObjects = 2)

        assertTrue(t.add(Rect(1.0, 1.0, 2.0, 2.0)))

        val result = t.search(Rect(0.9, 0.9, 2.1, 2.1))

        assertEquals(1, result.count())
        assertEquals(Rect(1.0, 1.0, 2.0, 2.0), result.firstOrNull())
    }

    @Test
    fun addOneMoreThanMaxObjectsAtALevelAndStillFindIt() {
        val t: QuadTree = QuadTreeRect(boundary = Rect(Vector2(0.0, 0.0), Vector2(5.0, 5.0)), maxObjects = 2)

        assertTrue(t.add(Rect(1.0, 1.0, 2.0, 2.0)))
        assertTrue(t.add(Rect(2.5, 2.5, 3.0, 3.0)))
        assertTrue(t.add(Rect(2.8, 2.8, 2.9, 2.9)))

        val result = t.search(Rect(2.7, 2.7, 3.0, 3.0))

        assertEquals(1, result.count())
        assertEquals(Rect(2.8, 2.8, 2.9, 2.9), result.firstOrNull())
    }

    @Test
    fun addARectOnTheBoundaryBetweenSplitsAndStillFindIt() {
        val t: QuadTree = QuadTreeRect(boundary = Rect(Vector2(0.0, 0.0), Vector2(5.0, 5.0)), maxObjects = 2)

        // Fill up level 0
        assertTrue(t.add(Rect(1.0, 1.0, 2.0, 2.0)))
        assertTrue(t.add(Rect(2.5, 2.5, 3.0, 3.0)))

        // Add a rectangle on the boundary
        assertTrue(t.add(Rect(2.4, 2.4, 2.6, 2.6)))

        // Search on the boundary
        val result = t.search(Rect(2.4, 2.4, 2.7, 2.7))

        assertEquals(1, result.count())
        assertEquals(Rect(2.4, 2.4, 2.6, 2.6), result.firstOrNull())
    }
}
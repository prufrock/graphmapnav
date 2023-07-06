package com.dkanen.graphmapnav.game.map

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2


class TileMapTest {
    val n = Thing.NOTHING
    val p = Thing.PLAYER
    val w = Tile.WALL
    val e = Tile.EMPTY

    val mapData = MapData(width = 3,
        tiles = listOf(
            w, w, w,
            w, e, w,
            w, w, w,
        ),
        things = listOf(
            n, n, n,
            n, p, n,
            n, n, n,
        )
    )

    val map = TileMap(mapData)

    @Test
    fun getHeight() {
        assertEquals(3, map.height)
    }

    @Test
    fun getWidth() {
        assertEquals(3, map.width)
    }

    @Test
    fun getSize() {
        assertEquals(Vector2(3.0, 3.0), map.size)
    }

    @Test
    fun get() {
        assertNull(map.get(3,0))
        assertEquals(w, map.get(0,0))
    }

    @Test
    fun getThing() {
        assertEquals(n, map.getThing(0,1))
        assertEquals(p, map.getThing(1,1))
    }

    @Test
    fun getTiles() {
        assertEquals(9, map.tiles.count())
    }

    @Test
    fun getThings() {
        assertEquals(9, map.things.count())
    }
}
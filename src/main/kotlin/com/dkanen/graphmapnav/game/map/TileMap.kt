package com.dkanen.graphmapnav.game.map

import org.openrndr.math.Vector2

class TileMap(
    val tiles: List<Tile>,
    val things: List<Thing>,
    val width: Int,
) {

    constructor(tileMap: MapData) : this(tiles = tileMap.tiles, things = tileMap.things, width = tileMap.width)
    val height: Int
        get() = tiles.count() / width
    val size: Vector2
        get() = Vector2(width.toDouble(), height.toDouble())

    /**
     * Might want to consider having this blow up if you index into non-existent space.
     */
    fun get(x: Int, y: Int): Tile?  {
        var tile: Tile? = null
        if (x < width && y < height) {
            tile = tiles.getOrNull(y * width + x)
        }
        return tile
    } // access a one dimensional array with two dimensions

    /**
     * There is some danger of access a non-existent with this one so be careful!
     * It's normally used in places where it's highly likely the index exists but if that changes it may be worth having
     * a nullable return value or returning NOTHING.
     *
     * You really shouldn't index beyond this so for now it's going to blow up to let you know you did something wrong.
     */
    fun getThing(x: Int, y: Int): Thing = things[y * width + x] // access a one dimensional array with two dimensions
}



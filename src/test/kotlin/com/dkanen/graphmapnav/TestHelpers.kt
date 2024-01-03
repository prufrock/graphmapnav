package com.dkanen.graphmapnav

import com.dkanen.graphmapnav.game.GraphWorld
import com.dkanen.graphmapnav.game.map.MapData
import com.dkanen.graphmapnav.game.map.Thing
import com.dkanen.graphmapnav.game.map.Tile
import com.dkanen.graphmapnav.game.map.TileMap
import com.dkanen.graphmapnav.collections.graphs.AdjacencyList
import com.dkanen.graphmapnav.game.Input
import org.openrndr.math.Vector2

fun map3x3(): TileMap {
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

    return TileMap(mapData)
}
fun smallTestWorld() = GraphWorld(
    entityGraph = AdjacencyList(),
    )

fun worldWithMap(map: TileMap) = GraphWorld(
    entityGraph = AdjacencyList(),
)

fun inputNoChange() = Input(0.0, Vector2(0.0), Vector2(0.0), null, false)
fun inputTimeStep(timeStep: Double) = Input(timeStep, Vector2(0.0), Vector2(0.0), null, false)

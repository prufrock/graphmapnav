package com.dkanen.graphmapnav.game.map

data class MapData(
    val width: Int,
    val tiles: List<Tile>,
    val things: List<Thing>,
)

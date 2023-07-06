package com.dkanen.graphmapnav.game.map

enum class Tile {
   EMPTY, // empty
   WALL // box
}

fun Tile.isWall(): Boolean = when(this) {
       Tile.WALL -> true
       else -> false
   }

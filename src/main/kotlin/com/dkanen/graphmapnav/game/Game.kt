package com.dkanen.graphmapnav.game

class Game(val world: World) {

    fun update(input: Input) {
        world.update(input)
    }
}
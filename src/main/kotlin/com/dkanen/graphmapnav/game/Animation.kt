package com.dkanen.graphmapnav.game

class Animation(val frames: List<String>, val duration: Double, var time: Double = 0.0) {
    fun update(input: GameInput) {
        if (!isComplete) {
            time += input.input.timeStep
        } else {
            // loop the animation
            time = 0.0
        }
    }

    val texture: String
        get() {
            if (duration <= 0) {
                return frames[0]
            }
            val t = (time % duration) / duration
            return frames[(frames.count() * t).toInt()]
        }

    val isComplete = time >= duration
}

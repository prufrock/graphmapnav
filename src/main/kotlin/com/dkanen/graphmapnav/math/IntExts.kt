package com.dkanen.graphmapnav.math

import com.dkanen.graphmapnav.game.ecs.entities.EntityId
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3


val Int.v2: Vector2
    get() = Vector2(x = this.toDouble(), y = 0.0)

/**
 * The homogenous coordinate is 0 because direction vectors don't move.
 */
val Int.v3Direction: Vector3
    get() = Vector3(x = this.toDouble(), y = 0.0, z = 0.0)

/**
 * The homogenous coordinate has to be 1 on points, so they can be translated.
 */
val Int.v3Point: Vector3
    get() = Vector3(x = this.toDouble(), y = 0.0, z = 1.0)


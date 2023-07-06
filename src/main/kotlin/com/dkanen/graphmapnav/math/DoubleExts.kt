package com.dkanen.graphmapnav.math

import org.openrndr.math.Vector2
import org.openrndr.math.Vector3

/**
 * Conditionally returns the additive inverse.
 */
fun Double.invert(bool: Boolean): Double = if(bool) {
    this * -1
} else {
    this
}

fun Double.ndcToScreen(size: Double, flip: Boolean = false): Double {
    // Add 1(unless the dimension is inverted in screen space) because ndc runs between -1 and 1 and this moves -1 to 0 which is the lowest value on the screen
    // Split the size of the dimension in half because ndc has the origin in the middle so 1 + 1 = 2 * halfSize = size
    return (((this.invert(flip) + 1) * (size * (0.5))))
}

fun Double.screenToNdc(size: Double, flip: Boolean = false): Double {
    return ((this / ( size * 0.5)) - 1).invert(flip)
}

val Double.v2: Vector2
    get() = Vector2(x = this, y = 0.0)

/**
 * The homogenous coordinate is 0 because direction vectors don't move.
 */
val Double.v3Direction: Vector3
    get() = Vector3(x = this, y = 0.0, z = 0.0)

/**
 * The homogenous coordinate has to be 1 on points, so they can be translated.
 */
val Double.v3Point: Vector3
    get() = Vector3(x = this, y = 0.0, z = 1.0)
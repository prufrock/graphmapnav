package com.dkanen.graphmapnav.math

import com.dkanen.graphmapnav.game.Camera
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3

/**
 * left and right are in world coordinates
 */
fun Vector3.worldToNdc(left: Double, right: Double, top: Double, bottom: Double): Vector2 {
    val xy = (orthographic(left, right, top, bottom) * this).xy
    return xy
}

fun Vector3.worldToNdc(camera: Camera): Vector2 {
    val xy = (camera.project() * this).xy
    return xy
}

fun Vector3.ndcToWorld(camera: Camera): Vector3 = camera.project().inversed * this

operator fun Vector3.minus(other: Vector2): Vector2 = Vector2(x = this.x - other.x, y = this.y - other.y)
operator fun Vector3.plus(other: Vector2): Vector2 = Vector2(x = this.x + other.x, y = this.y + other.y)

/**
 * Creates a Vector 3 representing a point. The homogeneous coordinate is 1, so it can be translated.
 */
fun Vector3Point(x: Double, y: Double): Vector3 = Vector3(x, y, 1.0)
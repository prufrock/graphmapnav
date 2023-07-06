package com.dkanen.graphmapnav.game

import com.dkanen.graphmapnav.math.orthographic
import org.openrndr.math.Matrix33
import org.openrndr.math.Vector2
import org.openrndr.panel.elements.round

/**
 * The camera is essentially responsible for converting the World into NDC space and back, but it lives in World space.
 * Thus, it's a portal between the World and NDC.
 * All parameters are specified in World space.
 */
class Camera(private var left: Double, private var right: Double, private var top: Double, private var bottom: Double) {
    val width: Double
        get() = (right - left).round(0)

    fun project(): Matrix33 = orthographic(left, right, top, bottom)
    fun translate(cameraVelocity: Vector2) {
        left += cameraVelocity.x
        right += cameraVelocity.x
        top += cameraVelocity.y
        bottom += cameraVelocity.y
    }
}
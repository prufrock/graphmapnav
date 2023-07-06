package com.dkanen.graphmapnav.math

import org.openrndr.math.*
import org.openrndr.math.transforms.scale
import org.openrndr.math.transforms.translate

/**
 * left, right, top, and bottom are in world coordinates
 */
fun orthographic(left: Double, right: Double, top: Double, bottom: Double): Matrix33 = Matrix33.fromColumnVectors(c0 = Vector3(
    x = (2 / (right - left)),
    y = 0.0,
    z = 0.0
), c1 = Vector3(
    x = 0.0,
    y = (2 / (top - bottom)),
    z = 0.0
), c2 = Vector3(
    x = -((right + left) / (right - left)),
    y = -((top + bottom) / (top - bottom)),
    z = 1.0
)
)

/**
 * Create a translation matrix from the vector.
 *
 * [translation] The amount to translate
 */
fun Matrix33.Companion.translate(translation: Vector3): Matrix33 {
    return translate(translation.x, translation.y)
}

/**
 * Create a translation matrix.
 *
 * [x] translate direction
 * [y] translate direction
 */
fun Matrix33.Companion.translate(x: Double, y: Double): Matrix33 {
    return fromColumnVectors(
        Vector3.UNIT_X,
        Vector3.UNIT_Y,
        Vector3(x, y, 1.0)
    )
}

fun Matrix33.Companion.translate(translate: Vector2): Matrix33 = translate(translate.x, translate.y)

fun Matrix33.Companion.scale(x: Double, y: Double): Matrix33 = fromColumnVectors(Vector3(x, 0.0, 0.0), Vector3(0.0, y, 0.0), Vector3(0.0, 0.0, 1.0))

fun Matrix33.Companion.transform(fixedPoint: Vector2, transformation: Matrix33): Matrix33 = translate(fixedPoint) * transformation * translate(-fixedPoint)

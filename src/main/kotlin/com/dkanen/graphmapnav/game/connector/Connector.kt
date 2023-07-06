package com.dkanen.graphmapnav.game.connector

import org.openrndr.color.ColorRGBa
import org.openrndr.math.*


/**
 * Connectors are not interactive. They can't be collided with or clicked on.
 */
class Connector(val lines: List<LineSegment>, val color: ColorRGBa, val weight: Double = 1.0)

data class LineSegment(val start: Vector2, var end: Vector2) {

}
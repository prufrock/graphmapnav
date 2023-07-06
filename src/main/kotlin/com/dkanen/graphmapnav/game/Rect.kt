package com.dkanen.graphmapnav.game

import org.openrndr.math.Vector2

data class Rect(var min: Vector2, var max: Vector2) {

    val area: Double
        get() = height * width

    val height: Double
        get() = max.y - min.y

    val width: Double
        get() = max.x - min.x

    private val corners: List<Vector2>
        get() = listOf(Vector2(min.x, min.y), Vector2(min.x, max.y), Vector2(max.x, max.y), Vector2(max.x, min.y))

    constructor(minX: Double, minY: Double, maxX: Double, maxY: Double) : this(min = Vector2(minX, minY), max = Vector2(maxX, maxY))

    fun intersection(rect: Rect): Vector2? {
        val left = Vector2(this.max.x - rect.min.x, 0.0) // world
        if (left.x <= 0) {
            return null
        }
        val right = Vector2(this.min.x - rect.max.x, 0.0) // world
        if (right.x >= 0) {
            return null
        }
        val up = Vector2(0.0, max.y - rect.min.y) // world
        if (up.y <= 0) {
            return null
        }
        val down = Vector2(0.0, min.y - rect.max.y) // world
        if (down.y >= 0) {
            return null
        }

        // sort by length with the smallest first and grab that one
        return listOf(left, right, up, down).minByOrNull { it.length }!!
    }

    fun contains(x: Double, y: Double): Boolean = contains(Vector2(x, y))

    fun contains(p: Vector2): Boolean = !(min.x > p.x || max.x < p.x || min.y > p.y || max.y < p.y)

    fun contains(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean = contains(Rect(minX, minY, maxX, maxY))

    fun contains(r: Rect): Boolean {
        // This can't contain the rectangle if it's not big enough.
        if (area < r.area) {
            return false
        }

        // All 4 corners must be inside this rectangle.
        return r.corners.count { contains(it) } == 4
        // can also find this by checking the distance from the midpoint...some day check to see if it's faster
    }

    fun divideHorizontal(): Pair<Rect, Rect> {
        val size = (max.y - min.y) / 2
        return Pair(
                Rect(min, Vector2(max.x, max.y - size)), Rect(Vector2(min.x, min.y + size), max)
        )
    }

    fun divideVertical(): Pair<Rect, Rect> {
        val size = (max.x - min.x) / 2
        return Pair(
            Rect(min, Vector2(max.x - size, max.y)), Rect(Vector2(min.x + size, min.y), max)
        )
    }
}

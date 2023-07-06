package com.dkanen.graphmapnav.game.ecs.components.physics

import com.dkanen.graphmapnav.game.ecs.entities.eslug
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.openrndr.math.Vector2

class CollisionTest {

    @Test
    fun intersection() {
        val collisionTwo = Collision("c1".eslug, Vector2(0.5), 0.5)

        assertNotNull(collisionTwo.intersection(Collision("c2".eslug, Vector2(0.25), 0.25)))
        assertEquals(Vector2(1.0, 0.0), collisionTwo.intersection(Collision("c3".eslug, Vector2(0.5), radius = 0.5)))
    }
}
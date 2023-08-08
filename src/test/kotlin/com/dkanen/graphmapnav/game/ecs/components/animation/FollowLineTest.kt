package com.dkanen.graphmapnav.game.ecs.components.animation

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.components.Component
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.entities.eslug
import com.dkanen.graphmapnav.game.ecs.messages.UpdatePosition
import com.dkanen.graphmapnav.inputNoChange
import com.dkanen.graphmapnav.inputTimeStep
import com.dkanen.graphmapnav.math.Vector20
import com.dkanen.graphmapnav.smallTestWorld
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.openrndr.math.Vector2

class FollowLineTest {

    @Test
    fun update() {
        val entity = Entity(slug = "pew-pew".eslug)

        entity.components += FollowLine(
            entity.slug,
            speed = 0.1,
            direction = Vector2(1.0, 0.0)
        )

        val mockComponent = MockComponent(entity.slug, Vector20())
        entity.components += mockComponent

        entity.update(smallTestWorld(), GameInput(clickedEntity = null, input = inputNoChange()))

        assertEquals(Vector20(), mockComponent.currentPosition)

        entity.update(smallTestWorld(), GameInput(clickedEntity = null, input = inputTimeStep(1.0)))

        assertEquals(Vector2(0.1, 0.0), mockComponent.currentPosition)

        entity.update(smallTestWorld(), GameInput(clickedEntity = null, input = inputTimeStep(1.0)))

        assertEquals(Vector2(0.2, 0.0), mockComponent.currentPosition)
    }

    class MockComponent(override val entitySlug: EntitySlug, var currentPosition: Vector2): Component {
        override fun update(entity: Entity, world: World, input: GameInput) {
        }

        override fun <T> receive(message: T) {
            when(message) {
                is UpdatePosition -> {
                    currentPosition += message.displacement
                }
            }
        }
    }
}
package com.dkanen.graphmapnav.game.ecs.components.animation

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.messages.UpdatePosition
import org.openrndr.math.Vector2
import org.openrndr.math.times

class FollowLine(override val entitySlug: EntitySlug, val speed: Double, val direction: Vector2): Animation {
    override fun update(entity: Entity, world: World, input: GameInput) {
        val displacement = (speed * input.input.timeStep * direction)

        entity.receive(UpdatePosition(displacement = displacement, this))
    }
}
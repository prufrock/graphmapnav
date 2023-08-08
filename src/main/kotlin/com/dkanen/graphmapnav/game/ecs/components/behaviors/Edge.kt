package com.dkanen.graphmapnav.game.ecs.components.behaviors

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.components.Component
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.messages.UpdateEnd
import com.dkanen.graphmapnav.game.ecs.messages.UpdateStart
import com.dkanen.graphmapnav.math.Vector20
import org.openrndr.math.Vector2

class Edge(
    override val entitySlug: EntitySlug,
    val sourceEntitySlug: EntitySlug,
    val destinationEntitySlug: EntitySlug,
    var sourcePosition: Vector2 = Vector20(),
    var destinationPosition: Vector2 = Vector20(),
    ) : Component {
    override fun update(entity: Entity, world: World, input: GameInput) {
        world.entityManager.find(sourceEntitySlug)?.let { e ->
            sourcePosition = e.graphics?.position ?: sourcePosition
            entity.receive(UpdateStart(sourcePosition, this))
        }

        world.entityManager.find(destinationEntitySlug)?.let { e ->
            destinationPosition = e.graphics?.position ?: destinationPosition
            entity.receive(UpdateEnd(destinationPosition, this))
        }
    }
}
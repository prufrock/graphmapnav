package com.dkanen.graphmapnav.game.ecs.components.input

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.components.Component
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.messages.UpdatePosition

class Input(override val entitySlug: EntitySlug) : Component {
    override fun update(entity: Entity, world: World, input: GameInput) {
        if (input.inputTargets.containsKey(entitySlug)) {
            input.inputTargets[entitySlug]?.let {
                entity.receive(UpdatePosition(it.velocity, this))
            }
        }
    }
}
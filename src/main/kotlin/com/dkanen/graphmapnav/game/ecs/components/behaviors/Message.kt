package com.dkanen.graphmapnav.game.ecs.components.behaviors

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug

class Message(override val entitySlug: EntitySlug, val source: EntitySlug, val destination: EntitySlug, val type: Type = Type.Request) : Behavior {
    override fun update(entity: Entity, world: World, input: GameInput) {
    }

    enum class Type {
        Request,
        Response
    }
}
package com.dkanen.graphmapnav.game.ecs.components

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug

/**
 * Component may just be a base type to identify anything that can be a component.
 */
interface Component {
    val entitySlug: EntitySlug

    fun update(entity: Entity, world: World, input: GameInput)

    fun <T>receive(message: T) {

    }

    fun tickReset() {

    }

    fun finalizeUpdate() {

    }
}
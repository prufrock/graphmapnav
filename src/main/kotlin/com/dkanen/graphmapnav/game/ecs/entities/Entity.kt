package com.dkanen.graphmapnav.game.ecs.entities

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.components.Component
import com.dkanen.graphmapnav.game.ecs.components.cameras.Camera
import com.dkanen.graphmapnav.game.ecs.components.behaviors.Edge
import com.dkanen.graphmapnav.game.ecs.components.graphics.Graphics
import com.dkanen.graphmapnav.game.ecs.components.input.Input
import com.dkanen.graphmapnav.game.ecs.components.physics.Collision
import com.dkanen.graphmapnav.game.ecs.components.animation.FollowLine
import com.dkanen.graphmapnav.game.ecs.messages.Message

/**
 * An entity is incredibly simple. It merely has an idea.
 */
 data class Entity(
    var id: EntityId = EntityId(-1),
    var slug: EntitySlug = EntitySlug("none"),
    var deleted: Boolean = false,
    // Don't update this entity while updating all the rest of the entities
    // This is for entities that are pulled out of the list to be updated
    // Going to cause a cache-miss until individual components are processed in order
    var arbitraryUpdate: Boolean = false,
    var components: List<Component> = listOf(),
    ) {

    val graphics: Graphics?
        get() = components.filterIsInstance<Graphics>().firstOrNull()

    val collision: Collision?
        get() = components.filterIsInstance<Collision>().firstOrNull()

    val camera: Camera?
        get() = components.filterIsInstance<Camera>().firstOrNull()

    fun update(world: World, gameInput: GameInput) {
        components.forEach {
            it.update(this, world, gameInput)
        }
    }

    fun <T: Message>receive(message: T) {
        components.forEach { it.receive(message) }
    }

    fun finalizeTick() {
        components.forEach { it.finalizeUpdate() }
    }
}

data class EntitySlug(val value: String) {
    override fun toString(): String = value
}

val String.eslug: EntitySlug
    get() = EntitySlug(this)

data class EntityId(val value: Int)

val Int.eid: EntityId
    get() = EntityId(this)
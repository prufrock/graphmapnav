package com.dkanen.graphmapnav.game.ecs.components.physics

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.Rect
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.components.Component
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.messages.CollidedSet
import com.dkanen.graphmapnav.game.ecs.messages.CollidedWith
import com.dkanen.graphmapnav.game.ecs.messages.UpdatePosition
import com.dkanen.graphmapnav.game.event.Event
import com.dkanen.graphmapnav.math.Vector2O
import org.openrndr.math.Vector2

data class Collision(override val entitySlug: EntitySlug, var position: Vector2, val radius: Double, val parent: Entity? = null, val collisionResponse: Boolean = false) : Component {

    val parentPosition: Vector2
        get() {
            // TODO: need a single place to get the position from
            return parent?.collision?.position ?: parent?.graphics?.position ?: Vector2O()
        }

    val rect: Rect
        get() = Rect((position.x + parentPosition.x) - radius, (position.y + parentPosition.y) - radius, (position.x + parentPosition.x) + radius, (position.y + parentPosition.y) + radius)

    override fun update(entity: Entity, world: World, input: GameInput) {
        // eventually needs to move in response to input
        if (input.clickedEntity?.slug == entitySlug) {
            println("clicked $entitySlug")
        }

        if (collisionResponse) {
            var attempts = 10
            var intersection = world.entityManager.largestIntersection(this)
            var totalIntersection = Vector2O()
            while (attempts > 0 && intersection != null) {
                totalIntersection -= intersection
                position -= intersection
                attempts -= 1
                intersection = world.entityManager.largestIntersection(this)
            }
            if (totalIntersection != Vector2O()) {
                entity.receive(UpdatePosition(totalIntersection, this))
            }
        }

        val collisions = mutableSetOf<EntitySlug>()
        world.entityManager.collisions.forEach { otherCollision ->
            if (entitySlug == otherCollision.entitySlug) {
                return@forEach
            }

            val intersection = intersection(otherCollision)
            if (intersection != null) {
                entity.receive(CollidedWith(otherCollision.entitySlug))
                collisions.add(otherCollision.entitySlug)
            }
        }

        if (collisions.isNotEmpty()) {
            entity.receive(CollidedSet(collisions))
            collisions.forEach {
                world.eventManager.emit(Event.Collision(entitySlug, it))
            }
        }
    }

    fun intersection(collision: Collision): Vector2? {
        return rect.intersection(collision.rect)
    }

    override fun <T> receive(message: T) {
        when(message) {
            is UpdatePosition -> {
                if (message.from !== this) {
                    position += message.displacement
                }
            }
        }
    }
}

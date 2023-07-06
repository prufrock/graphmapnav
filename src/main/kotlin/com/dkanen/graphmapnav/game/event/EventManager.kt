package com.dkanen.graphmapnav.game.event

import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.entities.eslug

interface EventManager {
    fun emit(event: Event)
}

class SimpleEventManager : EventManager {
    private val collisionResponders: MutableList<Responder<Event.Collision>> = mutableListOf()

    init {
        collisionResponders.add(Responder(Matcher(listOf({ e: Event.Collision -> e.entitySlug == "click-location".eslug }, { e: Event.Collision -> e.otherEntitySlug == "Services-0".eslug }))) {
            println(
                "Collision between ${it.entitySlug} and ${it.otherEntitySlug}"
            )
        })

        EventHandler<Event.Collision>(collisionResponders[0])
    }
    override fun emit(event: Event) {
        when (event) {
            is Event.Collision -> {
                collisionResponders.forEach { it.respond(event) }
            }
        }
    }
}

sealed class Event {
    data class Collision(val entitySlug: EntitySlug, val otherEntitySlug: EntitySlug) : Event()
}

typealias Predicate<T> = (T) -> Boolean
class Matcher<T: Event>(val predicates: List<Predicate<T>>) {
    fun matches(event: T): Boolean {
        return predicates.all { it(event) }
    }
}

class Responder<T: Event>(val matcher: Matcher<T>, val action: (T) -> Unit) {
    fun respond(event: T) {
        if (matcher.matches(event)) {
            action(event)
        }
    }
}

class EventHandler<T: Event>(val responder: Responder<T>) {
    fun handler(world: World, event: T) {
        responder.respond(event)
    }
}
package com.dkanen.graphmapnav.game.ecs.manager.tables

import com.dkanen.graphmapnav.collections.linkedlist.LinkedListIterator
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntityId
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.entities.eslug
import java.util.*

// TODO: Entities need to inform the Pool when they are deleted.
class EntityPool(val size: Int) {

    private val changesSinceLastIndex: MutableSet<Entity> = mutableSetOf()

    /**
     * I'm not sure about the performance of slice, but it creates a copy of the list. Structural
     * changes to the list should not change it.
     */
    val activeEntities: List<Entity>
        get() = pool.slice(0 until firstInactiveIndex)

    private val pool: MutableList<Entity>

    private var firstInactiveIndex = 0

    init {
        pool = MutableList(size) {
            val slug = EntitySlug("e$it")
            Entity(id = EntityId(it), slug = slug, deleted = true)
        }
    }

    fun elementAtOrNull(index: EntityId): Entity? = pool.getOrNull(index.value)

    fun create(): Entity? {
        if (firstInactiveIndex >= size) {
            return null
        }

        // TODO: Eventually refactor Entity so it only needs to start with an ID which is the EntityID.
        val entity = Entity(id = EntityId(firstInactiveIndex), slug = "".eslug)

        pool[firstInactiveIndex] = entity

        firstInactiveIndex++

        return entity
    }

    fun delete(entity: Entity) {
        entity.deleted = true

        firstInactiveIndex--

        // keep the pool sorted by with active entities first.
        Collections.swap(pool, entity.id.value, firstInactiveIndex)

        val lastId = entity.id
        entity.id = EntityId(firstInactiveIndex)
        elementAtOrNull(lastId)?.apply {
            id = lastId
            changesSinceLastIndex.add(this)
        }
    }

    /**
     * Allows the caller to pass in a lambda to update an index. For example the EntityTable uses it to index by slug.
     *
     * Only the pool should care that EntityId's are ordered Ints.
     */
    fun updateIndex(update: (EntityId, Entity) -> Unit) {
        pool.forEachIndexed { index, entity ->
            update(EntityId(index), entity)
        }
    }

    fun indexChanges(update: (Entity) -> Unit) {
        changesSinceLastIndex.forEach { entity ->
            update(entity)
        }

        changesSinceLastIndex.clear()
    }
}
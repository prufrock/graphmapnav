package com.dkanen.graphmapnav.game.ecs.manager.tables

import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntityId
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug

class EntityTable(private val pool: EntityPool = EntityPool(size = 100)): Table {

    val activeEntities: List<Entity>
        get() = pool.activeEntities

    private val idxEntitiesByEntitySlug: MutableMap<EntitySlug, EntityId> = mutableMapOf()

    init {
       pool.updateIndex { i: EntityId, entity: Entity ->
           if (!entity.deleted) {
               idxEntitiesByEntitySlug[entity.slug] = i
           }
       }
    }

    fun find(slug: EntitySlug) = idxEntitiesByEntitySlug[slug]?.let { id -> pool.elementAtOrNull(id) }

    fun create(slug: EntitySlug) = pool.create()?.apply {
        this.slug = slug
        idxEntitiesByEntitySlug[this.slug] = id
    }

    fun delete(entity: Entity): Unit {
        entity.deleted = true
        idxEntitiesByEntitySlug.remove(entity.slug)
        pool.delete(entity)
        pool.indexChanges { entity: Entity ->
            if (!entity.deleted) {
                idxEntitiesByEntitySlug[entity.slug] = entity.id
            }
        }
    }
}


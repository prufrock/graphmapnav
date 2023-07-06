package com.dkanen.graphmapnav.game.ecs.manager.tables

import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.entities.eid
import com.dkanen.graphmapnav.game.ecs.entities.eslug
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class EntityPoolTest {

    @Test
    fun `create when the table is full returns null`() {
        val pool = EntityPool(1);
        assertNotNull(pool.create())
        assertNull(pool.create())
    }

    @Test
    fun `create when there's room returns an entity`() {
        val pool = EntityPool(1);
        val entity = pool.create()!!

        assertEquals(0.eid, entity.id)
    }

    @Test
    fun `can create an entity after deleting one`() {
        val pool = EntityPool(1);
        val entity = pool.create()!!

        entity.slug = EntitySlug("first")
        assertEquals(0.eid, entity.id)

        pool.delete(entity)

        val entityTwo = pool.create()!!

        // notice how the index stays the same but the entity gets replaced
        assertEquals(0.eid, entityTwo.id)
        assertEquals("".eslug, entityTwo.slug)
    }
}
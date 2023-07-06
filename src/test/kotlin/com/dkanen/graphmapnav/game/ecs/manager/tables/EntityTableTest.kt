package com.dkanen.graphmapnav.game.ecs.manager.tables

import com.dkanen.graphmapnav.game.ecs.entities.EntityId
import com.dkanen.graphmapnav.game.ecs.entities.eslug
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class EntityTableTest {

    @Test
    fun `create when the table is full`() {
        val table = EntityTable(EntityPool(1))
        assertNotNull(table.create("Samus".eslug))
        assertNull(table.create("Ridley".eslug))
    }

    @Test
    fun find() {
        val table = EntityTable(EntityPool(1))

        val entityId = "Samus".eslug
        table.create(entityId)

        assertEquals(entityId, table.find(entityId)?.slug)
    }

    @Test
    fun `delete an entity`() {
        val table = EntityTable(EntityPool(1))

        val entity = table.create("Samus".eslug)
        assertNotNull(entity)
        assertNull(table.create("Ridley".eslug))
        table.delete(entity!!)
        assertNotNull(table.create("Metroid".eslug))
    }

    @Test
    fun `delete second to last entity and fix the index`() {
        val table = EntityTable(EntityPool(3))

        table.create("Samus".eslug)!!
        val entityTwo = table.create("Ridley".eslug)!!
        val entityThree = table.create("Mother-Brain".eslug)!!
        assertEquals(EntityId(2), entityThree.id)
        assertEquals("Mother-Brain".eslug, table.find("Mother-Brain".eslug)!!.slug)

        table.delete(entityTwo)

        assertEquals(EntityId(1), entityThree.id)
        assertEquals("Mother-Brain".eslug, table.find("Mother-Brain".eslug)!!.slug)
    }

    @Test
    fun `delete the last entity and fix the index`() {
        val table = EntityTable(EntityPool(3))

        table.create("Samus".eslug)!!
        table.create("Ridley".eslug)!!
        val entityThree = table.create("Mother-Brain".eslug)!!
        assertEquals(EntityId(2), entityThree.id)
        assertEquals("Mother-Brain".eslug, table.find("Mother-Brain".eslug)!!.slug)

        table.delete(entityThree)

        assertEquals(EntityId(2), entityThree.id)
        assertNull(table.find("Mother-Brain".eslug))
    }
}
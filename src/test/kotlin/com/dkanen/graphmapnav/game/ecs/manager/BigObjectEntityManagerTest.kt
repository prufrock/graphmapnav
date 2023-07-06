package com.dkanen.graphmapnav.game.ecs.manager

import com.dkanen.graphmapnav.collections.graphs.AdjacencyList
import com.dkanen.graphmapnav.collections.graphs.EdgeType
import com.dkanen.graphmapnav.game.Node
import com.dkanen.graphmapnav.game.actor.Square
import com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons.ToggleButtonCfg
import com.dkanen.graphmapnav.game.ecs.components.graphics.Graphics
import com.dkanen.graphmapnav.game.ecs.components.graphics.RndrGraphics
import com.dkanen.graphmapnav.game.ecs.components.physics.Collision
import com.dkanen.graphmapnav.game.ecs.entities.eslug
import com.dkanen.graphmapnav.math.Vector2O
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2

class BigObjectEntityManagerTest {

    @Test
    fun createDecoration() {
        val manager = BigObjectEntityManager()

        manager.createDecoration("1".eslug, RndrGraphics("1".eslug, Vector2O(), 0.5, Square(), colorStroke = ColorRGBa.BLACK, colorFill = ColorRGBa.BLACK))
        
        assertEquals("1".eslug, manager.activeEntities.first().slug, "Did the decoration get added to the entities?")
    }

    @Test
    fun createProp() {
        val manager = BigObjectEntityManager()

        manager.createProp("1".eslug,
            collision = Collision("1".eslug, Vector2(0.5), radius = 0.5),
            graphicsComponent = RndrGraphics("1".eslug, Vector2(0.5), 0.5, Square(), colorStroke = ColorRGBa.BLACK, colorFill = ColorRGBa.BLACK)
        )

        assertEquals("1".eslug, manager.activeEntities.first().slug, "Did the prop get added to the entities?")

        val other = manager.createProp("2".eslug, Vector2(0.5), radius = 0.5)

        manager.activeEntities.forEach {
            // may want to find a place to tuck the id check into
            if (other.slug != it.slug) {
                assertNotNull(other.collision!!.intersection(it.collision!!))
                assertEquals(Vector2(1.0, 0.0), other.collision!!.intersection(it.collision!!))
            }
        }
    }

    @Test
    fun createToggleButton() {
        val manager = BigObjectEntityManager()
        val button = manager.createToggleButton(
            slug = "b1".eslug,
            position = Vector2(1.0, 0.0),
            radius =  0.5,
            toggleButtonCfg = ToggleButtonCfg()
        )

        assertEquals("b1".eslug, button.slug)
    }

    @Test
    fun largestIntersectedEntity() {
        var manager11 = BigObjectEntityManager()
        manager11.createProp("b11".eslug, Vector2(2.0), 0.5)
        manager11.createProp("b12".eslug, Vector2(4.0), 0.5)
        val prop11 = manager11.createProp("p11".eslug, Vector2(2.5), 0.5)

        val found11 = manager11.largestIntersectedEntity(prop11)

        assertEquals("b11".eslug, found11?.slug, "Should collide with the one button")

        var manager21 = BigObjectEntityManager()
        manager21.createProp("b21".eslug, Vector2(2.0), 0.5)
        manager21.createProp("b22".eslug, Vector2(2.2), 0.5)
        val prop21 = manager21.createProp("p21".eslug, Vector2(2.5), 0.5)

        val found21 = manager21.largestIntersectedEntity(prop21)

        assertEquals("b22".eslug, found21?.slug, "Should collide with largest intersection")
    }

    @Test
    fun createFromGraphOneVertex() {
        val manager = BigObjectEntityManager()
        val graph = AdjacencyList<Node<String>>().also {
            it.createVertex(Node("Tallon Overworld", Vector2O()))
        }

        manager.createFromGraph("t", graph, Vector2O())

        assertEquals(2, manager.activeEntities.count(), "Should the node plus the label.")
        assertEquals("t-0".eslug, manager.activeEntities.first().slug)
        assertEquals("t-0_label".eslug, manager.activeEntities[1].slug)

        val graphics: MutableList<Graphics> = mutableListOf();

        manager.sceneGraph.render {
            graphics.add(it.data)
        }

        assertNotNull(graphics.firstOrNull{ it.entitySlug == "t-0".eslug })
        assertNotNull(graphics.firstOrNull{ it.entitySlug == "t-0_label".eslug })
    }

    @Test
    fun createFromGraphTwoVertices() {
        val manager = BigObjectEntityManager()
        val graph = AdjacencyList<Node<String>>().also {
            val v0 = it.createVertex(Node("Tallon Overworld", Vector2O()))
            val v1 = it.createVertex(Node("Magmoor", Vector2O()))
            it.add(EdgeType.DIRECTED, v0, v1, null)
        }

        manager.createFromGraph("t", graph, Vector2O())

        assertEquals(5, manager.activeEntities.count(), "2 labels 2 nodes 1 edge")
        assertEquals("t-0_edge_t-1_s".eslug, manager.activeEntities[0].slug)
        assertEquals("t-0".eslug, manager.activeEntities[1].slug)
        assertEquals("t-0_label".eslug, manager.activeEntities[2].slug)

        val graphics: MutableList<Graphics> = mutableListOf();

        manager.sceneGraph.render {
            graphics.add(it.data)
        }

        assertNotNull(graphics.firstOrNull{ it.entitySlug == "t-0".eslug })
        assertNotNull(graphics.firstOrNull{ it.entitySlug == "t-0_label".eslug })
    }
}
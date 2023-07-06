package com.dkanen.graphmapnav.collections.graphs.scene

import com.dkanen.graphmapnav.collections.graphs.*
import com.dkanen.graphmapnav.game.actor.Point
import com.dkanen.graphmapnav.game.ecs.components.graphics.renderModel
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.entities.eslug
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Matrix33
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import com.dkanen.graphmapnav.game.ecs.components.graphics.RndrGraphics as Graphics


private val Vector2.v3p: Vector3
    get() = Vector3(x, y, 1.0)

internal class SceneGraphTest {
    @Test
    fun singleChildNodeGetsParentTransformation() {
        val g: Scene = SceneGraph()

        val sq1 = g.createVertex(createGraphics("sq1".eslug))
        val sq2 = g.createVertex(createGraphics("sq2".eslug))

        g.addChild(sq1, sq2)

        sq1.data.position = Vector2(2.0, 0.0)

        val nodes: MutableMap<EntitySlug, Graphics> = mutableMapOf()
        g.render {
            nodes[it.data.entitySlug] = it.data as Graphics
        }

        assertEquals(Vector3(2.0, 0.0, 1.0), nodes[sq1.entityId]!!.renderModel().first())
        assertEquals(Vector3(2.0, 0.0, 1.0), nodes[sq1.entityId]!!.renderModel().first())
    }

    @Test
    fun twoChildrenEachWithChildrenTheyAllGetRootNodeButOnlyTheirParentsTransformations() {
        val g: Scene = SceneGraph()

        val sq1 = g.createVertex(createGraphics("sq1".eslug))

        val sq2 = g.createVertex(createGraphics("sq2".eslug))
        val sq2a = g.createVertex(createGraphics("sq2a".eslug))

        val sq3 = g.createVertex(createGraphics("sq3".eslug))
        val sq3a = g.createVertex(createGraphics("sq3a".eslug))

        g.addChild(sq1, sq2)
        g.addChild(sq2, sq2a)

        g.addChild(sq1, sq3)
        g.addChild(sq3, sq3a)

        sq1.data.position = Vector2(2.0, 0.0)
        sq2.data.position = Vector2(3.0, 0.0) // sq2 position should be sq1 + sq2

        val nodes: MutableMap<EntitySlug, Graphics> = mutableMapOf()
        g.render {
            nodes[it.data.entitySlug] = it.data as Graphics
        }

        assertEquals(Vector3(5.0, 0.0, 1.0), nodes[sq2.entityId]!!.renderModel().first())
        assertEquals(Vector3(5.0, 0.0, 1.0), nodes[sq2a.entityId]!!.renderModel().first())
        assertEquals(Vector3(2.0, 0.0, 1.0), nodes[sq3.entityId]!!.renderModel().first())
        assertEquals(Vector3(2.0, 0.0, 1.0), nodes[sq3a.entityId]!!.renderModel().first())
    }

    @Test
    fun changesFromTwoFrames() {
        val g: Scene = SceneGraph()

        val sq1 = g.createVertex(createGraphics("sq1".eslug))

        val sq2 = g.createVertex(createGraphics("sq2".eslug))

        g.addChild(sq1, sq2)

        // first frame
        sq1.data.position = Vector2(2.0, 0.0)

        val frame1: MutableMap<EntitySlug, Graphics> = mutableMapOf()
        g.render {
            frame1[it.data.entitySlug] = it.data as Graphics
        }

        // second frame
        sq1.data.position = Vector2(4.0, 0.0)

        val frame2: MutableMap<EntitySlug, Graphics> = mutableMapOf()
        g.render {
            frame2[it.data.entitySlug] = it.data as Graphics
        }

        assertEquals(Vector3(4.0, 0.0, 1.0), frame2[sq2.entityId]!!.renderModel().first())
    }

    private fun createGraphics(entitySlug: EntitySlug): Graphics = Graphics(
        entitySlug = entitySlug,
        position = Vector2.ZERO,
        radius = 0.5,
        model = Point(),
        colorFill = ColorRGBa.GREEN,
        colorStroke = ColorRGBa.BLUE,
        label = "",
        worldToScreen = Matrix33.IDENTITY
    )
}
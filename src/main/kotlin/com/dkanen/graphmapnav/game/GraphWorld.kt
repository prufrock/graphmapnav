package com.dkanen.graphmapnav.game

import com.dkanen.graphmapnav.collections.graphs.*
import com.dkanen.graphmapnav.game.actor.*
import com.dkanen.graphmapnav.game.ecs.components.cameras.OrthoCamera
import com.dkanen.graphmapnav.game.ecs.components.graphics.RndrGraphics
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.eslug
import com.dkanen.graphmapnav.game.ecs.manager.BigObjectEntityManager
import com.dkanen.graphmapnav.game.ecs.manager.tables.EntityPool
import com.dkanen.graphmapnav.game.ecs.manager.tables.EntityTable
import com.dkanen.graphmapnav.game.event.EventManager
import com.dkanen.graphmapnav.game.event.SimpleEventManager
import com.dkanen.graphmapnav.math.*
import org.openrndr.color.ColorRGBa
import org.openrndr.math.*

/**
 * The World is where the action takes place and all the decisions are made about how to respond to those actions.
 *
 * - The camera should have a position in World space and move about in World space. This could make it easier to set
 * up collision with the edges of the World and maybe even control systems for smooth zooming in on locations.
 * - Should `graph` be a scene graph? If it were then all actors should be attached to it. It sets up a nice situation
 * for grouping objects. `actors` should probably change to a scene graph. Everything in it right now is a free node.
 * - The scene graph is different but related to the graph being rendered. There will be cycles in the rendered graph,
 * but not in the scene graph.
 */
class GraphWorld(
    var entityGraph: Graph<Node<String>>,
    override val eventManager: EventManager = SimpleEventManager(),
) : World {

    override val entityManager = BigObjectEntityManager(entityTable = EntityTable(EntityPool(size = 200))).apply {
        // TODO: a builder would be great here
        createDecoration(
            "scene".eslug,
            RndrGraphics(
                entitySlug = "scene".eslug,
                position = Vector2(0.0, 0.0),
                radius = 0.0,
                colorFill = ColorRGBa.WHITE,
                colorStroke = ColorRGBa.BLACK,
                model = Point(),
            )
        )
        val camera = createCamera(
            "camera1".eslug,
            OrthoCamera(
                entitySlug = "camera1".eslug,
            ),
            RndrGraphics(
                entitySlug = "camera1".eslug,
                position = Vector2(4.0, 4.0),
                radius = 0.0,
                colorFill = ColorRGBa.GREEN,
                colorStroke = ColorRGBa.BLACK,
                model = Point(),
            )
        )
    }

    override val graphics: Scene
        get() = entityManager.sceneGraph

    override val eCamera: com.dkanen.graphmapnav.game.ecs.entities.Entity
        get() = entityManager.cameras.first()

    private var mouseClickLocation: Entity? = null

    private var levelCreated = false

    override fun update(input: Input) {
        if (input.timeStep.isNaN()) {
            return
        }

        input.clickPosition?.let { clickPosition ->
            if (mouseClickLocation != null) {
                //TODO: is there a reasonable way to avoid doubling up this zoom?
                mouseClickLocation?.graphics?.position = clickPosition.ndcToWorld(eCamera.camera!!, input.zoom)
                mouseClickLocation?.collision?.position = clickPosition.ndcToWorld(eCamera.camera!!, input.zoom)
            } else {
                mouseClickLocation =
                entityManager.createProp(
                    "click-location".eslug,
                    clickPosition.ndcToWorld(eCamera.camera!!, input.zoom),
                    0.1
                )
            }
        }

        val gameInput = GameInput(input = input, inputTargets = mapOf(
            "camera1".eslug to InputTarget("camera1".eslug, input.dragDisplacement),
            "player1".eslug to InputTarget("player1".eslug, input.playerVelocity)
        ))

        createLevel()

        // This is *real* ugly, but it ensures that an overlapping click only picks a single button by selecting
        // the first one with the largest intersection with the click location.
        if (input.mouseClicked && mouseClickLocation != null) {
            val clickPosition = mouseClickLocation!!

            // Find the button that was closest to the click
            gameInput.clickedEntity = entityManager.largestIntersectedEntity(clickPosition)
        }

        entityManager.activeEntities.firstOrNull { it.slug == gameInput.clickedEntity?.slug }?.update(this, gameInput)
        gameInput.clickedEntity = null

        entityManager.update(this, gameInput)

        entityManager.finalizeTick()
    }

    private fun createLevel() {
        if (levelCreated) {
            return
        }

        entityManager.createFromGraph("map", entityGraph, Vector2(0.0, 0.0))

        levelCreated = true
    }
}
package com.dkanen.graphmapnav.game

import com.dkanen.graphmapnav.collections.graphs.Scene
import com.dkanen.graphmapnav.game.actor.*
import com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons.TapButtonCfg
import com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons.TapConfig
import com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons.ToggleButtonCfg
import com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons.ToggleConfig
import com.dkanen.graphmapnav.game.ecs.components.cameras.OrthoCamera
import com.dkanen.graphmapnav.game.ecs.components.graphics.RndrGraphics
import com.dkanen.graphmapnav.game.ecs.components.physics.Collision
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.eslug
import com.dkanen.graphmapnav.game.ecs.manager.BigObjectEntityManager
import com.dkanen.graphmapnav.game.ecs.manager.ShapeColor
import com.dkanen.graphmapnav.game.event.EventManager
import com.dkanen.graphmapnav.game.event.SimpleEventManager
import com.dkanen.graphmapnav.game.map.Thing
import com.dkanen.graphmapnav.game.map.Tile
import com.dkanen.graphmapnav.game.map.TileMap
import com.dkanen.graphmapnav.math.ndcToWorld
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.color.presets.*
import org.openrndr.math.*
import com.dkanen.graphmapnav.game.ecs.components.input.Input as ECSInput


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
class GridWorld(
    var map: TileMap,
    override val eventManager: EventManager = SimpleEventManager(),
): World {
    override val entityManager = BigObjectEntityManager().apply {
        // TODO: a builder would be great here
        createDecoration(
            "scene".eslug,
            RndrGraphics(
                entitySlug = "scene".eslug,
                position = Vector2(0.0, 0.0),
                radius = 0.25,
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
                radius = 0.25,
                colorFill = ColorRGBa.GREEN,
                colorStroke = ColorRGBa.BLACK,
                model = Point(),
            )
        )
        createProp(
            "sq1".eslug,
            Collision(
                entitySlug = "sq1".eslug,
                position = Vector2(3.0, 3.0),
                radius = 0.25,
                parent = camera
            ),
            RndrGraphics(
                entitySlug = "sq1".eslug,
                position = Vector2(3.0, 3.0),
                radius = 0.25,
                colorFill = ColorRGBa.GREEN,
                colorStroke = ColorRGBa.BLACK,
                model = Point(),
                parent = camera
            )
        )
        createDecoration(
            "sq2".eslug,
            RndrGraphics(
                entitySlug = "sq2".eslug,
                position = Vector2(4.0, 3.0),
                radius = 0.25,
                colorFill = ColorRGBa.PURPLE,
                colorStroke = ColorRGBa.BLACK,
                model = Point(),
            )
        )
        createToggleButton(
            "btn1".eslug,
            position = Vector2(3.5, 4.5),
            radius = 0.1,
            toggleButtonCfg = ToggleButtonCfg(
                toggled = ToggleConfig(color = ShapeColor(fill = ColorRGBa.MAGENTA)),
                notToggled = ToggleConfig(color = ShapeColor(fill = ColorRGBa.GREY))
            )
        )

        createTapButton(
            "btn2".eslug,
            position = Vector2(2.5, 4.5),
            radius = 0.1,
            tapButtonCfg = TapButtonCfg(
                pressed = TapConfig(color = ShapeColor(fill = ColorRGBa.MAGENTA)),
                released = TapConfig(color = ShapeColor(fill = ColorRGBa.GREY))
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
            //TODO: this is a little ugly. I think need to get rid of the Transform idea and just set the position.
            if (mouseClickLocation != null) {
                mouseClickLocation?.graphics?.position = clickPosition.ndcToWorld(eCamera.camera!!)
                mouseClickLocation?.collision?.position = clickPosition.ndcToWorld(eCamera.camera!!)
            } else {
                mouseClickLocation =
                    entityManager.createProp("click-location".eslug, clickPosition.ndcToWorld(eCamera.camera!!), 0.1)
            }
        }

        val gameInput = GameInput(input = input, inputTargets = mapOf(
            "camera1".eslug to InputTarget("camera1".eslug, input.cameraVelocity),
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
        for (y in 0 until map.height) {
            for (x in 0 until map.width) {
                val position = Vector2(x = x + 0.5, y = y + 0.5) // offset to the center of the tile
                when (map.get(x, y)) {
                    Tile.EMPTY -> 0 // do nothing
                    Tile.WALL -> entityManager.createProp(slug = "wall$x,$y".eslug, position = Vector2(position.x, position.y), colorFill = ColorRGBa.GREY, radius = 0.5,)
                    else -> 0 // do nothing
                }

                val thing = map.getThing(x, y)
                when (thing) {
                    Thing.NOTHING -> 0 // do nothing
                    Thing.PLAYER -> {
                        val entity = entityManager.createProp(slug = "player1".eslug, position = Vector2(position.x, position.y), colorFill = ColorRGBa.YELLOW, radius = 0.05, collisionResponse = true)
                        entity.components += ECSInput("player1".eslug)
                    }
                }
            }
        }

        entityManager.createAnimatedDecoration("pew-pew".eslug)

        levelCreated = true
    }
}


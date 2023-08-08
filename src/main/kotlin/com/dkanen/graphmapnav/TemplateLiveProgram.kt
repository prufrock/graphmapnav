package com.dkanen.graphmapnav

import com.dkanen.graphmapnav.game.*
import com.dkanen.graphmapnav.game.actor.*
import com.dkanen.graphmapnav.game.ecs.components.graphics.renderModel
import com.dkanen.graphmapnav.game.graph.JsonGraphNodeList
import com.dkanen.graphmapnav.game.graph.Positioner.CardinalDirectionPositioner
import com.dkanen.graphmapnav.game.graph.Positioner.Positioner
import com.dkanen.graphmapnav.math.*
import com.dkanen.graphmapnav.openrndr.ArgsHolder
import com.dkanen.graphmapnav.openrndr.FontBook
import com.dkanen.graphmapnav.openrndr.drawSquareAt
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import org.openrndr.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import java.io.File
import kotlin.math.*

// Reduce the rate at which the camera moves in response to mouse drag events.
private const val DRAG_DAMPENER = 250.0

// The minimum drag distance required to register a drag event.
private const val DRAG_THRESHOLD = 0.004

// Reduces the rate at which the zoom factor changes in response to mouse wheel events.
private const val ZOOM_DAMPENER = 10

// The minimum allowed zoom factor.
private const val ZOOM_MIN = 0.5

// The maximum allowed zoom factor.
private const val ZOOM_MAX = 2.0

/**
 *  The live program is really cool because I can change the code in this file while the application is running and that
 *  triggers a reload. The part is that this file gets really mess so that I can edit it live. It also loses all the
 *  state in the thread since it has to reload the thread.
 *
 *  - Add more verbose configuration that can be managed from here.
 *  - See about having the Game run in a separate thread, so it doesn't lose state when the application reloads.
 */

fun main(args: Array<String>) = application {
    configure {
        width = 1024
        height = 1024
        unfocusBehaviour = UnfocusBehaviour.THROTTLE
        ArgsHolder.args = args
    }
    oliveProgram {
        var zoom = 1.0

        val parser = ArgParser("graphmapnav")
        val input by parser.option(ArgType.String, shortName = "i", description = "Input file").required()
        parser.parse(ArgsHolder.args)

        val json = File(input).readText()

        val mapper = jacksonMapperBuilder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).build()

        val graph: JsonGraphNodeList = mapper.readValue(json)

        // TODO: make the world/camera size configurable
        val cameraWidth = 8.0
        val cameraHeight = 8.0
        // remember these are in world space!
        val entityGraph = CardinalDirectionPositioner().position(graph, Vector2(cameraWidth / 2.0, cameraHeight / 2.0) + Vector2(0.0, 3.5))

        val game = Game(world = World(
            entityGraph = entityGraph,
        ))
        val maximumTimeStep: Double = 1.0 / 20 // cap at a minimum of 20 FPS
        val worldTimeStep: Double = 1.0 / 120 // number of steps to take each frame
        // The last time the frame was updated. Used to determine if interactions like mouse clicks should be considered
        // during a frame.
        var lastFrameTime: Double = Double.NEGATIVE_INFINITY

        // When converting from screen to NDC whether to flip(invert) the y-axis.
        val screenToNdcFlipY = true

        // The speed the camera is moving in a particular direction.
        var cameraVelocity: Vector2 = Vector2(0.0,0.0)
        // The amount the camera is translated in any direction.
        val cameraMovementRate = 0.002

        // NDC points used as reference.
        val ndc = Ndc(listOf((-1.0).v2, (-0.5).v2, 0.0.v2, 0.5.v2, 1.0.v2))
        var showNdcReference = false

        var showMovingCircle = false

        // The position of the mouse in NDC space the last time it was clicked.
        var mousePositionNdc: Vector2? = null
        // The last time the mouse was clicked.
        var lastClickedTime: Double = Double.NEGATIVE_INFINITY
        var mouseClicked = false
        var dragDisplacement = Vector2(0.0, 0.0)

        // The speed the player is moving in a particular direction.
        var playerVelocity = Vector2(0.0, 0.0)
        // The amount the player is translated in any direction.
        val playerMovementRate = 0.002

        keyboard.keyDown.listen {
            cameraVelocity = Vector2(0.0, 0.0)
            if (it.key == KEY_ARROW_LEFT) {
                cameraVelocity += Vector2(-cameraMovementRate, 0.0)
            }
            if (it.key == KEY_ARROW_RIGHT) {
                cameraVelocity += Vector2(cameraMovementRate, 0.0)
            }
            if (it.key == KEY_ARROW_UP) {
                cameraVelocity += Vector2(0.0, cameraMovementRate)
            }
            if (it.key == KEY_ARROW_DOWN) {
                cameraVelocity += Vector2(0.0, -cameraMovementRate)
            }
            if (it.name == "w") {
                playerVelocity = Vector2(0.0, -playerMovementRate)
            }
            if (it.name == "s") {
                playerVelocity = Vector2(0.0, playerMovementRate)
            }
            if (it.name == "a") {
                playerVelocity = Vector2(-playerMovementRate, 0.0)
            }
            if (it.name == "d") {
                playerVelocity = Vector2(playerMovementRate, 0.0)
            }
        }
        keyboard.keyUp.listen {
            if (it.key == KEY_ARROW_LEFT) {
                cameraVelocity = Vector2(0.0, 0.0)
            }
            if (it.key == KEY_ARROW_RIGHT) {
                cameraVelocity = Vector2(0.0, 0.0)
            }
            if (it.key == KEY_ARROW_UP) {
                cameraVelocity = Vector2(0.0, 0.0)
            }
            if (it.key == KEY_ARROW_DOWN) {
                cameraVelocity = Vector2(0.0, 0.0)
            }
            if (it.name == "w") {
                playerVelocity = Vector2(0.0, 0.0)
            }
            if (it.name == "s") {
                playerVelocity = Vector2(0.0, 0.0)
            }
            if (it.name == "a") {
                playerVelocity = Vector2(0.0, 0.0)
            }
            if (it.name == "d") {
                playerVelocity = Vector2(0.0, 0.0)
            }
        }
        mouse.buttonUp.listen {
            // -- it refers to a MouseEvent instance here
            lastClickedTime = application.seconds
            val ndcX = it.position.x.screenToNdc(width + 0.0)
            val ndcY = it.position.y.screenToNdc(height + 0.0, screenToNdcFlipY)
            mousePositionNdc = Vector2(ndcX, ndcY)
            mouseClicked = true
            dragDisplacement = Vector2.ZERO
        }

        mouse.scrolled.listen {
            zoom = (zoom + it.rotation.y / ZOOM_DAMPENER).coerceIn(ZOOM_MIN, ZOOM_MAX)
        }

        mouse.dragged.listen {
            dragDisplacement = (it.dragDisplacement / DRAG_DAMPENER) * -1.0
            if (abs(dragDisplacement.length) < DRAG_THRESHOLD) {
                dragDisplacement = Vector2.ZERO
            }
        }

        val font = "data/fonts/default.otf"
        val fontBook = FontBook(
            small = loadFont(font, 8.0),
            medium = loadFont(font, 16.0),
            large = loadFont(font, 32.0)
        )

        extend {
            // increase accuracy of collisions by reducing time between updates
            // also avoid spiralling when world updates take longer than frame step
            val timeStep = min(maximumTimeStep, deltaTime)
            val worldSteps = (timeStep / worldTimeStep).roundToInt()
            for (i in 0 .. worldSteps) {
                game.update(Input(
                    timeStep = timeStep / worldSteps,
                    cameraVelocity = cameraVelocity,
                    playerVelocity = playerVelocity,
                    clickPosition = run {
                        if (lastClickedTime > lastFrameTime) {
                            mousePositionNdc
                        } else {
                            null
                        }
                    },
                    mouseClicked = mouseClicked,
                    dragDisplacement = dragDisplacement,
                    zoom = zoom
                ))
                mouseClicked = false
            }

            // render
            drawer.clear(ColorRGBa.BLACK)
            if (showMovingCircle) {
                drawer.circle(
                    cos(seconds) * width / 2.0 + width / 2.0,
                    sin(0.5 * seconds) * height / 2.0 + height / 2.0,
                    140.0
                )
            }
            if (showNdcReference) {
                drawer.fill = ColorRGBa.WHITE
                drawer.point(Vector2(400.0, 400.0))
                ndc.points.forEach { ndc ->
                    drawer.drawSquareAt(
                        ndc.ndcToScreen(
                            width = width + 0.0,
                            height = height + 0.0,
                            flipY = screenToNdcFlipY
                        )
                    )
                }
            }

            game.world.graphics.render { v ->
                val graphics = v.data
                drawer.fill = graphics.colorFill
                drawer.stroke = graphics.colorStroke
                drawer.strokeWeight = 0.0
                // TODO move more of the properties of graphics into properties of the model since a point and a square and a circle have different properties.
                when(val model = graphics.model) {
                    is Point -> {
                        val camera = game.world.eCamera.camera!!
                        val location = (camera.worldToNdc * graphics.renderModel().first()).xy
                            .ndcToScreen(width = width + 0.0, height = height + 0.0, flipY = screenToNdcFlipY)
                        // Should the boxes relate to the width of the camera or the width of the world?
                        // If I use the camera width then I get zoom by scaling the camera width and height!
                        // Also, divide by 2.0 because the size of the box is specified as the `halfSize`.
                        val halfSize = graphics.radius * (width + 0.0 / (camera.width / 2))
                        drawer.drawSquareAt(location, halfSize)
                    }
                    else -> {
                        model.draw(drawer, graphics, game, width = width + 0.0, height = height + 0.0, fontBook = fontBook, zoom = zoom)
                    }
                }
            }
            // Set this to zero at the bottom of the frame so the drag doesn't keep drifting.
            dragDisplacement = Vector2.ZERO
            lastFrameTime = application.seconds
        }
    }
}
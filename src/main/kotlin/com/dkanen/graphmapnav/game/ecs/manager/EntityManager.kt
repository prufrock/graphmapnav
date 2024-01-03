package com.dkanen.graphmapnav.game.ecs.manager

import com.dkanen.graphmapnav.collections.graphs.Graph
import com.dkanen.graphmapnav.collections.graphs.Scene
import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.Node
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons.TapButtonCfg
import com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons.ToggleButtonCfg
import com.dkanen.graphmapnav.game.ecs.components.cameras.Camera
import com.dkanen.graphmapnav.game.ecs.components.graphics.Graphics
import com.dkanen.graphmapnav.game.ecs.components.physics.Collision
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.math.Vector20
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2

interface EntityManager {
    // perhaps a tree to make searching faster?
    val sceneGraph: Scene

    // Maybe a map if I need to switch cameras by their label?
    val cameras: List<Entity>
    val activeEntities: List<Entity>
    val collisions: List<Collision>

    fun createDecoration(slug: EntitySlug, graphicsComponent: Graphics): Entity

    fun createCamera(slug: EntitySlug, camera: Camera, graphicsComponent: Graphics): Entity

    /**
     * Props can be seen and bumped into.
     */
    fun createProp(
        slug: EntitySlug,
        position: Vector2,
        radius: Double,
        colorStroke: ColorRGBa = ColorRGBa.BLACK,
        colorFill: ColorRGBa = ColorRGBa.GREEN,
        collisionResponse: Boolean = false
    ): Entity

    /**
     * Find an entity by its ID.
     */
    fun find(slug: EntitySlug): Entity?
    fun finalizeTick()

    fun createAnimatedDecoration(slug: EntitySlug, position: Vector2 = Vector20(), radius: Double = 0.5, colorStroke: ColorRGBa = ColorRGBa.BLACK, colorFill: ColorRGBa = ColorRGBa.WHITE): Entity

    fun createAnimatedProp(
        slug: EntitySlug,
        position: Vector2 = Vector20(),
        radius: Double = 0.5,
        colorStroke: ColorRGBa = ColorRGBa.BLACK,
        colorFill: ColorRGBa = ColorRGBa.WHITE,
        direction: Vector2 = Vector20(),
        speed: Double = 0.0
    ): Entity

    /**
     * Props can be seen and bumped into.
     */
    fun createProp(slug: EntitySlug, collision: Collision, graphicsComponent: Graphics): Entity
    fun createToggleButton(slug: EntitySlug, position: Vector2, radius: Double, toggleButtonCfg: ToggleButtonCfg): Entity
    fun createTapButton(slug: EntitySlug, position: Vector2, radius: Double, tapButtonCfg: TapButtonCfg): Entity
    fun createFromGraph(name: String, graph: Graph<Node<String>>, origin: Vector2 = Vector20())

    // Entity Table
    fun update(world: World, gameInput: GameInput)

    // Collisions Table
    fun largestIntersectedEntity(entity: Entity): Entity?
    fun largestIntersection(thisCollision: Collision): Vector2?
}
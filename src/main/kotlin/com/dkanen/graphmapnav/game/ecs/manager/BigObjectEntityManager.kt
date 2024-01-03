package com.dkanen.graphmapnav.game.ecs.manager

import com.dkanen.graphmapnav.collections.graphs.Graph
import com.dkanen.graphmapnav.collections.graphs.Scene
import com.dkanen.graphmapnav.collections.graphs.SceneGraph
import com.dkanen.graphmapnav.collections.graphs.Vertex
import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.Node
import com.dkanen.graphmapnav.game.GraphWorld
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.actor.Label
import com.dkanen.graphmapnav.game.actor.Line
import com.dkanen.graphmapnav.game.actor.Square
import com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons.*
import com.dkanen.graphmapnav.game.ecs.components.cameras.Camera
import com.dkanen.graphmapnav.game.ecs.components.behaviors.Edge
import com.dkanen.graphmapnav.game.ecs.components.graphics.Graphics
import com.dkanen.graphmapnav.game.ecs.components.graphics.RndrGraphics
import com.dkanen.graphmapnav.game.ecs.components.input.Input
import com.dkanen.graphmapnav.game.ecs.components.physics.Collision
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.entities.eslug
import com.dkanen.graphmapnav.game.ecs.manager.tables.EntityPool
import com.dkanen.graphmapnav.game.ecs.manager.tables.EntityTable
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.color.presets.BLUE_VIOLET
import org.openrndr.extra.color.presets.PURPLE
import org.openrndr.math.Vector2

/**
 * Manages entities where the objects hold all of their components rather than just being and ID with components stored
 * else where.
 */
class BigObjectEntityManager(private val entityTable: EntityTable = EntityTable(EntityPool(size = 100))): EntityManager {
    override val activeEntities: List<Entity>
        get() = entityTable.activeEntities

    override val sceneGraph: Scene
        get()  {
            val graphicComponents: MutableList<Graphics> = mutableListOf()

            activeEntities.forEach { entity ->
                if (!entity.deleted) {
                    entity.graphics?.let {
                        graphicComponents.add(it)
                    }
                }
            }

            val scene = SceneGraph()
            val vertexMap = mutableMapOf<EntitySlug, Vertex<Graphics>>()
            // TODO: not a big fan of this double loop
            graphicComponents.forEach { component ->
                val v = scene.createVertex(component)
                // depends on the graphics components being added in order =/
                vertexMap[component.entitySlug] = v
            }

            graphicComponents.forEach { component ->
                val v = vertexMap[component.entitySlug]!!
                if (component.parent == null) {
                    scene.addChild(v)
                } else {
                    component.parent?.let { parent ->
                        scene.addChild(vertexMap[parent.slug]!!, v)
                    }
                }
            }

            return scene
        }

    override val cameras: MutableList<Entity>  = mutableListOf()

    private val inputs: MutableList<Input> = mutableListOf()

    override val collisions: List<Collision>
        get() = activeEntities.mapNotNull { it.collision }

    init {
        sceneGraph.createVertex(RndrGraphics("root".eslug))
    }

    override fun createDecoration(slug: EntitySlug, graphicsComponent: Graphics): Entity {
        //TODO make Entity optional
        val entity = entityTable.create(slug)!!
        entity.components += graphicsComponent

        return entity
    }

    override fun createCamera(slug: EntitySlug, camera: Camera, graphicsComponent: Graphics): Entity {
        val entity = entityTable.create(slug)!!

        entity.components += camera
        cameras.add(entity)

        entity.components += graphicsComponent
        Input(slug).also {
            entity.components += it
            inputs.add(it)
        }

        return entity
    }

    /**
     * Props can be seen and bumped into.
     */
    override fun createProp(slug: EntitySlug, collision: Collision, graphicsComponent: Graphics): Entity {
        val entity = entityTable.create(slug) ?: throw EntityAllocationException()
        entity.components += collision
        entity.components += graphicsComponent

        return entity
    }

    /**
     * Props can be seen and bumped into.
     */
    override fun createProp(
        slug: EntitySlug,
        position: Vector2,
        radius: Double,
        colorStroke: ColorRGBa,
        colorFill: ColorRGBa,
        collisionResponse: Boolean
    ): Entity {
        val graphics = RndrGraphics(
            entitySlug = slug,
            position = position,
            radius = radius,
            model = Square(),
            colorStroke = colorStroke,
            colorFill = colorFill,
        )

        val collision = Collision(
            entitySlug = slug,
            position = position,
            radius = radius,
            collisionResponse = collisionResponse,
        )

        return createProp(slug, collision, graphics)
    }

    override fun find(entitySlug: EntitySlug): Entity? = entityTable.find(entitySlug)

    override fun finalizeTick() {
        activeEntities.forEach { entity ->
            entity.finalizeTick()
            if (entity.deleted) {
                entityTable.delete(entity)
            }
        }
    }

    override fun createAnimatedDecoration(slug: EntitySlug, position: Vector2, radius: Double, colorStroke: ColorRGBa, colorFill: ColorRGBa): Entity {
        val entity = createDecoration(slug, RndrGraphics(
            entitySlug = slug,
            position = position,
            radius = radius,
            colorStroke = colorStroke,
            colorFill = colorFill,
        ))

        entity.components += com.dkanen.graphmapnav.game.ecs.components.animation.FollowLine(
            entity.slug,
            speed = 0.2,
            direction = Vector2(1.0, 0.0)
        )

        return entity
    }

    override fun createAnimatedProp(
        slug: EntitySlug,
        position: Vector2,
        radius: Double,
        colorStroke: ColorRGBa,
        colorFill: ColorRGBa,
        direction: Vector2,
        speed: Double
    ): Entity {
        val entity = createProp(
            slug,
            position = position,
            radius = radius,
            colorStroke = colorStroke,
            colorFill = colorFill
        )

        entity.components += com.dkanen.graphmapnav.game.ecs.components.animation.FollowLine(
            entity.slug,
            speed = speed,
            direction = direction
        )

        return entity
    }

    override fun createToggleButton(slug: EntitySlug, position: Vector2, radius: Double, toggleButtonCfg: ToggleButtonCfg): Entity {
        val entity = createProp(slug, position, radius)
        entity.components += ToggleButton(
            entitySlug = entity.slug,
            toggled = toggleButtonCfg.toggled,
            notToggled = toggleButtonCfg.notToggled
        )

        return entity
    }

    override fun createTapButton(slug: EntitySlug, position: Vector2, radius: Double, tapButtonCfg: TapButtonCfg): Entity {
        val entity = createProp(slug, position, radius)
        entity.components += TapButton(
            entitySlug = entity.slug,
            pressed = tapButtonCfg.pressed,
            released = tapButtonCfg.released
        )

        return entity
    }

    override fun createFromGraph(name: String, graph: Graph<Node<String>>, origin: Vector2) {
        val radius = 0.25
        var count = 0

        // create edges
        // Doing this first to get hide the lines
        // TODO: look into a better way to hide the lines
        graph.allVertices.forEach { v ->
            val entityId = getEntitySlug(name, v)

            val edges = graph.edges(v)

            // nothing to do here
            if (edges.isEmpty()) {
                return@forEach
            }

            edges.forEach { e ->
                val d = e.destination
                val destinationId = getEntitySlug(name, d)
                val edgeEntity = this.createDecoration(
                    slug = edgeId(entityId, destinationId, true),
                    graphicsComponent = RndrGraphics(
                        entitySlug = edgeId(entityId, destinationId, true),
                        radius = find(entityId)?.graphics?.radius ?: radius,
                        colorStroke = ColorRGBa.WHITE,
                        colorFill = find(entityId)?.graphics?.colorFill ?: ColorRGBa.BLUE_VIOLET,
                        strokeWeight = 1.0,
                        model = Line(),
                    )
                )

                edgeEntity.components += Edge(
                    entitySlug = entityId,
                    sourceEntitySlug = entityId,
                    destinationEntitySlug = destinationId,
                    sourcePosition = origin + v.data.position,
                    destinationPosition = origin + d.data.position
                )
            }
        }

        // create vertices
        graph.allVertices.forEach { v ->
            val position = origin + v.data.position
            val entityId = getEntitySlug(name, v)
            val parent = this.createProp(
                slug = entityId,
                collision = Collision(entitySlug = entityId, position = position, radius = radius),
                graphicsComponent = RndrGraphics(entitySlug = entityId, position = position, radius = radius, model = Square(), colorFill = ColorRGBa.PURPLE, colorStroke = ColorRGBa.BLUE)
            )

            //TODO find a better place managing truncation to live.
            val maxLength = 10
            val originalLabel = v.data.value
            val truncatedLabel = if (originalLabel.length > maxLength) { originalLabel.substring(0, maxLength) + "..."} else { originalLabel }

            val label = this.createDecoration(
                slug = labelSlug(entityId),
                graphicsComponent = RndrGraphics(
                    entitySlug = labelSlug(entityId),
                    position = Vector2(0.0, -0.05), // position is relative to the parent
                    radius = radius - 0.1,
                    colorFill = ColorRGBa.WHITE,
                    label = truncatedLabel,
                    model = Label(),
                    parent = parent
                )
            )

            parent.components += ToggleButton(
                entitySlug = parent.slug,
                toggled = ToggleConfig(color = ShapeColor(fill = ColorRGBa.MAGENTA)) { label.graphics!!.label = originalLabel },
                notToggled = ToggleConfig(color = ShapeColor(fill = ColorRGBa.PURPLE)) { label.graphics!!.label = truncatedLabel },
            )
            count++
        }
    }

    private fun labelSlug(slug: EntitySlug) = "${slug}_label".eslug

    private fun edgeId(entitySlug: EntitySlug, destinationSlug: EntitySlug, source: Boolean): EntitySlug {
        var type = "s"
        if (!source) {
            type = "d'"
        }
        return "${entitySlug}_edge_${destinationSlug}_$type".eslug
    }

    private fun <T>getEntitySlug(
        name: String,
        v: Vertex<Node<T>>
    ) = "${name}-${v.index}".eslug

    // Entity Table
    override fun update(world: World, gameInput: GameInput) {
        activeEntities.forEach { it.update(world, gameInput)}
    }

    // Collisions Table
    override fun largestIntersectedEntity(entity: Entity): Entity? = entity.collision?.let { sourceCollision ->
        largestIntersectedCollision(sourceCollision)?.let { c ->
            activeEntities.firstOrNull { it.slug == c.entitySlug }
        }
    }

    override fun largestIntersection(thisCollision: Collision): Vector2? {
        var largestIntersection: Vector2? = null
        collisions.forEachIndexed { i, other ->
            if (other.entitySlug != thisCollision.entitySlug) {
                val currentIntersection = thisCollision.intersection(other)
                if (currentIntersection != null) {
                    if (largestIntersection?.length == null || largestIntersection!!.length < currentIntersection.length) {
                        largestIntersection = currentIntersection
                    }
                }
            }

        }
        return largestIntersection
    }


    private fun largestIntersectedCollision(collision: Collision): Collision? {
        var largestIntersection: Vector2? = null
        var largestIntersectedCollision: Collision? = null
        collisions.forEachIndexed { i, other ->
            if (other.entitySlug != collision.entitySlug) {
                val currentIntersection = collision.intersection(other)
                if (currentIntersection != null) {
                    if (largestIntersection?.length == null || largestIntersection!!.length < currentIntersection.length) {
                        largestIntersection = currentIntersection
                        largestIntersectedCollision = other
                    }
                }
            }

        }
        return largestIntersectedCollision
    }
}

data class ShapeColor(val stroke: ColorRGBa = ColorRGBa.BLACK, val fill: ColorRGBa = ColorRGBa.BLUE)

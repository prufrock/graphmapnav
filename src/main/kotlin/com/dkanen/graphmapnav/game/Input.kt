package com.dkanen.graphmapnav.game

import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import org.openrndr.math.*

data class Input(
    val timeStep: Double,
    val playerVelocity: Vector2,
    val cameraVelocity: Vector2,
    // In NDC space
    val clickPosition: Vector2?,
    val mouseClicked: Boolean = false,
    val zoom: Double = 1.0,
    val dragDisplacement: Vector2 = Vector2.ZERO
)

data class GameInput(
    var clickedEntity: Entity? = null,
    val input: Input,
    val inputTargets: Map<EntitySlug, InputTarget> = emptyMap()
)

data class InputTarget(
    val entitySlug: EntitySlug,
    val velocity: Vector2,
)
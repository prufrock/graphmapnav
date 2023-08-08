package com.dkanen.graphmapnav.game.ecs.components.graphics

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.actor.Model
import com.dkanen.graphmapnav.game.actor.Point
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.messages.UpdateEnd
import com.dkanen.graphmapnav.game.ecs.messages.UpdatePosition
import com.dkanen.graphmapnav.game.ecs.messages.UpdateStart
import com.dkanen.graphmapnav.math.Vector20
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.color.presets.WHITE_SMOKE
import org.openrndr.math.Matrix33
import org.openrndr.math.Vector2

class RndrGraphics(
    override val entitySlug: EntitySlug,
    override var position: Vector2 = Vector20(),
    override var radius: Double = 0.5,
    override var model: Model = Point(),
    override var colorFill: ColorRGBa = ColorRGBa.WHITE_SMOKE,
    override var colorStroke: ColorRGBa = ColorRGBa.BLACK,
    override var strokeWeight: Double = 0.0,
    override var label: String = "",
    override var worldToScreen: Matrix33 = Matrix33.IDENTITY,
    override val modelToUpRightMatrix: Matrix33 = Matrix33.IDENTITY,
    override var uprightToWorldMatrix: Matrix33 = Matrix33.IDENTITY,
    override var start: Vector2 = Vector20(),
    override var end: Vector2 = Vector20(),
    override var parent: Entity? = null,
) : Graphics {

    override fun update(entity: Entity, world: World, input: GameInput) {
    }

    override fun <T> receive(message: T) {
        when(message) {
            is UpdatePosition -> {
                position += message.displacement
            }
            is UpdateStart -> {
                start = message.position
            }
            is UpdateEnd -> {
                end = message.position
            }
        }
    }
}
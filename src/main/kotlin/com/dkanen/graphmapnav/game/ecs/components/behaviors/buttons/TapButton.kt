package com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.components.Component
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.manager.ShapeColor
import com.dkanen.graphmapnav.game.ecs.messages.Clicked

class TapButton(
    override val entitySlug: EntitySlug,
    private var state: State = State.Released,
    private val pressed: TapConfig = TapConfig(),
    private val released: TapConfig = TapConfig(),
    private val activeDuration: Double = 0.5
    ) : Component {

    private var activeTime: Double = 0.0

    override fun update(entity: Entity, world: World, input: GameInput) {
        if (input.clickedEntity?.slug == entity.slug) {
            press(entity)
        }

        when(state) {
            State.Pressed -> {
                entity.graphics?.let {
                    it.colorStroke = pressed.color.stroke
                    it.colorFill = pressed.color.fill
                }
                activeTime += input.input.timeStep
                if (activeTime >= activeDuration) {
                    state = State.Released
                    activeTime = 0.0
                    released.action()
                }
            }
            State.Released -> {
                entity.graphics?.let {
                    it.colorStroke = released.color.stroke
                    it.colorFill = released.color.fill
                }
            }
        }
    }

    private fun press(entity: Entity) {
        state = when(state) {
            // stay pressed if pressed
            State.Pressed -> {
                State.Pressed
            }

            State.Released -> {
                pressed.action()
                entity.receive(Clicked)
                State.Pressed
            }
        }
    }

    enum class State {
        Pressed,
        Released
    }
}

data class TapConfig(
    val color: ShapeColor = ShapeColor(),
    val action: () -> Unit = {},
)

data class TapButtonCfg(
    val pressed: TapConfig = TapConfig(),
    val released: TapConfig = TapConfig(),
)
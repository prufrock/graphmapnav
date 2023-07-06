package com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.World
import com.dkanen.graphmapnav.game.ecs.components.Component
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.EntitySlug
import com.dkanen.graphmapnav.game.ecs.manager.ShapeColor

class ToggleButton(override val entitySlug: EntitySlug, private var state: State = State.NotToggled, private val toggled: ToggleConfig = ToggleConfig(), private val notToggled: ToggleConfig = ToggleConfig()) : Component {

    override fun update(entity: Entity, world: World, input: GameInput) {
        if (input.clickedEntity?.slug == entity.slug) {
            toggle()
        }

        when(state) {
            State.Toggled -> {
                entity.graphics?.let {
                    it.colorStroke = toggled.color.stroke
                    it.colorFill = toggled.color.fill
                }
            }
            State.NotToggled -> {
                entity.graphics?.let {
                    it.colorStroke = notToggled.color.stroke
                    it.colorFill = notToggled.color.fill
                }
            }
        }
    }

    private fun toggle() {
        when(state) {
            State.Toggled -> {
                state = State.NotToggled
                notToggled.action()
            }
            State.NotToggled -> {
                state = State.Toggled
                toggled.action()
            }
        }
    }

    enum class State {
        Toggled,
        NotToggled
    }
}

data class ToggleConfig(
    val color: ShapeColor = ShapeColor(),
    val action: () -> Unit = {},
)

data class ToggleButtonCfg(
    val notToggled: ToggleConfig = ToggleConfig(),
    val toggled: ToggleConfig = ToggleConfig(),
)
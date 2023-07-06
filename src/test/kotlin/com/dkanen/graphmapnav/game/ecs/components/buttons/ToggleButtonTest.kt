package com.dkanen.graphmapnav.game.ecs.components.buttons

import com.dkanen.graphmapnav.game.GameInput
import com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons.ToggleButton
import com.dkanen.graphmapnav.game.ecs.components.behaviors.buttons.ToggleConfig
import com.dkanen.graphmapnav.game.ecs.components.graphics.RndrGraphics
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.entities.eslug
import com.dkanen.graphmapnav.game.ecs.manager.ShapeColor
import com.dkanen.graphmapnav.inputNoChange
import com.dkanen.graphmapnav.smallTestWorld
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.color.presets.BLUE_VIOLET

class ToggleButtonTest {
    @Test
    fun testUpdate() {
        val button = ToggleButton(
            entitySlug = "tb1".eslug,
            toggled = ToggleConfig(color = ShapeColor(fill = ColorRGBa.MAGENTA)),
            notToggled = ToggleConfig(color = ShapeColor(fill = ColorRGBa.BLUE_VIOLET))
        )
        val entity = Entity(slug = "tb1".eslug, components = listOf(button, RndrGraphics(entitySlug = "tb1".eslug)))

        assertEquals("tb1".eslug, button.entitySlug)

        entity.update(smallTestWorld(), GameInput(clickedEntity = null, input = inputNoChange()))

        assertEquals(ColorRGBa.BLUE_VIOLET, entity.graphics?.colorFill, "Color when it's not toggled.")

        entity.update(smallTestWorld(), GameInput(clickedEntity = entity, input = inputNoChange()))

        assertEquals(ColorRGBa.MAGENTA, entity.graphics?.colorFill, "Color when it's toggled.")

        entity.update(smallTestWorld(), GameInput(clickedEntity = entity, input = inputNoChange()))

        assertEquals(ColorRGBa.BLUE_VIOLET, entity.graphics?.colorFill, "Color after it's been toggled and then de-toggled.")
    }
}
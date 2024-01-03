package com.dkanen.graphmapnav.game

import com.dkanen.graphmapnav.collections.graphs.Scene
import com.dkanen.graphmapnav.game.ecs.components.graphics.Graphics
import com.dkanen.graphmapnav.game.ecs.entities.Entity
import com.dkanen.graphmapnav.game.ecs.manager.EntityManager
import com.dkanen.graphmapnav.game.event.EventManager

interface World {

    val eCamera: Entity

    val entityManager: EntityManager

    val graphics: Scene
    val eventManager: EventManager
    fun update(input: Input)
}
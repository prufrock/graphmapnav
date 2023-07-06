package com.dkanen.graphmapnav.game.ecs.manager

class EntityAllocationException: Exception("Failed to create an Entity. We must have run out. More than likely the pool is full.")
package com.dkanen.graphmapnav.game.graph

import com.dkanen.graphmapnav.game.graph.Positioner.CardinalDirection

data class JsonGraphNodeList(val list: List<JsonGraphNode>)

data class JsonGraphEdge(val name: String, val direction: CardinalDirection = CardinalDirection.EAST)

data class JsonGraphNode(val name: String, var position: List<Double>?, val edges: List<JsonGraphEdge>)
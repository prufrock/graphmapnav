package com.dkanen.graphmapnav.game.graph.Positioner

import com.dkanen.graphmapnav.collections.graphs.Graph
import com.dkanen.graphmapnav.collections.graphs.Vertex
import com.dkanen.graphmapnav.game.Node
import com.dkanen.graphmapnav.game.graph.JsonGraphNodeList
import com.dkanen.graphmapnav.math.Vector20
import org.openrndr.math.Vector2

interface Positioner {
    fun position(nodeList: JsonGraphNodeList, origin: Vector2 = Vector20()): Graph<Node<String>>
}

fun Graph<Node<String>>.createVertex(name: String, position: Vector2): Vertex<Node<String>> = createVertex(Node(name, position))
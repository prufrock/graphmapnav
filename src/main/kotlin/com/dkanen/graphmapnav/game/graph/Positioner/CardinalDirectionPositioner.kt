package com.dkanen.graphmapnav.game.graph.Positioner

import com.dkanen.graphmapnav.collections.graphs.AdjacencyList
import com.dkanen.graphmapnav.collections.graphs.Graph
import com.dkanen.graphmapnav.collections.graphs.Vertex
import com.dkanen.graphmapnav.game.Node
import com.dkanen.graphmapnav.game.graph.JsonGraphNodeList
import org.openrndr.math.Vector2

class CardinalDirectionPositioner: Positioner {
    override fun position(nodeList: JsonGraphNodeList, origin: Vector2): Graph<Node<String>> {
        val finalGraph = AdjacencyList<Node<String>>()
        val vertexIndex = mutableMapOf<String, Vertex<Node<String>>>()

        // TODO give these loops names
        nodeList.list.forEach { jsonNode ->
            val name = jsonNode.name

            val currentVertex = vertexIndex[name] ?: finalGraph.createVertex(name,  origin)
            val node = currentVertex.data
            vertexIndex[name] = currentVertex

            jsonNode.edges.forEachIndexed { i, edge ->
                val angle = when(edge.direction) {
                    CardinalDirection.EAST -> 0.0
                    CardinalDirection.SOUTH -> 90.0
                    CardinalDirection.WEST -> 180.0
                    CardinalDirection.NORTH -> 270.0
                }
                val edgeNode = Node(edge.name, node.position + Vector2(1.0, 0.0).rotate(angle))
                val edgeVertex = vertexIndex[edge.name] ?: finalGraph.createVertex(edgeNode)
                vertexIndex[edge.name] = edgeVertex
                finalGraph.addDirectedEdge(currentVertex, edgeVertex, 0.0)
            }
        }

        return finalGraph
    }
}

enum class CardinalDirection {
    EAST,
    SOUTH,
    WEST,
    NORTH
}
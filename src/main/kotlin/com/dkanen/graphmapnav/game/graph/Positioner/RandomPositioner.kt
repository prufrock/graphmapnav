package com.dkanen.graphmapnav.game.graph.Positioner

import com.dkanen.graphmapnav.collections.graphs.AdjacencyList
import com.dkanen.graphmapnav.collections.graphs.Graph
import com.dkanen.graphmapnav.collections.graphs.Vertex
import com.dkanen.graphmapnav.game.Node
import com.dkanen.graphmapnav.game.graph.JsonGraphNodeList
import com.dkanen.graphmapnav.math.Vector20
import com.dkanen.graphmapnav.math.random
import org.openrndr.math.Vector2
import kotlin.random.Random

class RandomPositioner(private val min: Double, private val max: Double): Positioner {
    override fun position(nodeList: JsonGraphNodeList, origin: Vector2): Graph<Node<String>> {
        val finalGraph = AdjacencyList<Node<String>>()
        val vertexIndex = mutableMapOf<String, Vertex<Node<String>>>()

        // TODO give these loops names
        nodeList.list.forEach { jsonNode ->
            val name = jsonNode.name

            val currentVertex = vertexIndex[name] ?: finalGraph.createVertex(name,  Vector20().random(min, max))
            vertexIndex[name] = currentVertex

            jsonNode.edges.forEachIndexed { i, edge ->
                val edgeNode = Node(edge.name, Vector20().random(min, max))
                val edgeVertex = vertexIndex[edge.name] ?: finalGraph.createVertex(edgeNode)
                vertexIndex[edge.name] = edgeVertex
                finalGraph.addDirectedEdge(currentVertex, edgeVertex, 0.0)
            }
        }

        return finalGraph
    }
}
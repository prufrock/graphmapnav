package com.dkanen.graphmapnav.game.graph.Positioner

import com.dkanen.graphmapnav.collections.graphs.AdjacencyList
import com.dkanen.graphmapnav.collections.graphs.Graph
import com.dkanen.graphmapnav.collections.graphs.Vertex
import com.dkanen.graphmapnav.game.Node
import com.dkanen.graphmapnav.game.graph.JsonGraphNodeList
import com.dkanen.graphmapnav.math.Vector20
import com.dkanen.graphmapnav.math.random
import org.openrndr.math.Vector2
import org.openrndr.math.clamp
import kotlin.math.pow

/**
 * Tolerance doesn't make sense to me yet. Should it be the average distance between all nodes?
 * Nodes that are connected to each other aren't overcoming the repulsive force, so they are spread out.
 */
class ForceDirectedPositioner(private val min: Double, private val max: Double): Positioner {
    override fun position(nodeList: JsonGraphNodeList, origin: Vector2): Graph<Node<String>> {
        val finalGraph = randomlyPositionNodes(nodeList)

        val edgeLength = 0.005 // K
        var converged = false
        val stepLength = 0.0001
        val tolerance = 0.001
        val maxIterations = 50000
        var iteration = 0
        while (! converged && iteration < maxIterations) {
            val vertices = finalGraph.allVertices
            val xOriginal: Vector2 = (vertices[0].data.position - vertices[vertices.count() - 1].data.position).abs()
            vertices.forEach { vertex ->
                val node = vertex.data
                val attractiveForces = mutableListOf<Vector2>()
                val repulsiveForces = mutableListOf<Vector2>()
                finalGraph.edges(vertex).forEach { otherVertex ->
                    if (vertex != otherVertex) {
                        val otherNode = otherVertex.destination.data
                        val attractiveForce = attractiveSpringForce(node, otherNode, edgeLength)
                        attractiveForces.add(attractiveForce)
                        val repulsiveForce = repulsiveSpringForce(node, otherNode, edgeLength)
                        repulsiveForces.add(repulsiveForce)
                    }
                }
                val attractiveForce = attractiveForces.reduceOrNull { acc, vector2 -> acc + vector2 } ?: Vector20()
                val repulsiveForce = repulsiveForces.reduceOrNull { acc, vector2 -> acc + vector2 } ?: Vector20()
                val force = attractiveForce + repulsiveForce
                val newPosition = node.position + force * stepLength
                if (!newPosition.x.isNaN() && !newPosition.y.isNaN()) {
                    println("newPosition: $newPosition")
//                    node.position =  newPosition.clamp(Vector2(min), Vector2(max))
                    node.position = newPosition
                    println("nodePosition: ${node.position}")
                }
            }
            val xChanged: Vector2 = (vertices[0].data.position - vertices[vertices.count() - 1].data.position).abs()

            if (iteration >= (maxIterations / 2) &&(xOriginal - xChanged).abs().length < tolerance) {
                println("converged in iterations: $iteration")
                converged = true
            }
            iteration += 1
        }

        return finalGraph
    }

    private fun randomlyPositionNodes(nodeList: JsonGraphNodeList): Graph<Node<String>> {
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

    private fun attractiveSpringForce(firstNode: Node<String>, secondNode: Node<String>, edgeLength: Double): Vector2 {
        return - (
                ((firstNode.position - secondNode.position).abs().sq() / edgeLength) *
                (firstNode.position - secondNode.position / (firstNode.position - secondNode.position).abs())
                )
    }

    private fun repulsiveSpringForce(firstNode: Node<String>, secondNode: Node<String>, edgeLength: Double): Vector2 {
        // works better when pow is 1(edgeLength isn't squared)
        return ((firstNode.position - secondNode.position).abs().sq() * (1/edgeLength.pow(2)) *
                        (firstNode.position - secondNode.position / (firstNode.position - secondNode.position).abs())
                )
    }
}

fun Vector2.abs(): Vector2 {
    return Vector2(Math.abs(this.x), Math.abs(this.y))
}

fun Vector2.sq(): Vector2 {
    return Vector2(this.x * this.x, this.y * this.y)
}
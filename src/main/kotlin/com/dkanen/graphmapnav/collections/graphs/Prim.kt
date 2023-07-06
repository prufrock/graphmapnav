package com.dkanen.graphmapnav.collections.graphs

import com.dkanen.graphmapnav.collections.queues.AbstractPriorityQueue
import com.dkanen.graphmapnav.collections.queues.ComparatorPriorityQueueImpl
import kotlin.math.roundToInt

object Prim {
    private fun <T: Any> addAvailableEdges(
        vertex: Vertex<T>,
        graph: Graph<T>,
        visited: Set<Vertex<T>>,
        priorityQueue: AbstractPriorityQueue<Edge<T>>
    ) {
        // Add all of a vertex's unvisited edges to a priority queue.
        // The priority queue ensures the lowest weight edge is taken next.
        graph.edges(vertex).forEach { edge ->
            if (edge.destination !in visited) {
                priorityQueue.enqueue(edge)
            }
        }
    }

    /**
     * Produces a minimum spanning tree for the graph.
     * Time complexity: O(E log E) because each time the smallest edge is dequeue its edges
     * have to be traversed O(E). Then each of its edges have to be inserted into the priority
     * queue O(log E).
     */
    fun <T: Any> produceMinimumSpanningTree(
        graph: AdjacencyList<T>
    ): Pair<Double, AdjacencyList<T>> {
        var cost = 0.0 // Track the total cost of the minimum spanning tree.
        val mst = AdjacencyList<T>() // The minimum spanning tree.
        val visited = mutableSetOf<Vertex<T>>() // All the visited vertices so we don't visit them again.

        // A minimum priority queue the keeps the smallest edge at the front of the queue.
        val comparator = Comparator<Edge<T>> { first, second ->
            val firstWeight = first.weight ?: 0.0
            val secondWeight = second.weight ?: 0.0
            (secondWeight - firstWeight).roundToInt()
        }
        val priorityQueue = ComparatorPriorityQueueImpl(comparator)

        // Bring the vertices into the minimum spanning tree graph.
        mst.copyVertices(graph)

        // If the graph is empty return what you got
        // otherwise let's get started.
        val start = graph.vertices.firstOrNull() ?: return Pair(cost, mst)

        // Add the current vertex to the list of visited vertices
        visited.add(start)
        // Gather all of that vertex's edges into the priority queue.
        addAvailableEdges(start, graph, visited, priorityQueue)

        while(true) {
            // As long as there is stuff in the queue keep going.
            val smallestEdge = priorityQueue.dequeue() ?: break
            // Examine the vertex this edge points to.
            val vertex = smallestEdge.destination
            // If it's already been examined keep going.
            if (visited.contains(vertex)) continue

            // Mark this vertex visited.
            visited.add(vertex)
            // Add the cost of the edge to the total.
            cost += smallestEdge.weight ?: 0.0

            // Add the edge to the graph.
            mst.add(EdgeType.UNDIRECTED, smallestEdge.source, smallestEdge.destination, smallestEdge.weight)

            // Add all of this vertex's edges to the priority queue for processing.
            addAvailableEdges(vertex, graph, visited, priorityQueue)
        }

        return Pair(cost, mst)
    }
}
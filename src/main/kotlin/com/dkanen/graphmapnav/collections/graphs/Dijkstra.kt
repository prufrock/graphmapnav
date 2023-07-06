package com.dkanen.graphmapnav.collections.graphs

import com.dkanen.graphmapnav.collections.queues.ComparatorPriorityQueueImpl

/**
 * Finds the shortest paths from a given node.
 */
class Dijkstra<T: Any>(private val graph: AdjacencyList<T>) {

    /**
     * Follows route from the destination to the start.
     */
    private fun route(destination: Vertex<T>, paths: HashMap<Vertex<T>, Visit<T>>): ArrayList<Edge<T>> {
        var vertex = destination // The vertex expected to be reached.
        val path = arrayListOf<Edge<T>>() // The edges followed to get to the vertex.

        loop@ while (true) {
            val visit = paths[vertex] ?: break

            when(visit.type) {
                // As long as there is a path with valid edges
                VisitType.EDGE -> visit.edge?.let {
                    // Add the edge to the path.
                    path.add(it)
                    // Use the `source` of the edge as the path to the next vertex to follow.
                    vertex = it.source
                }
                // End the loop once it reaches the START
                VisitType.START -> break@loop
            }
        }

        return path
    }

    /**
     * Calculate the cost of getting to the destination by working backwards from the destination
     * to the start. Then add up all the weights.
     */
    private fun distance(destination: Vertex<T>, paths: HashMap<Vertex<T>, Visit<T>>): Double {
        val path = route(destination, paths)
        return path.sumOf { it.weight ?: 0.0 }
    }

    /**
     * Finds the shortest path from the start to all the vertices in the graph.
     * Time complexity: O(E log V) because all edges need to be traversed and managing the priority is O(log V)
     */
    fun shortestPath(start: Vertex<T>): HashMap<Vertex<T>, Visit<T>> {
        // Create `paths` with the start vertex
        val paths: HashMap<Vertex<T>, Visit<T>> = HashMap()
        paths[start] = Visit(VisitType.START)

        // Whichever path is smaller goes to the front of the priority queue.
        // min-priority queue
        val distanceComparator = Comparator<Vertex<T>> { first, second ->
            (distance(second, paths) - distance(first, paths)).toInt()
        }

        val priorityQueue = ComparatorPriorityQueueImpl(distanceComparator)

        priorityQueue.enqueue(start)

        while (true) {
            // always take the shortest path because Dijkstra is greedy
            val vertex = priorityQueue.dequeue() ?: break
            val edges = graph.edges(vertex)

            // examine all the edges before examining the child's edges (bfs)
            edges.forEach {
                // if an edge doesn't have a weight don't consider it.
                val weight = it.weight ?: return@forEach

                // Add the path if it hasn't been visited before.
                // OR
                // Add the path destination if the path is less than an existing path to that destination.
                // Is there a way to not check the distance every time? Cache the calculated distances?
                if (paths[it.destination] == null
                    || distance(vertex, paths) + weight < distance(it.destination, paths)) {
                    paths[it.destination] = Visit(VisitType.EDGE, it)
                    // enqueue this shortest path so it can keep being greedy
                    priorityQueue.enqueue(it.destination)
                    // then go back around again!
                }
            }
        }

        return paths
    }

    /**
     * Returns the shortest path to the given vertex.
     */
    fun shortestPath(destination: Vertex<T>, paths: HashMap<Vertex<T>, Visit<T>>): ArrayList<Edge<T>> {
        return route(destination, paths)
    }

    fun getAllShortestPath(source: Vertex<T>): HashMap<Vertex<T>, ArrayList<Edge<T>>> {
        val paths = HashMap<Vertex<T>, ArrayList<Edge<T>>>()

        // Find all the paths from the source.
        val pathsFromSource = shortestPath(source)

        // Look through each of the edges to find the path, if it exists, to the source.
        graph.allVertices.forEach {
            if (it != source) {
                val path = shortestPath(it, pathsFromSource)
                if (path.isNotEmpty()) {
                    paths[it] = path
                }
            }
        }

        return paths
    }
}

class Visit<T: Any>(val type: VisitType, val edge: Edge<T>? = null)

enum class VisitType {
    START, // The vertex is the starting edge
    EDGE // The vertex has an edge that leads back to START
}
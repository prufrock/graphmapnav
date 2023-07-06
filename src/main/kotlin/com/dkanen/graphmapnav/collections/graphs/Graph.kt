package com.dkanen.graphmapnav.collections.graphs

import com.dkanen.graphmapnav.collections.queues.StackQueue
import com.dkanen.graphmapnav.collections.stacks.StackImpl

interface Graph<T: Any> {

    abstract val allVertices: ArrayList<Vertex<T>>

    /**
     * Creates a vertex and adds it to the graph.
     */
    fun createVertex(data: T): Vertex<T>

    /**
     * Adds a directed edge between two vertices.
     */
    fun addDirectedEdge(source: Vertex<T>,
                        destination: Vertex<T>,
                        weight: Double?)

    /**
     * Adds an undirected (or bi-directional) edge between two vertices.
     */
    fun addUndirectedEdge(source: Vertex<T>,
                          destination: Vertex<T>,
                          weight: Double?) {
        addDirectedEdge(source, destination, weight)
        addDirectedEdge(destination, source, weight)
    }

    /**
     * Uses `EdgeType` to add either a directed or undirected edge between two vertices.
     */
    fun add(edge: EdgeType,
            source: Vertex<T>,
            destination: Vertex<T>,
            weight: Double?) {
        when (edge) {
            EdgeType.DIRECTED -> addDirectedEdge(source, destination, weight)
            EdgeType.UNDIRECTED -> addUndirectedEdge(source, destination, weight)
        }
    }

    /**
     * Use a depth-first traversal to climb down a path until the destination is found.
     * Use the stack to back-track.
     * Handles cycles!
     * Time complexity: O(V + E)
     */
    fun numberOfPaths(source: Vertex<T>, destination: Vertex<T>): Int {
        // keeps track of the number of paths found
        val numberOfPaths = Ref(0)
        // tracks all the visited vertices
        val visited: MutableSet<Vertex<T>> = mutableSetOf()
        // paths uses all these bits to actually find the number of paths
        paths(source, destination, visited, numberOfPaths)
        return numberOfPaths.value
    }

    private fun paths(
        source: Vertex<T>,
        destination: Vertex<T>,
        visited: MutableSet<Vertex<T>>,
        pathCount: Ref<Int>
    ) {
        // Don't allow cycling back through the source.
        visited.add(source)
        if (source == destination) {
            // Increment paths when the source matches the destination.
            pathCount.value += 1
        } else {
            // Check all the connected edges.
            val neighbors = edges(source)
            neighbors.forEach {edge ->
                // Recursively check all of the edges that haven't been visited yet during this traversal.
                if (edge.destination !in visited) {
                    paths(edge.destination, destination, visited, pathCount)
                }
            }
        }

        // Remove the source after traversing all the connected nodes so it can be visited from other nodes.
        visited.remove(source)
    }

    /**
     * Returns a list of outgoing edges from a specific vertex.
     */
    fun edges(source: Vertex<T>): ArrayList<Edge<T>>

    /**
     * Returns the weight of the edge between two vertices.
     */
    fun weight(source: Vertex<T>, destination: Vertex<T>): Double?

    /**
     * Starts at a vertex then traverses its neighbors and then those neighbors and so on.
     * This is more like a breadth first sort since it doesn't have a goal.
     * time complexity: O(V+E)
     * space complexity: O(V)
     */
    fun breadthFirstTraversal(source: Vertex<T>, visitor: Visitor<T>) {
        // tracks the vertices to visit next
        val queue = StackQueue<Vertex<T>>()
        // ensures vertices are only visited once
        val enqueued = mutableSetOf<Vertex<T>>()
        // tracks the order vertices were stored in

        // get the BFS part started by enqueuing the starting vertex
        queue.enqueue(source)
        // add it to the set of enqueued vertices, so it isn't visited again
        enqueued.add(source)

        while (true) {
            // as long as there are items to dequeue keeping going
            val vertex = queue.dequeue() ?: break

            // do something with the vertex
            visitor(vertex)
            // queue up all the neighbors that haven't been queued before
            val neighborEdges = edges(vertex)
            neighborEdges.forEach {
                if (!enqueued.contains(it.destination)) {
                    queue.enqueue(it.destination)
                    enqueued.add(it.destination)
                }
            }
        }
    }

    /**
     * A recursive implementation of the breadth first traversal.
     * Time complexity O(V+E)
     */
    fun breadthFirstTraversalRecursive(source: Vertex<T>, visitor: Visitor<T>) {
        val vertexQueue = StackQueue<Vertex<T>>()
        // it's better to name this enqueued rather than visited set because
        // the algo may not visit it right away.
        val enqueued = mutableSetOf<Vertex<T>>()

        vertexQueue.enqueue(source)
        enqueued.add(source)

        breadthFirstTraversalRecursiveImplementation(vertexQueue, enqueued, visitor)
    }

    private fun breadthFirstTraversalRecursiveImplementation(
        vertexQueue: StackQueue<Vertex<T>> = StackQueue(),
        enqueued: MutableSet<Vertex<T>> = mutableSetOf(),
        visitor: Visitor<T>
    ) {

        // This is really clever.
        // You don't need to pass the source vertex removing the need to peek before calling.
        // It avoids needing to empty the queue before adding the current vertex's neighbors.
        val vertex = vertexQueue.dequeue() ?: return

        // Process the vertex.
        visitor(vertex)

        // Add all this node's unvisited neighbors
        edges(vertex).forEach { edge ->
            if (!enqueued.contains(edge.destination)) {
                vertexQueue.enqueue(edge.destination)
                // Don't forget to add it to `enqueued` so we don't loop on it again.
                enqueued.add(edge.destination)
            }
        }

        // Go back around again.
        breadthFirstTraversalRecursiveImplementation(vertexQueue, enqueued, visitor)
    }


    /**
     * Uses breadthFirstTraversal so has the same Big O time and space complexity.
     */
    fun breadthFirstSort(source: Vertex<T>): ArrayList<Vertex<T>> {
        val visited = ArrayList<Vertex<T>>()

        breadthFirstTraversal(source) { visited.add(it) }

        return visited
    }

    /**
     * Check to see if the vertex is not connected to the rest of the graph.
     * This implementation relies on `allVertices` property where implementation supplies a list of vertices.
     * Especially interesting for AdjacencyList which uses the keys as the list.
     */
    fun isDisconnected(source: Vertex<T>): Boolean {
        val firstVertex = allVertices.firstOrNull() ?: return false

        val visited = breadthFirstSort(firstVertex)
        // stop as soon as you DO NOT find a vertex in the list of visited.
        allVertices.forEach {
            if (!visited.contains(it)) return true
        }

        return false
    }

    /**
     * Search a graph depth first: start at a vertex and follow edges to the furthest connected vertex
     * then work backward.
     * Time complexity: O(V+E)
     * Space complexity: O(V)
     */
    fun depthFirstSearch(source: Vertex<T>): ArrayList<Vertex<T>> {
        // Store the vertices that need to be processed
        val stack = StackImpl<Vertex<T>>()
        // Stores the order in which the nodes were visited (replace with visitor later)
        val visited = arrayListOf<Vertex<T>>()
        // Track the vertices that were already added to the stack to avoid adding them again.
        val pushed = mutableSetOf<Vertex<T>>()

        // Get the party started with the source vertex.
        stack.push(source)
        pushed.add(source)
        // pre-order traversal
        visited.add(source)

        // Put the label on here to break out of nested loops
        outer@ while (true) {
            // stop if there's nothing left to process.
            if (stack.isEmpty) break

            // grab the first vertex but don't pop it yet
            val vertex = stack.peek()!!
            val neighbors = edges(vertex)

            // if it has no neighbors pop it
            if (neighbors.isEmpty()) {
                stack.pop()
                continue
            }

            // Visit each one and add each to the stack.
            for (i in 0 until neighbors.size) {
                val destination = neighbors[i].destination
                // Only check a vertex if it hasn't been checked yet.
                if (destination !in pushed) {
                    stack.push(destination)
                    pushed.add(destination)
                    visited.add(destination)
                    // Continue back to the while loop without popping so we can back track to here
                    continue@outer
                }
            }
            // Pop after all the neighbors have been processed.
            stack.pop()
        }

        return visited
    }

    fun depthFirstSearchRecursive(source: Vertex<T>, pushed: MutableSet<Vertex<T>> = mutableSetOf(), visit: Visitor<T>) {
        // Since this is recursive simply calling the function pushes the vertex onto the call stack so a `stack` isn't needed.
        pushed.add(source)

        // Let the visitor process the vertex
        visit(source)

        // Grab it's neighbors
        val neighbors = edges(source)

        // If no neighbors backtrack!
        if (neighbors.isEmpty()) {
            return
        }

        // Search the neighbors unless they have already been searched
        neighbors.forEach { edge ->
            if (!pushed.contains(edge.destination)) {
                depthFirstSearchRecursive(edge.destination, pushed, visit)
            }
        }

        // Backtrack after processing all the neighbors
        return
    }

    /**
     * Sort a graph topologically.
     * Ordered so that all nodes can be ordered from left to right
     * and all the nodes point to the right.
     * TODO: create a version that generates a graph.
     */
    fun topSort(): ArrayList<Vertex<T>> {
        val ordered = arrayListOf<Vertex<T>>()
        val pushed = mutableSetOf<Vertex<T>>()

        allVertices.forEach { source ->
            if (!pushed.contains(source)) {
                depthFirstSearchRecursiveVisitBackTrack(source, pushed) {
                    ordered.add(it)
                }
            }
        }

        ordered.reverse()

        return ordered
    }

    private fun depthFirstSearchRecursiveVisitBackTrack(source: Vertex<T>, pushed: MutableSet<Vertex<T>> = mutableSetOf(), visit: Visitor<T>) {
        // Since this is recursive simply calling the function pushes the vertex onto the call stack so a `stack` isn't needed.
        pushed.add(source)

        // Grab it's neighbors
        val neighbors = edges(source)

        // Search the neighbors unless they have already been searched
        neighbors.forEach { edge ->
            if (!pushed.contains(edge.destination)) {
                depthFirstSearchRecursiveVisitBackTrack(edge.destination, pushed, visit)
            }
        }

        // Backtrack after processing all the neighbors
        visit(source)
        return
    }

    /**
     * A graph has a cycle if there is more than one path to the same vertex.
     * This only checks to see if there is a cycle somewhere in the paths connected to `source`.
     * If the graph is disconnected then there may still be a cycle.
     * There also be a cycle if there is no path from `source` to the cycle.
     */
    fun hasCycle(source: Vertex<T>): Boolean {
        return checkForCycle(source)
    }

    private fun checkForCycle(source: Vertex<T>, pushed: MutableSet<Vertex<T>> = mutableSetOf()): Boolean {
        // Since this is recursive simply calling the function pushes the vertex onto the call stack.
        pushed.add(source)

        // Search the neighbors unless they have already been searched
        edges(source).forEach { edge ->
            if (pushed.contains(edge.destination)) {
                // Got one! Pass it up!
                return true
            } else {
                // Keep checking for neighbors for cycles until you find one.
                // Once you do pass it up the call stack.
                if (checkForCycle(edge.destination, pushed)) {
                    return true
                }
            }
        }

        // The solution removes the current vertex before returning here.
        // I don't think a graph exists that this would be necessary.
        // If a vertex ever has to be checked again there is a cycle.
        // pushed.remove(source)
        return false
    }
}

enum class EdgeType {
    DIRECTED,
    UNDIRECTED
}

// Allows primitives like Int to be passed by reference
data class Ref<T: Any>(var value: T)

typealias Visitor<T> = (Vertex<T>) -> Unit

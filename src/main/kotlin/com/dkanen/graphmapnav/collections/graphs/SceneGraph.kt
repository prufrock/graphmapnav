package com.dkanen.graphmapnav.collections.graphs

import com.dkanen.graphmapnav.game.ecs.components.graphics.Graphics
import com.dkanen.graphmapnav.math.translate
import org.openrndr.math.Matrix33

interface Scene {

    /**
     * Creates a vertex and adds it to the graph.
     */
    fun createVertex(data: Graphics): Vertex<Graphics>

    fun addChild(data: Graphics): Vertex<Graphics>

    fun addChild(data: Vertex<Graphics>): Vertex<Graphics>

    fun addChild(parent: Vertex<Graphics>, child: Vertex<Graphics>)

    fun render(action: (Vertex<Graphics>) -> Unit)
}

class SceneGraph(val graph: Graph<Graphics> = AdjacencyList()): Scene, Graph<Graphics> by graph {
    private var rootNode: Vertex<Graphics>? = null

    val vertices: List<Vertex<Graphics>>
        get() =  graph.breadthFirstSort(rootNode!!)

    override fun createVertex(data: Graphics): Vertex<Graphics> {
        val v = graph.createVertex(data)

        if (rootNode == null) {
            rootNode = v
        }

        return v
    }

    /**
     * Add a child to the root node.
     */
    override fun addChild(data: Graphics): Vertex<Graphics> {
        val v = createVertex(data)

        addChild(rootNode!!, v)

        return v
    }

    override fun addChild(data: Vertex<Graphics>): Vertex<Graphics> {
        addChild(rootNode!!, data)

        return data
    }

    override fun addChild(parent: Vertex<Graphics>, child: Vertex<Graphics>) = addDirectedEdge(parent, child, null)

    /**
     * Use a depth first traversal to apply transformations from parent to children in the graph.
     */
    private fun transform(source: Vertex<Graphics>, pushed: MutableSet<Vertex<Graphics>> = mutableSetOf(), render: Matrix33 = Matrix33.IDENTITY) {
        // Since this is recursive simply calling the function pushes the vertex onto the call stack so a `stack` isn't needed.
        pushed.add(source)

        val graphics = source.data
        // Perform the transformation here so that as the stack unrolls children get the correct transformations
        val renderProduct = Matrix33.translate(graphics.position.x, graphics.position.y) * render
        source.data.uprightToWorldMatrix = renderProduct

        // Grab it's neighbors
        val neighbors = edges(source)

        // If no neighbors backtrack!
        if (neighbors.isEmpty()) {
            return
        }

        // Search the neighbors unless they have already been searched
        neighbors.forEach { edge ->
            if (!pushed.contains(edge.destination)) {
                transform(edge.destination, pushed, renderProduct)
            }
        }

        // Backtrack after processing all the neighbors
        return
    }

    override fun render(action: (Vertex<Graphics>) -> Unit) {
        // TODO: the first element always needs to be the root of the scene!
        // Could make this one operation if the order was depth first.
        // Painterly ordering makes more sense with depth first but may have to rethink that.
        rootNode?.let {
            transform(it)
            breadthFirstTraversalRecursive(it, action)
        }
    }
}

val Vertex<Graphics>.entityId
    get() = data.entitySlug

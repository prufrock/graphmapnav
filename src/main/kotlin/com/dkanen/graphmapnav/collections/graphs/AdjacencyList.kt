package com.dkanen.graphmapnav.collections.graphs

/**
 * With an adjacency list the graph stores a list of outgoing edges for each vertex.
 */
class AdjacencyList<T: Any>: Graph<T> {
    private val adjacencies: HashMap<Vertex<T>, ArrayList<Edge<T>>> = HashMap()

    override val allVertices: ArrayList<Vertex<T>>
        get() = ArrayList(adjacencies.keys)

    /**
     * A set of all the vertices.
     */
    val vertices: Set<Vertex<T>>
        get() = adjacencies.keys

    /**
     * Copy all the vertices from a graph into this one.
     */
    fun copyVertices(graph: AdjacencyList<T>) {
        graph.vertices.forEach {
            adjacencies[it] = arrayListOf()
        }
    }

    override fun createVertex(data: T): Vertex<T> {
        // create the vertex with it's place in the adjacency list and the data
        val vertex = Vertex(adjacencies.count(), data)
        // store the vertex in the adjacency list with a fresh list for its outgoing edges
        adjacencies[vertex] = ArrayList()
        return vertex
    }

    override fun addDirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        // create a new edge
        val edge = Edge(source, destination, weight)
        // add it to the adjacency list for the vertex.
        adjacencies[source]?.add(edge)
    }

    override fun edges(source: Vertex<T>) = adjacencies[source] ?: arrayListOf()
    // either return the stored edges or an empty list if the source isn't in the adjacency list

    override fun weight(source: Vertex<T>, destination: Vertex<T>): Double? {
        return edges(source).firstOrNull { it.destination == destination }?.weight
    }

    override fun toString(): String {
        return buildString {
            adjacencies.forEach { (vertex, edges) ->
                val edgeString = edges.joinToString {
                    it.destination.data.toString()
                }
                append("${vertex.data} ---> [ $edgeString ]\n")
            }
        }
    }
}
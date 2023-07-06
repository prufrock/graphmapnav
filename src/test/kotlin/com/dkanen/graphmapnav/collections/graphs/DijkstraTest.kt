package com.dkanen.graphmapnav.collections.graphs

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
class DijkstraTest {
    @Test
    fun shortestPath() {
        val graph = AdjacencyList<String>()
        val a = graph.createVertex("A")
        val b = graph.createVertex("B")
        val c = graph.createVertex("C")
        val d = graph.createVertex("D")
        val e = graph.createVertex("E")
        val f = graph.createVertex("F")
        val g = graph.createVertex("G")
        val h = graph.createVertex("H")

        graph.add(EdgeType.DIRECTED, a, b, 8.0)
        graph.add(EdgeType.DIRECTED, a, f, 9.0)
        graph.add(EdgeType.DIRECTED, a, g, 1.0)

        graph.add(EdgeType.DIRECTED, b, e, 1.0)
        graph.add(EdgeType.DIRECTED, b, f, 3.0)

        graph.add(EdgeType.DIRECTED, c, b, 3.0)
        graph.add(EdgeType.DIRECTED, c, e, 1.0)

        graph.add(EdgeType.DIRECTED, e, c, 8.0)
        graph.add(EdgeType.DIRECTED, e, d, 2.0)

        graph.add(EdgeType.DIRECTED, f, a, 2.0)

        graph.add(EdgeType.DIRECTED, g, c, 3.0)

        graph.add(EdgeType.DIRECTED, h, f, 2.0)
        graph.add(EdgeType.DIRECTED, h, g, 5.0)

        val dijkstra = Dijkstra(graph)

        val pathsFromA = dijkstra.shortestPath(a)
        val path = dijkstra.shortestPath(d, pathsFromA)

        assertEquals(1.0, path[3].weight)

        path.forEach {
            println("${it.source.data} --|${it.weight ?: 0.0}|--> ${it.destination.data}")
        }
    }

    @Test
    fun getAllShortestPath() {
        val graph = AdjacencyList<String>()
        val a = graph.createVertex("A")
        val b = graph.createVertex("B")
        val c = graph.createVertex("C")
        val d = graph.createVertex("D")
        val e = graph.createVertex("E")
        val f = graph.createVertex("F")
        val g = graph.createVertex("G")
        val h = graph.createVertex("H")

        graph.add(EdgeType.DIRECTED, a, b, 8.0)
        graph.add(EdgeType.DIRECTED, a, f, 9.0)
        graph.add(EdgeType.DIRECTED, a, g, 1.0)

        graph.add(EdgeType.DIRECTED, b, e, 1.0)
        graph.add(EdgeType.DIRECTED, b, f, 3.0)

        graph.add(EdgeType.DIRECTED, c, b, 3.0)
        graph.add(EdgeType.DIRECTED, c, e, 1.0)

        graph.add(EdgeType.DIRECTED, e, c, 8.0)
        graph.add(EdgeType.DIRECTED, e, d, 2.0)

        graph.add(EdgeType.DIRECTED, f, a, 2.0)

        graph.add(EdgeType.DIRECTED, g, c, 3.0)

        graph.add(EdgeType.DIRECTED, h, f, 2.0)
        graph.add(EdgeType.DIRECTED, h, g, 5.0)

        val dijkstra = Dijkstra(graph)

        val paths = dijkstra.getAllShortestPath(a)

        paths.forEach { v, edges ->
            println(v.data)
            edges.forEach {
                println("${it.source.data} --|${it.weight ?: 0.0}|--> ${it.destination.data}")
            }
        }

        assertEquals(3, paths[b]!!.count())
    }
}
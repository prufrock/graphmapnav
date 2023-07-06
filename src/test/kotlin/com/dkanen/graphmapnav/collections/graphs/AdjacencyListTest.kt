package com.dkanen.graphmapnav.collections.graphs

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
internal class AdjacencyListTest {
    @Test
    fun `airport example`() {
        val graph = AdjacencyList<String>()

        val singapore = graph.createVertex("Singapore")
        val tokyo = graph.createVertex("Tokyo")
        val hongKong = graph.createVertex("Hong Kong")
        val detroit = graph.createVertex("Detroit")
        val sanFrancisco = graph.createVertex("San Francisco")
        val washingtonDC = graph.createVertex("Washington DC")
        val austinTexas = graph.createVertex("Austin")
        val seattle = graph.createVertex("Seattle")

        graph.add(EdgeType.UNDIRECTED, singapore, hongKong, 300.0)
        graph.add(EdgeType.UNDIRECTED, singapore, tokyo, 500.0)
        graph.add(EdgeType.UNDIRECTED, hongKong, tokyo, 250.0)
        graph.add(EdgeType.UNDIRECTED, tokyo, detroit, 450.0)
        graph.add(EdgeType.UNDIRECTED, tokyo, washingtonDC, 300.0)
        graph.add(EdgeType.UNDIRECTED, hongKong, sanFrancisco, 600.0)
        graph.add(EdgeType.UNDIRECTED, detroit, austinTexas, 50.0)
        graph.add(EdgeType.UNDIRECTED, austinTexas, washingtonDC, 292.0)
        graph.add(EdgeType.UNDIRECTED, sanFrancisco, washingtonDC, 337.0)
        graph.add(EdgeType.UNDIRECTED, washingtonDC, seattle, 277.0)
        graph.add(EdgeType.UNDIRECTED, sanFrancisco, seattle, 218.0)
        graph.add(EdgeType.UNDIRECTED, austinTexas, sanFrancisco, 297.0)

        assertEquals(600.0, graph.weight(hongKong, sanFrancisco))
        println(graph)
        println("San Francisco Outgoing Flights:")
        println("--------------------------------")
        graph.edges(sanFrancisco).forEach { edge ->
            println("from: ${edge.source.data} to: ${edge.destination.data}")
        }
    }

    @Test
    fun `number of paths`() {
        val graph = AdjacencyList<String>()

        val a = graph.createVertex("a")
        val b = graph.createVertex("b")
        val c = graph.createVertex("c")
        val d = graph.createVertex("d")
        val e = graph.createVertex("e")

        // by outgoing edges
        graph.add(EdgeType.DIRECTED, a, b, 1.0)
        graph.add(EdgeType.DIRECTED, a, c, 1.0)
        graph.add(EdgeType.DIRECTED, a, d, 1.0)
        graph.add(EdgeType.DIRECTED, a, e, 1.0)

        graph.add(EdgeType.DIRECTED, b, c, 1.0)
        graph.add(EdgeType.DIRECTED, b, d, 1.0)

        graph.add(EdgeType.DIRECTED, c, e, 1.0)

        graph.add(EdgeType.DIRECTED, d, e, 1.0)
        assertEquals(1.0, graph.weight(a, e))

        assertEquals(0, graph.numberOfPaths(e, d))
        assertEquals(1, graph.numberOfPaths(d, e))
        assertEquals(2, graph.numberOfPaths(b, e))
        assertEquals(5, graph.numberOfPaths(a, e))


        val result = graph.breadthFirstSort(a)

        assertEquals(listOf("a","b","c","d","e"), result.map { it.data })


    }

    @Test
    fun `breadth first recursive traversal`() {
        val resultRecursive = arrayListOf<Vertex<String>>()

        val graph = AdjacencyList<String>()

        val a = graph.createVertex("a")
        val b = graph.createVertex("b")
        val c = graph.createVertex("c")
        val d = graph.createVertex("d")
        val e = graph.createVertex("e")

        // by outgoing edges
        graph.add(EdgeType.DIRECTED, a, b, 1.0)
        graph.add(EdgeType.DIRECTED, a, c, 1.0)

        graph.add(EdgeType.DIRECTED, b, d, 1.0)

        graph.add(EdgeType.DIRECTED, d, e, 1.0)
        graph.add(EdgeType.DIRECTED, d, a, 1.0)

        graph.breadthFirstTraversalRecursive(a) {
            resultRecursive.add(it)
        }

        println(graph)
        assertEquals(listOf("a","b","c","d","e"), resultRecursive.map { it.data })
    }

    @Test
    fun `disconnected graphs`() {
        val graph = AdjacencyList<String>()

        val a = graph.createVertex("a")
        val b = graph.createVertex("b")
        val c = graph.createVertex("c")
        val d = graph.createVertex("d")
        val e = graph.createVertex("e")

        // by outgoing edges
        graph.add(EdgeType.DIRECTED, a, b, 1.0)
        graph.add(EdgeType.DIRECTED, a, c, 1.0)

        // `a` has no connection to d

        graph.add(EdgeType.DIRECTED, d, e, 1.0)
        graph.add(EdgeType.DIRECTED, d, a, 1.0)

        assertTrue(graph.isDisconnected(a))
    }
    @Test
    fun `Connected graphs`() {
        val graph = AdjacencyList<String>()

        val a = graph.createVertex("a")
        val b = graph.createVertex("b")
        val c = graph.createVertex("c")
        val d = graph.createVertex("d")
        val e = graph.createVertex("e")

        // by outgoing edges
        graph.add(EdgeType.DIRECTED, a, b, 1.0)
        graph.add(EdgeType.DIRECTED, a, c, 1.0)

        // connected `a` to `d` through `b`
        graph.add(EdgeType.DIRECTED, b, d, 1.0)

        graph.add(EdgeType.DIRECTED, d, e, 1.0)
        graph.add(EdgeType.DIRECTED, d, a, 1.0)

        assertFalse(graph.isDisconnected(a))
    }

    @Test
    fun `Test depth first search`() {
        val graph = AdjacencyList<String>()

        val a = graph.createVertex("a")
        val b = graph.createVertex("b")
        val c = graph.createVertex("c")
        val d = graph.createVertex("d")
        val e = graph.createVertex("e")
        val f = graph.createVertex("f")
        val g = graph.createVertex("g")
        val h = graph.createVertex("h")

        // by outgoing edges
        graph.add(EdgeType.DIRECTED, a, b, 1.0)
        graph.add(EdgeType.DIRECTED, a, c, 1.0)
        graph.add(EdgeType.DIRECTED, a, d, 1.0)

        graph.add(EdgeType.DIRECTED, b, e, 1.0)

        graph.add(EdgeType.DIRECTED, c, f, 1.0)
        graph.add(EdgeType.DIRECTED, c, g, 1.0)

        graph.add(EdgeType.DIRECTED, e, h, 1.0)
        graph.add(EdgeType.DIRECTED, e, f, 1.0)

        val result = graph.depthFirstSearch(a)

        println(graph)
        assertEquals(listOf(a, b, e, h, f, c, g, d), result)
    }

    @Test
    fun `depth first recursive traversal`() {
        val resultRecursive = arrayListOf<Vertex<String>>()

        val graph = AdjacencyList<String>()

        val a = graph.createVertex("a")
        val b = graph.createVertex("b")
        val c = graph.createVertex("c")
        val d = graph.createVertex("d")
        val e = graph.createVertex("e")
        val f = graph.createVertex("f")
        val g = graph.createVertex("g")
        val h = graph.createVertex("h")

        // by outgoing edges
        graph.add(EdgeType.DIRECTED, a, b, 1.0)
        graph.add(EdgeType.DIRECTED, a, c, 1.0)
        graph.add(EdgeType.DIRECTED, a, d, 1.0)

        graph.add(EdgeType.DIRECTED, b, e, 1.0)

        graph.add(EdgeType.DIRECTED, c, f, 1.0)
        graph.add(EdgeType.DIRECTED, c, g, 1.0)

        graph.add(EdgeType.DIRECTED, e, h, 1.0)
        graph.add(EdgeType.DIRECTED, e, f, 1.0)

        graph.depthFirstSearchRecursive(a) {
            resultRecursive.add(it)
        }

        println(graph)
        assertEquals(listOf(a, b, e, h, f, c, g, d), resultRecursive )
    }

    @Test
    fun `one graph without a cycle and another with`() {
        val graph = AdjacencyList<String>()

        val a = graph.createVertex("a")
        val b = graph.createVertex("b")
        val c = graph.createVertex("c")

        graph.add(EdgeType.DIRECTED, a, b, 1.0)
        graph.add(EdgeType.DIRECTED, b, c, 1.0)
        println(graph)
        assertFalse(graph.hasCycle(a))

        // Add a cycle from c to a
        graph.add(EdgeType.DIRECTED, c, a, 1.0)
        println(graph)
        assertTrue(graph.hasCycle(a))
    }

    @Test
    fun `Unable to detect a cycle in a disconnected graph`() {
        val graph = AdjacencyList<String>()

        val a = graph.createVertex("a")
        val b = graph.createVertex("b")
        val c = graph.createVertex("c")
        val d = graph.createVertex("d")

        graph.add(EdgeType.DIRECTED, a, b, 1.0)

        // A cycle between these `c` and `d` but `a` and `b` aren't connected
        graph.add(EdgeType.DIRECTED, c, d, 1.0)
        graph.add(EdgeType.DIRECTED, d, c, 1.0)

        println(graph)
        assertFalse(graph.hasCycle(a))
        assertTrue(graph.hasCycle(c))
    }

    @Test
    fun `Unable to detect a cycle to vertex without a path`() {
        val graph = AdjacencyList<String>()

        val a = graph.createVertex("a")
        val b = graph.createVertex("b")
        val c = graph.createVertex("c")
        val d = graph.createVertex("d")

        graph.add(EdgeType.DIRECTED, a, b, 1.0)
        // c has a path to `a` but `a` doesn't have a path to `c` so `a` can't see the cycle
        graph.add(EdgeType.DIRECTED, c, b, 1.0)
        // cycle between `c` and `d`
        graph.add(EdgeType.DIRECTED, c, d, 1.0)
        graph.add(EdgeType.DIRECTED, d, c, 1.0)

        println(graph)
        assertFalse(graph.hasCycle(a))
        assertTrue(graph.hasCycle(c))
    }

    @Test
    fun `challenge two The small world`() {
        val graph = AdjacencyList<String>()

        val vincent = graph.createVertex("Vincent")
        val chelsey = graph.createVertex("Chelsey")
        val ruiz = graph.createVertex("Ruiz")
        val patrick = graph.createVertex("Patrick")
        val ray = graph.createVertex("Ray")
        val sun = graph.createVertex("Sun")
        val cole = graph.createVertex("Cole")
        val kerry = graph.createVertex("Kerry")

        graph.addUndirectedEdge(vincent, chelsey, 1.0)
        graph.addUndirectedEdge(vincent, ruiz, 1.0)
        graph.addUndirectedEdge(vincent, patrick, 1.0)

        graph.addUndirectedEdge(ruiz, ray, 1.0)
        graph.addUndirectedEdge(ruiz, sun, 1.0)
        graph.addUndirectedEdge(ruiz, vincent, 1.0)

        graph.addUndirectedEdge(patrick, cole, 1.0)
        graph.addUndirectedEdge(patrick, kerry, 1.0)

        graph.addUndirectedEdge(cole, ruiz, 1.0)
        graph.addUndirectedEdge(cole, vincent, 1.0)

        assertEquals(1.0, graph.weight(vincent, cole))

        println(graph)
    }

    @Test
    fun `topSort with three nodes`() {
        val graph = AdjacencyList<String>()

        // Intentionally create them out of order so B is processed first.
        val b = graph.createVertex("B")
        val a = graph.createVertex("A")
        val c = graph.createVertex("C")

        graph.addDirectedEdge(a, b, 1.0);
        graph.addDirectedEdge(b, c, 1.0);

        val top = graph.topSort()

        top.forEach {
            println(it.data)
        }

        assertEquals("A", top[0].data)
        assertEquals("B", top[1].data)
        assertEquals("C", top[2].data)
    }
}
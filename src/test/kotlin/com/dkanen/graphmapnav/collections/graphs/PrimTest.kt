package com.dkanen.graphmapnav.collections.graphs

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PrimTest {
    @Test
    fun produceMinimumSpanningTree() {
        val graph = AdjacencyList<String>()

        val one = graph.createVertex("1")
        val two = graph.createVertex("2")
        val three = graph.createVertex("3")
        val four = graph.createVertex("4")
        val five = graph.createVertex("5")
        val six = graph.createVertex("6")

        graph.addUndirectedEdge(one, two, 6.0)
        graph.addUndirectedEdge(one, three, 1.0)
        graph.addUndirectedEdge(one, four, 5.0)

        graph.addUndirectedEdge(two, three, 5.0)
        graph.addUndirectedEdge(two, five, 3.0)

        graph.addUndirectedEdge(three, four, 5.0)
        graph.addUndirectedEdge(three, five, 6.0)
        graph.addUndirectedEdge(three, six, 4.0)

        graph.addUndirectedEdge(four, six, 2.0)

        graph.addUndirectedEdge(five, six, 6.0)

        val (cost, mst) = Prim.produceMinimumSpanningTree(graph)

        println("cost: $cost")
        println("mst:")
        println(mst)
    }
}
package com.dkanen.graphmapnav.collections.linkedlist

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class NodeTest {

    @Test
    fun `link 3 nodes together`() {
        val node1 = Node(1)
        val node2 = Node(2)
        val node3 = Node(3)

        node1.next = node2
        node2.next = node3

        assertEquals("1 -> 2 -> 3", node1.toString())
    }
}
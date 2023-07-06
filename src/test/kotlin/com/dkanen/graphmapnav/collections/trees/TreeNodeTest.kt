package com.dkanen.graphmapnav.collections.trees

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TreeNodeTest {
    @Test
    fun forEachTraversals() {
        val testData = mutableListOf<Pair<TreeNode<String>, List<List<String>>>>()

        // Depth 4
        // the number of characters at a level tells you what level that is
        // the depth is the length of the longest string
        testData.add(
            TreeNode("A").apply {
                add("AA").apply{
                    add("AAA")
                }
                add("AB").apply {
                    add("ABA")
                    add("ABB").apply {
                        add("ABBA")
                    }
                }
            } to listOf(
                listOf("A", "AA", "AB", "AAA", "ABA", "ABB", "ABBA"),
                listOf("A", "AA", "AAA", "AB", "ABA", "ABB", "ABBA")
            )
        )

        // Depth 6
        testData.add(
            TreeNode("A").apply {
                add("AA").apply {
                    add("AAA")
                    add("AAB").apply {
                        add("AABA")
                        add("AABB").apply {
                            add("AABBA").apply {
                                add("AABBAA")
                            }
                        }
                    }
                }
                add("AB").apply {
                    add("ABA")
                    add("ABB").apply {
                        add("ABBA")
                    }
                }
            } to listOf(
                listOf("A", "AA", "AB", "AAA", "AAB", "ABA", "ABB", "AABA", "AABB", "ABBA", "AABBA", "AABBAA"),
                listOf("A", "AA", "AAA", "AAB", "AABA", "AABB", "AABBA", "AABBAA", "AB", "ABA", "ABB", "ABBA")
            )
        )

        testData.forEach { (tree, expected) ->
            val breadthFirst = mutableListOf<String>()

            tree.forEachBreadthFirst {
                breadthFirst.add(it.value)
            }

            val depthFirst = mutableListOf<String>()

            tree.forEachDepthFirst {
                depthFirst.add(it.value)
            }

            assertEquals(expected.first(), breadthFirst)

            if (expected.count() > 1) {
                assertEquals(expected[1], depthFirst)
            }
        }
    }
    @Test
    fun search() {
        val tree = TreeNode("A").apply {
            add("AA").apply {
                add("AAA")
            }
            add("AB").apply {
                add("ABA")
                add("ABB").apply {
                    add("ABBA")
                }
            }
        }

        assertNull(tree.search("AC"))
        assertEquals("AA", tree.search("AA")?.value)
    }

    @Test
    fun printBreadthFirst() {
        val tree = TreeNode("15").apply {
            add("1").apply {
                add("1")
                add("5")
                add("0")
            }
            add("17").apply {
                add("2")
            }
            add("20").apply {
                add("5")
                add("7")
            }
        }

        assertEquals("15\n1 17 20 \n1 5 0 2 5 7 \n", tree.toStringBreadthFirst())
    }
}
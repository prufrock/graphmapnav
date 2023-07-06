package com.dkanen.graphmapnav.collections.trees

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BinaryNodeTest {
    @Test
    fun `visiting nodes`() {
        val trees: List<Pair<BinaryNode<Int>, Map<String, List<Int>>>> = listOf(
            BinaryNode(7).apply {
                leftChild = BinaryNode(1).apply {
                    leftChild = BinaryNode(0)
                    rightChild = BinaryNode(5)
                }
                rightChild = BinaryNode(9).apply {
                    leftChild = BinaryNode(8)
                }
            } to mapOf(
                "inOrder" to listOf(0, 1, 5, 7, 8, 9),
                "preOrder" to listOf(7, 1, 0, 5, 9, 8),
                "postOrder" to listOf(0, 5, 1, 8, 9, 7),
            ))

        trees.forEach {
            assertEquals(it.second["inOrder"], mutableListOf<Int>().also { nodes ->
                it.first.traverseInOrder { nodes.add(it) }
            })
            assertEquals(it.second["preOrder"], mutableListOf<Int>().also { nodes ->
                it.first.traversePreOrder { nodes.add(it) }
            })
            assertEquals(it.second["postOrder"], mutableListOf<Int>().also { nodes ->
                it.first.traversePostOrder { nodes.add(it) }
            })
        }
    }

    @Test
    fun maxHeight() {
        val trees = listOf<Pair<BinaryNode<Int>, Int>>(
            BinaryNode(7) to 0,
            BinaryNode(7).apply {
                leftChild = BinaryNode(1)
            } to 1,
            BinaryNode(7).apply {
                leftChild = BinaryNode(1)
                rightChild = BinaryNode(8)
            } to 1,
            BinaryNode(7).apply {
                leftChild = BinaryNode(1).apply {
                    leftChild = BinaryNode(0)
                    rightChild = BinaryNode(5)
                }
                rightChild = BinaryNode(8)
            } to 2
        )

        trees.forEach {
            assertEquals(it.second, it.first.height(0))
        }
    }

    @Test
    fun toList()
    {
        val trees = listOf<Pair<BinaryNode<Int>, List<Int?>>>(
            BinaryNode(7) to listOf(7, null, null),
            BinaryNode(7).apply {
                leftChild = BinaryNode(1)
                rightChild = BinaryNode(8)
            } to listOf(7, 1, null, null, 8, null, null),
            BinaryNode(7).apply {
                leftChild = BinaryNode(1).apply {
                    rightChild = BinaryNode(5)
                }
                rightChild = BinaryNode(8)
            } to listOf(7, 1, null, 5, null, null, 8, null, null),
            BinaryNode(7).apply {
                leftChild = BinaryNode(1).apply {
                    leftChild = BinaryNode(0)
                    rightChild = BinaryNode(5)
                }
                rightChild = BinaryNode(8)
            } to listOf(7, 1, 0, null, null, 5, null, null, 8, null, null)
        )

        trees.forEach {
            assertEquals(it.second, it.first.serialize())
            assertEquals(it.second, it.first.deserialize(it.first.serialize())?.serialize())
            print(it.first.deserialize(it.first.serialize()))
        }
    }
}
package com.dkanen.graphmapnav.collections.trees.avltree

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AVLTreeTest {

    @Test
    fun insert() {
        val samples = listOf(
            TreeTestData(listOf(10,15,16,18), listOf(10,15,16,18)),
            TreeTestData(listOf(15,10,16,4), listOf(4,10,15,16))
        )

        samples.forEach { s ->
            val tree = AVLTree<Int>()
            s.input.forEach {
                tree.insert(it)
            }
            print(tree)
            val inorder = mutableListOf<Int>()

            tree.root?.traverseInOrder {
                inorder.add(it)
            }

            assertTrue(tree.root!!.balanceFactor < 2)
            assertEquals(s.output, inorder)
        }
    }

    @Test
    fun remove() {
        val samples = listOf(
            TreeTestData(listOf(10,15,16,18), listOf(15,16,18), listOf(10)),
            TreeTestData(listOf(10,15,16,18), listOf(10,16,18), listOf(15)),
            TreeTestData(listOf(10,15,16,18), listOf(10,15,18), listOf(16)),
            TreeTestData(listOf(10,15,16,18), listOf(10,15,16), listOf(18)),
            TreeTestData(listOf(10,15,16,18), listOf(15,18), listOf(10,16))
        )

        samples.forEach { s ->
            val tree = AVLTree<Int>()
            s.input.forEach { i ->
                tree.insert(i)
            }

            print(tree)
            s.remove.forEach { r ->
                tree.remove(r)
            }

            print(tree)
            val inorder = mutableListOf<Int>()

            tree.root?.traverseInOrder {
                inorder.add(it)
            }

            assertTrue(tree.root!!.balanceFactor < 2)
            assertEquals(s.output, inorder)
        }
    }
}

/**
 * A neat little way to structure your test data for your tree.
 */
data class TreeTestData<T>(val input: List<T>, val output: List<T>, val remove: List<T> = listOf())
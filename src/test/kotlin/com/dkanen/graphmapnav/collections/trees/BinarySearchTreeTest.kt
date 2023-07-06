package com.dkanen.graphmapnav.collections.trees

import com.dkanen.graphmapnav.collections.trees.BinarySearchTree
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BinarySearchTreeTest {
    @Test
    fun `build an unbalanced BST`() {
        val bst = BinarySearchTree<Int>()
        (0..4).forEach {
            bst.insert(it)
        }

        val list = mutableListOf<Int>()

        bst.root?.traverseInOrder { list.add(it) }

        // an unbalanced tree
        println(bst)
        assertEquals(listOf(0,1,2,3,4), list)
    }

    @Test
    fun `take some care to build a balanced BST`() {
        // have to be aware of the correct ordering in order to balance the tree
        val bst = BinarySearchTree<Int>().apply {
            insert(3)
            insert(1)
            insert(4)
            insert(0)
            insert(2)
            insert(5)
        }
        val list = mutableListOf<Int>()

        bst.root?.traverseInOrder { list.add(it) }

        println("A balanced tree")
        println(bst)
        // the resulting order is still correct
        assertEquals(listOf(0,1,2,3,4,5), list, "the resulting order is still correct")

        assertTrue(bst.contains(5), "value should be there")
        assertFalse(bst.contains(10), "value shouldn't be there")

        assertTrue(bst.isBinarySearchTree)

        println("remove a value from the BST")
        bst.remove(3)
        println(bst)
        val removedList = mutableListOf<Int>()

        bst.root?.traverseInOrder { removedList.add(it) }
        assertEquals(listOf(0,1,2,4,5), removedList, "value should be removed.")

        assertTrue(bst.isBinarySearchTree)
    }

    @Test
    fun `equals`() {
        val bst1 = BinarySearchTree<Int>().apply {
            insert(3)
            insert(1)
            insert(4)
            insert(0)
            insert(2)
            insert(5)
        }

        val bst2 = BinarySearchTree<Int>().apply {
            insert(3)
            insert(1)
            insert(4)
            insert(0)
            insert(2)
            insert(5)
        }

        assertTrue(bst1 == bst2)
    }
    @Test
    fun `contains works`() {
        // an empty tree contains an empty tree
        run {
            val bst1 = BinarySearchTree<Int>().apply {}
            val bst2 = BinarySearchTree<Int>().apply {}

            assertTrue(bst1.contains(bst2))
        }
        // an empty tree doesn't contain a non-empty tree
        run {
            val bst1 = BinarySearchTree<Int>().apply {}
            val bst2 = BinarySearchTree<Int>().apply {
                insert(0)
            }

            assertFalse(bst1.contains(bst2))

            val bst3 = BinarySearchTree<Int>().apply {
                insert(0)
            }
            val bst4 = BinarySearchTree<Int>().apply {}

            assertFalse(bst3.contains(bst4))
        }
        // a tree with the same values as another tree contains the other tree
        run {
            val bst1 = BinarySearchTree<Int>().apply {
                insert(3)
                insert(1)
                insert(4)
            }
            val bst2 = BinarySearchTree<Int>().apply {
                insert(3)
                insert(1)
                insert(4)
            }

            assertTrue(bst1.contains(bst2))
        }
    }
}
package com.dkanen.graphmapnav.collections.trees.avltree

import java.lang.Integer.max

class AVLTree<T : Comparable<T>> {

    var root: AVLNode<T>? = null

    fun insert(value: T) {
        root = insert(root, value)
    }

    private fun insert(node: AVLNode<T>?, value: T): AVLNode<T> {
        node ?: return AVLNode(value)
        if (value < node.value) {
            node.leftChild = insert(node.leftChild, value)
        } else {
            node.rightChild = insert(node.rightChild, value)
        }
        val balancedNode = balanced(node)
        balancedNode.height = Integer.max(balancedNode.leftHeight ?: 0, balancedNode.rightHeight ?: 0) + 1
        return balancedNode
    }

    fun contains(value: T): Boolean {
        // start at the beginning
        var current = root

        // keep searching nodes until it hits the end
        while (current != null) {
            // if it finds the value return true
            if (current.value == value) {
                return true
            }

            // haven't found the value yet
            // go to the left if it's less than the current node
            // to the right if it's greater than or equal to the current node
            current = if (value < current.value) {
                current.leftChild
            } else {
                current.rightChild
            }
        }

        return false
    }

    /**
     * A simple facade method the calls the recursive power house.
     */
    fun remove(value: T) {
        root = remove(root, value)
    }

    private fun remove(node: AVLNode<T>?, value: T): AVLNode<T>? {
        node ?: return null

        when {
            // remove the node with the matching value
            value == node.value -> {
                // when the value matches
                // if it has no children
                // remove the node by returning null
                if (node.leftChild == null && node.rightChild == null) {
                    return null
                }

                // if the matching node has no left child
                // use the right child
                if (node.leftChild == null) {
                    return node.rightChild
                }

                // if the matching node has no right child
                // use the left child
                if (node.rightChild == null) {
                    return node.leftChild
                }

                // if this node has both a left and a right child
                // find the smallest value of the right child
                // replace the current node with the value because
                // it's larger than the current node, and it's left children
                // but smaller than the current node's right children
                node.rightChild?.min?.value?.let {
                    node.value = it
                }

                // Remove the node whose value we just set to the current nodes value
                // the value was taken from the right side so head down that side
                node.rightChild = remove(node.rightChild, node.value)
            }
            // if the value to remove is less than the current node go down the left
            // otherwise go right
            value < node.value -> node.leftChild = remove(node.leftChild, value)
            else -> node.rightChild = remove(node.rightChild, value)
        }

        val balancedNode = balanced(node)
        // the height of the tallest child plus one more for those children.
        balancedNode.height = max(
            balancedNode.leftHeight,
            balancedNode.rightHeight
        ) + 1

        return balancedNode
    }

    override fun toString() = root?.toString() ?: "empty tree"

    private fun leftRotate(node: AVLNode<T>): AVLNode<T> {
        // the tree is rotating from right to LEFT so select the right child as the pivot(node to move left).
        val pivot = node.rightChild!!

        // node is going to take the place of the pivots left child
        // this means we have move child around to maintain an in-order traversal
        // to do this we make the pivots left child the nodes right child because
        // we know the left child is lt the pivot but gte the node.
        node.rightChild = pivot.leftChild
        pivot.leftChild = node

        // recalculate the heights
        // height is the number of nodes to the farthest away child so find the max of the two children
        // add one so that a null child(-1) results in 0 height
        node.height = Integer.max(node.leftHeight, node.rightHeight) + 1
        pivot.height = Integer.max(pivot.leftHeight, pivot.rightHeight) + 1

        //replace node with the pivot in the tree
        return pivot
    }

    private fun rightRotate(node: AVLNode<T>): AVLNode<T> {
        // Rotate to the right so pivot on the left child
        val pivot = node.leftChild!!

        // re-arrange so that the pivot is a parent of the node
        // the pivots right child is gte the pivot and the node is gte the pivot so the pivots right child is less than the node and can be it's left child.
        // Finally, the node is gte the pivot, so it becomes it's right child.
        node.leftChild = pivot.rightChild
        pivot.rightChild = node

        // recalculate the heights
        // height is the number of nodes to the farthest away child so find the max of the two children
        // add one so that a null child(-1) results in 0 height
        node.height = Integer.max(node.leftHeight, node.rightHeight) + 1
        pivot.height = Integer.max(pivot.leftHeight, pivot.rightHeight) + 1

        // replace the node with the pivot in the tree
        return pivot
    }

    /**
     * For when the unbalanced node has one child on the right and that child has a leaf on the left.
     */
    private fun rightLeftRotate(node: AVLNode<T>): AVLNode<T> {
        val rightChild = node.rightChild ?: return node
        node.rightChild = rightRotate(rightChild)
        return leftRotate(node)
    }

    /**
     * For when the unbalanced node has one child on the left and that has child has a leaf on the right
     */
    private fun leftRightRotate(node: AVLNode<T>): AVLNode<T> {
        val leftChild = node.leftChild ?: return node
        node.leftChild = leftRotate(leftChild)
        return rightRotate(node)
    }

    private fun balanced(node: AVLNode<T>): AVLNode<T> {
        return when (node.balanceFactor) {
            // left heavy
            2 -> {
                if (node.leftChild?.balanceFactor == -1) {
                    leftRightRotate(node)
                } else {
                    rightRotate(node)
                }
            }
            // right heavy
            -2 -> {
                if (node.rightChild?.balanceFactor == 1) {
                    rightLeftRotate(node)
                } else {
                    leftRotate(node)
                }
            }
            // balanced!
            else -> node
        }
    }
}
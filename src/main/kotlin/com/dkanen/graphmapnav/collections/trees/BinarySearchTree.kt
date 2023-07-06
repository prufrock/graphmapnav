package com.dkanen.graphmapnav.collections.trees

/**
 * Since the BinarySearchTree needs to keep everything sorted it can only hold items that are comparable.
 */
class BinarySearchTree<T: Comparable<T>>() {

    var root: BinaryNode<T>? = null

    val isBinarySearchTree: Boolean
        get() = isBinarySearchTree(root)

    override fun toString() = root?.toString() ?: "empty tree"

    fun insert(value: T) {
        root = insert(root, value)
    }

    private fun insert(
        node: BinaryNode<T>?,
        value: T
    ): BinaryNode<T> {
        // the base case to end the recursive call
        // if the node is null there's nothing more to do
        node ?: return BinaryNode(value)

        // since this a binary search tree do a comparison to decide
        // whether the nodes goes on the left or the right
        if (value < node.value) {
            node.leftChild = insert(node.leftChild, value)
        } else {
            node.rightChild = insert(node.rightChild, value)
        }

        return node
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
     * time: O(n)
     * space: O(n)
     */
    fun contains(bst: BinarySearchTree<T>): Boolean {
        if (root == null && bst.root == null) {
            return true
        }

        // if this root or that root is null then this doesn't contain that
        return root?.let {
            bst.root?.let { bstRoot ->
                var containsSubTree = true

                // walk the other tree and if any value is not in this tree it doesn't contain it.
                bstRoot.traverseInOrder { value ->
                    if (containsSubTree) {
                        containsSubTree = contains(value)
                    }
                }

                containsSubTree
            } ?: false
        } ?: false
    }

    /**
     * A simple facade method the calls the recursive power house.
     */
    fun remove(value: T) {
        root = remove(root, value)
    }

    private fun remove(node: BinaryNode<T>?, value: T): BinaryNode<T>? {
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
            // if the value to remove is less than the current node decent down the left
            // otherwise go right
            value < node.value -> node.leftChild = remove(node.leftChild, value)
            else -> node.rightChild = remove(node.rightChild, value)
        }

        return node
    }

    private fun isBinarySearchTree(node: BinaryNode<T>?): Boolean {
        // an empty BST is still valid BST
        if (node == null) {
            return true
        }

        // The left child must be smaller than it's parent
        node.leftChild?.let {
            if (it.value >= node.value) {
                return false
            }
        }

        // The right child must be greater than or equal to it's parent
        node.rightChild?.let {
            if (it.value < node.value) {
                return false
            }
        }

        // If all the children are all valid then the whole thing is valid
        return isBinarySearchTree(node.leftChild) && isBinarySearchTree(node.rightChild)
    }

    /**
     * Checks to see if two BSTs are exactly the same.
     */
    override fun equals(other: Any?): Boolean {
        return if (other != null && other is BinarySearchTree<*>) {
            root == other.root
        } else {
            false
        }
    }
}
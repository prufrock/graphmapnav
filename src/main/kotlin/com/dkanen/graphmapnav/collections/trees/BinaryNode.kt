package com.dkanen.graphmapnav.collections.trees

import java.lang.Integer.max

class BinaryNode<T : Comparable<T>>(value: T): TraversableBinaryNode<BinaryNode<T>, T>(value) {
    /**
     * Assuming the left tree is always the smallest(BST) recursively find the last left child of this BinaryNode and return that node.
     * This is a good example of a little function that make another action way easier: Removing an element from a BST.
     */
    val min: BinaryNode<T>?
        get() = leftChild?.min ?: this

    /**
     * Useful when serializing where you need to know where the null nodes are.
     * https://www.raywenderlich.com/books/data-structures-algorithms-in-kotlin/v2.0/chapters/7-binary-trees#toc-chapter-014-anchor-003
     */
    fun traversePreOrderWithNull(visit: VisitorTb<T?>) {
        visit(value)
        leftChild?.traversePreOrderWithNull(visit) ?: visit(null) // if the child is null let the visitor know.
        rightChild?.traversePreOrderWithNull(visit) ?: visit(null)
    }

    /**
     * Find the height of the tree
     * O(n) time it has to visit every node
     * O(log n) since it only keeps what it hasn't processed on the stack
     */
    fun height(height: Int = 0): Int {
        return max(
            leftChild?.height(height + 1) ?: height,
            rightChild?.height(height + 1) ?: height
        )
    }

    /**
     * Convert the tree to a list.
     * https://www.raywenderlich.com/books/data-structures-algorithms-in-kotlin/v2.0/chapters/7-binary-trees#toc-chapter-014-anchor-003
     */
    fun serialize(node: BinaryNode<T> = this): MutableList<T?> {
        val list = mutableListOf<T?>()
        node.traversePreOrderWithNull { list.add(it) }
        return list
    }

    /**
     * Convert a list back into a tree.
     * https://www.raywenderlich.com/books/data-structures-algorithms-in-kotlin/v2.0/chapters/7-binary-trees#toc-chapter-014-anchor-003
     */
    fun deserialize(list: MutableList<T?>): BinaryNode<T>? {
        // it's faster to reverse the list
        // removing from the front requires constant shifting of the list
        return deserializeOptimized(list.asReversed())
    }

    /**
     * Perform the deserialization on a reversed list.
     * This avoids having the list constantly shift when removing from the front.
     * Could replace the list with a stack.
     */
    fun deserializeOptimized(list: MutableList<T?>): BinaryNode<T>? {
        // the base case, if it's null you've hit a leaf so go back up.
        // remove from the end of the list so the list doesn't have to shift
        val rootValue = list.removeAt(list.lastIndex) ?: return null

        // create a tree from the current value
        // performed in similar order to the pre-order traversal
        val root = BinaryNode(rootValue)

        // recursively process the elements of the list
        // relies on the fact that the list is passed as a reference and is mutable
        root.leftChild = deserializeOptimized(list)
        root.rightChild = deserializeOptimized(list)

        return root
    }

    /**
     * An alternative solution to height solved recursively.
     * https://www.raywenderlich.com/books/data-structures-algorithms-in-kotlin/v2.0/chapters/7-binary-trees
     */
    fun height(node: BinaryNode<T>? = this): Int {
        // if the node is not null take the tallest of the children and add 1
        // if the node is null return -1 to counterbalance the + 1 to add nothing
        return node?.let { 1 + max(height(node.leftChild),
            height(node.rightChild)) } ?: -1
    }

    override fun toString() = diagram(this)

    private fun diagram(node: BinaryNode<T>?,
                        top: String = "",
                        root: String = "",
                        bottom: String = ""): String {
        return node?.let {
            if (node.leftChild == null && node.rightChild == null) {
                "$root${node.value}\n"
            } else {
                diagram(node.rightChild, "$top ", "$top┌──", "$top| ") + root + "${node.value}\n" + diagram(node.leftChild, "$bottom| ", "$bottom└──", "$bottom ")
            }
        } ?: "${root}null\n"
    }

    override fun equals(other: Any?): Boolean {
        // if the other isn't a binary node then it's not equal
        if (other !is BinaryNode<*>) {
            return false
        }

        // if both are null they are equal
        if (value == null && other.value == null) {
            return true
        }

        // if value is null here then other must not be null, so they are not equal
        return value?.let {
            // check that they have the same value
            // then recursively check if the left and right child are equal.
            return it == other.value && leftChild == other.leftChild && rightChild == other.rightChild
        } ?: false
    }
}

fun <T> MutableList<T?>.pop(): T? {
    if (this.isEmpty()) {
        return null
    }

    return this.removeAt(0)
}
package com.dkanen.graphmapnav.collections.trees

open class TraversableBinaryNode<Self : TraversableBinaryNode<Self, T>, T>(var value: T) {
    // The Self here allows the implementing class to
    var leftChild: Self? = null
    var rightChild: Self? = null

    /**
     * Traverse the tree in order visiting the lowest left node first.
     * O(n)
     */
    fun traverseInOrder(visit: VisitorTb<T>) {
        leftChild?.traverseInOrder(visit)
        visit(value)
        rightChild?.traverseInOrder(visit)
    }

    /**
     * Traverse the tree in pre-order visiting the root node first.
     */
    fun traversePreOrder(visit: VisitorTb<T>) {
        visit(value)
        leftChild?.traversePreOrder(visit)
        rightChild?.traversePreOrder(visit)
    }

    fun traversePostOrder(visit: VisitorTb<T>) {
        leftChild?.traversePostOrder(visit)
        rightChild?.traversePostOrder(visit)
        visit(value)
    }
}
typealias VisitorTb<T> = (T) -> Unit
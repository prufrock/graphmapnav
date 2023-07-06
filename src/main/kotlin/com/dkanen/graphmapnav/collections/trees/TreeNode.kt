package com.dkanen.graphmapnav.collections.trees

import com.dkanen.graphmapnav.collections.queues.ArrayListQueue as Queue

/**
 * A node in a tree.
 * It has a value to love and protect.
 * It has a list of children to relate to.
 */
class TreeNode<T>(val value: T) {
    private val children: MutableList<TreeNode<T>> = mutableListOf()

    fun add(child: TreeNode<T>) = children.add(child)
    fun add(element: T): TreeNode<T> = TreeNode(element).also {
        children.add(it)
    }

    /**
     * A depth first for each over the tree.
     * pre-order
     * O(n)
     */
    fun forEachDepthFirst(visit: Visitor<T>) {
        // visit the current node first
        visit(this)
        // starting from the left most node(first node)
        children.forEach {
            // visit each node by
            // following a branch to the bottom
            // and visit all of its children before coming back up
            it.forEachDepthFirst(visit)
        }
    }

    /**
     * Traverse the tree one level at a time applying the visit to the parent and then the children.
     * pre-order
     * O(n)
     */
    fun forEachBreadthFirst(visit: Visitor<T>) {
        visit(this) // pre-order because this node is processed before the children
        val queue = Queue<TreeNode<T>>() // Adding the children to a queue makes it possible to visit all of this nodes children before continuing on
        children.forEach { queue.enqueue(it) }

        var node = queue.dequeue() // process them in queued order
        while (node != null) { // as long as there are nodes keeping going
            visit(node) // process the current node
            node.children.forEach { queue.enqueue(it) } // add all the nodes children to the queue, eventually their children too
            node = queue.dequeue() // process the next node in the queue
        }
    }

    /**
     * Alias for breadth first traversal.
     */
    fun forEachLevelOrder(visit: Visitor<T>) {
        forEachBreadthFirst(visit)
    }

    /**
     * Creates a string representation of the tree.
     * O(n)
     */
    fun toStringBreadthFirst(): String {
        val stringBuilder = StringBuilder()
        // get the root node in there
        stringBuilder.append(this.value.toString())
        stringBuilder.append("\n")

        // track the number of nodes still left to process before arriving at the next level
        var nodesAtCurrentLevel: Int

        val queue = Queue<TreeNode<T>>()
        children.forEach {
            queue.enqueue(it)
        }
        // get the count of the root nodes children
        // from here we can commence the *party*
        nodesAtCurrentLevel = queue.count

        var node = queue.dequeue()
        while (node != null) {
            stringBuilder.append(node.value.toString() + " ")
            nodesAtCurrentLevel-- // decrement after adding to the string since the node has been "processed"

            // add all the children to queue
            node.children.forEach {
                queue.enqueue(it)
            }
            // when all the nodes at the current level have been processed
            // update the value to the current length of the queue
            // this works because each node at the current level has been adding its children
            // as they were processed
            if (nodesAtCurrentLevel == 0) {
                nodesAtCurrentLevel = queue.count
                // add the new line because we know we've moved on to the next level
                stringBuilder.append("\n")
            }

            node = queue.dequeue() // get the next node
        }

        return stringBuilder.toString()
    }

    /**
     * Search all the nodes of the tree for the value.
     * O(n) since the tree isn't guaranteed to be sorted.
     */
    fun search(value: T): TreeNode<T>? {
        var result: TreeNode<T>? = null

        // search the tree until the matching value is found
        // if there is more than one match the last match wins
        forEachBreadthFirst {
            if (it.value == value) {
                result = it
            }
        }

        return result
    }
}

typealias Visitor<T> = (TreeNode<T>) -> Unit
package com.dkanen.graphmapnav.collections.trees.avltree

import com.dkanen.graphmapnav.collections.trees.TraversableBinaryNode


class AVLNode<T : Comparable<T>>(value: T): TraversableBinaryNode<AVLNode<T>, T>(value) {
    val min: AVLNode<T>?
      get() = leftChild?.min ?: this

    var height = 0

    val leftHeight: Int
      get() = leftChild?.height ?: -1

    val rightHeight: Int
      get() = rightChild?.height ?: -1

    // if the balance factors differs by more than one the tree is not balanced
    val balanceFactor: Int
      get() = leftHeight - rightHeight

    override fun toString() = diagram(this)

    private fun diagram(node: AVLNode<T>?,
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
}
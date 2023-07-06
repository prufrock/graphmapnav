package com.dkanen.graphmapnav.collections.linkedlist

/**
 * Holds a value and a link to the next Node in the list.
 */
class Node<T : Any>(var value: T, var next: Node<T>? = null) {
// `T : Any` makes it so a nullable type can not be passed

    /**
     * Prints this node and any linked nodes.
     */
    override fun toString(): String {
        return if (next != null) {
            "$value -> ${next.toString()}"
        } else {
            "$value"
        }
    }
}
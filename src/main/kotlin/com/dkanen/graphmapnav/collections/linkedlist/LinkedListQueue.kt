package com.dkanen.graphmapnav.collections.linkedlist

import com.dkanen.graphmapnav.collections.queues.Queue

class LinkedListQueue<T: Any>: Queue<T> {
    private val list = LinkedList<T>()

    private var size = 0

    override val count: Int
      get() = size

    /**
     * O(1) since it's O(1) for a linked list.
     */
    override fun enqueue(element: T): Boolean {
        // add the element to the end of the list
        list.append(element)
        // increase the size by 1
        size++
        return true
    }

    /**
     * O(1) LinkedList.pop() is O(1)
     */
    override fun dequeue(): T? {
        // if there's nothing to dequeue return early and avoid decrementing size below 0.
        if (isEmpty) return null
        // reduce the size by 1
        size--
        // remove the node from the front or null
        return list.pop()
    }

    override fun peek(): T? = list.nodeAt(0)?.value

    override fun toList(): List<T> = list.toList()
}
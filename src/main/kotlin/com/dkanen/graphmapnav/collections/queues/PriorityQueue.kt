package com.dkanen.graphmapnav.collections.queues

import com.dkanen.graphmapnav.collections.heaps.ComparableHeapImpl
import com.dkanen.graphmapnav.collections.heaps.ComparatorHeapImpl
import com.dkanen.graphmapnav.collections.heaps.Heap


abstract class AbstractPriorityQueue<T: Any>: Queue<T> {

    abstract val heap: Heap<T>
        get

    override fun enqueue(element: T): Boolean {
        heap.insert(element)
        return true
    }

    override fun dequeue() = heap.remove()

    override val count: Int
        get() = heap.count

    override fun peek() = heap.peek()
}

class ComparablePriorityQueueImpl<T: Comparable<T>>: AbstractPriorityQueue<T>() {
    override val heap = ComparableHeapImpl<T>()

    override fun toList(): List<T> = heap.elements
}

class ComparatorPriorityQueueImpl<T: Any>(private val comparator: Comparator<T>): AbstractPriorityQueue<T>() {

    override val heap: Heap<T> = ComparatorHeapImpl(comparator)
    override fun toList(): List<T> = heap.elements
}
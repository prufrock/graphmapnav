package com.dkanen.graphmapnav.collections.heaps

interface Heap<T: Any>: Collection<T> {

    var elements: ArrayList<T>

    fun peek(): T?

    /**
     * Merge two heaps
     */
    fun merge(heap: Heap<T>)
}
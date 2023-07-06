package com.dkanen.graphmapnav.collections.heaps

class ComparableHeapImpl<T: Comparable<T>>(): AbstractHeap<T>() {

    override fun compare(a: T, b: T): Int = a.compareTo(b)

    companion object {

        /**
         * Create a new heap from the provided ArrayList.
         */
        fun <T: Comparable<T>> create(elements: ArrayList<T>): Heap<T> {
            val heap = ComparableHeapImpl<T>()
            heap.heapify(elements)
            return heap
        }
    }
}
package com.dkanen.graphmapnav.collections.heaps

class ComparatorHeapImpl<T: Any>(
    private val comparator: Comparator<T>
): AbstractHeap<T>() {

    override fun compare(a: T, b: T): Int = comparator.compare(a, b)

    /**
     * Convert an array list to a heap.
     */
    companion object {
        fun <T: Any> create(elements: ArrayList<T>, comparator: Comparator<T>): Heap<T> {
            val heap = ComparatorHeapImpl(comparator)
            heap.heapify(elements)
            return heap
        }
    }
}

/**
 * Use a heap to find the nthSmallest element such that at the end the top element in the heap
 * is nth smallest element in the array.
 */
fun ArrayList<Int>.nthSmallest(n: Int): Int? {
    // nothing to do if empty or there aren't enough elements
    if (isEmpty() || count() < n) return null

    val heap = ComparableHeapImpl.create(arrayListOf<Int>())

    forEach {
        if (heap.count < n) { // fill the heap until n
            heap.insert(it)
        } else if (heap.peek()!! > it) { // heap.count was checked, so it shouldn't be null
            heap.remove() // remove the item bigger than *it* since we want an order heap the n smallest integers
            heap.insert(it) // place *it* in and the heap orders it for us
        }
    }

    // since the heap kept the n items in the heap nicely ordered the first element in the heap is the nth smallest!
    return heap.peek()
}
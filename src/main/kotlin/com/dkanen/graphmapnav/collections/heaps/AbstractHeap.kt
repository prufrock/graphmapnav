package com.dkanen.graphmapnav.collections.heaps

import java.util.*
import kotlin.collections.ArrayList

abstract class AbstractHeap<T: Any>(): Heap<T> {

    /**
     * Returns a positive number if *a* has a priority than *b*,
     * a negative number if *a* has a lower priority than *b*,
     * and zero if they are equal.
     */
    abstract fun compare(a: T, b: T): Int

    override var elements: ArrayList<T> = ArrayList<T>()

    override val count: Int
        get() = elements.size

    override fun peek(): T? = elements.firstOrNull()

    /**
     * Finds the left child of the node at *index*.
     */
    private fun leftChildIndex(index: Int) = (2 * index) + 1

    /**
     * Finds the right child of the node at *index*.
     */
    private fun rightChildIndex(index: Int) = (2 * index) + 2

    /**
     * Finds the parent of the node at *index*.
     */
    private fun parentIndex(index: Int) = (index - 1) / 2

    /**
     * Time: O(log n)
     */
    override fun insert(element: T) {
        // put the element at the end
        elements.add(element)
        // move it into the correct position
        siftUp(count - 1)
    }

    private fun siftUp(index: Int) {
        var child = index
        var parent = parentIndex(child)

        // remember compare uses *priority* so it can be flexible on minHeap or maxHeap
        while(child > 0 && compare(elements[child], elements[parent]) > 0) {
            Collections.swap(elements, child, parent)
            // propagate the element up the heap
            child = parent
            parent = parentIndex(child)
        }
    }

    /**
     * Removing from a heap is like removing from a queue:
     * you remove the item with the highest priority first.
     * Time: O(log n)
     */
    override fun remove(): T? {
        if (isEmpty) return null

        // swap the first and last element
        // this ensures the array stays filled
        Collections.swap(elements, 0, count - 1)
        // hold on to the item to return
        val item = elements.removeAt(count - 1)
        siftDown(0)
        return item
    }

    private fun siftDown(index: Int) {
        var parent = index
        while (true) {
            val left = leftChildIndex(parent)
            val right = rightChildIndex(parent)
            var candidate = parent
            // if the left child has a higher priority
            // that may be the one we want to swap
            // so make it the candidate
            if (left < count && compare(elements[left], elements[candidate]) > 0) {
                candidate = left
            }
            // if the right child has a higher priority
            // then that is in fact the one we want to swap
            // it gets to be the candidate
            if (right < count && compare(elements[right], elements[candidate]) > 0) {
                candidate = right
            }
            // if they're the same well then nothing to do here
            if (candidate == parent) {
                return
            }
            // swap the parent element and the candidate
            // since the candidate has a higher priority
            Collections.swap(elements, parent, candidate)
            // the parent has moved into the candidate's position
            // so set the parent to the candidate
            // then head back to the top of the loop!
            parent = candidate
        }
    }

    /**
     * Time: O(log n)
     */
    override fun remove(index: Int): T? {
        // You've gone too far, time to be done
        if (index >= count) return null

        return if (index == count - 1) {
            // when it's last element just remove that element and return it
            elements.removeAt(count - 1)
        } else {
            // for any other element swap it with the last element
            Collections.swap(elements, index, count - 1)
            // then remove it
            val item = elements.removeAt(count - 1)
            // adjust the swapped element, was the last one, down as far as it goes
            siftDown(index)
            // adjust the swapped element up as far as it goes
            siftUp(index)
            // return the removed item
            item
        }
    }

    /**
     * Merge two heaps
     */
    override fun merge(heap: Heap<T>) {
        elements.addAll(heap.elements)
        buildHeap()
    }

    /**
     * Converts array to a heap
     */
    protected fun heapify(values: ArrayList<T>) {
        elements = values
        buildHeap()
    }

    private fun buildHeap() {
        if (!elements.isEmpty()) {
            // You only need to do half the array
            // since the sifting process ends up putting the parent nodes in the right places
            (count / 2 downTo 0).forEach {
                siftDown(it)
            }
        }
    }

    /**
     * Time: O(n) because you may have to search all the elements to find it,
     * but there it should be faster than that because this takes advantage of the heap's structure.
     */
    private fun index(element: T, i: Int): Int? {
        // if the index is bigger than the array the element isn't in the heap
        if (i >= count) {
            return null
        }
        // if the element has a higher priority than the current element then it can't be in the heap
        // since the heap is always sorted from highest to lowest priority
        if (compare(element, elements[i]) > 0) {
            return null
        }
        // the element was found!
        if (element == elements[i]) {
            return i
        }

        // recursively check to see if the left child has the element
        val leftChildIndex = index(element, leftChildIndex(i))
        if (leftChildIndex != null) return leftChildIndex
        // then check to see if the right child has it
        val rightChildIndex = index(element, rightChildIndex(i))
        if (rightChildIndex != null) return rightChildIndex

        // if the element isn't the current one, and neither the left child or right child have it
        // the element isn't in the heap
        return null
    }
}

fun <T: Comparable<T>> ArrayList<T>.isMinHeap(): Boolean {
    // If there aren't any elements in the array it sure could be a min heap
    // or if there's only one element
    if (isEmpty() || count() == 1) {
        return true
    }

    (count() / 2 downTo 0).forEach { index ->
        val leftChildIndex = (2 * index) + 1
        if (leftChildIndex < count() && get(index) > get(leftChildIndex)) {
            return false
        }

        val rightChildIndex = (2 * index) + 2
        if (rightChildIndex < count() && get(index) > get(rightChildIndex)) {
            return false
        }
    }

    return true
}
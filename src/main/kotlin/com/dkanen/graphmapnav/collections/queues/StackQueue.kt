package com.dkanen.graphmapnav.collections.queues

import com.dkanen.graphmapnav.collections.stacks.StackImpl


/**
 * A stack queue uses 2 stacks to make a queue. The right stack receives elements while the left stack is used to reverse the right stack and remove elements. This swapping stacks makes the queue FIFO.
 */
class StackQueue<T: Any>: Queue<T> {
    private val leftStack = StackImpl<T>()
    private val rightStack = StackImpl<T>()

    // Both stacks need to be empty for the queue to be empty.
    override val isEmpty: Boolean
        get() = leftStack.isEmpty && rightStack.isEmpty

    // have to add both stacks together to get the total size.
    override val count: Int
        get() = leftStack.count + rightStack.count

    /**
     * Amortized O(1) because `transferElements()` is O(n) but each element in the queue only has to be moved once so future calls are O(1).
     */
    override fun peek(): T? {
        // If there aren't any elements in the left stack move them over.
        if (leftStack.isEmpty) {
            transferElements()
        }
        return leftStack.peek()
    }

    /**
     * @inherit
     * O(1)
     */
    override fun enqueue(element: T): Boolean {
        rightStack.push(element)
        return true
    }

    /**
     * @inherit
     * O(1) amortized
     */
    override fun dequeue(): T? {
        // if it's empty move the elements to the left stack.
        // this reverses the stack and makes it simple to access the bottom of the right stack.
        if (leftStack.isEmpty) {
            transferElements()
        }
        return leftStack.pop()
    }

    /**
     * Moves the elements from the right stack to the left stack.
     * O(n)
     */
    private fun transferElements() {
        var nextElement = rightStack.pop()
        while (nextElement != null) {
            leftStack.push(nextElement)
            nextElement = rightStack.pop()
        }
    }

    override fun toString(): String {
        return "Left stack: \n$leftStack \n Right stack: \n$rightStack"
    }


    override fun toList(): List<T> = leftStack.toList() + rightStack.toList().reversed()
}
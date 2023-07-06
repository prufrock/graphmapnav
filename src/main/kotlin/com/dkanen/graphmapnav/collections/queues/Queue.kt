package com.dkanen.graphmapnav.collections.queues

import com.dkanen.graphmapnav.collections.stacks.StackImpl


/**
 * A queue.
 * It doesn't allow nullables since it's <T : Any>.
 */
interface Queue<T : Any> {

    /**
     * Add an element at the back of the queue.
     */
    fun enqueue(element: T): Boolean

    /**
     * Remove an element from the front of the queue and return it.
     */
    fun dequeue(): T?

    val count: Int

    val isEmpty: Boolean
        get() = count == 0

    /**
     * Look at the element at the front of the queue without removing it.
     */
    fun peek(): T?

    /**
     * Return the elements of the queue as a List.
     */
    fun toList(): List<T>
}

class ArrayListQueue<T: Any>: Queue<T> {
    private val list = arrayListOf<T>()

    /**
     * O(1)
     */
    override val count: Int
        get() = list.size

    /**
     * O(1)
     */
    override fun peek(): T? = list.getOrNull(0)

    /**
     * O(1) because the list has empty space at the back.
     */
    override fun enqueue(element: T): Boolean {
        list.add(element)
        return true
    }

    /**
     * O(n) operation because removing an element from the beginning of the list requires moving the remaining elements forward 1.
     */
    override fun dequeue(): T? = if (isEmpty) null else list.removeAt(0)

    override fun toList(): List<T> = list

    override fun toString(): String = list.toString()
}


/**
 * Allows you to treat the queue as a group of players playing a game.
 * Each time it's called the function returns the player whose turn it is.
 * It also puts them at the back of the queue, so they can take their turn again.
 *
 * Time complexity depends on how the queue store's it's elements.
 */
fun <T: Any> Queue<T>.nextPlayer(): T? {
    val nextPlayer = this.dequeue() ?: return null // get the next player whose up or null if there are no players
    this.enqueue(nextPlayer) // put them at the back of the line
    return nextPlayer // give the player back to the caller
}

/**
 * Reverse the order of the elements in the queue.
 * O(n) because looping over the elements twice is 2n which becomes O(n).
 */
fun <T: Any> Queue<T>.reverse() {
    if (isEmpty) return

    val stack = StackImpl<T>() // create a stack because it reverses the queue by being LIFO

    while (!isEmpty) {
        stack.push(dequeue()!!) // put all the elements in the queue in the stack
    }

    while(!stack.isEmpty) {
        enqueue(stack.pop()!!) // put all the elements in the stack back in the queue but reversed!
    }
}
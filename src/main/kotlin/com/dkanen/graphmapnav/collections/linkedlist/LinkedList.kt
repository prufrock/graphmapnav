package com.dkanen.graphmapnav.collections.linkedlist

import kotlin.math.floor

/**
 * A container for linked list nodes that makes it easier to manage the list.
 */
class LinkedList<T : Any> : Collection<T>, MutableIterable<T>, MutableCollection<T> {

    // the first node to make push nodes on to the front easier
    private var head: Node<T>? = null
    // the last node to make appending nodes quicker
    private var tail: Node<T>? = null
    // so we know how many nodes there are
    override var size = 0
      private set



    override fun isEmpty(): Boolean = size == 0

    /**
     * Add a value to the front of the list.
     * O(1)
     */
    fun push(value: T) = apply {
        // The new value becomes a new node that becomes the head of the list
        // The old head then becomes the tail of the new head since it keeps it's tail the old nodes are still in the list
        head = Node(value = value, next = head)

        // Initially tail is empty so set it head.
        // This won't cause an infinite loop though because when traversing from the front we follow the links from the head and never look at the tail.
        if (tail == null) {
            tail = head
        }
        // the list got one bigger so increment
        size++
    }

    /**
     * Add a value at the end of the list.
     * O(1)
     */
    fun append(value: T) = apply {
        // if it's empty just put it at the front of the queue
        // appending to an empty list is the same as push
        if (isEmpty()) {
            push(value)
            return this
        }
        val newNode = Node(value = value)
        // make the last node link to the appended node
        // since the last node is linked to the head this links it into the whole list
        tail!!.next = newNode

        // we're adding it to the end so set the new node to be the last node
        tail = newNode

        // increase the size to account for the new node.
        size++
    }

    /**
     * Find a node at the provided index. Returns null if the list is empty or the index doesn't exist otherwise return
     * the node at that index.
     * O(i) or O(n) if it's the last element
     */
    fun nodeAt(index: Int): Node<T>? {
        // keep track of the node we're looking at
        var currentNode = head
        // keep track of how many nodes we've looked at
        var currentIndex = 0

        // keep moving through the list until we reach the index
        // if we hit the end, null, return null otherwise return
        // the node found.
        while (currentNode != null && currentIndex < index) {
            currentNode = currentNode.next
            currentIndex++
        }

        return currentNode
    }

    /**
     * Inserts a value into the LinkedList after a particular node.
     * TODO rename to insertAfter(afterNode: Node<T>, value: T)
     * TODO add insertAt(index: Int, node: Node<T>)
     * O(1)
     */
    fun insert(value: T, afterNode: Node<T>): Node<T> {
        // The node to put it after is the tail use append
        if (tail == afterNode) {
            append(value)
            // Tail is nullable but we know it's set so use the not-null-assertion operator.
            return tail!!
        }

        // Create a node for the value then use the afterNode's next as this nodes next.
        // This allows it to take the afterNode's place in the link.
        val newNode = Node(value = value, next = afterNode.next)

        // The new node is going to be after the afterNode so it to afterNode's next
        afterNode.next = newNode

        // The list is bigger now so increase the size
        size++

        return newNode
    }

    /**
     * Remove and return the value at the front of the list.
     * O(1)
     */
    fun pop(): T? {
        // There's nothing here so return null
        if (isEmpty()) return null

        // get value from the front of the list
        val result = head?.value
        // make the element head is linked to the new head
        // this removes the first element
        head = head?.next
        // reduce the size of the list after removing an element
        size--
        // if the list is now empty set tail to null
        if (isEmpty()) {
            tail = null
        }

        return result
    }

    /**
     * Return and remove the last item from the list.
     * O(n)
     */
    fun removeLast(): T? {
        // Get the current value of head, if it's null the list is empty so return null
        val head = head ?: return null
        // If head is the only node it's also the last so just pop
        if (head.next == null) return pop()
        // Reduce the size of the list
        size--

        // We're going to move down the front of the list to the end because the tail node doesn't have a reference to
        // the node before it. `prev` starts out as head so the node just before the one removed can be unlinked to it.
        // `current` starts out as head because we're starting at the front of the list and working forward by following
        // links.
        var prev = head
        var current = head

        // The last element in the list has a null `next` so keep looking for that then eject that node without `next`
        var next = current.next
        while (next != null) {
            prev = current
            current = next
            next = current.next
        }

        // Unlink the node just before last from the removed node
        prev.next = null
        // Update the tail with the new tail
        tail = prev
        // Return the value that was removed
        return current.value
    }

    /**
     * Remove the node after the provided node and return it or null if there's a node.
     * O(1)
     */
    fun removeAfter(node: Node<T>): T? {
        // the node to remove if it exists otherwise null
        val result = node.next?.value

        // if the node to be removed, `next`, is that same as tail update tail to `node` since it's the new tail.
        if (node.next == tail) {
            tail = node
        }

        // only reduce the size if the element after this element exists.
        if (node.next != null) {
            size--
        }

        // if there's a node after `node` use the `next` after the removed node.
        node.next = node.next?.next

        // return the removed node or null
        return result
    }

    override fun toString(): String {
        return if (isEmpty()) {
            "Empty list"
        } else {
            head.toString()
        }
    }

    /**
     * Part of the Iterator interface.
     */
    override fun iterator(): MutableIterator<T> {
        return LinkedListIterator(this)
    }


    /**
     * MutableCollection
     */
    override fun clear() {
        head = null
        tail = null
        size = 0
    }

    /**
     * MutableCollection
     */
    override fun addAll(elements: Collection<T>): Boolean {
        for (element in elements) {
            append(element)
        }
        return true
    }

    /**
     * MutableCollection
     */
    override fun add(element: T): Boolean {
        append(element)
        return true
    }

    /**
     * MutableCollection
     * O(n^3) because we add a loop to `iterator.remove`() and use `contains` on elements.
     */
    override fun retainAll(elements: Collection<T>): Boolean {
        var result = false
        val iterator = this.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            // remove any items that aren't in `elements`
            if (!elements.contains(item)) {
                iterator.remove()
                result = true
            }
        }
        return result
    }

    /**
     * MutableCollection
     * O(n^2) because iterator.remove is O(n)
     */
    override fun remove(element: T): Boolean {
        // use the iterator since it has a remove method on it
        val iterator = iterator()

        while (iterator.hasNext()) {
            val item = iterator.next()

            // check to see if the element matches then remove it
            if (item == element) {
                iterator.remove()
                return true
            }
        }

        return false
    }

    /**
     * MutableCollection
     * O(n^3) because remove is O(n^2) and we're adding another loop!
     */
    override fun removeAll(elements: Collection<T>): Boolean {
        var result = false
        for (item in elements) {
            result = remove(item) || result
        }
        return result
    }

    /**
     * Check to see if `element` is in the list.
     * Part of the Collection interface.
     * O(n)
     */
    override fun contains(element: T): Boolean {
        for (item in this) {
            if (item == element) return true
        }
        return false
    }

    /**
     * Check to see if this list contains all items in `elements`.
     * Part of the Collection interface.
     * O(n^2)
     */
    override fun containsAll(elements: Collection<T>): Boolean {
        // loop over the elements
        for (searched in elements) {
            // if even one of the elements isn't in the list return false
            if (!contains(searched)) return false
        }
        return true
    }

    fun addInReverse(list: LinkedList<T>, node: Node<T>) {

        val next = node.next
        if (next != null) {
            addInReverse(list, next)
        }

        // `addInReverse` is called before `append` which means we descend all the way
        // to the bottom of the list before appending
        list.append(node.value)
    }
}

class LinkedListIterator<T : Any>(private val list: LinkedList<T>) : MutableIterator<T> {
    // keep track of the current position of the iterator
    private var index = 0

    // keep track of the last node returned to make it easier to find the next one:
    private var lastNode: Node<T>? = null

    /**
     * If index is bigger than the size of the list than there are not more elements to get.
     */
    override fun hasNext(): Boolean {
        return index < list.size
    }

    override fun next(): T {
        // throw if the index gets larger than the list
        if (index >= list.size) throw IndexOutOfBoundsException()
        // if the index is zero then start with the first node otherwise avoid iterating over the list and update
        // `lastNode` to the current `lastNode`'s `next`.
        lastNode = if (index == 0) {
            list.nodeAt(0)
        } else {
            lastNode?.next
        }

        // don't forget to increment the index because we've moved forward one
        index++
        return lastNode!!.value
    }

    /**
     * Removes the last element returned
     * Part of MutableIterator interface
     * O(n) because it uses `nodeAt()`
     */
    override fun remove() {
        // If the index is on the first element just pop
        // because the last element returned is the first element.
        if (index == 1) {
            list.pop()
        } else {
            // at any index beyond 1 or less than 1
            // get the element 2 before the current index
            val prevNode = list.nodeAt(index - 2) ?: return
            // then use removeAfter to remove the last node returned
            list.removeAfter(prevNode)
            // now set `lastNode` to the node before the one removed
            // this ensures `next()` returns the correct node
            lastNode = prevNode
        }
        // move the index back 1 so `next()` reads the item after the one removed
        // otherwise an item would get skipped.
        index--
    }
}

/**
 * Reverse the list
 * O(n)
 */
fun <T : Any>LinkedList<T>.reverse(): LinkedList<T> {
    // nothing to do so just return an empty list
    if (isEmpty()) {
        return LinkedList<T>()
    }

    val reversed = LinkedList<T>()

    // only get the first node so it should be O(1)
    var current = nodeAt(0)
    while (current != null) {
        reversed.push(current.value)
        current = current.next
    }

    return reversed
}

fun <T : Any>LinkedList<T>.reverseRecursive(): LinkedList<T> {
    val result = LinkedList<T>()
    val head = this.nodeAt(0)
    if (head != null) {
        addInReverse(result, head)
    }
    return result
}

/**
 * Find the middle element of the linked list by iterating
 * O(n)
 */
fun <T : Any>LinkedList<T>.middleIterate(): Node<T>? {
    var currentIndex = 0
    // use floor rather than ceiling because the index starts at 0
    val middleIndex = floor(size / 2.0).toInt()

    var current = nodeAt(0)
    while (current != null && currentIndex != middleIndex) {
        current = current.next
        currentIndex++
    }

    return current
}

/**
 * Find the middle element of the linked list using the runner technique
 * O(n) but a little faster
 */
fun <T : Any>LinkedList<T>.middle(): Node<T>? {
    var slow = this.nodeAt(0)
    var fast = this.nodeAt(0)

    while (fast != null) {
        fast = fast.next
        if (fast != null) {
            // fast goes 2 elements for every 1 slow goes
            // this means fast is always twice as far from the beginning as slow
            // so then when fast reaches the end slow is in the middle
            // super fascinating
            fast = fast.next
            slow = slow?.next
        }
    }

    return slow
}

fun <T : Comparable<T>>LinkedList<T>.merge(that: LinkedList<T>): LinkedList<T> {
    // if either list is empty just bail out
    if (that.isEmpty()) return this
    if (isEmpty()) return that

    val mergedList = LinkedList<T>()

    // start at the beginning
    var thisNode = nodeAt(0)
    var thatNode = that.nodeAt(0)

    // keep going until either one is empty
    while (thisNode != null && thatNode != null) {
        // we want them to stay sorted from in ascending order
        if (thisNode.value < thatNode.value) {
            // add the node then go to the next node
            mergedList.append(thisNode.value)
            thisNode = thisNode.next
        } else {
            // add the node then go to the next node
            mergedList.append(thatNode.value)
            thatNode = thatNode.next
        }
    }

    // empty out this list
    while (thisNode != null) {
        mergedList.append(thisNode.value)
        thisNode = thisNode.next
    }

    // empty out that list
    while (thatNode != null) {
        mergedList.append(thatNode.value)
        thatNode = thatNode.next
    }

    return mergedList
}
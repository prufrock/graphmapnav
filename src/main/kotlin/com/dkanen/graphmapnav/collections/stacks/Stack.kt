package com.dkanen.graphmapnav.collections.stacks


interface Stack<T : Any> {
    fun push(element: T)
    fun pop(): T?

    /**
     * Look at the top element of the stack without changing the stack.
     */
    fun peek(): T?

    /**
     * The number of elements in the stack.
     */
    val count: Int

    /**
     * Whether the stack is empty.
     */
    val isEmpty: Boolean
        get() = count == 0

    /**
     * Return the stack as a list.
     */
    fun toList(): List<T>
}

class StackImpl<T : Any> : Stack<T> {
    // holds the data
    // going to treat the end of the list as the top of the stack
    // this make pushing and popping O(1)
    private val storage = arrayListOf<T>()

    override val count: Int
        get() = storage.size

    companion object {
        /**
         * Uses a companion object as a factory method to construct a new stack from anything that is Iterable.
         * @param items The Iterable stuff to put in the stack.
         */
        fun <T : Any> create(items: Iterable<T>): Stack<T> {
            val stack = StackImpl<T>()
            for (item in items) {
                stack.push(item)
            }
            return stack
        }
    }

    /**
     * Add an element to the top of the stack.
     * @param element The element to add to the stack.
     * O(1)
     */
    override fun push(element: T) {
        storage.add(element)
    }

    /**
     * Pop an element off the top of the stack.
     * O(1)
     */
    override fun pop(): T? {
        if (isEmpty) {
            return null
        }
        return storage.removeAt(count - 1)
    }

    override fun peek(): T? {
        return storage.lastOrNull()
    }

    // print out the stack in a pleasant way
    override fun toString() = buildString {
        appendLine("----top----")
        storage.asReversed().forEach {
            appendLine("$it")
        }
        appendLine("-----------")
    }

    override fun toList() = storage.asReversed()
}

/**
 * Create a stack from the arguments passed as elements.
 * @param elements The elements to create the stack from.
 */
fun <T : Any> stackOf(vararg elements: T): Stack<T> = StackImpl.create(elements.asList())

/**
 * Reverse the elements in a stack.
 * O(n)
 */
fun <T : Any> Stack<T>.reverse(): Stack<T> {
    val reverseStack = StackImpl<T>()

    // Use the superpower of a Stack to facilitate back tracking and just pop and then push.
    while(peek() != null) {
        pop()?.let {
            reverseStack.push(it)
        }
    }

    return reverseStack
}

/**
 * Checks to see if parentheses are balanced using a stack.
 * @params A string with parentheses in it.
 * O(n)
 */
fun String.balanced(): Boolean {
    val stack = StackImpl<Char>()

    forEach {
        when(it) {
            '(' -> stack.push(it)
            ')' -> if (stack.isEmpty) {
                // bail out if we try to push a closing brace into an empty stack
                // since a closing brace should always come after an opening one
                return false
            } else {
                stack.pop()
            }
        }
    }

    return stack.isEmpty
}
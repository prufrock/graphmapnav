package com.dkanen.graphmapnav.collections.stacks

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StackImplTest {
    @Test
    fun `test pushing and popping`() {
        val stack = StackImpl<Int>().apply {
            push(1)
            push(2)
            push(3)
            push(4)
        }

        // Each element is put on top of the stack, so they come out in reverse order.
        assertEquals(listOf(4, 3, 2, 1), stack.toList())
        // The last element added is returned (LIFO).
        assertEquals(4, stack.pop())
        // When an element is popped it's removed from the stack.
        assertEquals(listOf(3, 2, 1), stack.toList())
    }

    @Test
    fun `create a stack from a list`() {
        val list = listOf("A", "B", "C", "D")
        val stack = StackImpl.create(list)
        list.reversed().forEach {
            assertEquals(it, stack.pop())
        }
    }

    @Test
    fun `create a stack from literals`() {
        val stack = stackOf(1.0, 2.0, 3.0, 4.0)

        assertEquals(4.0, stack.pop())
    }

    @Test
    fun `reverse a stack`() {
        val stack = stackOf(1.0, 2.0, 3.0, 4.0)

        assertEquals(listOf(4.0, 3.0, 2.0, 1.0), stack.toList())
        assertEquals(listOf(1.0, 2.0, 3.0, 4.0), stack.reverse().toList())
    }

    @Test
    fun `check for balanced parentheses`() {
        assertTrue("h((e))llo(world)()".balanced())
        assertFalse("(hello world".balanced())
        assertFalse("(h)e)llo world".balanced())
    }
}
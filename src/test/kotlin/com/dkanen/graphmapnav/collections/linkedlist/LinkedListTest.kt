package com.dkanen.graphmapnav.collections.linkedlist

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class LinkedListTest {

    @Test
    fun `push 3 items to the linked list`() {
        val list = LinkedList<Int>()
        list.push(3).push(2).push(1)

        // Notice how the values start with the last element added.
        assertEquals("1 -> 2 -> 3", list.toString())
    }

    @Test
    fun `append 3 items to the linked list`() {
        val list = LinkedList<Int>()
        list.append(1).append(2).append(3)

        // We're adding new elements to the end now so the last element IS the last element.
        assertEquals("1 -> 2 -> 3", list.toString())
    }

    @Test
    fun `insert after a particular node`() {
        val list = LinkedList<Int>()
        list.push(3).push(2).push(1)

        assertEquals("1 -> 2 -> 3", list.toString(), "before inserting")
        var middleNode = list.nodeAt(1)!!
        for (i in 1..3) {
            // keep updating the middle node with last inserted to keep inserting nodes at the same point
            middleNode = list.insert(-1 * i, middleNode)
        }

        assertEquals("1 -> 2 -> -1 -> -2 -> -3 -> 3", list.toString(), "after inserting")
    }

    @Test
    fun `pop an item from the list`() {
        val list = LinkedList<Int>()
        list.push(3).push(2).push(1)

        assertEquals("1 -> 2 -> 3", list.toString())
        val popped = list.pop()
        assertEquals("2 -> 3", list.toString())
        assertEquals(1, popped)
    }

    @Test
    fun `remove the last node`() {
        val list = LinkedList<Int>()
        list.push(3).push(2).push(1)

        assertEquals("1 -> 2 -> 3", list.toString(), "before removing")
        val removedValue = list.removeLast()

        assertEquals("1 -> 2", list.toString(), "after removing")
        assertEquals(3, removedValue, "the removed value")
    }

    @Test
    fun `remove a node after a particular node`() {
        val list = LinkedList<Int>()
        list.push(3).push(2).push(1)

        assertEquals("1 -> 2 -> 3", list.toString(), "before removing a node")
        // the node we want to remove is at 1
        val index = 1
        // get the node before that node
        val node = list.nodeAt(index - 1)!!
        // remove the node after that node
        val removedValue = list.removeAfter(node)
        assertEquals("1 -> 3", list.toString(), "after removing the middle index")
        assertEquals(2, removedValue, "the value removed")
    }

    @Test
    fun `use the Iterator interface to loop over the list`() {
        val list = LinkedList<Int>()
        list.push(3).push(2).push(1)
        assertEquals("1 -> 2 -> 3", list.toString(), "current value")

        var sum = 0
        // can use `for` and other Iterator extensions
        for (item in list) {
            sum += item
        }
        assertEquals(6, sum, "the result of adding the items in the list with a for loop")
    }

    @Test
    fun `removing elements`() {
        val list: MutableCollection<Int> = LinkedList()
        list.add(3)
        list.add(2)
        list.add(1)

        assertEquals("3 -> 2 -> 1", list.toString())
        list.remove(1)
        assertEquals("3 -> 2", list.toString())
    }

    @Test
    fun `retaining elements`() {
        val list: MutableCollection<Int> = LinkedList()
        list.add(3)
        list.add(2)
        list.add(1)
        list.add(4)
        list.add(5)

        assertEquals("3 -> 2 -> 1 -> 4 -> 5", list.toString())
        list.retainAll(listOf(3, 4, 5))
        assertEquals("3 -> 4 -> 5", list.toString())
    }

    @Test
    fun `remove elements with removeAll`() {
        val list: MutableCollection<Int> = LinkedList()
        list.add(3)
        list.add(2)
        list.add(1)
        list.add(4)
        list.add(5)

        assertEquals("3 -> 2 -> 1 -> 4 -> 5", list.toString())
        list.removeAll(listOf(3, 4, 5))
        assertEquals("2 -> 1", list.toString())
    }

    @Test
    fun `reverse the list`() {
        val list = LinkedList<Int>()
        list.push(3).push(2).push(1)

        assertEquals("1 -> 2 -> 3", list.toString(), "current value")
        assertEquals("3 -> 2 -> 1", list.reverse().toString(), "reversed")
    }

    @Test
    fun `reverse the list recursive`() {
        val list = LinkedList<Int>()
        list.push(3).push(2).push(1)

        assertEquals("1 -> 2 -> 3", list.toString(), "current value")
        assertEquals("3 -> 2 -> 1", list.reverseRecursive().toString(), "reversed")
    }

    @Test
    fun `find the middle with the middle iterator`() {
        val list1 = LinkedList<Int>()
        list1.push(4).push(3).push(2).push(1)

        assertEquals(3, list1.middleIterate()?.value)

        val list2 = LinkedList<Int>()
        list2.push(3).push(2).push(1)

        assertEquals(2, list2.middleIterate()?.value)
    }

    @Test
    fun `find the middle with the runner iterator`() {
        val list1 = LinkedList<Int>()
        list1.push(4).push(3).push(2).push(1)

        assertEquals(3, list1.middle()?.value)

        val list2 = LinkedList<Int>()
        list2.push(3).push(2).push(1)

        assertEquals(2, list2.middle()?.value)
    }

    @Test
    fun `merge 1 element with no elements`() {
        val list1 = LinkedList<Int>()
        list1.push(1)

        val list2 = LinkedList<Int>()

        val expected = LinkedList<Int>()
        expected.push(1)

        assertEquals(expected.toString(), list1.merge(list2).toString())
    }

    @Test
    fun `merge 1 element with 1 element less than current element`() {
        val list1 = LinkedList<Int>().push(1)

        val list2 = LinkedList<Int>().push(-1)

        val expected = LinkedList<Int>()
        expected.push(1).push(-1)

        assertEquals(expected.toString(), list1.merge(list2).toString())
    }

    @Test
    fun `merge 1 element with 1 element greater than current element`() {
        val list1 = LinkedList<Int>().push(1)

        val list2 = LinkedList<Int>().push(4)

        val expected = LinkedList<Int>()
        expected.push(4).push(1)

        assertEquals(expected.toString(), list1.merge(list2).toString())
    }

        @Test
    fun `merge two sorted linked lists into a single sorted linked list`() {
        val list1 = LinkedList<Int>()
        list1.push(11).push(10).push(4).push(1)

        val list2 = LinkedList<Int>()
        list2.push(6).push(3).push(2).push(-1)

        val expected = LinkedList<Int>()
        expected.push(11).push(10).push(6).push(4).push(3).push(2).push(1).push(-1)

        assertEquals(expected.toString(), list1.merge(list2).toString())
    }
}



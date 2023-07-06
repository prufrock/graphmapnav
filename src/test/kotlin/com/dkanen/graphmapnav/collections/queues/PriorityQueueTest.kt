package com.dkanen.graphmapnav.collections.queues

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PriorityQueueTest {
    @Test
    fun `max priority queue`() {
        val priorityQueue = ComparablePriorityQueueImpl<Int>()

        arrayListOf(1, 12, 3, 4, 1, 6, 8, 7).forEach {
            priorityQueue.enqueue(it)
        }

        val items = mutableListOf<Int>()

        while (!priorityQueue.isEmpty) {
            priorityQueue.dequeue()?.let {
                items.add(it)
            }
        }

        assertEquals(listOf(12, 8, 7, 6, 4, 3, 1, 1), items)
    }

    @Test
    fun `min priority queue`() {
        val stringLengthComparator = Comparator<String> { o1, o2 ->
            val length1 = o1?.length ?: -1
            val length2 = o2?.length ?: -1
            length1 - length2
        }

        val priorityQueue = ComparatorPriorityQueueImpl(stringLengthComparator)

        arrayListOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine").forEach {
            priorityQueue.enqueue(it)
        }

        val items = mutableListOf<String>()

        while (!priorityQueue.isEmpty) {
            priorityQueue.dequeue()?.let {
                items.add(it)
            }
        }

        assertEquals(listOf("three", "eight", "seven", "nine", "four", "five", "one", "two", "six"), items)
    }

    @Test
    fun `sorting Person by military then age`() {
        val waitListComparator = Comparator<Person> { p1, p2 ->
            p1.priority - p2.priority
        }

        val priorityQueue = ComparatorPriorityQueueImpl(waitListComparator)



        arrayListOf<Person>(
            Person("Sally", 10, false),
            Person("Malcom", 20, false),
            Person("Rhonda", 5, true)
        ).forEach {
            priorityQueue.enqueue(it)
        }

        val result = mutableListOf<String>()

        while(!priorityQueue.isEmpty) {
            priorityQueue.dequeue()?.let {
                result.add(it.name)
            }
        }

        assertEquals(listOf("Rhonda", "Malcom", "Sally"), result)
    }
}

data class Person(val name: String, val age: Int, val isMilitary: Boolean) {
    val priority: Int
        get() = 200 * isMilitary.toInt() + age

    override fun toString(): String {
        return "$name - $age - $isMilitary"
    }
}

fun Boolean.toInt(): Int {
    return when(this) {
        true -> 1
        false -> 0
    }
}
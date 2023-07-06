package com.dkanen.graphmapnav.collections.queues

import java.util.*
import kotlin.collections.ArrayList

abstract class AbstractPriorityQueueArrayList<T: Any>: Queue<T> {

    abstract val arrayList: ArrayList<T>

    override fun enqueue(element: T): Boolean {
        arrayList.add(element)

        sort()

        return true
    }

    abstract fun sort();

    override fun dequeue(): T? = if (isEmpty) null else arrayList.removeAt(0)

    override val count: Int
        get() = arrayList.count()

    override fun peek(): T? = arrayList.firstOrNull()

    override fun toList(): List<T> = arrayList
}

class ComparableArrayListPriorityQueueImpl<T: Comparable<T>>: AbstractPriorityQueueArrayList<T>() {
    override val arrayList: ArrayList<T> = arrayListOf<T>()

    override fun sort() {
        arrayList.sort()
    }

    override fun toList(): List<T> = arrayList
}

class ComparatorArrayListPriorityQueueImpl<T: Any>(private val comparator: Comparator<T>) : AbstractPriorityQueueArrayList<T> () {
    override val arrayList: ArrayList<T> = arrayListOf<T>()

    override fun sort() {
        arrayList.sortWith(comparator)
    }

    override fun toList(): List<T> = arrayList
}
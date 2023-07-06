package com.dkanen.graphmapnav.collections.ringbuffer

import com.dkanen.graphmapnav.collections.queues.Queue

class RingBufferQueue<T: Any>(size: Int): Queue<T> {
    private val ringBuffer: RingBuffer<T> = ArrayListRingBuffer(size)

    /**
     * O(1)
     */
    override val count: Int
        get() = ringBuffer.count

    /**
     * O(1)
     */
    override fun peek(): T? = ringBuffer.first

    /**
     * Return false if the ring buffer is full.
     * O(1)
     */
    override fun enqueue(element: T): Boolean = ringBuffer.write(element)

    /**
     * As long as there's something to return, return it.
     * O(1)
     */
    override fun dequeue(): T? = if (isEmpty) null else ringBuffer.read()

    override fun toString() = ringBuffer.toString()

    override fun toList(): List<T> = ringBuffer.toList()
}
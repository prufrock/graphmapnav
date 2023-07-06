package com.dkanen.graphmapnav.collections.ringbuffer

interface RingBuffer<T : Any> {
    // The number of items in the buffer
    val count: Int

    // The element at the front of the buffer
    val first: T?

    val isEmpty: Boolean

    val isFull: Boolean

    /**
     * Add the element to the buffer.
     * @param element The element to add.
     */
    fun write(element: T): Boolean

    /**
     * Read an element from the buffer.
     */
    fun read(): T?
    fun toList(): List<T>
}


class ArrayListRingBuffer<T : Any>(private val size: Int) : RingBuffer<T> {

    private var storage = ArrayList<T?>(size)
    private var readIndex = 0
    private var writeIndex = 0

    /**
     * Determine the number of readable elements by taking the difference between the current location of the `writeIndex` and the read index.
     * This can't become negative because the `writeIndex` is always larger than the `readIndex`.
     * This is possible because the modulo of the size is always used to determine the current index to read from.
     */
    private val availableSpaceForReading: Int
      get() = (writeIndex - readIndex)

    /**
     * Determine how many more elements can be added by taking the difference between the size and what's available for reading.
     */
    private val availableSpaceForWriting: Int
      get() = (size - availableSpaceForReading)

    override val count: Int
        get() = availableSpaceForReading
    override val first: T?
        get() = storage.getOrNull(readIndex)
    override val isEmpty: Boolean
        get() = (count == 0)
    override val isFull: Boolean
        get() = (availableSpaceForWriting == 0)

    override fun read(): T? {
        return if (!isEmpty) {
            // use the modulo of the readIndex and the size since the read index goes around and around the ring buffer but should always read from a value less than size.
            val element = storage[readIndex % size]
            // move the read index ahead one for the next time read is called.
            readIndex += 1
            // return the element
            element
        } else {
            null
        }
    }

    override fun write(element: T): Boolean {
        // Don't write if full
        return if (!isFull) {
            // if storage size is less than the size of the buffer there's room to add more elements to storage.
            if (storage.size < size) {
                storage.add(element)
            } else {
                // once storage is full reuse the position of the `writeIndex`
                // the `writeIndex` wraps around the buffer so use the modulo of the size to determine where it should write to.
                storage[writeIndex % size] = element
            }
            // move the `writeIndex` forward
            // The `writeIndex` is always increasing and the modulo is used above. Otherwise, you have to worry about values like `availableSpaceForReading` becoming negative.
            // There is a limit on the size of an Integer that will, at some point, causing the RingBuffer to break after too many writes.
            writeIndex += 1
            true
        } else {
            false
        }
    }

    override fun toString(): String {
        // read the values out of the buffer by reading from the readable entries in storage.
        val values = (0 until availableSpaceForReading).map { offset ->
            "${storage[(readIndex + offset) % size]!!}"
        }

        return values.joinToString(prefix = "[", separator = ", ", postfix = "]")
    }

    override fun toList(): List<T> = (0 until availableSpaceForReading).map {
        // the read index needs to stay where it is since this is more of a debugging aid
        // that's why it goes from readIndex to the availableSpaceReading
        // where the current iteration is used as an offset
        // then it modulos the size since readIndex keeps growing unbound.
            offset -> storage[(readIndex + offset) % size]!!
    }
}
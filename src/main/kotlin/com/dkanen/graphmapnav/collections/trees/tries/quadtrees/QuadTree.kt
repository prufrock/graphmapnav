package com.dkanen.graphmapnav.collections.trees.tries.quadtrees

import com.dkanen.graphmapnav.game.Rect

interface QuadTree {
    fun add(rect: Rect): Boolean
    fun search(rect: Rect): List<Rect>
}

class QuadTreeRect(private val boundary: Rect, private val level: Int = 0, private val maxObjects: Int = 10, private val maxLevels: Int = 5): QuadTree {

    private val elements: MutableList<Rect> = mutableListOf()
    private val nodes: MutableList<QuadTree> = mutableListOf()

    override fun add(rect: Rect): Boolean {
        // if it's outside the quadtree reject it
        if (!boundary.contains(rect)) {
            return false
        }

        // if the tree hasn't split yet store it here
        if (!hasSplit() && hasVacancies()) {
            elements.add(rect)
            return true
        }

        if (!hasSplit()) {
            split()
        }

        // If it doesn't fit in a partition add it to this level.
        return if (partitionInsert(rect)) {
            true
        } else {
            elements.add(rect)
        }
    }

    override fun search(rect: Rect): List<Rect> {
        val found = mutableListOf<Rect>()

        // Get out early if this node can't hold the search rect
        if (!boundary.contains(rect)) {
            return found
        }

        elements.forEach {
            if (rect.contains(it)) {
                found.add(it)
            }
        }

        // check nodes
        nodes.forEach {
            found += it.search(rect)
        }

        return found
    }

    /**
     * Attempts to insert in the partitions. Returns false if it fails.
     */
    private fun partitionInsert(rect: Rect): Boolean {
        val iter = nodes.iterator()

        while(iter.hasNext()) {
            val partition = iter.next()
            if (partition.add(rect)) {
                return true
            }
        }

        return false
    }

    private fun split(): Boolean {
        val (ab, cd) = boundary.divideHorizontal()
        val (a, b) = ab.divideVertical()
        val (c, d) = cd.divideVertical()

        nodes.add(QuadTreeRect(a, level + 1, maxObjects, maxLevels))
        nodes.add(QuadTreeRect(b, level + 1, maxObjects, maxLevels))
        nodes.add(QuadTreeRect(c, level + 1, maxObjects, maxLevels))
        nodes.add(QuadTreeRect(d, level + 1, maxObjects, maxLevels))

        // redistribute elements
        val oldElements = elements.toMutableList()
        elements.clear()
        oldElements.forEach {
            add(it)
        }

        return true
    }

    private fun hasVacancies(): Boolean = elements.count() < maxObjects

    private fun hasSplit() = nodes.isNotEmpty()
}
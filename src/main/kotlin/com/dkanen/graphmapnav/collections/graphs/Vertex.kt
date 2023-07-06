package com.dkanen.graphmapnav.collections.graphs

/**
 * A vertex has a unique index within its graph and holds a piece of data.
 * Defined as a data class so it can be easily used as a key in a map.
 */
data class Vertex<T: Any>(val index: Int, val data: T) {
}
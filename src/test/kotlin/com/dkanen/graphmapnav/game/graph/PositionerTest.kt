package com.dkanen.graphmapnav.game.graph

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class PositionerTest {

    private val oneNode = readGraphFile("one-node.json")
    private val twoNodeEast = readGraphFile("two-node-east.json")
    private val twoNodeWest = readGraphFile("two-node-west.json")
    private val twoNodeNorth = readGraphFile("two-node-north.json")
    private val twoNodeSouth = readGraphFile("two-node-south.json")
    private val twoNodeDoubleLink = readGraphFile("two-node-double-link.json")
    private val threeNodeLineWest = readGraphFile("three-node-line-west.json")

    @Test
    fun oneNode() {
        val positioner = Positioner()

        val mapGraph = positioner.position(oneNode)

        assertEquals(1, mapGraph.allVertices.size)
    }

    @Test
    fun twoNode() {
        val positioner = Positioner()
        val testList: List<Pair<JsonGraphNodeList, Vector2>> = listOf(
            Pair(twoNodeEast, Vector2(1.0, 0.0)),
            Pair(twoNodeSouth, Vector2(0.0, 1.0)),
            Pair(twoNodeWest, Vector2(-1.0, 0.0)),
            Pair(twoNodeNorth, Vector2(0.0, -1.0)),
        )

        testList.forEach { (nodeList, expected) ->
            val mapGraph = positioner.position(nodeList)
            assertEquals(2, mapGraph.allVertices.size)
            val vertices = mapGraph.allVertices
            vertices.first { it.data.value == "Reflecting Pool" }.let {
                assertEquals(0.0, it.data.position.x)
                assertEquals(0.0, it.data.position.y)
            }

            vertices.first { it.data.value == "Ante Chamber" }.let {
                assertEquals(expected.x, it.data.position.x, 0.01)
                assertEquals(expected.y, it.data.position.y, 0.01)
            }
        }
    }

    @Test
    fun ensureThreeConnectedNodesArePositionedCorrectly() {
       val threeNode = threeNodeLineWest

        val positioner = Positioner()
        val mapGraph = positioner.position(threeNode)

        assertEquals(3, mapGraph.allVertices.size)
        val vertices = mapGraph.allVertices
        vertices.first { it.data.value == "Reflecting Pool" }.let {
            assertEquals(0.0, it.data.position.x)
            assertEquals(0.0, it.data.position.y)
        }

        vertices.first { it.data.value == "Ante Chamber" }.let {
            assertEquals(-1.0, it.data.position.x, 0.01)
            assertEquals(0.0, it.data.position.y, 0.01)
        }

        vertices.first { it.data.value == "Furnace" }.let {
            assertEquals(-2.0, it.data.position.x, 0.01)
            assertEquals(0.0, it.data.position.y, 0.01)
        }
    }

    @Test
    fun doNotDuplicateNodes() {
        val nodeList = twoNodeDoubleLink

        val positioner = Positioner()
        val mapGraph = positioner.position(nodeList)

        assertEquals(2, mapGraph.allVertices.count())
    }

    private fun readGraphFile(filename: String): JsonGraphNodeList {
        val json = javaClass.classLoader.getResource(filename)!!.readText()
        val mapper = jacksonMapperBuilder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).build()

        return mapper.readValue(json)
    }
}
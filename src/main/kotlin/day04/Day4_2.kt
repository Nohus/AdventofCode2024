package day04

import solve
import utils.Direction8.NORTH_EAST
import utils.Direction8.NORTH_WEST
import utils.Direction8.SOUTH_EAST
import utils.Direction8.SOUTH_WEST
import utils.toGrid

fun main() = solve { lines ->
    val mapping = lines.toGrid()
    mapping.keys.count { point ->
        val a = List(3) { mapping[point.move(NORTH_WEST).move(SOUTH_EAST, it)] }.joinToString("")
        val b = List(3) { mapping[point.move(SOUTH_WEST).move(NORTH_EAST, it)] }.joinToString("")
        listOf(a, b).all { it == "MAS" || it.reversed() == "MAS" }
    }
}

package day10

import solve
import utils.Point
import utils.toGrid

fun main() = solve { lines ->
    val grid = lines.toGrid().mapValues { (_, v) -> v.digitToInt() }
    val trailheads = grid.filterValues { it == 0 }.keys
    trailheads.sumOf { trailhead ->
        val incompletePaths = mutableListOf(listOf(trailhead))
        val endpoints = mutableSetOf<Point>()
        while (incompletePaths.isNotEmpty()) {
            val path = incompletePaths.removeFirst()
            val point = path.last()
            val value = grid[point]!!
            if (value == 9) endpoints += point
            else incompletePaths += point.getAdjacentSides()
                .filter { grid[it] == value + 1 }
                .map { path + it }
        }
        endpoints.size
    }
}

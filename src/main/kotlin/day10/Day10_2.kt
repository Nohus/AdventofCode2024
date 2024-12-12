package day10

import solve
import utils.Point
import utils.toGrid

fun main() = solve { lines ->
    val grid = lines.toGrid().mapValues { (_, v) -> v.digitToInt() }
    val trailheads = grid.filterValues { it == 0 }.keys
    trailheads.sumOf { trailhead ->
        val incompletePaths = mutableListOf(listOf(trailhead))
        val completePaths = mutableSetOf<List<Point>>()
        while (incompletePaths.isNotEmpty()) {
            val path = incompletePaths.removeFirst()
            val point = path.last()
            val value = grid[point]!!
            if (value == 9) completePaths += path
            else incompletePaths += point.getNeighbors()
                .filter { grid[it] == value + 1 }
                .map { path + it }
        }
        completePaths.size
    }
}

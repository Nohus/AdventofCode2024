package day12

import solve
import utils.Point
import utils.toGrid

fun main() = solve { lines ->
    val grid = lines.toGrid()
    val regionMapping = mutableMapOf<Point, Int>()
    var regionId = -1
    for ((point, plant) in grid) {
        if (point in regionMapping) continue
        regionId++
        val toVisit = mutableListOf(point)
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (grid[current] == plant) {
                regionMapping[current] = regionId
                toVisit += current.getNeighbors().filter { it !in toVisit && it !in regionMapping }
            }
        }
    }

    (0..regionId).sumOf { id ->
        val points = regionMapping.filter { it.value == id }.keys
        val area = points.size
        val perimeter = points.sumOf { point ->
            4 - (point.getNeighbors().count { it in points })
        }
        area * perimeter
    }
}

package day12

import solve
import utils.Direction
import utils.Point
import utils.Turn.LEFT
import utils.Turn.RIGHT
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
        val allBorders = Direction.entries.sumOf { direction ->
            var borderCount = 0
            val visitedPoints = mutableSetOf<Point>()
            for (point in points) {
                if (point in visitedPoints) continue
                val isBorder = regionMapping[point.move(direction)] != id
                if (isBorder) {
                    borderCount++
                    visitedPoints += point
                    listOf(LEFT, RIGHT).map { direction.rotate(it) }.forEach { sideDirection ->
                        var current = point
                        do {
                            current = current.move(sideDirection)
                            visitedPoints += current
                        } while (regionMapping[current] == id && regionMapping[current.move(direction)] != id)
                    }
                }
            }
            borderCount
        }
        area * allBorders
    }
}

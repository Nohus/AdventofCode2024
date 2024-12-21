package day20

import solve
import utils.Direction
import utils.Point
import utils.toGrid

fun main() = solve { lines ->
    val grid = lines.toGrid()
    val raceStart = grid.entries.first { it.value == 'S' }.key
    val distances = dijkstra(grid, raceStart)
    grid.keys.filter { it in distances }.sumOf { start ->
        grid.keys.filter { it in distances && it.manhattan(start) <= 20 }
            .count { end -> distances[end]!! - distances[start]!! - start.manhattan(end) >= 100 }
    }
}

private fun dijkstra(grid: Map<Point, Char>, from: Point): Map<Point, Int> {
    val unvisited = mutableListOf(from to 0)
    val visited = mutableMapOf<Point, Int>()
    while (unvisited.isNotEmpty()) {
        val (position, distance) = unvisited.removeFirst()
        if (distance >= (visited[position] ?: Int.MAX_VALUE)) continue
        visited[position] = distance
        unvisited += Direction.entries
            .map { direction -> position.move(direction) }
            .filter { grid[it] != '#' }
            .map { it to distance + 1 }
            .filter { (position, distance) -> distance < (visited[position] ?: Int.MAX_VALUE) }
    }
    return visited
}

package day06

import solve
import solveRaw
import utils.Direction
import utils.Point
import utils.Turn
import utils.mapToInts
import utils.swap
import utils.toGrid
import java.lang.RuntimeException

fun main() = solve { lines ->
    val grid = lines.toGrid()
    val first = grid.entries.first { it.value == '^' }.key
    val visited = walk(grid, first)!!.map { it.first }.toSet()
    (visited - first).count { obstruction ->
        walk(grid.toMutableMap().also { it[obstruction] = '#' }, first) == null
    }
}

private fun walk(grid: Map<Point, Char>, first: Point): Set<Pair<Point, Direction>>? {
    var current = first
    var direction = Direction.NORTH
    val visited = mutableSetOf(current to direction)
    while (true) {
        val next = current.move(direction)
        if (next !in grid) break
        if (grid[next] == '#') {
            direction = direction.rotate(Turn.RIGHT)
        } else {
            current = next
            if (current to direction in visited) return null
            visited.add(current to direction)
        }
    }
    return visited
}

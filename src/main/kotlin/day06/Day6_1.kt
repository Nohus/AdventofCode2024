package day06

import solve
import solveRaw
import utils.Direction
import utils.Direction8
import utils.Point
import utils.Turn
import utils.mapToInts
import utils.toGrid

fun main() = solve { lines ->
    val grid = lines.toGrid()
    var current = grid.entries.first { it.value == '^' }.key
    var direction = Direction.NORTH
    val visited = mutableSetOf(current)
    while (true) {
        val next = current.move(direction)
        if (next !in grid) break
        if (grid[next] == '#') {
            direction = direction.rotate(Turn.RIGHT)
        } else {
            current = next
            visited.add(current)
        }
    }
    visited.size
}

package day15

import solveRaw
import utils.Direction
import utils.Point
import utils.toGrid

fun main() = solveRaw { input ->
    val (gridText, movesText) = input.split("\n\n")
    val grid = gridText.lines().toGrid().toMutableMap()
    var robot = grid.entries.first { it.value == '@' }.key.also { grid[it] = '.' }
    val moves = movesText.lines().joinToString("").map { Direction.of(it) }

    fun push(pos: Point, direction: Direction): Boolean {
        if (grid[pos] == '.') return true
        if (grid[pos] == '#') return false
        val next = pos.move(direction)
        if (push(next, direction)) {
            grid[pos] = '.'
            grid[next] = 'O'
            return true
        } else return false
    }

    moves.forEach { direction ->
        val next = robot.move(direction)
        if (push(next, direction)) robot = next
    }

    grid.filterValues { it == 'O' }.keys.sumOf { it.y * 100 + it.x }
}

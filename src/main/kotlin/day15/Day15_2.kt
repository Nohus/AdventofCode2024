package day15

import solveRaw
import utils.Direction
import utils.Direction.EAST
import utils.Direction.WEST
import utils.Point
import utils.toGrid

fun main() = solveRaw { input ->
    val (gridText, movesText) = input.split("\n\n")
    val originalGrid = gridText.lines().toGrid().toMutableMap()
    var robot = originalGrid.entries.first { it.value == '@' }.key.also { originalGrid[it] = '.' }
    val moves = movesText.lines().joinToString("").map { Direction.of(it) }

    robot = robot.copy(x = robot.x * 2)
    val grid = mutableMapOf<Point, Char>()
    originalGrid.forEach { (point, value) ->
        val new = point.copy(x = point.x * 2)
        when (value) {
            '#' -> { grid[new] = '#'; grid[new.move(EAST)] = '#' }
            '.' -> { grid[new] = '.'; grid[new.move(EAST)] = '.' }
            'O' -> { grid[new] = '['; grid[new.move(EAST)] = ']' }
        }
    }

    fun canPush(pos: Point, direction: Direction): Boolean {
        if (grid[pos] == '.') return true
        if (grid[pos] == '#') return false
        val next = pos.move(direction)
        return if (direction == EAST || direction == WEST) {
            canPush(next, direction)
        } else {
            val otherSegment = if (grid[pos] == '[') EAST else WEST
            canPush(next, direction) && canPush(next.move(otherSegment), direction)
        }
    }

    fun push(pos: Point, direction: Direction) {
        if (grid[pos] == '.') return
        val next = pos.move(direction)
        if (direction == EAST || direction == WEST) {
            push(next, direction)
            grid[next] = grid[pos]!!
            grid[pos] = '.'
        } else {
            val otherSegment = if (grid[pos] == '[') EAST else WEST
            push(next, direction)
            push(next.move(otherSegment), direction)
            grid[next] = grid[pos]!!
            grid[next.move(otherSegment)] = grid[pos.move(otherSegment)]!!
            grid[pos] = '.'
            grid[pos.move(otherSegment)] = '.'
        }
    }

    moves.forEach { direction ->
        val next = robot.move(direction)
        if (canPush(next, direction)) {
            push(next, direction)
            robot = next
        }
    }

    grid.filterValues { it == '[' }.keys.sumOf { it.y * 100 + it.x }
}

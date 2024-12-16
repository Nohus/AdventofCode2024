package day16

import solve
import utils.Direction
import utils.Point
import utils.Turn.LEFT
import utils.Turn.RIGHT
import utils.toGrid
import java.util.PriorityQueue

fun main() = solve { lines ->
    val grid = lines.toGrid().toMutableMap()
    val start = grid.entries.first { it.value == 'S' }.key
    val end = grid.entries.first { it.value == 'E' }.key

    data class State(
        val point: Point,
        val direction: Direction,
    )

    val visited = mutableSetOf<State>()
    val unvisited = PriorityQueue<Pair<State, Int>>(compareBy { it.second })
    unvisited += State(start, Direction.EAST) to 0
    while (unvisited.isNotEmpty()) {
        val (state, cost) = unvisited.remove().also { visited += it.first }
        unvisited.remove(state to cost)
        if (state.point == end) return@solve cost
        unvisited += listOf(
            State(state.point, state.direction.rotate(LEFT)) to cost + 1000,
            State(state.point, state.direction.rotate(RIGHT)) to cost + 1000,
            State(state.point.move(state.direction), state.direction) to cost + 1,
        ).filter { it.first !in visited && grid[it.first.point] != '#' }
    }
}

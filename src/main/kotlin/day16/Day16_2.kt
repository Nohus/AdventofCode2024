package day16

import solve
import utils.Direction
import utils.Point
import utils.Turn.LEFT
import utils.Turn.RIGHT
import utils.toGrid
import java.util.PriorityQueue

fun main() = solve(additionalTiming = true) { lines ->
    val grid = lines.toGrid().toMutableMap()
    val start = grid.entries.first { it.value == 'S' }.key
    val end = grid.entries.first { it.value == 'E' }.key

    data class State(
        val point: Point,
        val direction: Direction,
        val cost: Int,
        val previous: State?,
    )

    val visited = mutableSetOf<Pair<Point, Direction>>()
    val unvisited = PriorityQueue<State>(compareBy { it.cost })
    unvisited += State(start, Direction.EAST, 0, null)
    val bestPoints = mutableSetOf(end)
    var bestCost = Int.MAX_VALUE
    while (unvisited.isNotEmpty()) {
        val state = unvisited.remove().also { visited += (it.point to it.direction) }
        if (state.point == end) {
            if (state.cost <= bestCost) {
                bestCost = state.cost
                var current = state
                do {
                    bestPoints += current.point
                    current = current.previous
                } while (current != null)
            } else break
        }
        unvisited += buildList {
            val left = state.direction.rotate(LEFT)
            if (grid[state.point.move(left)] != '#') {
                add(State(state.point, left, state.cost + 1000, state))
            }
            val right = state.direction.rotate(RIGHT)
            if (grid[state.point.move(right)] != '#') {
                add(State(state.point, right, state.cost + 1000, state))
            }
            if (grid[state.point.move(state.direction)] != '#') {
                add(State(state.point.move(state.direction), state.direction, state.cost + 1, state))
            }
        }.filter { (it.point to it.direction) !in visited }
    }
    bestPoints.size
}

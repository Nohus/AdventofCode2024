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
    data class Path(
        val state: State,
        val cost: Int,
        val previous: List<State>,
    )

    val visited = mutableSetOf<State>()
    val unvisited = PriorityQueue<Path>(compareBy { it.cost })
    unvisited += Path(State(start, Direction.EAST), 0, emptyList())
    val bestPoints = mutableSetOf(end)
    var bestCost = Int.MAX_VALUE
    while (unvisited.isNotEmpty()) {
        val (state, cost, path) = unvisited.remove().also { visited += it.state }
        if (state.point == end) {
            if (cost <= bestCost) {
                bestCost = cost
                bestPoints += path.map { it.point }
            } else break
        }
        val newPath = path + state
        unvisited += listOf(
            Path(State(state.point, state.direction.rotate(LEFT)), cost + 1000, newPath),
            Path(State(state.point, state.direction.rotate(RIGHT)), cost + 1000, newPath),
            Path(State(state.point.move(state.direction), state.direction), cost + 1, newPath),
        ).filter { it.state !in visited && grid[it.state.point] != '#' }
    }
    bestPoints.size
}

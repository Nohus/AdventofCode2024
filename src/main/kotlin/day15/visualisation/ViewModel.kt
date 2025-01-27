package day15.visualisation

import adventofcode2024.generated.resources.Res
import adventofcode2024.generated.resources.obstacle5
import adventofcode2024.generated.resources.wall_horizontal
import adventofcode2024.generated.resources.wall_north_west
import adventofcode2024.generated.resources.wall_south_east
import adventofcode2024.generated.resources.wall_vertical
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import utils.Direction
import utils.Point
import utils.getBounds
import utils.toGrid
import java.io.File

class ViewModel {

    data class State(
        val grid: Map<Point, Tile> = emptyMap(),
        val robot: Point = Point(0, 0),
        val robotDirection: Direction = Direction.EAST,
        val markedBoxes: List<Point> = emptyList(),
        val moves: List<Direction> = emptyList(),
    )

    sealed interface Tile {
        data object Empty : Tile
        data object Box : Tile
        data class Wall(val drawable: DrawableResource) : Tile
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job)
    private val input = File("src/main/kotlin/day15/visualisation/input_vis.txt").readText()
    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    init {
        setup()
        _state.update { it.copy(moves = emptyList()) }
        solve()
    }

    fun onKeyEvent(event: KeyEvent) {
        if (event.type != KeyEventType.KeyDown) return
        if (event.key == Key.Escape) {
            setup()
        } else if (event.key == Key.DirectionUp) {
            _state.update { it.copy(moves = listOf(Direction.NORTH)) }
        } else if (event.key == Key.DirectionDown) {
            _state.update { it.copy(moves = listOf(Direction.SOUTH)) }
        } else if (event.key == Key.DirectionLeft) {
            _state.update { it.copy(moves = listOf(Direction.WEST)) }
        } else if (event.key == Key.DirectionRight) {
            _state.update { it.copy(moves = listOf(Direction.EAST)) }
        }
    }

    private fun setup() {
        val (gridText, movesText) = input.split("\n\n")
        val textGrid = gridText.lines().toGrid().toMutableMap()
        val robot = textGrid.entries.first { it.value == '@' }.key.also { textGrid[it] = '.' }
        val bounds = textGrid.getBounds()

        val grid = textGrid.mapValues { (point, value) ->
            when (value) {
                'O' -> Tile.Box
                '.' -> Tile.Empty
                '#' -> {
                    val drawable = when {
                        point == Point(0, 0) -> Res.drawable.wall_north_west
                        point.x == bounds.first - 1 && point.y == bounds.second - 1 -> Res.drawable.wall_south_east
                        point.x == 0 -> Res.drawable.wall_vertical
                        point.y == 0 -> Res.drawable.wall_horizontal
                        point.x == bounds.first - 1 -> Res.drawable.wall_vertical
                        point.y == bounds.second - 1 -> Res.drawable.wall_horizontal
                        else -> Res.drawable.obstacle5
                    }
                    Tile.Wall(drawable)
                }
                else -> throw IllegalArgumentException("Unexpected tile value $value")
            }
        }
        val moves = movesText.lines().joinToString("").map { Direction.of(it) }
        _state.update {
            it.copy(grid = grid, robot = robot, moves = moves)
        }
    }

    private fun solve() {
        scope.launch {
            while (true) {
                if (state.value.moves.isEmpty()) {
                    delay(50)
                }
                val direction = state.value.moves.firstOrNull() ?: continue
                val next = state.value.robot.move(direction)
                _state.update { it.copy(robotDirection = direction) }
                if (push(next, direction)) _state.update { it.copy(robot = next) }
                _state.update { it.copy(markedBoxes = emptyList(), moves = it.moves.drop(1)) }
                delay(200)
            }
        }
    }

    private suspend fun push(pos: Point, direction: Direction): Boolean {
        delay(500)
        _state.update { it.copy(markedBoxes = it.markedBoxes + pos) }
        if (state.value.grid[pos] is Tile.Empty) return true
        if (state.value.grid[pos] is Tile.Wall) return false
        val next = pos.move(direction)
        if (push(next, direction)) {
            val grid = state.value.grid.toMutableMap()
            grid[pos] = Tile.Empty
            grid[next] = Tile.Box
            _state.update { it.copy(grid = grid) }
            return true
        } else return false
    }
}

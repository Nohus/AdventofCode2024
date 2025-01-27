package day15.visualisation

import adventofcode2024.generated.resources.Res
import adventofcode2024.generated.resources.crate
import adventofcode2024.generated.resources.crate_marked
import adventofcode2024.generated.resources.icon
import adventofcode2024.generated.resources.robot_horizontal
import adventofcode2024.generated.resources.robot_vertical
import adventofcode2024.generated.resources.stone_tile_1
import adventofcode2024.generated.resources.stone_tile_2
import adventofcode2024.generated.resources.stone_tile_3
import adventofcode2024.generated.resources.stone_tile_4
import adventofcode2024.generated.resources.stone_tile_5
import adventofcode2024.generated.resources.stone_tile_6
import adventofcode2024.generated.resources.stone_tile_7
import adventofcode2024.generated.resources.stone_tile_8
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import day15.visualisation.ViewModel.State
import day15.visualisation.ViewModel.Tile.Box
import day15.visualisation.ViewModel.Tile.Empty
import day15.visualisation.ViewModel.Tile.Wall
import org.jetbrains.compose.resources.painterResource
import utils.Direction
import utils.Point
import utils.getBounds

fun main() = application {
    Window(
        state = WindowState(width = 1200.dp, height = 800.dp),
        onCloseRequest = ::exitApplication,
        title = "Advent of Code 2024: Day 15 Part 1",
        icon = painterResource(Res.drawable.icon)
    ) {
        val viewModel = remember { ViewModel() }
        val state by viewModel.state.collectAsState()

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .focusable()
                .onKeyEvent { event ->
                    viewModel.onKeyEvent(event)
                    return@onKeyEvent true
                }
        ) {
            Grid(state)
            Column {
                if (state.moves.isNotEmpty()) {
                    Text(
                        text = "Moving:",
                        style = MaterialTheme.typography.h3
                    )
                    Text(
                        text = "${state.moves.first()}",
                        style = MaterialTheme.typography.h1
                    )
                }
                Text(
                    text = "Next:",
                    style = MaterialTheme.typography.h3
                )
                state.moves.drop(1).forEach { move ->
                    Text(
                        text = "$move",
                        style = MaterialTheme.typography.h3
                    )
                }
            }
        }
    }
}

private val tileSize = 32.dp

@Composable
private fun Grid(state: State) {
    val (width, height) = state.grid.getBounds()
    val scale = 2f
    Box(Modifier
        .padding(tileSize * scale)
        .width(tileSize * (width) * scale)
        .height(tileSize * (height) * scale)
    ) {
        Box(Modifier.scale(scale)) {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    Image(
                        painter = getStoneTile(x, y),
                        contentDescription = null,
                        modifier = Modifier.offset(x = tileSize * x, y = tileSize * y)
                    )
                    val point = Point(x, y)
                    when (val tile = state.grid[point]) {
                        Box -> Image(
                            painter = painterResource(if (point in state.markedBoxes) Res.drawable.crate_marked else Res.drawable.crate),
                            contentDescription = null,
                            modifier = Modifier.offset(x = tileSize * x, y = tileSize * y)
                        )
                        is Wall -> Image(
                            painter = painterResource(tile.drawable),
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = tileSize * x, y = tileSize * y)
                                .offset(x = -tileSize, y = -tileSize)
                        )
                        Empty -> {}
                        null -> {}
                    }
                }
            }
            Robot(state)
        }
    }
}

@Composable
private fun Robot(state: State) {
    val drawable = when (state.robotDirection) {
        Direction.NORTH -> Res.drawable.robot_vertical
        Direction.EAST -> Res.drawable.robot_horizontal
        Direction.SOUTH -> Res.drawable.robot_vertical
        Direction.WEST -> Res.drawable.robot_horizontal
    }
    val x = state.robot.x
    val y = state.robot.y
    val animatedX by animateFloatAsState(x.toFloat())
    val animatedY by animateFloatAsState(y.toFloat())
    Image(
        painter = painterResource(drawable),
        contentDescription = null,
        modifier = Modifier
            .offset(x = tileSize * animatedX, y = tileSize * animatedY)
    )
}

@Composable
private fun getStoneTile(x: Int, y: Int): Painter {
    val variant = getSpriteIndex(4, 2, Point(x, y))
    val drawable = when (variant) {
        0 -> Res.drawable.stone_tile_1
        1 -> Res.drawable.stone_tile_2
        2 -> Res.drawable.stone_tile_3
        3 -> Res.drawable.stone_tile_4
        4 -> Res.drawable.stone_tile_5
        5 -> Res.drawable.stone_tile_6
        6 -> Res.drawable.stone_tile_7
        7 -> Res.drawable.stone_tile_8
        else -> throw IllegalStateException()
    }
    return painterResource(drawable)
}

private fun getSpriteIndex(patternWidth: Int, patternHeight: Int, mapCoordinate: Point): Int {
    val x = (mapCoordinate.x + 1) % patternWidth
    val y = (mapCoordinate.y + 1) % patternHeight
    val xPattern = (x + (patternWidth - 1)) % patternWidth
    val yPattern = (y + (patternHeight - 1)) % patternHeight
    return yPattern * patternWidth + xPattern
}

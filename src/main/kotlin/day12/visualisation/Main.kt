package day12.visualisation

import adventofcode2024.generated.resources.Res
import adventofcode2024.generated.resources.icon
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import day12.visualisation.ViewModel.PlotStatus
import day12.visualisation.ViewModel.Region
import day12.visualisation.ViewModel.State
import day12.visualisation.ViewModel.Status
import org.jetbrains.compose.resources.painterResource
import utils.Direction
import utils.Point
import utils.getBounds

fun main() = application {
    Window(
        state = WindowState(width = 1200.dp, height = 800.dp),
        onCloseRequest = ::exitApplication,
        title = "Advent of Code 2024: Day 12",
        icon = painterResource(Res.drawable.icon)
    ) {
        val viewModel = remember { ViewModel() }
        val state by viewModel.state.collectAsState()

        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F0F23))
                .padding(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                StatusText(state.status)
                Grid(state, Modifier.weight(1f))
            }
            Regions(
                regions = state.regions,
            )
            Sidebar(
                regions = state.regions,
                onStartClick = viewModel::onStartClick,
                onAreasClick = viewModel::onAreasClick,
                onPerimetersClick = viewModel::onPerimetersClick,
                onBordersClick = viewModel::onBordersClick,
                onSpeedUp = viewModel::onSpeedUp,
            )
        }
    }
}

@Composable
private fun StatusText(status: Status) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        when (status) {
            Status.Idle -> {
                Text(
                    text = "",
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFFFFFFF),
                    modifier = Modifier
                        .padding(2.dp)
                )
            }
            is Status.MappingRegion -> {
                Text(
                    text = "Mapping region ${status.regionId}",
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFFFFFFF),
                    modifier = Modifier
                        .background(Theme.getRegionColor(status.regionId))
                        .padding(2.dp)
                )
            }
            Status.RegionsMapped -> {
                Text(
                    text = "All regions mapped",
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFFFFFFF),
                    modifier = Modifier
                        .padding(2.dp)
                )
            }
            is Status.Area -> {
                Text(
                    text = "Counting area of region ${status.regionId}",
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFFFFFFF),
                    modifier = Modifier
                        .background(Theme.getRegionColor(status.regionId))
                        .padding(2.dp)
                )
            }
            is Status.Border -> Text(
                text = "Counting ${status.direction} borders of region ${status.regionId}",
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFFFFF),
                modifier = Modifier
                    .background(Theme.getRegionColor(status.regionId))
                    .padding(2.dp)
            )
            is Status.Perimeter -> Text(
                text = "Counting perimeter of region ${status.regionId}",
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFFFFF),
                modifier = Modifier
                    .background(Theme.getRegionColor(status.regionId))
                    .padding(2.dp)
            )
        }
    }
}

@Composable
private fun Grid(state: State, modifier: Modifier) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val (width, height) = remember(state.grid) { state.grid.getBounds() }
        val plotWidth = maxWidth / width
        val plotHeight = maxHeight / height
        val plotSize = minOf(plotWidth, plotHeight)
        Column(
            modifier = Modifier
                .border(1.dp, Color(0xFF333340))
                .padding(2.dp)
                .background(Color(0xFF10101a))
        ) {
            for (y in 0..<height) {
                Row {
                    for (x in 0..<width) {
                        val point = Point(x, y)
                        val plot = state.grid[point] ?: continue
                        var color = when (plot.status) {
                            PlotStatus.Unassigned -> Color(0xFF10101a)
                            is PlotStatus.Queued -> {
                                Theme.getRegionColor(plot.status.regionId).copy(alpha = 0.2f)
                            }
                            is PlotStatus.Assigned -> {
                                Theme.getRegionColor(plot.status.regionId)
                            }
                        }
                        if (state.highlightedRegion != null && (plot.status as? PlotStatus.Assigned)?.regionId != state.highlightedRegion) {
                            color = color.copy(alpha = 0.2f)
                        }
                        val animatedColor by animateColorAsState(color, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        Box(
                            modifier = Modifier
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .modifyIf(point == state.current) {
                                        border(2.dp, Color.White)
                                    }
                                    .drawWithContent {
                                        drawContent()
                                        val colors = arrayOf(0f to Color.Transparent, 0.8f to Color.Transparent, 1f to Color.White)
                                        if (Direction.NORTH in plot.borders) {
                                            val start = Offset(size.width / 2, size.height / 2)
                                            val end = Offset(size.width / 2, 0f)
                                            drawLine(
                                                brush = Brush.linearGradient(colorStops = colors, start = start, end = end),
                                                start = start,
                                                end = end,
                                                strokeWidth = size.width,
                                            )
                                        }
                                        if (Direction.SOUTH in plot.borders) {
                                            val start = Offset(size.width / 2, size.height / 2)
                                            val end = Offset(size.width / 2, size.height)
                                            drawLine(
                                                brush = Brush.linearGradient(colorStops = colors, start = start, end = end),
                                                start = start,
                                                end = end,
                                                strokeWidth = size.width,
                                            )
                                        }
                                        if (Direction.WEST in plot.borders) {
                                            val start = Offset(size.width / 2, size.height / 2)
                                            val end = Offset(0f, size.height / 2)
                                            drawLine(
                                                brush = Brush.linearGradient(colorStops = colors, start = start, end = end),
                                                start = start,
                                                end = end,
                                                strokeWidth = size.height,
                                            )
                                        }
                                        if (Direction.EAST in plot.borders) {
                                            val start = Offset(size.width / 2, size.height / 2)
                                            val end = Offset(size.width, size.height / 2)
                                            drawLine(
                                                brush = Brush.linearGradient(colorStops = colors, start = start, end = end),
                                                start = start,
                                                end = end,
                                                strokeWidth = size.height,
                                            )
                                        }
                                    }
                                    .size(plotSize)
                                    .background(animatedColor)
                            ) {
                                Text(
                                    text = plot.textOverride ?: plot.name.toString(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFCCCCCC)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Regions(
    regions: List<Region>,
) {
    Column(
        modifier = Modifier.widthIn(min = 150.dp)
    ) {
        Text(
            text = "Regions",
            fontFamily = FontFamily.Monospace,
            color = Color(0xFFCCCCCC)
        )
        regions.forEach { region ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Theme.getRegionColor(region.id))
                        .size(10.dp)
                )
                val hasDuplicates = regions.count { it.name == region.name } > 1
                Text(
                    text = "${region.name}${if (hasDuplicates) "${region.id}" else ""}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    color = Color(0xFFCCCCCC),
                    modifier = Modifier.widthIn(min = 30.dp)
                )
                if (region.area != null) {
                    Text(
                        text = " A=${region.area}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 18.sp,
                        color = Color(0xFFFFFFFF)
                    )
                }
                if (region.perimeter != null) {
                    Text(
                        text = " P=${region.perimeter}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 18.sp,
                        color = Color(0xFFFFFFFF)
                    )
                }
                if (region.borders != null) {
                    Text(
                        text = " B=${region.borders}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 18.sp,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Sidebar(
    regions: List<Region>,
    onStartClick: () -> Unit,
    onAreasClick: () -> Unit,
    onPerimetersClick: () -> Unit,
    onBordersClick: () -> Unit,
    onSpeedUp: (Boolean) -> Unit,
) {
    Column {
        Text(
            text = "Actions",
            fontFamily = FontFamily.Monospace,
            color = Color(0xFFCCCCCC)
        )
        Link("1. Find regions", onStartClick)
        Link("2. Calculate areas", onAreasClick)
        Link("3. Calculate perimeters", onPerimetersClick)
        Link("4. Calculate borders", onBordersClick)
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Part 1",
            fontFamily = FontFamily.Monospace,
            color = Color(0xFFCCCCCC)
        )
        if (regions.isNotEmpty() && regions.all { it.area != null && it.perimeter != null }) {
            Text(
                text = regions.sumOf { it.area!! * it.perimeter!! }.toString(),
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp,
                color = Color(0xFFCCCCCC)
            )
        } else {
            Text(
                text = "–",
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp,
                color = Color(0xFFCCCCCC)
            )
        }
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Part 2",
            fontFamily = FontFamily.Monospace,
            color = Color(0xFFCCCCCC)
        )
        if (regions.isNotEmpty() && regions.all { it.area != null && it.borders != null }) {
            Text(
                text = regions.sumOf { it.area!! * it.borders!! }.toString(),
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp,
                color = Color(0xFFCCCCCC)
            )
        } else {
            Text(
                text = "–",
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp,
                color = Color(0xFFCCCCCC)
            )
        }
        Spacer(Modifier.height(16.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .onPointerEvent(PointerEventType.Enter) { onSpeedUp(true) }
                .onPointerEvent(PointerEventType.Exit) { onSpeedUp(false) }
                .border(1.dp, Color(0xFF333340))
                .background(Color(0xFF10101a))
                .padding(16.dp)
        ) {
            Text(
                text = "Hover to speed up",
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFCCCCCC)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Link(text: String, onClick: () -> Unit) {
    var color by remember { mutableStateOf(Color(0xFF009900)) }
    Text(
        text = text,
        fontFamily = FontFamily.Monospace,
        fontSize = 18.sp,
        color = color,
        modifier = Modifier
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onClick)
            .onPointerEvent(PointerEventType.Enter) { color = Color(0xFF99ff99) }
            .onPointerEvent(PointerEventType.Exit) { color = Color(0xFF009900) }
    )
}

fun Modifier.modifyIf(condition: Boolean, modifier: @Composable Modifier.() -> Modifier): Modifier = composed {
    if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

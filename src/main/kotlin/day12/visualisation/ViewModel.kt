package day12.visualisation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.Direction
import utils.Point
import utils.Turn.LEFT
import utils.Turn.RIGHT
import utils.toGrid
import java.io.File

class ViewModel {
    private val grid = File("src/main/kotlin/day12/visualisation/input_vis.txt").readLines().toGrid()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job)
    private var isSpeedUp = false

    data class State(
        val grid: Map<Point, Plot>,
        val status: Status = Status.Idle,
        val current: Point? = null,
        val highlightedRegion: Int? = null,
        val regions: List<Region> = emptyList(),
    )

    sealed class Status {
        data object Idle : Status()
        data class MappingRegion(val regionId: Int) : Status()
        data object RegionsMapped : Status()
        data class Area(val regionId: Int) : Status()
        data class Perimeter(val regionId: Int) : Status()
        data class Border(val regionId: Int, val direction: Direction) : Status()
    }

    data class Region(
        val id: Int,
        val name: Char,
        val area: Int? = null,
        val perimeter: Int? = null,
        val borders: Int? = null,
    )

    data class Plot(
        val name: Char,
        val textOverride: String? = null,
        val borders: List<Direction> = emptyList(),
        val status: PlotStatus = PlotStatus.Unassigned,
    )

    sealed class PlotStatus {
        data object Unassigned : PlotStatus()
        data class Queued(val regionId: Int) : PlotStatus()
        data class Assigned(val regionId: Int) : PlotStatus()
    }

    private val _state = MutableStateFlow(State(emptyMap()))
    val state = _state.asStateFlow()

    init {
        setup()
    }

    fun onStartClick() {
        setup()
        scope.launch {
            val regionMapping = mutableMapOf<Point, Int>()
            var regionId = -1
            for ((point, plant) in grid) {
                updateState { copy(current = point) }
                if (point in regionMapping) continue
                regionId++
                updateState { copy(status = Status.MappingRegion(regionId)) }
                val toVisit = mutableListOf(point)
                updatePlot(point) { copy(status = PlotStatus.Queued(regionId)) }
                while (toVisit.isNotEmpty()) {
                    val current = toVisit.removeFirst()
                    if (grid[current] == plant) {
                        regionMapping[current] = regionId
                        updateState { copy(
                            regions = List(regionId + 1) { id ->
                                Region(id, grid[regionMapping.entries.first { it.value == id }.key]!!.name)
                            })
                        }
                        updatePlot(current) { copy(status = PlotStatus.Assigned(regionId)) }
                        toVisit += current.getNeighbors().filter { it !in toVisit && it !in regionMapping }.also {
                            it.forEach { updatePlot(it) { copy(status = PlotStatus.Queued(regionId)) } }
                        }
                    } else {
                        updatePlot(current) { copy(status = PlotStatus.Unassigned) }
                    }
                }
            }
            updateState { copy(current = null, status = Status.RegionsMapped) }
        }
    }

    fun onAreasClick() {
        job.cancelChildren()
        scope.launch {
            _state.update { it.copy(grid = it.grid.mapValues { (k, v) -> v.copy(textOverride = "") }) }
            val regionIds = _state.value.regions.map { it.id }
            regionIds.forEach { regionId ->
                updateState { copy(status = Status.Area(regionId)) }
                val points = _state.value.grid.entries
                    .filter { (it.value.status as? PlotStatus.Assigned)?.regionId == regionId }
                updateState { copy(highlightedRegion = regionId) }
                points.forEachIndexed { index, (point, plot) ->
                    updateState(withDelay = false) { copy(current = point) }
                    _state.update { it.copy(grid = it.grid.mapValues { (k, v) ->
                        if ((v.status as? PlotStatus.Assigned)?.regionId == regionId) {
                            v.copy(textOverride = "")
                        } else {
                            v
                        }
                    }) }
                    updatePlot(point) { copy(textOverride = "${index + 1}") }
                }
                updateState { copy(
                    regions = regions.map { if (it.id == regionId) it.copy(area = points.size) else it }
                ) }
            }
            updateState { copy(highlightedRegion = null, current = null, status = Status.Idle) }
        }
    }

    fun onPerimetersClick() {
        job.cancelChildren()
        scope.launch {
            _state.update { it.copy(grid = it.grid.mapValues { (k, v) -> v.copy(textOverride = "") }) }
            val regionIds = _state.value.regions.map { it.id }
            regionIds.forEach { regionId ->
                updateState { copy(status = Status.Perimeter(regionId)) }
                val entries = _state.value.grid.entries
                    .filter { (it.value.status as? PlotStatus.Assigned)?.regionId == regionId }
                val points = entries.map { it.key }
                updateState { copy(highlightedRegion = regionId) }
                var perimeterTotal = 0
                entries.forEach { (point, plot) ->
                    updateState(withDelay = false) { copy(current = point) }
                    val perimeter = 4 - (point.getNeighbors().count { it in points })
                    perimeterTotal += perimeter
                    updatePlot(point) {
                        if (perimeter > 0) copy(textOverride = "$perimeter")
                        else copy(textOverride = "")
                    }
                }
                updateState { copy(
                    regions = regions.map { if (it.id == regionId) it.copy(perimeter = perimeterTotal) else it }
                ) }
            }
            updateState { copy(highlightedRegion = null, current = null, status = Status.Idle) }
        }
    }

    fun onBordersClick() {
        job.cancelChildren()
        scope.launch {
            _state.update { it.copy(grid = it.grid.mapValues { (k, v) -> v.copy(textOverride = "") }) }
            val regionIds = _state.value.regions.map { it.id }
            regionIds.forEach { regionId ->
                val entries = _state.value.grid.entries
                    .filter { (it.value.status as? PlotStatus.Assigned)?.regionId == regionId }
                val points = entries.map { it.key }
                updateState { copy(highlightedRegion = regionId) }
                val allBorders = Direction.entries.sumOf { direction ->
                    updateState { copy(status = Status.Border(regionId, direction)) }
                    var borderCount = 0
                    val visitedPoints = mutableSetOf<Point>()
                    for ((point, plot) in entries) {
                        updateState { copy(current = point) }
                        if (point in visitedPoints) continue
                        val isBorder = _state.value.grid[point.move(direction)]?.regionId != regionId
                        if (isBorder) {
                            updatePlot(point) { copy(borders = borders + direction, textOverride = direction.toString()) }
                            borderCount++
                            visitedPoints += point
                            listOf(LEFT, RIGHT).map { direction.rotate(it) }.forEach { sideDirection ->
                                var current = point
                                do {
                                    current = current.move(sideDirection)
                                    visitedPoints += current
                                    if (_state.value.grid[current]?.regionId == regionId && _state.value.grid[current.move(direction)]?.regionId != regionId) {
                                        updatePlot(current) { copy(borders = borders + direction, textOverride = direction.toString()) }
                                    }
                                } while (_state.value.grid[current]?.regionId == regionId && _state.value.grid[current.move(direction)]?.regionId != regionId)
                            }
                        }
                    }
                    _state.update { it.copy(grid = it.grid.mapValues { (k, v) -> v.copy(textOverride = "") }) }
                    borderCount
                }
                updateState { copy(
                    regions = regions.map { if (it.id == regionId) it.copy(borders = allBorders) else it }
                ) }
            }
            updateState { copy(highlightedRegion = null, current = null, status = Status.Idle) }
        }
    }

    fun onSpeedUp(enabled: Boolean) {
        isSpeedUp = enabled
    }

    private fun setup() {
        job.cancelChildren()
        _state.value = State(grid.mapValues { Plot(it.value) })
    }

    private suspend fun updateState(withDelay: Boolean = true, update: State.() -> State) {
        _state.update(update)
        if (withDelay) wait()
    }

    private suspend fun updatePlot(point: Point, update: Plot.() -> Plot) {
        val existing = _state.value.grid[point] ?: return
        _state.update { it.copy(grid = it.grid + (point to existing.update())) }
        wait()
    }

    private suspend fun wait() {
        val delay = 10_000L / grid.size
        val millis = if (isSpeedUp) delay / 10 else delay
        delay(millis)
    }

    private val Plot.regionId: Int? get() {
        return (this.status as? PlotStatus.Assigned)?.regionId
    }
}

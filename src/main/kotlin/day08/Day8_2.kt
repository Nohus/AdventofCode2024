package day08

import solve
import utils.Point
import utils.getBounds
import utils.toGrid

fun main() = solve { lines ->
    val grid = lines.toGrid()
    val bounds = grid.getBounds()
    val antennas = grid.values.distinct() - '.'
    antennas.flatMap { antenna ->
        val positions = grid.filterValues { it == antenna }.keys
        positions.flatMap { start ->
            (positions - start).map { end ->
                generateSequence(start) { it + (end - start) }
                    .takeWhile { it.withinBounds(bounds) }.toList()
            }.flatten()
        }
    }.distinct().size
}

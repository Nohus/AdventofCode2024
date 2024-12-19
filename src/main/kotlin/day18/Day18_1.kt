package day18

import solve
import utils.Direction
import utils.Point
import utils.findInts
import java.util.PriorityQueue

fun main() = solve { lines ->
    val points = lines.map { it.findInts().let { Point(it[0], it[1])} }
    val start = Point(0,0)
    val exit = Point(70,70)
    val grid = mutableMapOf<Point, Boolean>()
    points.take(1024).forEach { grid[it] = true }

    val visited = mutableSetOf<Point>()
    val unvisited = PriorityQueue<Pair<Point, Int>>(compareBy { it.second })
    unvisited += start to 0
    while (unvisited.isNotEmpty()) {
        val (current, cost) = unvisited.remove().also { visited += it.first }
        if (current == exit) return@solve cost
        unvisited += Direction.entries.map { direction -> current.move(direction) to (cost + 1) }
            .filter { it.first !in visited && grid[it.first] != true && it.first.withinBounds(71 to 71) && it !in unvisited }
    }
}

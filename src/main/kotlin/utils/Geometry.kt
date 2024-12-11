package utils

import utils.Direction.*
import utils.Turn.*
import kotlin.math.*

data class PointL(val x: Long, val y: Long) {

    companion object {
        val ORIGIN = PointL(0, 0)
    }

    operator fun plus(other: PointL): PointL {
        return PointL(x + other.x, y + other.y)
    }

    operator fun minus(other: PointL): PointL {
        return PointL(x - other.x, y - other.y)
    }

    operator fun times(other: Int): PointL {
        return PointL(x * other, y * other)
    }

    fun move(direction: Direction, distance: Long = 1) = when (direction) {
        EAST -> PointL(x + distance, y)
        WEST -> PointL(x - distance, y)
        NORTH -> PointL(x, y - distance)
        SOUTH -> PointL(x, y + distance)
    }
}

data class PointF(val x: Double, val y: Double)

data class Point(val x: Int, val y: Int) {

    companion object {
        val ORIGIN = Point(0, 0)
    }

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun minus(other: Point): Point {
        return Point(x - other.x, y - other.y)
    }

    operator fun times(other: Int): Point {
        return Point(x * other, y * other)
    }

    fun move(direction: Direction, distance: Int = 1) = when (direction) {
        EAST -> Point(x + distance, y)
        WEST -> Point(x - distance, y)
        NORTH -> Point(x, y - distance)
        SOUTH -> Point(x, y + distance)
    }

    fun move(direction: Direction8, distance: Int = 1) = when (direction) {
        Direction8.NORTH -> Point(x, y - distance)
        Direction8.NORTH_EAST -> Point(x + distance, y - distance)
        Direction8.EAST -> Point(x + distance, y)
        Direction8.SOUTH_EAST -> Point(x + distance, y + distance)
        Direction8.SOUTH -> Point(x, y + distance)
        Direction8.SOUTH_WEST -> Point(x - distance, y + distance)
        Direction8.WEST -> Point(x - distance, y)
        Direction8.NORTH_WEST -> Point(x - distance, y - distance)
    }

    fun manhattan(other: Point) = abs(x - other.x) + abs(y - other.y)

    fun distance(other: Point) = sqrt((x - other.x).toDouble().pow(2) + (y - other.y).toDouble().pow(2))

    fun getAdjacentSides(): List<Point> = listOf(
        Point(x, y - 1), Point(x - 1, y), Point(x + 1, y), Point(x, y + 1),
    )

    fun getAdjacent(): List<Point> = listOf(
        Point(x - 1, y - 1), Point(x, y - 1), Point(x + 1, y - 1),
        Point(x - 1, y), Point(x + 1, y),
        Point(x - 1, y + 1), Point(x, y + 1), Point(x + 1, y + 1),
    )

    fun isAdjacentSide(other: Point) = manhattan(other) == 1

    fun angleTo(other: Point): Double {
        val angle = Math.toDegrees(atan2((other.y - y).toDouble(), (other.x - x).toDouble())) + 90
        return if (angle >= 0) angle else angle + 360
    }

    fun withinBounds(width: Int, height: Int) = x in 0 until width && y in 0 until height

    fun withinBounds(size: Pair<Int, Int>) = x in 0 until size.first && y in 0 until size.second
}

fun <T> List<String>.toGrid(transform: (Char) -> T): Map<Point, T> {
    return mutableMapOf<Point, T>().also {
        forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                it[Point(x, y)] = transform(char)
            }
        }
    }
}

fun List<String>.toGrid(): Map<Point, Char> {
    return toGrid { it }
}

fun <T> Map<Point, T>.printArea(visualization: (T) -> Char = { it.toString()[0] }) {
    val xRange = keys.minOf { it.x }..keys.maxOf { it.x }
    val yRange = keys.minOf { it.y }..keys.maxOf { it.y }
    for (y in yRange) {
        for (x in xRange) {
            val value = get(Point(x, y))
            if (value != null) {
                print(visualization(value))
            } else {
                print(" ")
            }
        }
        println()
    }
}

fun <T> Map<Point, T>.getWidth(): Int {
    return maxOf { it.key.x } + 1
}

fun <T> Map<Point, T>.getHeight(): Int {
    return maxOf { it.key.y } + 1
}

fun <T> Map<Point, T>.getXRange(): IntRange {
    return 0..<getWidth()
}

fun <T> Map<Point, T>.getYRange(): IntRange {
    return 0..<getHeight()
}

fun <T> Map<Point, T>.getBounds(): Pair<Int, Int> {
    return getWidth() to getHeight()
}

@JvmName("printAreaBoolean")
fun Map<Point, Boolean>.printArea() {
    printArea { if (it) '█' else ' ' }
}

@JvmName("printAreaChar")
fun Map<Point, Char>.printArea() {
    printArea { it }
}

data class Vector3L(val x: Long, val y: Long, val z: Long) {

}

data class Vector3(val x: Int, val y: Int, val z: Int) {

    companion object {
        val ORIGIN = Vector3(0, 0, 0)
    }

    operator fun plus(other: Vector3): Vector3 {
        return Vector3(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Vector3): Vector3 {
        return Vector3(x - other.x, y - other.y, z - other.z)
    }

    operator fun get(index: Int): Int = when (index) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IllegalArgumentException()
    }

    fun getAdjacent(): List<Vector3> {
        val adjacent = mutableListOf<Vector3>()
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    if (x == 0 && y == 0 && z == 0) continue
                    adjacent += (Vector3(x, y, z) + this)
                }
            }
        }
        return adjacent
    }

    fun getAdjacentSides(): List<Vector3> {
        val adjacent = mutableListOf<Vector3>()
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    if (x == 0 && y == 0 && z == 0) continue
                    if (x.absoluteValue + y.absoluteValue + z.absoluteValue > 1) continue
                    adjacent += (Vector3(x, y, z) + this)
                }
            }
        }
        return adjacent
    }
}

typealias VectorN<T> = List<T>

fun VectorN<Int>.getAdjacent(): List<VectorN<Int>> {
    val adjacent = mutableListOf(this)
    for (dimension in indices) {
        adjacent += adjacent.flatMap {
            listOf(
                it.toMutableList().also { it[dimension] -= 1 },
                it.toMutableList().also { it[dimension] += 1 }
            )
        }
    }
    return adjacent.filterNot { it == this }
}

enum class Turn {
    RIGHT, LEFT;

    override fun toString() = when (this) {
        RIGHT -> "↻"
        LEFT -> "↺"
    }
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun rotate(turn: Turn, times: Int): Direction {
        return (0 until (times % 4)).toList().fold(this) { acc, _ -> acc.rotate(turn) }
    }

    fun rotate(turn: Turn): Direction {
        return if (turn == RIGHT) when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        } else when (this) {
            NORTH -> WEST
            EAST -> NORTH
            SOUTH -> EAST
            WEST -> SOUTH
        }
    }

    fun reverse() = when (this) {
        NORTH -> SOUTH
        SOUTH -> NORTH
        WEST -> EAST
        EAST -> WEST
    }

    override fun toString() = when (this) {
        NORTH -> "↑"
        EAST -> "→"
        SOUTH -> "↓"
        WEST -> "←"
    }
}

enum class Direction8 {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

    fun rotate(turn: Turn, times: Int): Direction8 {
        return (0 until (times % 4)).toList().fold(this) { acc, _ -> acc.rotate(turn) }
    }

    fun rotate(turn: Turn): Direction8 {
        return if (turn == RIGHT) when (this) {
            NORTH -> NORTH_EAST
            NORTH_EAST -> EAST
            EAST -> SOUTH_EAST
            SOUTH_EAST -> SOUTH
            SOUTH -> SOUTH_WEST
            SOUTH_WEST -> WEST
            WEST -> NORTH_WEST
            NORTH_WEST -> NORTH
        } else when (this) {
            NORTH -> NORTH_WEST
            NORTH_EAST -> NORTH
            EAST -> NORTH_EAST
            SOUTH_EAST -> EAST
            SOUTH -> SOUTH_EAST
            SOUTH_WEST -> SOUTH
            WEST -> SOUTH_WEST
            NORTH_WEST -> WEST
        }
    }

    fun reverse() = when (this) {
        NORTH -> SOUTH
        NORTH_EAST -> SOUTH_WEST
        EAST -> WEST
        SOUTH_EAST -> NORTH_WEST
        SOUTH -> NORTH
        SOUTH_WEST -> NORTH_EAST
        WEST -> EAST
        NORTH_WEST -> SOUTH_EAST
    }

    override fun toString() = when (this) {
        NORTH -> "↑"
        NORTH_EAST -> "↗"
        EAST -> "→"
        SOUTH_EAST -> "↘"
        SOUTH -> "↓"
        SOUTH_WEST -> "↙"
        WEST -> "←"
        NORTH_WEST -> "↖"
    }
}

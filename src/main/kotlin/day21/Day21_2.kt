package day21

import day21.DpadKey.*
import solve
import utils.Point
import utils.getPermutations

enum class DpadKey { UP, RIGHT, DOWN, LEFT, ACTIVATE }
typealias MoveSequence = List<DpadKey>
data class CacheKey(val fromButton: DpadKey, val toButton: DpadKey, val level: Int)
private val cache = mutableMapOf<CacheKey, Long>()

fun main() = solve { lines ->
    lines.sumOf { line ->
        getPossibleNumpadMoves(line.toList()).minOf { moves ->
            var currentPosition = ACTIVATE
            moves.sumOf { move ->
                getMovesCount(currentPosition, move, level = 1, maxLevel = 25).also { currentPosition = move }
            }
        } * line.filter { it.isDigit() }.toInt()
    }
}

private fun getMovesCount(fromButton: DpadKey, toButton: DpadKey, level: Int, maxLevel: Int): Long {
    cache[CacheKey(fromButton, toButton, level)]?.let { return it }
    val moves = getPossibleDpadMoves(listOf(toButton), fromButton)
    if (level == maxLevel) return moves.minOf { it.size.toLong() }
    return moves.minOf { sequence ->
        var currentKey = ACTIVATE
        sequence.sumOf { button ->
            getMovesCount(currentKey, button, level + 1, maxLevel).also { currentKey = button }
        }
    }.also { cache[CacheKey(fromButton, toButton, level)] = it }
}

private fun getPossibleNumpadMoves(code: List<Char>): List<MoveSequence> {
    val keyPositions = mapOf(
        '7' to Point(0, 0), '8' to Point(1, 0), '9' to Point(2, 0),
        '4' to Point(0, 1), '5' to Point(1, 1), '6' to Point(2, 1),
        '1' to Point(0, 2), '2' to Point(1, 2), '3' to Point(2, 2),
        '0' to Point(1, 3), 'A' to Point(2, 3),
    )
    return getPossibleKeypadMoves(keyPositions, code, 'A', Point(0, 3))
}

private fun getPossibleDpadMoves(moves: List<DpadKey>, currentKey: DpadKey): List<MoveSequence> {
    val keyPositions = mapOf(
        UP to Point(1, 0), ACTIVATE to Point(2, 0),
        LEFT to Point(0, 1), DOWN to Point(1, 1), RIGHT to Point(2, 1),
    )
    return getPossibleKeypadMoves(keyPositions, moves, currentKey, Point(0, 0))
}

private fun <T> getPossibleKeypadMoves(keyPositions: Map<T, Point>, keys: List<T>, initialKey: T, blankPosition: Point): List<MoveSequence> {
    return buildList {
        var currentPosition = keyPositions[initialKey]!!
        keys.forEach { key ->
            val targetPosition = keyPositions[key]!!
            buildList {
                repeat((targetPosition.x - currentPosition.x)) { add(RIGHT) }
                repeat((currentPosition.x - targetPosition.x)) { add(LEFT) }
                repeat((currentPosition.y - targetPosition.y)) { add(UP) }
                repeat((targetPosition.y - currentPosition.y)) { add(DOWN) }
            }.getPermutations().filter { permutation ->
                permutation.fold(currentPosition) { position, move ->
                    position.move(move).also { if (it == blankPosition) return@filter false }
                }
                true
            }.map { it + ACTIVATE }.let { add(it) }
            currentPosition = targetPosition
        }
    }.fold(listOf(emptyList())) { possibilities, choice ->
        choice.flatMap { option -> possibilities.map { it + option } }
    }
}

private fun Point.move(direction: DpadKey, distance: Int = 1) = when (direction) {
    RIGHT -> Point(x + distance, y)
    LEFT -> Point(x - distance, y)
    UP -> Point(x, y - distance)
    DOWN -> Point(x, y + distance)
    ACTIVATE -> error("Not a direction")
}

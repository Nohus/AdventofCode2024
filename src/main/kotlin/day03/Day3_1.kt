package day03

import solveRaw

fun main() = solveRaw { input ->
    """mul\((\d+),(\d+)\)""".toRegex().findAll(input).sumOf {
        it.groupValues[1].toInt() * it.groupValues[2].toInt()
    }
}

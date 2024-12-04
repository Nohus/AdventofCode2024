package day03

import solveRaw

fun main() = solveRaw { input ->
    var enabled = true
    """(mul\((\d+),(\d+)\)|do\(\)|don't\(\))""".toRegex().findAll(input).sumOf {
        if ("mul" in it.value) {
            if (enabled) return@sumOf it.groupValues[2].toInt() * it.groupValues[3].toInt()
        } else if ("'" in it.value) enabled = false
        else enabled = true
        0
    }
}

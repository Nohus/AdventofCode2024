package day04

import solve
import utils.Direction8
import utils.toGrid

fun main() = solve { lines ->
    val mapping = lines.toGrid()
    mapping.keys.sumOf { point ->
        Direction8.entries.count {
            List(4) { distance -> mapping[point.move(it, distance)] }.joinToString("") == "XMAS"
        }
    }
}

package day11

import solve
import utils.isEven
import utils.toLongs

fun main() = solve { lines ->
    var stones = lines.first().toLongs()
    repeat(25) {
        stones = stones.flatMap { stone ->
            val string = stone.toString()
            when {
                stone == 0L -> listOf(1)
                string.length.isEven() -> {
                    val n = string.length / 2
                    listOf(string.take(n).toLong(), string.drop(n).toLong())
                }
                else -> listOf(stone * 2024)
            }
        }
    }
    stones.size
}

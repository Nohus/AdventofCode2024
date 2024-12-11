package day07

import solve
import utils.toLongs

fun main() = solve { lines ->
    lines
        .map { it.split(": ").let { it[0].toLong() to it[1].toLongs() } }
        .filter { (target, numbers) -> target in evaluate1(0, numbers) }
        .sumOf { (target, _) -> target }
}

fun evaluate1(result: Long, remaining: List<Long>): List<Long> {
    if (remaining.isEmpty()) return listOf(result)
    return evaluate1(result + remaining.first(), remaining.drop(1)) +
            evaluate1(result * remaining.first(), remaining.drop(1))
}
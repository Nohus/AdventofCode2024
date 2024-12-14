package day07

import solve
import utils.splitLongs

fun main() = solve { lines ->
    lines
        .map { it.split(": ").let { it[0].toLong() to it[1].splitLongs() } }
        .filter { (target, numbers) -> target in evaluate(0, numbers) }
        .sumOf { (target, _) -> target }
}

fun evaluate(result: Long, remaining: List<Long>): List<Long> {
    if (remaining.isEmpty()) return listOf(result)
    return evaluate(result + remaining.first(), remaining.drop(1)) +
            evaluate(result * remaining.first(), remaining.drop(1)) +
            evaluate("$result${remaining.first()}".toLong(), remaining.drop(1))
}

package day11

import solve
import utils.isEven
import utils.toLongs
import kotlin.math.sign

val cache = mutableMapOf<Pair<Long, Int>, Long>()

fun main() = solve { lines ->
    lines.first().toLongs().sumOf { calculateCount(it, 75) }
}

fun blink(stone: Long): List<Long> = when {
    stone == 0L -> listOf(1)
    stone.toString().length.isEven() -> {
        val whole = stone.toString()
        listOf(whole.take(whole.length / 2).toLong(), whole.drop(whole.length / 2).toLong())
    }
    else -> listOf(stone * 2024)
}

fun calculateCount(stone: Long, iterations: Int): Long {
    if (iterations == 0) return 1
    cache[stone to iterations]?.let { return it }
    return blink(stone).sumOf { calculateCount(it, iterations - 1) }
        .also { cache[stone to iterations] = it }
}

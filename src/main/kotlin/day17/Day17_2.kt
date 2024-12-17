package day17

import solve
import utils.mapToInts
import kotlin.math.pow

fun main() = solve { lines ->
    val target = lines[4].substringAfter(": ").split(",").mapToInts()
    val unvisited = (0L..7L).map { listOf(it) }.toMutableList()
    while (unvisited.isNotEmpty()) {
        val next = unvisited.removeFirst()
        val a = next.reversed().reduceIndexed { index, acc, l -> acc + l * (8.0).pow(index).toLong() }
        val output = program(a)
        val isMatching = output.size == next.size && target.takeLast(output.size) == output
        if (isMatching) {
            if (output == target) return@solve a
            unvisited += (0L..7L).map { next + listOf(it) }
        }
    }
}

private fun program(a: Long): List<Int> {
    var a = a
    var b = 0L
    var c = 0L
    val output = mutableListOf<Long>()

    while (true) {
        b = a.mod(8L) xor 1L
        c = a shr b.toInt()
        b = b xor c
        b = b xor 0b100
        output += b.mod(8L)
        a /= 8
        if (a == 0L) break
    }

    return output.map { it.toInt() }
}

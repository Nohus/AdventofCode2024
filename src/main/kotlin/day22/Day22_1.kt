package day22

import solve
import utils.mapToLongs

fun main() = solve { lines ->
    lines.mapToLongs().sumOf { initial ->
        generateSequence(initial) { next(it) }.take(2001).last()
    }
}

private fun next(number: Long): Long {
    val a = (number * 64 xor number).mod(16777216L)
    val b = (a / 32 xor a).mod(16777216L)
    return (b * 2048 xor b).mod(16777216L)
}

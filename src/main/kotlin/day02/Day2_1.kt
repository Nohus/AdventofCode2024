package day02

import solve
import utils.toInts
import utils.toPair
import kotlin.math.abs

fun main() = solve { lines ->
    lines.map { it.toInts() }.count {
        val sorted = it.sorted()
        (it == sorted || it == sorted.reversed()) && it.windowed(2).all { (x, y) -> abs(x - y) in 1..3 }
    }
}

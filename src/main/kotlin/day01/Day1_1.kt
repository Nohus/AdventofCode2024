package day01

import solve
import utils.toInts
import utils.toPair
import kotlin.math.abs

fun main() = solve { lines ->
    val (a, b) = lines.map { it.toInts().toPair() }.unzip()
    a.sorted().zip(b.sorted()).sumOf { (x, y) -> abs(x - y) }
}

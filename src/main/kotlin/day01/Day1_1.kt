package day01

import solve
import utils.splitInts
import utils.toPair
import kotlin.math.abs

fun main() = solve { lines ->
    val (a, b) = lines.map { it.splitInts().toPair() }.unzip()
    a.sorted().zip(b.sorted()).sumOf { (x, y) -> abs(x - y) }
}

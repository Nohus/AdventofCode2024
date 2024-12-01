package day01

import solve
import kotlin.math.abs

fun main() = solve { lines ->
    val a = lines.map { it.substringBefore(" ").toInt() }.sorted()
    val b = lines.map { it.substringAfterLast(" ").toInt() }.sorted()
    a.zip(b).sumOf { (x, y) -> abs(x - y) }
}

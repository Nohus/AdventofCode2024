package day01

import solve
import utils.splitInts
import utils.toPair

fun main() = solve { lines ->
    val (a, b) = lines.map { it.splitInts().toPair() }.unzip()
    a.sumOf { x -> b.count { it == x } * x }
}

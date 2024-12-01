package day01

import solve
import utils.toInts
import utils.toPair

fun main() = solve { lines ->
    val (a, b) = lines.map { it.toInts().toPair() }.unzip()
    a.sumOf { x -> b.count { it == x } * x }
}

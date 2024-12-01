package day01

import solve

fun main() = solve { lines ->
    val a = lines.map { it.substringBefore(" ").toInt() }
    val b = lines.map { it.substringAfterLast(" ").toInt() }
    a.sumOf { x -> b.count { it == x } * x }
}

package day02

import solve
import utils.toInts
import utils.toPair
import kotlin.math.abs

fun main() = solve { lines ->
    lines.map { it.toInts() }.count {
        it.indices.any { index ->
            val new = it.take(index) + it.drop(index + 1)
            val sorted = new.sorted()
            (new == sorted || new == sorted.reversed()) && new.windowed(2).all { (x, y) -> abs(x - y) in 1..3 }
        }
    }
}

package day02

import solve
import utils.toInts
import utils.toPair
import utils.withoutIndex
import kotlin.math.abs

fun main() = solve { lines ->
    lines.map { it.toInts() }.count {
        it.indices.any { index ->
            val new = it.withoutIndex(index)
            val sorted = new.sorted()
            (new == sorted || new == sorted.reversed()) && new.windowed(2).all { (x, y) -> abs(x - y) in 1..3 }
        }
    }
}

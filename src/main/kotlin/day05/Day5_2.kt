package day05

import solveRaw
import utils.mapToInts
import utils.swap

fun main() = solveRaw { input ->
    val (r, u) = input.split("\n\n").map { it.lines() }
    val rules = r.map { it.split("|").mapToInts() }
    val updates = u.map { it.split(",").mapToInts() }

    updates.filterNot { update ->
        rules.filter { it.all { it in update } }.all { (a, b) -> update.indexOf(a) < update.indexOf(b) }
    }.sumOf { update ->
        val new = update.toMutableList()
        for (i in 0..(new.size / 2)) {
            for (j in i..new.lastIndex) {
                if (rules.any { (first, second) -> first == new[j] && second == new[i] }) new.swap(i, j)
            }
        }
        new[new.size / 2]
    }
}

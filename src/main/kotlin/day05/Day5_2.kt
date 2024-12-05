package day05

import solveRaw
import utils.mapToInts

fun main() = solveRaw { input ->
    val (r, u) = input.split("\n\n").map { it.lines() }
    val rules = r.map { it.split("|").mapToInts() }
    val updates = u.map { it.split(",").mapToInts() }

    fun isValid(update: List<Int>): Boolean {
        return rules.filter { it.all { it in update } }.all { (a, b) ->
            update.indexOf(a) < update.indexOf(b)
        }
    }

    updates.filterNot { isValid(it) }.sumOf { update ->
        val new = update.toMutableList()
        for (i in new.indices) {
            for (j in i..new.lastIndex) {
                val a = new[i]
                val b = new[j]
                if (rules.any { (first, second) -> first == b && second == a }) {
                    new[i] = b
                    new[j] = a
                }
            }
        }
        new[new.size / 2]
    }
}

package day05

import solveRaw
import utils.mapToInts

fun main() = solveRaw { input ->
    val (r, u) = input.split("\n\n").map { it.lines() }
    val rules = r.map { it.split("|").mapToInts() }
    val updates = u.map { it.split(",").mapToInts() }
    updates.filter { update ->
        rules.filter { it.all { it in update } }.all { (a, b) ->
            update.indexOf(a) < update.indexOf(b)
        }
    }.sumOf { it[it.size / 2] }
}

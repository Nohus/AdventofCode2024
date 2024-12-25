package day25

import solveRaw

fun main() = solveRaw { input ->
    val (locks, keys) = input.split("\n\n").map { block ->
        List(5) { index -> block.lines().count { it[index] == '#' } - 1 } to block.lines()[0].all { it == '#' }
    }.partition { (_, isLock) -> isLock }.let { it.first.map { it.first } to it.second.map { it.first } }
    locks.sumOf { lock ->
        keys.count { key ->
            (0..4).all { lock[it] + key[it] <= 5 }
        }
    }
}

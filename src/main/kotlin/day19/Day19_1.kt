package day19

import solve

fun main() = solve { lines ->
    val patterns = lines.first().split(", ")
    val designs = lines.drop(2)

    designs.count { design ->
        val unvisited = mutableListOf<String>()
        val visited = mutableListOf<String>()
        unvisited += patterns.filter { design.startsWith(it) }
        while (unvisited.isNotEmpty()) {
            val current = unvisited.maxBy { it.length }
            unvisited.remove(current)
            if (current.length == design.length) return@count true
            val toAdd = patterns.map { current + it }
                .filter { design.startsWith(it) }
                .filter { it !in visited }
            visited += toAdd
            unvisited += toAdd
        }
        false
    }
}

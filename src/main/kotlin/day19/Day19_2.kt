package day19

import solve

fun main() = solve { lines ->
    val patterns = lines.first().split(", ")
    val designs = lines.drop(2)
    val cache = mutableMapOf<String, Long>()

    fun options(design: String): Long {
        cache[design]?.let { return it }
        return patterns.filter { design.startsWith(it) }.sumOf { current ->
            options(design.removePrefix(current)) + if (current.length == design.length) 1 else 0
        }.also { cache[design] = it }
    }

    designs.sumOf { options(it) }
}

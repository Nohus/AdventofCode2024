package day23

import solve

fun main() = solve { lines ->
    val connections = mutableMapOf<String, List<String>>()
    lines.forEach { line ->
        val (a, b) = line.split("-")
        connections[a] = ((connections[a] ?: listOf()) + b)
        connections[b] = ((connections[b] ?: listOf()) + a)
    }
    connections.entries.sortedBy { it.key }.flatMap { (computer, connected) ->
        buildSet {
            connected.forEach { a ->
                connected.forEach { b ->
                    add(listOf(a, b).sorted())
                }
            }
        }.filter { set ->
            set.all { other ->
                val otherConnections = connections[other].orEmpty()
                ((set - other) + computer).all { it in otherConnections }
            }
        }.map { listOf(computer) + it }
    }.map { it.sorted() }.distinct().filter { it.any { it.startsWith("t") } }.size
}

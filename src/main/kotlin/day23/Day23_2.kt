package day23

import solve

fun main() = solve { lines ->
    val connections = mutableMapOf<String, List<String>>()
    lines.forEach { line ->
        val (a, b) = line.split("-")
        connections[a] = ((connections[a] ?: listOf()) + b)
        connections[b] = ((connections[b] ?: listOf()) + a)
    }
    val cache = mutableMapOf<List<String>, List<String>>()

    fun getBiggestSubnet(network: List<String>): List<String> {
        cache[network]?.let { return it }
        val isValid = network.all { computer ->
            (network - computer).all { it in connections[computer].orEmpty() }
        }
        return if (isValid) network else {
            network.map { computer -> network - computer }.map { getBiggestSubnet(it) }.maxBy { it.size }
        }.also { cache[network] = it }
    }

    connections.entries.map { (from, to) -> (listOf(from) + to).sorted() }
        .map { getBiggestSubnet(it) }.maxBy { it.size }.joinToString(",")
}

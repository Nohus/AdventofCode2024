package day24

import solveRaw

fun main() = solveRaw { input ->
    val (ia, ib) = input.split("\n\n")
    val wires = ia.lines().associate { line ->
        val (wire, state) = line.split(": ")
        wire to (state == "1")
    }.toMutableMap()

    data class Gate(
        val input1: String,
        val input2: String,
        val gate: String,
        val output: String,
    )

    val gates = ib.lines().map { line ->
        val (l1, output) = line.split(" -> ")
        val gate = when {
            "AND" in l1 -> "AND"
            "XOR" in l1 -> "XOR"
            "OR" in l1 -> "OR"
            else -> error("")
        }
        val (wire1, wire2) = l1.split(" $gate ")
        Gate(wire1, wire2, gate, output)
    }.toMutableList()

    while (gates.isNotEmpty()) {
        val gate = gates.first { it.input1 in wires && it.input2 in wires }
        gates -= gate
        val input1 = wires[gate.input1] ?: error("")
        val input2 = wires[gate.input2] ?: error("")
        val output = when (gate.gate) {
            "AND" -> input1 and input2
            "XOR" -> input1 xor input2
            "OR" -> input1 or input2
            else -> error("")
        }
        wires[gate.output] = output
    }

    wires.filter { it.key.startsWith("z") }.entries.sortedByDescending { it.key }.map { wire ->
        if (wire.value) "1" else "0"
    }.joinToString("").toLong(2)
}

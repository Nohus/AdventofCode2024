package day24

import solveRaw

data class Gate(
    val input1: String,
    val input2: String,
    val gate: String,
    val output: String,
) {
    override fun toString(): String {
        return "$input1 $gate $input2 -> $output"
    }
}

fun main() = solveRaw { input ->
    val (ia, ib) = input.split("\n\n")
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

    // Solved by hand

    // stq OR jss -> z07, this should output carry of bit 7, nqk
    // krv XOR btq -> nqk, this is xor output of bit 7, so z07

    // fth AND qcs -> z32, should output intermediate value for OR carry
    // fth XOR qcs -> srn, this is xor output of bit 32, so z32

    // x24 AND y24 -> z24, should be fpq
    // nmp XOR mqq -> fpq, should be output, z24

    // x17 XOR y17 -> pcp, should be fgt
    // y17 AND x17 -> fgt, should be pcp

    fun checkBit(index: Int) {
        val xInput = String.format("x%02d", index)
        val yInput = String.format("y%02d", index)
        val inputXor = gates.singleOrNull { it.gate == "XOR" && ((it.input1 == xInput && it.input2 == yInput) || (it.input1 == yInput && it.input2 == xInput)) }
        val inputAnd = gates.singleOrNull { it.gate == "AND" && ((it.input1 == xInput && it.input2 == yInput) || (it.input1 == yInput && it.input2 == xInput)) }
        if (inputXor != null && inputAnd != null) {
            val andCarry = gates.singleOrNull { it.gate == "AND" && (it.input1 == inputXor.output || it.input2 == inputXor.output) }
            if (andCarry == null) {
                println("Bit $index is missing AND carry gate")
            }
            val xorOutput = gates.singleOrNull { it.gate == "XOR" && (it.input1 == inputXor.output || it.input2 == inputXor.output) && it.output.startsWith("z") }
            if (xorOutput == null) {
                println("Bit $index is missing XOR output gate")
                println("Input XOR: $inputXor")
                println("Input AND: $inputAnd")
                println("AND input XOR with prev carry: $andCarry")
            }
            if (andCarry != null && xorOutput != null) {
                val orCarry = gates.singleOrNull { it.gate == "OR" && ((it.input1 == andCarry.output && it.input2 == inputAnd.output) || (it.input2 == andCarry.output && it.input1 == inputAnd.output)) }
                if (orCarry != null) {
                    if (index == -1 /* Was used to check neighbor adders of incorrect adders */) {
                        println("Correct adder of bit $index")
                        println("Input XOR: $inputXor")
                        println("Input AND: $inputAnd")
                        println("AND input XOR with prev carry: $andCarry")
                        println("XOR input XOR with prev carry to get output: $xorOutput")
                        println("OR carry: $orCarry")
                    }
                } else {
                    println("Bit $index is missing final OR gate")
                    println("Input XOR: $inputXor")
                    println("Input AND: $inputAnd")
                    println("AND input XOR with prev carry: $andCarry")
                    println("XOR input XOR with prev carry to get output: $xorOutput")
                }
            }
        } else {
            println("Bit $index is missing some input gates: $inputXor, $inputAnd")
        }
    }

    repeat(45) {
        checkBit(it)
    }

    "z07,nqk,z32,srn,z24,fpq,pcp,fgt".split(",").sorted().joinToString(",")
}

package day17

import solve
import utils.findInts
import utils.mapToInts
import kotlin.math.pow

fun main() = solve { lines ->
    var a = lines[0].findInts()[0]
    var b = lines[1].findInts()[0]
    var c = lines[2].findInts()[0]
    var ip = 0
    val program = lines[4].substringAfter(": ").split(",").mapToInts()
    val output = mutableListOf<Int>()

    while (true) {
        if (ip !in program.indices) break
        val instruction = program.subList(ip, ip + 2)
        val opcode = instruction[0]
        val operand = instruction[1]
        val comboOperand = when (operand) {
            in 0..3 -> operand
            4 -> a
            5 -> b
            6 -> c
            else -> 0
        }
        var jumped = false

        when (opcode) {
            0 -> a = (a / 2.0.pow(comboOperand.toDouble())).toInt()
            1 -> b = b xor operand
            2 -> b = comboOperand.mod(8)
            3 -> {
                if (a != 0) {
                    ip = operand
                    jumped = true
                }
            }
            4 -> b = b xor c
            5 -> output += comboOperand.mod(8)
            6 -> b = (a / 2.0.pow(comboOperand.toDouble())).toInt()
            7 -> c = (a / 2.0.pow(comboOperand.toDouble())).toInt()
        }
        if (!jumped) ip += 2
    }

    output.joinToString(",")
}

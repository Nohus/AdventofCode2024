package day13

import solveRaw
import utils.getInts

fun main() = solveRaw { input ->
    input.split("\n\n").sumOf { block ->
        val lines = block.lines()
        val (ax, ay) = """.*\+(\d+).*\+(\d+)""".toRegex().find(lines[0])!!.getInts()
        val (bx, by) = """.*\+(\d+).*\+(\d+)""".toRegex().find(lines[1])!!.getInts()
        val (px, py) = """.*=(\d+).*=(\d+)""".toRegex().find(lines[2])!!.getInts()

        var bestCost: Int? = null
        for (aAmount in 0..100) {
            for (bAmount in 0..100) {
                val x = aAmount * ax + bAmount * bx
                val y = aAmount * ay + bAmount * by
                val cost = aAmount * 3 + bAmount
                if (px == x && py == y && cost < (bestCost ?: Int.MAX_VALUE)) {
                    bestCost = cost
                }
            }
        }
        bestCost ?: 0
    }
}

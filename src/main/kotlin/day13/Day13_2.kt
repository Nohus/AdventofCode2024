package day13

import solveRaw
import utils.getInts

fun main() = solveRaw { input ->
    input.split("\n\n").sumOf { block ->
        val lines = block.lines()
        val (ax, ay) = """.*\+(\d+).*\+(\d+)""".toRegex().find(lines[0])!!.getInts()
        val (bx, by) = """.*\+(\d+).*\+(\d+)""".toRegex().find(lines[1])!!.getInts()
        val (px, py) = """.*=(\d+).*=(\d+)""".toRegex().find(lines[2])!!.getInts().map { it + 10000000000000L }

        val bAmount = (py * ax - px * ay) / (ax * by - ay * bx).toDouble()
        val aAmount = (px - bAmount * bx) / ax.toDouble()
        if (bAmount % 1.0 == 0.0 && aAmount % 1.0 == 0.0) { // Integer solutions
            aAmount.toLong() * 3 + bAmount.toLong()
        } else 0L
    }
}

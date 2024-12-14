package day14

import solve
import utils.Point
import utils.findInts
import utils.getInts

fun main() = solve { lines ->
    data class Robot(val pos: Point, val velocity: Point)
    var robots = lines.map { line ->
        val (x, y, vx, vy) = line.findInts()
        Robot(Point(x, y), Point(vx, vy))
    }
    val width = 101
    val height = 103

    var i = 0
    do {
        i++
        robots = robots.map { robot ->
            robot.copy(pos = Point(
                x = (robot.pos.x + robot.velocity.x + width) % width,
                y = (robot.pos.y + robot.velocity.y + height) % height
            ))
        }
    } while (robots.groupBy { it.pos }.any { it.value.size > 1 })
    i
}

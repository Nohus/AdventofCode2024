package day14

import solve
import solveRaw
import utils.Point
import utils.findInts
import utils.getInts
import utils.printArea

fun main() = solve { lines ->
    data class Robot(val pos: Point, val velocity: Point)
    var robots = lines.map { line ->
        val (x, y, vx, vy) = line.findInts()
        Robot(Point(x, y), Point(vx, vy))
    }
    val width = 101
    val height = 103

    repeat(100) {
        robots = robots.map { robot ->
            robot.copy(pos = Point(
                x = (robot.pos.x + robot.velocity.x + width) % width,
                y = (robot.pos.y + robot.velocity.y + height) % height
            ))
        }
    }

    val middleX = width / 2
    val middleY = height / 2
    val q1 = robots.count { it.pos.x < middleX && it.pos.y < middleY }
    val q2 = robots.count { it.pos.x > middleX && it.pos.y < middleY }
    val q3 = robots.count { it.pos.x < middleX && it.pos.y > middleY }
    val q4 = robots.count { it.pos.x > middleX && it.pos.y > middleY }
    q1 * q2 * q3 * q4
}

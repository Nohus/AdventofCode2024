package day09

import solve

fun main() = solve { lines ->
    val blocks = mutableListOf<Int?>()
    var fileId = 0
    lines.first().map { it.digitToInt() }.forEachIndexed { index, c ->
        repeat(c) { blocks += if (index % 2 == 0) fileId else null }
        if (index % 2 == 0) fileId++
    }

    while (true) {
        val freeIndex = blocks.indexOf(null)
        if (freeIndex !in 0..<blocks.lastIndex) break
        blocks[freeIndex] = blocks.removeLast()
    }
    blocks.foldIndexed(0L) { index: Int, acc: Long, fileId: Int? -> acc + (fileId ?: 0) * index }
}

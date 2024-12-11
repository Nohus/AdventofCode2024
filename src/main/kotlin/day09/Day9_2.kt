package day09

import solve
import utils.indexOf

fun main() = solve(additionalTiming = true) { lines ->
    val blocks = mutableListOf<Int?>()
    var fileId = -1
    lines.first().map { it.digitToInt() }.forEachIndexed { index, c ->
        if (index % 2 == 0) fileId++
        repeat(c) { blocks += if (index % 2 == 0) fileId else null }
    }

    while (fileId >= 0) {
        val fileIndex = blocks.indexOf(fileId)
        val fileSize = blocks.drop(fileIndex).takeWhile { it == fileId }.size
        val fitsAtIndex = blocks.indexOf(List(fileSize) { null })
        if (fitsAtIndex > -1 && fitsAtIndex < fileIndex) {
            repeat(fileSize) {
                blocks[fitsAtIndex + it] = fileId
                blocks[fileIndex + it] = null
            }
        }
        fileId--
    }
    blocks.foldIndexed(0L) { index: Int, acc: Long, fileId: Int? -> acc + (fileId ?: 0) * index }
}

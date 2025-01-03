package utils

fun List<String>.mapToInts(): List<Int> {
    return map { it.toInt() }
}

fun List<String>.mapToLongs(): List<Long> {
    return map { it.toLong() }
}

fun <T> List<T>.toPair(): Pair<T, T> {
    return this[0] to this[1]
}

fun <T> List<T>.withoutIndex(index: Int): List<T> {
    require(index >= 0) { "Index must be positive" }
    return take(index) + drop(index + 1)
}

fun <T> MutableList<T>.swap(indexA: Int, indexB: Int) {
    val a = this[indexA]
    this[indexA] = this[indexB]
    this[indexB] = a
}

fun <T> List<T>.indexOf(sublist: List<T>): Int {
    outer@for (i in 0..(lastIndex - sublist.lastIndex)) {
        for (j in 0..sublist.lastIndex) {
            if (this[i + j] != sublist[j]) continue@outer
        }
        return i
    }
    return -1
}

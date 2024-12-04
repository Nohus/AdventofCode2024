package utils

fun String.toWords(): List<String> {
    return split("\\s+".toRegex())
}

fun String.toInts(): List<Int> {
    return toWords().map { it.toInt() }
}

fun String.toLongs(): List<Long> {
    return toWords().map { it.toLong() }
}

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

fun MatchResult.getInts(): List<Int> {
    return groupValues.drop(1).mapToInts()
}

fun MatchResult.getLongs(): List<Long> {
    return groupValues.drop(1).mapToLongs()
}

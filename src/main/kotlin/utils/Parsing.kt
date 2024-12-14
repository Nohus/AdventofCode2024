package utils

fun String.splitWords(): List<String> {
    return split("\\s+".toRegex())
}

fun String.splitInts(): List<Int> {
    return splitWords().map { it.toInt() }
}

fun String.splitLongs(): List<Long> {
    return splitWords().map { it.toLong() }
}
}

fun MatchResult.getInts(): List<Int> {
    return groupValues.drop(1).mapToInts()
}

fun MatchResult.getLongs(): List<Long> {
    return groupValues.drop(1).mapToLongs()
}

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

fun MatchResult.getInts(): List<Int> {
    return groupValues.drop(1).mapToInts()
}

fun MatchResult.getLongs(): List<Long> {
    return groupValues.drop(1).mapToLongs()
}

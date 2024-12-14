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

fun String.findInts(): List<Int> {
    return """-?\d+""".toRegex().findAll(this).map { it.value.toInt() }.toList()
}

fun String.findLongs(): List<Long> {
    return """-?\d+""".toRegex().findAll(this).map { it.value.toLong() }.toList()
}

fun MatchResult.getInts(): List<Int> {
    return groupValues.drop(1).mapToInts()
}

fun MatchResult.getLongs(): List<Long> {
    return groupValues.drop(1).mapToLongs()
}

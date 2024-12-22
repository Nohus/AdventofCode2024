package day22

import solveSuspending
import utils.mapAsync
import utils.mapToLongs

fun main() = solveSuspending(additionalTiming = true) { lines ->
    val sequencesToPrices = lines.mapToLongs().mapAsync { initial ->
        generateSequence(initial) { next(it) }.take(2001).toList()
    }.mapAsync { secretNumbers ->
        secretNumbers.map { it % 10 }.windowed(2).map { (previousPrice, currentPrice) ->
            currentPrice to currentPrice - previousPrice
        }
    }.mapAsync { pricesWithChanges ->
        pricesWithChanges.windowed(4).reversed().associate { fourPricesWithChanges ->
            fourPricesWithChanges.map { it.second } to fourPricesWithChanges[3].first
        }
    }
    sequencesToPrices.flatMap { it.keys }.distinct().mapAsync { changeSequence ->
        sequencesToPrices.sumOf { it[changeSequence] ?: 0 }
    }.max()
}

private fun next(number: Long): Long {
    val a = (number * 64 xor number).mod(16777216L)
    val b = (a / 32 xor a).mod(16777216L)
    return (b * 2048 xor b).mod(16777216L)
}

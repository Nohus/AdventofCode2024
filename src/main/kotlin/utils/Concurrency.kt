package utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

context(CoroutineScope)
suspend fun <T, R> Iterable<T>.mapAsync(transform: (T) -> R): List<R> {
    return map { async { transform(it) } }.awaitAll()
}

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

private fun getInputFile(): File {
    val name = Throwable().stackTrace.first { it.className.contains("day") }.fileName
    val day = name.substringBefore("_").removePrefix("Day").padStart(2, '0')
    val part = name.substringAfter("_").removeSuffix(".kt")
    val file = File("src/main/kotlin/day$day/input$part.txt")
    return if (file.readText().isBlank()) {
        File("src/main/kotlin/day$day/input1.txt")
    } else file
}

private fun getInput(): String {
    return getInputFile().readText()
}

fun printInput(input: String) {
    if (input.contains("\n")) {
        if (input.lines().size >= 10) {
            println("Input:")
            println(input.lines().take(2).joinToString("\n"))
            println("[> not showing ${input.lines().size - 4} lines <]")
            println(input.lines().takeLast(2).joinToString("\n"))
        } else {
            println("Input:\n$input")
        }
    } else {
        println("Input: $input")
    }
}

fun solveRaw(
    additionalTiming: Boolean = false,
    trim: Boolean = true,
    solve: (String) -> Any?
) {
    val input = getInput().let { if (trim) it.trim() else it }
    printInput(input)
    solveRaw(input, additionalTiming, solve)
}

fun solve(
    additionalTiming: Boolean = false,
    trim: Boolean = true,
    solve: (List<String>) -> Any?
) {
    val input = getInput().let { if (trim) it.trim() else it }
    printInput(input)
    solveRaw(input.lines(), additionalTiming, solve)
}

fun solveRawSuspending(
    additionalTiming: Boolean = false,
    trim: Boolean = true,
    solve: suspend CoroutineScope.(String) -> Any?
) {
    val scope = CoroutineScope(Job())
    val input = getInput().let { if (trim) it.trim() else it }
    printInput(input)
    solveRaw(input, additionalTiming) { runBlocking { scope.solve(it) } }
}

fun solveSuspending(
    additionalTiming: Boolean = false,
    trim: Boolean = true,
    solve: suspend CoroutineScope.(List<String>) -> Any?
) {
    val scope = CoroutineScope(Job())
    val input = getInput().let { if (trim) it.trim() else it }
    printInput(input)
    solveRaw(input.lines(), additionalTiming) { runBlocking { scope.solve(it) } }
}

private fun <T> solveRaw(
    input: T,
    additionalTiming: Boolean = false,
    solve: (T) -> Any?
) {
    val (answer, duration) = measureTimedValue {
        solve(input).toString()
    }
    val time = "${String.format("%.3f", duration.inWholeMicroseconds / 1000.0)}ms"
    if (answer != "kotlin.Unit") {
        println("Out: $answer [$time]")
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(answer), null)
        Thread.sleep(200) // Wait so the system has chance to notice the clipboard change
    } else {
        println("No answer [$time]")
    }

    if (additionalTiming) {
        println("Rerunning for timing")
        val count = if (duration < 100.milliseconds) 1000 else 25
        val fastest = List(count) {
            measureTime { solve(input) }
        }.minOrNull()!!
        println("Fastest time: [${String.format("%.3f", fastest.inWholeMicroseconds / 1000.0)}ms]")
    }
}

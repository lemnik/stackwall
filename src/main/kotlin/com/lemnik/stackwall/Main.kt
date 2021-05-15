package com.lemnik.stackwall

import java.io.File

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Usage: stackwall <application.apk> <stacktrace.txt>")
        return
    }

    val (applicationFile, stacktraceFile) = args

    val stackSizeSource = ApkStackSizeSource(File(applicationFile))
    val stackOverflow = File(stacktraceFile).useLines { lines ->
        OverflowParser.parseStackOverflow(
            lines
                .filter { it.isNotBlank() }
                .toList()
        )
    }

    if (stackOverflow == null) {
        println("Couldn't read stack overflow file: ${args[1]}")
        return
    }

    var low = 0
    var high = 0

    val stack = stackOverflow.stackTrace
    val width = stack.maxOf { it.toString().length }

    stack.forEach { element ->
        print("${element.toString().padEnd(width)} = ")

        try {
            val stackRequirement = stackSizeSource.findStackSize(element)
            when {
                stackRequirement == null -> println("<unknown>")
                stackRequirement.first == stackRequirement.last -> {
                    low += stackRequirement.first
                    high += stackRequirement.first
                    println(stackRequirement.first)
                }
                else -> {
                    low += stackRequirement.first
                    high += stackRequirement.last
                    println("${stackRequirement.first}..${stackRequirement.last}")
                }
            }
        } catch (ex: NoSuchMethodException) {
            println("<no-such-method>")
        }

        // add a word to each counter for the return-address
        low++
        high++
    }

    // subtract a single word from each counter (since the last frame doesn't have a return address)
    low--
    high--

    println("Total Range: $low..$high (${low * 4}b..${high * 4}b)")
}
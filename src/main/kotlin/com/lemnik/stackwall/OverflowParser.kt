package com.lemnik.stackwall

object OverflowParser {
    fun parseStackOverflow(lines: List<String>): StackOverflow? {
        val stackSizeIndex = lines[0].indexOf("stack size", ignoreCase = true)
        var stackSize: Int? = null

        if (stackSizeIndex != -1) {
            val stackSizeLine = lines[0]
            val stackSizeString = stackSizeLine.substring(stackSizeIndex + "stack size".length).trim()

            stackSize = parseStackSize(stackSizeString)
        }

        val stackElements = lines
            .dropWhile { StackElement.parse(it) == null }
            .map { StackElement.parse(it)!! }

        return StackOverflow(stackSize, stackElements).takeUnless { it.isEmpty() }
    }

    private fun parseStackSize(stackSizeString: String): Int? {
        val matchedParts = Regex("""(\d+)(\w+)""").matchEntire(stackSizeString)?.groupValues ?: return null
        return when (matchedParts[2].toLowerCase()) {
            "kb" -> matchedParts[1].toInt() * 1024
            "mb" -> matchedParts[1].toInt() * 1024 * 1024
            else -> null
        }
    }
}
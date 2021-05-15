package com.lemnik.stackwall

data class StackElement(
    val filename: String?,
    val lineNumber: Int?,
    val fullClassName: String,
    val methodName: String,
) {

    val typeName get() = "L${fullClassName.replace('.', '/')};"

    override fun toString(): String = "$fullClassName::$methodName"

    companion object {
        private val ADB_REGEX = Regex("""^\d\d-\d\d \d\d:\d\d:\d\d\.\d{3}: [ADEIVW]/.+""")

        private fun parseJava(formatted: String): StackElement? {
            val match = Regex("""\s*(?:at\s)?([\w.]+/)?([^(]+)\(([\w\d+.: ]+)\)""").matchEntire(formatted)
                ?: return null

            val groups = match.groupValues
            return StackElement(
                groups[3].takeIf { it.contains(':') }?.substringBefore(':'),
                groups[3].takeIf { it.contains(':') }?.substringAfter(':')?.toIntOrNull(),
                groups[2].substringBeforeLast('.'),
                groups[2].substringAfterLast('.'),
            )
        }

        private fun parseBugsnag(formatted: String): StackElement? {
            val match = Regex("""([\w.]+)(\:\d+)? (.+)""").matchEntire(formatted)
                ?: return null

            val groups = match.groupValues
            return StackElement(
                groups[1].takeIf { it != "Unknown" },
                groups[2].toIntOrNull(),
                groups[3].substringBeforeLast('.'),
                groups[3].substringAfterLast('.'),
            )
        }

        tailrec fun parse(formatted: String): StackElement? {
            if (!formatted.matches(ADB_REGEX)) {
                return parseJava(formatted)
                    ?: parseBugsnag(formatted)
            }

            // we trim the ADB prefix data off, and try to parse the result
            return parse(formatted.split(':').drop(4).joinToString(":"))
        }
    }
}

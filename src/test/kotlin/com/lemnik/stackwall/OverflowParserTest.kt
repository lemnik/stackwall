package com.lemnik.stackwall

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class OverflowParserTest {
    @Test
    fun parsePlain() {
        val overflow = OverflowParser.parseStackOverflow(readExampleOverflow("plain.txt"))!!
        assertNull(overflow.stackSize)
        assertEquals(
            StackElement(
                "View.java",
                11556,
                "android.view.View",
                "getDrawableState"
            ),
            overflow.stackTrace[0]
        )

        assertEquals(28, overflow.stackTrace.size)
    }

    /**
     * Test that we can recognise and parse ADB logs
     */
    @Test
    fun parseADB() {
        val overflow = OverflowParser.parseStackOverflow(readExampleOverflow("adb.txt"))!!
        assertNull(overflow.stackSize)
        assertEquals(
            StackElement(
                "View.java",
                11556,
                "android.view.View",
                "getDrawableState"
            ),
            overflow.stackTrace[0]
        )

        assertEquals(28, overflow.stackTrace.size)
    }

    private fun readExampleOverflow(name: String): List<String> {
        return this::class.java.getResourceAsStream("/examples/$name").reader().useLines { it.toList() }
    }
}
package com.lemnik.stackwall

interface StackSizeSource {
    fun findStackSize(element: StackElement): IntRange?
}
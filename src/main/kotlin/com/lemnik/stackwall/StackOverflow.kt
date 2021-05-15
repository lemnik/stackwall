package com.lemnik.stackwall

data class StackOverflow(val stackSize: Int?, val stackTrace: List<StackElement>) {
    fun isEmpty() = stackTrace.isEmpty()
}
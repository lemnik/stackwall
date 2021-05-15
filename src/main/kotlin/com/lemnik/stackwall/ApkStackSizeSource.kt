package com.lemnik.stackwall

import org.jf.dexlib2.DexFileFactory
import org.jf.dexlib2.Opcodes
import org.jf.dexlib2.iface.DexFile
import org.jf.dexlib2.iface.MultiDexContainer
import java.io.File

class ApkStackSizeSource(val apk: File) : StackSizeSource {

    private val container: MultiDexContainer<out DexFile> = DexFileFactory.loadDexContainer(apk, Opcodes.getDefault())

    override fun findStackSize(element: StackElement): IntRange? {
        val bytecode = container.dexEntryNames
            .map { container.getEntry(it) }
            .flatMap { it?.dexFile?.classes ?: emptySet() }
            .find { it.type == element.typeName } ?: return null

        // FIXME: Try and match the line-numbers if available - to handle overloads and produce a more accurate result
        val methods = bytecode.methods
            .filter { it.name == element.methodName }
            .takeIf { it.isNotEmpty() } ?: throw NoSuchMethodException("Cannot find method for: $element")

        return IntRange(
            methods.minOf { it.implementation?.registerCount ?: 0 },
            methods.maxOf { it.implementation?.registerCount ?: 0 },
        )
    }
}
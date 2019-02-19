package ru.hse.spb.execution

import ru.hse.spb.exceptions.NoSuchDirectoryException
import ru.hse.spb.pipeline.Pipeline
import java.io.File
import java.nio.file.Paths

/**
 * Implementation of ls (lists files) command.
 * If there are no arguments prints files in current directory, else prints in the given one.
 * Ignores pipeline inputs.
 */
class Ls(arguments: List<String>, prev: Executable?) :
    OneTypeArgumentsExecutable<String>(arguments, prev) {
    override fun processArgumentsInput(): String {
        val old = System.getProperty("user.dir")
        val path = Paths.get(System.getProperty("user.dir"), arguments[0])
        if (!path.toFile().exists() || !path.toFile().isDirectory) {
            throw NoSuchDirectoryException(arguments[0])
        }
        System.setProperty("user.dir", path.toAbsolutePath().toString())
        val result = processEmptyInput()
        System.setProperty("user.dir", old)
        return result
    }

    override fun processPipelineInput(pipeLine: Pipeline): String = processEmptyInput()

    override fun processEmptyInput(): String {
        var result = ""
        File(System.getProperty("user.dir")).listFiles().forEach { result = result + it.name + " " + (if (it.isDirectory) "directory" else "file")+ "\n" }
        return result
    }

}
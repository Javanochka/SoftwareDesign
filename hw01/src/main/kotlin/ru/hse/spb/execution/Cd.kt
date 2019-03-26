package ru.hse.spb.execution

import ru.hse.spb.exceptions.NoSuchDirectoryException
import ru.hse.spb.execution.Utils.setenv
import ru.hse.spb.pipeline.Pipeline
import java.nio.file.Paths
import java.lang.reflect.AccessibleObject.setAccessible
import java.util.*


/**
 * Implementation of cd (change directory) command.
 * If no arguments changes to home directory, else changes to the given one.
 * Ignores pipelines.
 */
class Cd(arguments: List<String>, prev: Executable?) :
    OneTypeArgumentsExecutable<String>(arguments, prev) {
    override fun processPipelineInput(pipeLine: Pipeline): String = processEmptyInput()

    override fun processEmptyInput(): String {
        setenv("PWD", System.getProperty("user.home"))
        return ""
    }

    override fun processArgumentsInput(): String {
        val path = Paths.get(System.getenv("PWD"), arguments[0]).toAbsolutePath().normalize()
        if (!path.toFile().exists() || !path.toFile().isDirectory) {
            throw NoSuchDirectoryException(arguments[0])
        }
        setenv("PWD", path.toString())
        return ""
    }

}
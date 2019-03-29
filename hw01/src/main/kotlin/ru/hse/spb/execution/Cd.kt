package ru.hse.spb.execution

import ru.hse.spb.environment.GlobalEnvironment
import ru.hse.spb.exceptions.NoSuchDirectoryException
import ru.hse.spb.pipeline.Pipeline
import java.nio.file.Paths


/**
 * Implementation of cd (change directory) command.
 * If no arguments changes to home directory, else changes to the given one.
 * Ignores pipelines.
 */
class Cd(arguments: List<String>, prev: Executable?) :
    OneTypeArgumentsExecutable<String>(arguments, prev) {
    override fun processPipelineInput(pipeLine: Pipeline): String = processEmptyInput()

    override fun processEmptyInput(): String {
        GlobalEnvironment.userDir = System.getProperty("user.home")
        return ""
    }

    override fun processArgumentsInput(): String {
        val path = Paths.get(GlobalEnvironment.userDir, arguments[0]).toAbsolutePath().normalize()
        if (!path.toFile().exists() || !path.toFile().isDirectory) {
            throw NoSuchDirectoryException(arguments[0])
        }
        GlobalEnvironment.userDir = path.toString()
        return ""
    }

}
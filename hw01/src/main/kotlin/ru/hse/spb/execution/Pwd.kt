package ru.hse.spb.execution

import ru.hse.spb.pipeline.Pipeline
import java.nio.file.Paths

/**
 * Implementation of pwd command: takes no arguments and returns current directory
 */
class Pwd(prev: Executable?) : NoArgumentsExecutable(prev) {
    override fun processPipelineInput(pipeLine: Pipeline) = System.getProperty("user.dir") + "\n"

    override fun processEmptyInput() = System.getProperty("user.dir") + "\n"
}
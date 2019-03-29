package ru.hse.spb.execution

import ru.hse.spb.environment.GlobalEnvironment
import ru.hse.spb.pipeline.Pipeline

/**
 * Implementation of pwd command: takes no arguments and returns current directory
 */
class Pwd(prev: Executable?) : NoArgumentsExecutable(prev) {
    override fun processPipelineInput(pipeLine: Pipeline) = GlobalEnvironment.userDir + "\n"

    override fun processEmptyInput() = GlobalEnvironment.userDir + "\n"
}
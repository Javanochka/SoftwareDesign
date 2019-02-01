package ru.hse.spb.lexer

import ru.hse.spb.environment.GlobalEnvironment
import ru.hse.spb.exceptions.IncorrectQuotingException

/**
 * This implementation goes through string and substitutes all occurrences of $ and deletes quoting.
 * Also it partitions input on string tokens (words)
 */
object InterpolationLexer : Lexer {
    private var variableName: String? = null
    private var currentStringPart = ""
    private var state = State.NO_QUOTING

    private enum class State {
        DOUBLE_QUOTING, SINGLE_QUOTING, NO_QUOTING
    }

    /***
     * Tokenize an input and substitutes global variables
     * @return list of tokens
     */
    override fun tokenize(input: String): List<String> {
        clear()
        val result = mutableListOf<String>()

        for (ch in input) {
            when (ch) {
                '"' ->
                    when (state) {
                        State.DOUBLE_QUOTING -> {
                            state = State.NO_QUOTING
                        }
                        State.SINGLE_QUOTING -> {
                            addChar(ch)
                        }
                        State.NO_QUOTING -> {
                            state = State.DOUBLE_QUOTING
                        }
                    }
                '\'' ->
                    when (state) {
                        State.DOUBLE_QUOTING -> addChar(ch)
                        State.SINGLE_QUOTING -> state = State.NO_QUOTING
                        State.NO_QUOTING -> state = State.SINGLE_QUOTING
                    }
                '$' ->
                    when (state) {
                        State.SINGLE_QUOTING -> addChar(ch)
                        else -> {
                            currentStringPart += variableName?.let { GlobalEnvironment.getValue(it) }.orEmpty()
                            variableName = ""
                        }
                    }
                '|' -> {
                    processSeparatorSymbol(ch, result)
                    result.add("|")
                }
                ' ' ->
                    processSeparatorSymbol(ch, result)
                '=' ->
                    when (state) {
                        State.NO_QUOTING -> {
                            changeState(result, State.NO_QUOTING)
                            result.add("=")
                            result[result.lastIndex] = result[result.lastIndex - 1].
                                also { result[result.lastIndex - 1] = result[result.lastIndex] }
                        }
                        else -> addChar(ch)
                    }
                else ->
                    addChar(ch)
            }
        }

        if (state != State.NO_QUOTING) {
            throw IncorrectQuotingException("Incorrect quotes!")
        }

        return result.apply { dropToResult(this) }
    }

    private fun processSeparatorSymbol(ch: Char, result: MutableList<String>) {
        when (state) {
            State.DOUBLE_QUOTING -> {
                currentStringPart += variableName?.let { GlobalEnvironment.getValue(it) }.orEmpty() + ' '
                variableName = null
            }
            State.SINGLE_QUOTING -> addChar(ch)
            State.NO_QUOTING -> changeState(result, State.NO_QUOTING)
        }
    }

    private fun addChar(ch: Char) {
        if (variableName == null) currentStringPart += ch else variableName += ch
    }

    private fun changeState(result: MutableList<String>, newState: State) {
        state = newState
        dropToResult(result)
    }

    private fun dropToResult(result: MutableList<String>) {
        if (!(currentStringPart == "" && variableName == null)) {
            result.add(currentStringPart + variableName?.let { GlobalEnvironment.getValue(it) }.orEmpty())
            variableName = null
            currentStringPart = ""
        }
    }

    private fun clear() {
        state = State.NO_QUOTING
        variableName = null
        currentStringPart = ""
    }
}
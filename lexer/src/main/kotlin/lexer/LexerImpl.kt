package org.example.lexer

import org.example.factories.Position
import org.example.factories.TokenFactory
import org.example.lexer.stringDivider.StringDividerImpl
import org.example.lexer.tokenMatchers.TokenMatcherImpl
import org.example.token.Token

class LexerImpl(version: String) : Lexer {
    private val tokenMatcher = TokenMatcherImpl(version)
    private val stringDivider = StringDividerImpl()

    override fun tokenize(input: String): List<Token> {
        var acumulatedPos = 0
        return stringDivider.stringToList(input).flatMapIndexed { lineIndex, line ->
            line.mapIndexed { _, word ->
                val totalLength = line.map { it.length }.sum() + countSpaces(input) - 1
                val position = word.length + checkForSpaces(acumulatedPos + word.length, input, totalLength) + acumulatedPos
                TokenFactory.createValueToken(
                    tokenMatcher.getToken(word, Position(lineIndex, position)),
                    word,
                    Position(lineIndex, acumulatedPos),
                )
                    .also { acumulatedPos = position }
            }
        }
    }

    private fun checkForSpaces(
        position: Int,
        input: String,
        length: Int,
    ): Int {
        return if (position >= length) {
            0
        } else if (input[position] != ' ') {
            0
        } else {
            1
        }
    }

    private fun countSpaces(input: String): Int {
        val index = input.indexOf(';')
        return if (index != -1) input.substring(0, index).count { it == ' ' } else input.count { it == ' ' }
    }
}

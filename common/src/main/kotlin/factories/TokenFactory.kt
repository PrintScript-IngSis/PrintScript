package org.example.factories
import org.example.token.Token
import org.example.token.TokenType

class TokenFactory {
    companion object {
        fun createValueToken(
            type: TokenType,
            value: String,
            position: Position,
        ): Token {
            if (type == TokenType.LITERAL_STRING) {
                return Token(type, value.substring(1, value.length - 1), position)
            }
            if (type == TokenType.LITERAL_NUMBER) {
                return Token(type, value, position)
            }
            if (type == TokenType.LITERAL_BOOLEAN) {
                return Token(type, value.toBoolean().toString(), position)
            }
            return Token(type, value, position)
        }

        fun createToken(
            type: TokenType,
            result: MatchResult,
            input: String,
        ): Token {
            val position = calculatePosition(result.range.first, input)
            return createValueToken(type, result.value, position)
        }

        private fun calculatePosition(
            startIndex: Int,
            input: String,
        ): Position {
            var line = 0
            var column = 0
            for (i in 0 until startIndex) {
                if (input[i] == '\n') {
                    line++
                    column = 1
                } else {
                    column++
                }
            }
            return Position(line, column)
        }
    }
}

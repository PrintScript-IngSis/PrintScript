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
                return Token(type, value.toDouble().toString(), position)
            }
            if (type == TokenType.LITERAL_BOOLEAN) {
                return Token(type, value.toBoolean().toString(), position)
            }
            return Token(type, value, position)
        }
    }
}

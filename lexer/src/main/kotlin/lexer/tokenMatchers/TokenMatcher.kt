package org.example.lexer.tokenMatchers
import org.example.factories.Position
import org.example.token.TokenType

interface TokenMatcher {
    fun getToken(
        input: String,
        position: Position,
    ): TokenType
}

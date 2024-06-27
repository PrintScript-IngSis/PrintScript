package org.example

import org.example.token.TokenType

class SwitchType {
    companion object {
        fun typeToLiteral(type: TokenType): TokenType {
            return when (type) {
                in setOf(TokenType.TYPE_NUMBER, TokenType.LITERAL_NUMBER) -> TokenType.LITERAL_NUMBER
                in setOf(TokenType.TYPE_STRING, TokenType.LITERAL_STRING) -> TokenType.LITERAL_STRING
                in setOf(TokenType.TYPE_BOOLEAN, TokenType.LITERAL_BOOLEAN) -> TokenType.LITERAL_BOOLEAN
                else -> throw Exception("Unknown type")
            }
        }
    }
}

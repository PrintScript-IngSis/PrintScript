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

        fun literalToType(type: TokenType): TokenType {
            return when (type) {
                in setOf(TokenType.LITERAL_NUMBER, TokenType.TYPE_NUMBER) -> TokenType.TYPE_NUMBER
                in setOf(TokenType.LITERAL_STRING, TokenType.TYPE_NUMBER) -> TokenType.TYPE_STRING
                in setOf(TokenType.LITERAL_BOOLEAN, TokenType.TYPE_NUMBER) -> TokenType.TYPE_BOOLEAN
                else -> throw Exception("Unknown type")
            }
        }
    }
}

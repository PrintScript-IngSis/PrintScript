package org.example

import org.example.token.TokenType

class SwitchType {
    companion object {
        fun typeToLiteral(type: TokenType): TokenType {
            return when (type) {
                TokenType.TYPE_NUMBER -> TokenType.LITERAL_NUMBER
                TokenType.TYPE_STRING -> TokenType.LITERAL_STRING
                TokenType.TYPE_BOOLEAN -> TokenType.LITERAL_BOOLEAN
                else -> throw Exception("Unknown type")
            }
        }

        fun literalToType(type: TokenType): TokenType {
            return when (type) {
                TokenType.LITERAL_NUMBER -> TokenType.TYPE_NUMBER
                TokenType.LITERAL_STRING -> TokenType.TYPE_STRING
                TokenType.LITERAL_BOOLEAN -> TokenType.TYPE_BOOLEAN
                else -> throw Exception("Unknown type")
            }
        }
    }
}

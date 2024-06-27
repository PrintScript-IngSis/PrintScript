package lexer.util

import org.example.token.TokenType

class Constants {
    companion object {
        //version 1.0

        val KEYWORD_MAP_V10 =
            mapOf(
                "let" to TokenType.KEYWORD_LET,
                "println" to TokenType.KEYWORD_PRINTLN,
            )

        val TYPE_MAP_V10 =
            mapOf(
                "string" to TokenType.TYPE_STRING,
                "number" to TokenType.TYPE_NUMBER,
            )

        val OPERATOR_MAP_V10 =
            mapOf(
                "+" to TokenType.OPERATOR_PLUS,
                "-" to TokenType.OPERATOR_MINUS,
                "*" to TokenType.OPERATOR_MULTIPLY,
                "/" to TokenType.OPERATOR_DIVIDE,
                "=" to TokenType.ASSIGNATOR,
                "(" to TokenType.PARENTHESIS_OPEN,
                ")" to TokenType.PARENTHESIS_CLOSE,
                ":" to TokenType.COLON,
                ";" to TokenType.SEMICOLON,
            )

        val IDENTIFIER_CONSTRAINTS_V10 = listOf("let", "println", "number", "string")

        //version 1.1

        val KEYWORD_MAP_V11 =
            mapOf(
                "let" to TokenType.KEYWORD_LET,
                "println" to TokenType.KEYWORD_PRINTLN,
                "const" to TokenType.KEYWORD_CONST,
                "if" to TokenType.KEYWORD_IF,
                "else" to TokenType.KEYWORD_ELSE,
                "readInput" to TokenType.KEYWORD_READ_INPUT,
                "readEnv" to TokenType.KEYWORD_READ_ENV,
            )

        val TYPE_MAP_V11 =
            mapOf(
                "string" to TokenType.TYPE_STRING,
                "number" to TokenType.TYPE_NUMBER,
                "boolean" to TokenType.TYPE_BOOLEAN,
            )

        val OPERATOR_MAP_V11 =
            mapOf(
                "+" to TokenType.OPERATOR_PLUS,
                "-" to TokenType.OPERATOR_MINUS,
                "*" to TokenType.OPERATOR_MULTIPLY,
                "/" to TokenType.OPERATOR_DIVIDE,
                "=" to TokenType.ASSIGNATOR,
                "(" to TokenType.PARENTHESIS_OPEN,
                ")" to TokenType.PARENTHESIS_CLOSE,
                ":" to TokenType.COLON,
                ";" to TokenType.SEMICOLON,
                "{" to TokenType.BRACKET_OPEN,
                "}" to TokenType.BRACKET_CLOSE,
            )

        val IDENTIFIER_CONSTRAINTS_V11 =
            listOf(
                "const",
                "let",
                "println",
                "number",
                "string",
                "boolean",
                "if",
                "else",
                "readInput",
                "true",
                "false",
                "readEnv",
            )
    }
}

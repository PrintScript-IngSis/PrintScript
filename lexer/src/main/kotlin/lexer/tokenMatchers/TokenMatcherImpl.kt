package org.example.lexer.tokenMatchers

import org.example.factories.Position
import org.example.token.TokenType

class TokenMatcherImpl(version: String) : TokenMatcher {
    companion object {
        val version10Map: Map<Regex, TokenType> =
            mapOf(
                Regex("let") to TokenType.KEYWORD_LET,
                Regex("const") to TokenType.NOT_IMPLEMENTED,
                Regex("if") to TokenType.NOT_IMPLEMENTED,
                Regex("else") to TokenType.NOT_IMPLEMENTED,
                Regex("string") to TokenType.TYPE_STRING,
                Regex("number") to TokenType.TYPE_NUMBER,
                Regex("println") to TokenType.KEYWORD_PRINTLN,
                Regex("boolean") to TokenType.NOT_IMPLEMENTED,
                Regex("true|false") to TokenType.NOT_IMPLEMENTED,
                Regex("readInput") to TokenType.NOT_IMPLEMENTED,
                Regex("readEnv") to TokenType.NOT_IMPLEMENTED,
                Regex("[a-zA-Z][a-zA-Z0-9_]*") to TokenType.IDENTIFIER,
                Regex("=") to TokenType.ASSIGNATOR,
                Regex(":") to TokenType.COLON,
                Regex(";") to TokenType.SEMICOLON,
                Regex("[0-9]+(?:\\.[0-9]+)?") to TokenType.LITERAL_NUMBER,
                Regex("""(['"]).*?\1""") to TokenType.LITERAL_STRING,
                Regex("<") to TokenType.OPERATOR_LESS,
                Regex(">") to TokenType.OPERATOR_GREATER,
                Regex("\\+") to TokenType.OPERATOR_PLUS,
                Regex("-") to TokenType.OPERATOR_MINUS,
                Regex("\\*") to TokenType.OPERATOR_MULTIPLY,
                Regex("/") to TokenType.OPERATOR_DIVIDE,
                Regex("\\(") to TokenType.PARENTHESIS_OPEN,
                Regex("\\)") to TokenType.PARENTHESIS_CLOSE,
                Regex("\\{") to TokenType.BRACKET_OPEN,
                Regex("}") to TokenType.BRACKET_CLOSE,
            )

        val version11Map: Map<Regex, TokenType> =
            mapOf(
                Regex("let") to TokenType.KEYWORD_LET,
                Regex("const") to TokenType.KEYWORD_CONST,
                Regex("if") to TokenType.KEYWORD_IF,
                Regex("else") to TokenType.KEYWORD_ELSE,
                Regex("string") to TokenType.TYPE_STRING,
                Regex("number") to TokenType.TYPE_NUMBER,
                Regex("boolean") to TokenType.TYPE_BOOLEAN,
                Regex("println") to TokenType.KEYWORD_PRINTLN,
                Regex("readInput") to TokenType.KEYWORD_READ_INPUT,
                Regex("readEnv") to TokenType.KEYWORD_READ_ENV,
                Regex("true|false") to TokenType.LITERAL_BOOLEAN,
                Regex("[a-zA-Z][a-zA-Z0-9_]*") to TokenType.IDENTIFIER,
                Regex("=") to TokenType.ASSIGNATOR,
                Regex(":") to TokenType.COLON,
                Regex(";") to TokenType.SEMICOLON,
                Regex("[0-9]+(?:\\.[0-9]+)?") to TokenType.LITERAL_NUMBER,
                Regex("""(['"]).*?\1""") to TokenType.LITERAL_STRING,
                Regex("<") to TokenType.OPERATOR_LESS,
                Regex(">") to TokenType.OPERATOR_GREATER,
                Regex("\\+") to TokenType.OPERATOR_PLUS,
                Regex("-") to TokenType.OPERATOR_MINUS,
                Regex("\\*") to TokenType.OPERATOR_MULTIPLY,
                Regex("/") to TokenType.OPERATOR_DIVIDE,
                Regex("\\(") to TokenType.PARENTHESIS_OPEN,
                Regex("\\)") to TokenType.PARENTHESIS_CLOSE,
                Regex("\\{") to TokenType.BRACKET_OPEN,
                Regex("}") to TokenType.BRACKET_CLOSE,
            )
    }

    private val regexMap: Map<Regex, TokenType> =
        when (version) {
            "1.0" -> version10Map
            "1.1" -> version11Map
            else -> throw IllegalArgumentException("Version $version not supported")
        }

    override fun getToken(
        input: String,
        position: Position,
    ): TokenType {
        for ((regex, tokenType) in regexMap) {
            if (regex.matches(input)) {
                if (tokenType != TokenType.NOT_IMPLEMENTED) {
                    return tokenType
                } else {
                    throw IllegalArgumentException("Token $input not implemented")
                }
            }
        }
        throw IllegalArgumentException("No matching token for input: $input, in line ${position.line} and column ${position.column}")
    }
}

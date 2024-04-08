package org.example.lexer.tokenMatchers
import org.example.factories.Position
import org.example.token.TokenType

class TokenMatcherImpl : TokenMatcher {
    private val regexMap: Map<Regex, TokenType> =
        mapOf(
            Regex("let") to TokenType.KEYWORD_LET,
            Regex("const") to TokenType.KEYWORD_CONST,
            Regex("if") to TokenType.KEYWORD_IF,
            Regex("else") to TokenType.KEYWORD_ELSE,
            Regex("string") to TokenType.TYPE_STRING,
            Regex("number") to TokenType.TYPE_NUMBER,
            Regex("boolean") to TokenType.TYPE_BOOLEAN,
            Regex("println") to TokenType.OPERATOR_PRINTLN,
            Regex("true|false") to TokenType.LITERAL_BOOLEAN,
            Regex("[a-zA-Z][a-zA-Z0-9_]*") to TokenType.IDENTIFIER,
            Regex("=") to TokenType.ASSIGNATOR,
            Regex(":") to TokenType.COLON,
            Regex(";") to TokenType.SEMICOLON,
            Regex("[0-9]+") to TokenType.LITERAL_NUMBER,
            Regex("""(['"]).*?\1""") to TokenType.LITERAL_STRING,
            Regex("\\+") to TokenType.OPERATOR_PLUS,
            Regex("-") to TokenType.OPERATOR_MINUS,
            Regex("\\*") to TokenType.OPERATOR_MULTIPLY,
            Regex("/") to TokenType.OPERATOR_DIVIDE,
            Regex("\\(") to TokenType.PARENTHESIS_OPEN,
            Regex("\\)") to TokenType.PARENTHESIS_CLOSE,
            Regex("\\{") to TokenType.BRACKET_OPEN,
            Regex("\\}") to TokenType.BRACKET_CLOSE,
        )

    override fun getToken(
        input: String,
        position: Position,
    ): TokenType {
        for ((regex, tokenType) in regexMap) {
            if (regex.matches(input)) {
                return tokenType
            }
        }
        throw IllegalArgumentException("No matching token for input: $input, in line ${position.line} and column ${position.column}")
    }
}

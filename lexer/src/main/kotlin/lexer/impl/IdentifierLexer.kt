package lexer.impl

import lexer.util.TokenPatterns
import org.example.factories.TokenFactory
import org.example.lexer.Lexer
import org.example.token.Token
import org.example.token.TokenType

class IdentifierLexer(private val constraints: List<String>) : Lexer {
    private val pattern = TokenPatterns.IDENTIFIER_PATTERN

    override fun tokenize(input: String): List<Token> {
        return pattern.findAll(input).mapNotNull { result ->
            if (isIdentifier(result, input)) {
                TokenFactory.createToken(TokenType.IDENTIFIER, result, input)
            } else {
                null
            }
        }.toList()
    }

    private fun isIdentifier(
        result: MatchResult,
        input: String,
    ) = !constraints.contains(result.value) && !TokenPatterns.isInQuotes(result, input)
}

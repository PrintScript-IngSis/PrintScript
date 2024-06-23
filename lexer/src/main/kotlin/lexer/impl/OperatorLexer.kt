package lexer.impl

import lexer.util.TokenPatterns
import org.example.factories.TokenFactory
import org.example.lexer.Lexer
import org.example.token.Token
import org.example.token.TokenType

class OperatorLexer(private val typeMap: Map<String, TokenType>) : Lexer {
    private val pattern = TokenPatterns.OPERATOR_PATTERN(typeMap)

    override fun tokenize(input: String): List<Token> {
        return pattern.findAll(input).mapNotNull { result ->
            if (!TokenPatterns.isInQuotes(result, input)) {
                typeMap[result.value]?.let { TokenFactory.createToken(it, result, input) }
            } else {
                null
            }
        }.toList()
    }
}

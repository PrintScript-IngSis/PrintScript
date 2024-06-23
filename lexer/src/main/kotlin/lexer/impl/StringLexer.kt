package lexer.impl

import lexer.util.TokenPatterns
import org.example.factories.TokenFactory
import org.example.lexer.Lexer
import org.example.token.Token
import org.example.token.TokenType

class StringLexer : Lexer {
    private val pattern = TokenPatterns.STRING_LITERAL

    override fun tokenize(input: String): List<Token> {
        return pattern.findAll(input).map { result ->
            TokenFactory.createToken(TokenType.LITERAL_STRING, result, input)
        }.toList()
    }
}

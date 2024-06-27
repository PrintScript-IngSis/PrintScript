package lexer.impl

import org.example.lexer.Lexer
import org.example.token.Token

class CompositeLexer(private val lexers: List<Lexer>) : Lexer {
    override fun tokenize(input: String): List<Token> {
        return lexers.flatMap { lexer ->
            lexer.tokenize(input)
        }.sortedWith(compareBy({ it.position.line }, { it.position.column }))
    } // for each lexer, toxenize to verify true or false w regex
}

package lexer.builder

import lexer.impl.CompositeLexer
import org.example.lexer.Lexer

class LexerBuilderImpl(private val lexers: List<Lexer>) : LexerBuilder {
    override fun withLexer(lexer: Lexer): LexerBuilder {
        val updatedLexers = lexers + lexer
        return LexerBuilderImpl(updatedLexers)
    }

    override fun build(): Lexer {
        return CompositeLexer(lexers)
    }
}

package lexer.builder

import org.example.lexer.Lexer

interface LexerBuilder {
    fun withLexer(lexer: Lexer): LexerBuilder
    fun build(): Lexer
}

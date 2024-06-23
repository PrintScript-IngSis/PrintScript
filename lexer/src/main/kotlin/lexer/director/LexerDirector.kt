package lexer.director

import lexer.builder.LexerBuilderImpl
import lexer.impl.BooleanLexer
import lexer.impl.IdentifierLexer
import lexer.impl.KeywordLexer
import lexer.impl.NumberLexer
import lexer.impl.OperatorLexer
import lexer.impl.StringLexer
import lexer.impl.TypeLexer
import lexer.util.Constants
import org.example.lexer.Lexer

class LexerDirector {
    fun createLexer(version: String): Lexer {
        val builder = LexerBuilderImpl(listOf())
        return when (version) {
            "1.0" -> {
                builder
                    .withLexer(KeywordLexer(Constants.KEYWORD_MAP_V10))
                    .withLexer(TypeLexer(Constants.TYPE_MAP_V10))
                    .withLexer(
                        IdentifierLexer(
                            Constants.IDENTIFIER_CONSTRAINTS_V10,
                        ),
                    )
                    .withLexer(NumberLexer())
                    .withLexer(OperatorLexer(Constants.OPERATOR_MAP_V10))
                    .withLexer(StringLexer())
                    .build()
            }

            "1.1" -> {
                builder
                    .withLexer(KeywordLexer(Constants.KEYWORD_MAP_V11))
                    .withLexer(TypeLexer(Constants.TYPE_MAP_V11))
                    .withLexer(
                        IdentifierLexer(Constants.IDENTIFIER_CONSTRAINTS_V11),
                    )
                    .withLexer(NumberLexer())
                    .withLexer(OperatorLexer(Constants.OPERATOR_MAP_V11))
                    .withLexer(StringLexer())
                    .withLexer(BooleanLexer())
                    .build()
            }

            else -> {
                throw Exception("No implementation found for version: $version")
            }
        }
    }
}

package lexer.util

import org.example.token.TokenType

class TokenPatterns {
    companion object {
        val STRING_LITERAL = Regex("""(['"])(.*?)\1""")

        val NUMBER_LITERAL = Regex("""-?\b\d+(\.\d+)?\b""")

        val IDENTIFIER_PATTERN = Regex("""[a-zA-Z_][a-zA-Z0-9_]*""")

        val BOOLEAN_LITERAL = Regex("""^(true|false)${'$'}""")

        val KEYWORD_PATTERN: (Map<String, TokenType>) -> Regex =
            { tokens -> Regex("""\b(${tokens.keys.joinToString("|")})\b""") }

        val TYPE_PATTERN: (Map<String, TokenType>) -> Regex =
            { tokens -> Regex("""\b(${tokens.keys.joinToString("|")})\b""") }

        val OPERATOR_PATTERN: (Map<String, TokenType>) -> Regex = { tokens ->
            val operatorRegexPattern = tokens.keys.joinToString("|") { Regex.escape(it) }
            Regex("[$operatorRegexPattern]")
        }

        fun isInQuotes(
            matchResult: MatchResult,
            input: String,
        ): Boolean {
            val quotesMatch =
                STRING_LITERAL.findAll(input).map { result -> Pair(result.range.first, result.range.last + 1) }
                    .toList()
            val resultRange = Pair(matchResult.range.first, matchResult.range.last + 1)
            return quotesMatch.any { (start, end) -> start <= resultRange.first && end >= resultRange.second }
        }
    }
}

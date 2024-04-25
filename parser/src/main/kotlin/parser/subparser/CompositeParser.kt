package org.example.parser.subparser

import org.example.ast.nodes.StatementNode
import org.example.token.Token

class CompositeParser(private val parsers: List<Subparser>) : Subparser {
    override fun canParse(tokens: List<Token>): Boolean {
        TODO("Not yet implemented")
    }

    override fun parse(tokens: List<Token>): StatementNode {
        for (parser in parsers) {
            if (parser.canParse(tokens)) {
                return parser.parse(tokens)
            }
        }
        throw IllegalArgumentException("Invalid statement")
    }
}

package org.example.parser.subparser

import org.example.ast.nodes.StatementNode
import org.example.token.Token

interface Subparser {
    fun canParse(tokens: List<Token>): Boolean

    fun parse(tokens: List<Token>): StatementNode
}

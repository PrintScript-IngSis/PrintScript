package org.example.parser

import org.example.ast.nodes.Node
import org.example.token.Token

interface Parser {
    fun parse(tokens: List<Token>): Node
}

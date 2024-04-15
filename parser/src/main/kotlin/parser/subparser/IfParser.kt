package org.example.parser.subparser

import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.StatementNode
import org.example.parser.Parser
import org.example.parser.ParserImpl
import org.example.token.Token
import org.example.token.TokenType

class IfParser(private val tokens: List<Token>) : Parser {
    override fun parse(): StatementNode {
        val condition = ExpressionNode.IdentifierNode(tokens[2])
        val firstBracetOpen = findCurlyBracetOpen(tokens, 0)
        val firstBracetClose = findCurlyBracetClose(tokens, 0)
        val trueStatement = ParserImpl(tokens.subList(firstBracetOpen + 1, firstBracetClose)).parse().getStatements()[0]
        val falseStatement =
            ParserImpl(
                tokens.subList(
                    findCurlyBracetOpen(tokens, firstBracetClose) + 1,
                    findCurlyBracetClose(tokens, firstBracetClose + 1),
                ),
            ).parse().getStatements()[0]
        return StatementNode.IfNode(condition, trueStatement, falseStatement)
    }

    private fun findCurlyBracetOpen(
        tokens: List<Token>,
        index: Int,
    ): Int {
        for (i in index until tokens.size) {
            if (tokens[i].type == TokenType.BRACKET_OPEN) {
                return i
            }
        }
        throw IllegalArgumentException("No matching bracket open")
    }

    private fun findCurlyBracetClose(
        tokens: List<Token>,
        index: Int,
    ): Int {
        for (i in index until tokens.size) {
            if (tokens[i].type == TokenType.BRACKET_CLOSE) {
                return i
            }
        }
        throw IllegalArgumentException("No matching bracket close")
    }
}

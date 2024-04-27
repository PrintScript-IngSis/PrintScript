package org.example.parser.subparser

import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.StatementNode
import org.example.parser.ParserImpl
import org.example.token.Token
import org.example.token.TokenType

class IfParser() : Subparser {
    override fun canParse(tokens: List<Token>): Boolean {
        return tokens[0].type == TokenType.KEYWORD_IF
    }

    override fun parse(tokens: List<Token>): StatementNode {
        val condition = ExpressionNode.IdentifierNode(tokens[2])
        val firstBracketOpen = findCurlyBracketOpen(tokens, 0)
        val firstBracketClose = findCurlyBracketClose(tokens, 0)
        val trueStatement = ParserImpl().parse(tokens.subList(firstBracketOpen + 1, firstBracketClose)).getStatements()[0]
        val hasFalseStatement = tokens.size > firstBracketClose + 1 && tokens[firstBracketClose + 2].type == TokenType.KEYWORD_ELSE
        if (!hasFalseStatement) {
            return StatementNode.IfNode(condition, trueStatement, null)
        } else {
            val falseStatement =
                ParserImpl().parse(
                    tokens.subList(
                        findCurlyBracketOpen(tokens, firstBracketClose) + 1,
                        findCurlyBracketClose(tokens, firstBracketClose + 2),
                    ),
                ).getStatements()[0]
            return StatementNode.IfNode(condition, trueStatement, falseStatement)
        }
    }

    private fun findCurlyBracketOpen(
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

    private fun findCurlyBracketClose(
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

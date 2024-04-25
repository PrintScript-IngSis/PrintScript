package org.example.parser.subparser

import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.StatementNode
import org.example.parser.TokenSearcher
import org.example.parser.subparsers.OperationCropper
import org.example.token.Token
import org.example.token.TokenType

class ReassignationParser() : Subparser {
    override fun canParse(tokens: List<Token>): Boolean {
        return tokens[0].type == TokenType.IDENTIFIER
    }

    override fun parse(tokens: List<Token>): StatementNode {
        val idNode = ExpressionNode.IdentifierNode(TokenSearcher.searchForToken(tokens, listOf(TokenType.IDENTIFIER)))
        val valueNode =
            OperationParser.createValueNode(
                OperationCropper.crop(tokens, TokenType.ASSIGNATOR).listIterator(),
            )
                ?: throw Exception("Expected value after reassignment operator")
        return StatementNode.AssignationNode(idNode, valueNode)
    }
}

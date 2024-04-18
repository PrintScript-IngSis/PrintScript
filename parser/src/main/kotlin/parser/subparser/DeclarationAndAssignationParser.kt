package org.example.parser.subparser

import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.StatementNode
import org.example.parser.Parser
import org.example.parser.TokenSearcher
import org.example.parser.subparsers.OperationCropper
import org.example.token.Token
import org.example.token.TokenType

class DeclarationAndAssignationParser(private val tokens: List<Token>) : Parser {
    override fun parse(): StatementNode {
        val valueNode =
            OperationParser.createValueNode(
                OperationCropper.crop(tokens, TokenType.ASSIGNATOR).listIterator(),
            )
                ?: throw Exception("Expected value after assignment operator")
        return StatementNode.DeclarationAndAssignationNode(createVariableNode(), valueNode)
    }

    private fun createVariableNode(): ExpressionNode.VariableNode {
        val idNode = ExpressionNode.IdentifierNode(TokenSearcher.searchForToken(tokens, listOf(TokenType.IDENTIFIER)))
        val typeNode =
            ExpressionNode.TypeNode(
                TokenSearcher.searchForToken(tokens, listOf(TokenType.TYPE_STRING, TokenType.TYPE_NUMBER, TokenType.TYPE_BOOLEAN)),
            )
        return ExpressionNode.VariableNode(idNode, typeNode)
    }
}

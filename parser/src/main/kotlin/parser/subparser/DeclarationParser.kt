package org.example.parser.subparser

import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.Node
import org.example.ast.nodes.StatementNode
import org.example.parser.Parser
import org.example.parser.TokenSearcher
import org.example.parser.subparsers.OperationCropper
import org.example.token.Token
import org.example.token.TokenType

class DeclarationParser(private val tokens: List<Token>) : Parser {
    override fun parse(): Node {
        val valueNode =
            OperationParser.createValueNode(
                OperationCropper.crop(tokens, TokenType.ASSIGNATOR).listIterator(),
            )
                ?: throw Exception("Expected value after assignment operator")
        return StatementNode.DeclarationNode(createVariableNode(), valueNode)
    }

    private fun createVariableNode(): StatementNode.VariableNode {
        val idNode = ExpressionNode.IdentifierNode(TokenSearcher.searchForToken(tokens, listOf(TokenType.IDENTIFIER)))
        val typeNode =
            ExpressionNode.TypeNode(
                TokenSearcher.searchForToken(tokens, listOf(TokenType.TYPE_STRING, TokenType.TYPE_NUMBER, TokenType.TYPE_BOOLEAN)),
            )
        return StatementNode.VariableNode(idNode, typeNode)
    }
}

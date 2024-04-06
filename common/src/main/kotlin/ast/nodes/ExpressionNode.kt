package org.example.ast.nodes

import kotlinx.serialization.Serializable
import org.example.token.Token

@Serializable
sealed class ExpressionNode : Node() {
    @Serializable
    data class BinaryOperationNode(val token: Token, val leftChild: ExpressionNode, val rightChild: ExpressionNode) : ExpressionNode()

    @Serializable
    data class LiteralNode(val token: Token) : ExpressionNode()

    @Serializable
    data class IdentifierNode(val token: Token) : ExpressionNode()

    @Serializable
    data class TypeNode(val token: Token) : ExpressionNode()

    fun token(): Token{
        return when (this) {
            is BinaryOperationNode -> token
            is LiteralNode -> token
            is IdentifierNode -> token
            is TypeNode -> token
        }
    }
}

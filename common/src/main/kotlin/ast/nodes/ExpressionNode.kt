package org.example.ast.nodes

import kotlinx.serialization.Serializable
import org.example.token.Token

@Serializable
sealed class ExpressionNode : Node() {
    @Serializable
    data class BinaryOperationNode(
        val value: Token,
        val leftChild: ExpressionNode,
        val rightChild: ExpressionNode,
    ) : ExpressionNode()

    @Serializable
    data class LiteralNode(val token: Token) : ExpressionNode()

    @Serializable
    data class IdentifierNode(val id: Token) : ExpressionNode()

    @Serializable
    data class TypeNode(val typeToken: Token) : ExpressionNode()

    fun token(): Token {
        return when (this) {
            is BinaryOperationNode -> value
            is LiteralNode -> token
            is IdentifierNode -> id
            is TypeNode -> typeToken
        }
    }
}

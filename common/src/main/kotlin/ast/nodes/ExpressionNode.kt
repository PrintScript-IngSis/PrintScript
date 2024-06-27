package org.example.ast.nodes

import kotlinx.serialization.Serializable
import org.example.token.Token

@Serializable
sealed class ExpressionNode : Node() {
    @Serializable
    data class BinaryOperationNode(val token: Token,
                                   val leftChild: ExpressionNode,
                                   val rightChild: ExpressionNode) : ExpressionNode()

    @Serializable
    data class LiteralNode(val token: Token) : ExpressionNode()

    @Serializable
    sealed class IdNode : ExpressionNode() {
        @Serializable
        data class IdentifierNode(val token: Token) : IdNode()

        @Serializable
        data class CreateIdentifierNode(val token: Token,
                                        val mutable: Boolean) : IdNode()
    }

    @Serializable
    data class VariableNode(val identifier: IdNode.CreateIdentifierNode,
                            val dataType: TypeNode) : ExpressionNode()

    @Serializable
    data class TypeNode(val token: Token) : ExpressionNode()

    @Serializable
    data class InputNode(val token: Token) : ExpressionNode()

    @Serializable
    data class ReadEnvNode(val token: Token,
                           val variable: IdNode.IdentifierNode) : ExpressionNode()

    fun token(): Token {
        return when (this) {
            is BinaryOperationNode -> token
            is LiteralNode -> token
            is IdNode.IdentifierNode -> token
            is IdNode.CreateIdentifierNode -> token
            is TypeNode -> token
            is VariableNode -> identifier.token()
            is InputNode -> token
            is ReadEnvNode -> token
        }
    }
}

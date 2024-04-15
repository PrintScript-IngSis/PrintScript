package org.example.ast.nodes

import kotlinx.serialization.Serializable

@Serializable
sealed class StatementNode : Node() {
    @Serializable
    data class AssignationNode(val identifier: ExpressionNode.IdentifierNode, val expression: ExpressionNode) : StatementNode()

    @Serializable
    data class DeclarationNode(val variable: ExpressionNode.VariableNode, val expression: ExpressionNode) : StatementNode()

    @Serializable
    data class IfNode(
        val condition: ExpressionNode.IdentifierNode,
        val trueStatementNode: StatementNode,
        val falseStatementNode: StatementNode?,
    ) : StatementNode()

    @Serializable
    data class PrintNode(val printable: ExpressionNode) : StatementNode()
}

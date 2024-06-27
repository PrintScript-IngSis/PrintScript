package org.example.ast.nodes

import kotlinx.serialization.Serializable

@Serializable
sealed class StatementNode : Node() {
    @Serializable
    data class AssignationNode(
        val identifier: ExpressionNode.IdNode.IdentifierNode,
        val expression: ExpressionNode,
    ) : StatementNode()

    @Serializable
    data class DeclarationAndAssignationNode(
        val variable: ExpressionNode.VariableNode,
        val expression: ExpressionNode,
    ) : StatementNode()

    @Serializable
    data class DeclarationNode(val variable: ExpressionNode.VariableNode) : StatementNode()

    @Serializable
    data class IfNode(
        val condition: ExpressionNode.IdNode.IdentifierNode,
        val trueStatementNode: StatementNode,
        val falseStatementNode: StatementNode?,
    ) : StatementNode()

    @Serializable
    data class PrintNode(val printable: ExpressionNode) : StatementNode()
}

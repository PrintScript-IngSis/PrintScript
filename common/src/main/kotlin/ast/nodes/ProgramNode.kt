package org.example.ast.nodes

import kotlinx.serialization.Serializable

@Serializable
data class ProgramNode(private val statements: List<StatementNode>) : Node() {
    fun getStatements(): List<StatementNode> {
        return statements
    }
}

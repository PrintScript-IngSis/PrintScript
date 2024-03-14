package org.example.ast

import org.example.ast.nodes.*

interface NodeVisitor<T> {
    fun visit(declarationNode: DeclarationNode): T
    fun visit(assignationNode: AssignationNode): T
    fun visit(binaryDeclarationNode: BinaryDeclarationNode): T
    fun visit(literalNode: LiteralNode): T
    fun visit(identifierNode: IdentifierNode): T
    fun visit(programNode: ProgramNode): T
}
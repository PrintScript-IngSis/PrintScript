package org.example.interpreter

import org.example.SwitchType
import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.Node
import org.example.ast.nodes.ProgramNode
import org.example.ast.nodes.StatementNode
import org.example.factories.Literal
import org.example.token.TokenType

class InterpreterImpl() : Interpreter {
    private var variables = mapOf<String, Literal>()

    fun getVariables(): Map<String, Literal> {
        return variables
    }

    override fun interpret(ast: ProgramNode) {
        val statements = ast.getStatements()
        for (statement in statements) {
            interpretStatementNode(statement)
        }
    }

    private fun interpretStatementNode(node: StatementNode) {
        when (node) {
            is StatementNode.PrintNode -> println(interpretPrintNode(node))
            is StatementNode.DeclarationAndAssignationNode -> interpretDeclarationAndAssignationNode(node)
            is StatementNode.DeclarationNode -> interpretDeclarationNode(node)
            is StatementNode.AssignationNode -> interpretAssignationNode(node)
            is StatementNode.IfNode -> interpretIfNode(node)
        }
    }

    private fun interpretDeclarationNode(node: StatementNode.DeclarationNode) {
        val id = node.variable.identifier.token().value
        if (variables.containsKey(id)) {
            throw Exception("Variable $id already exists")
        }
        val type = SwitchType.typeToLiteral(node.variable.dataType.token.type)
        val map = variables.toMutableMap()
        map[id] = Literal("", type, node.variable.identifier.mutable)
        variables = map.toMap()
    }

    private fun interpretIfNode(node: StatementNode.IfNode) {
        val condition = node.condition
        val trueStatement = node.trueStatementNode
        val falseStatement = node.falseStatementNode
        val variable = variables.getValue(condition.token.value).type
        if (variable === TokenType.LITERAL_BOOLEAN) {
            if (variables.getValue(condition.token.value).value == "true") {
                interpretStatementNode(trueStatement)
            } else if (variables.getValue(condition.token.value).value == "false") {
                if (falseStatement != null) {
                    interpretStatementNode(falseStatement)
                }
            }
        } else {
            throw Exception("Invalid Argument Type for if statement")
        }
    }

    private fun interpretPrintNode(node: StatementNode.PrintNode): String {
        return when (val printable = node.printable) {
            is ExpressionNode.LiteralNode -> {
                node.printable.token().value
            }
            is ExpressionNode.IdNode -> {
                printValueOfId(printable)
            }
            is ExpressionNode.BinaryOperationNode -> {
                getExpression(printable, true).value
            }
            else -> throw Exception("Unknown node type")
        }
    }

    private fun printValueOfId(node: ExpressionNode.IdNode): String {
        val id = node.token().value
        if (variables.containsKey(id)) {
            return variables.getValue(id).value
        } else {
            throw Exception("Variable $id not found")
        }
    }

    private fun interpretDeclarationAndAssignationNode(node: StatementNode.DeclarationAndAssignationNode) {
        val expression: Literal
        val id: String = node.variable.identifier.token().value
        if (variables.containsKey(id)) {
            throw Exception("Variable $id already exists")
        }
        if (node.expression is ExpressionNode.InputNode) {
            val type = SwitchType.typeToLiteral(node.variable.dataType.token.type)
            println(node.expression.token().value)
            var value = readln()
            value = if (type == TokenType.LITERAL_NUMBER) value.toDouble().toString() else value
            expression = Literal(value, type, node.variable.identifier.mutable)
        } else {
            expression = getExpression(node.expression, node.variable.identifier.mutable)
        }
        if (expression.type != SwitchType.typeToLiteral(node.variable.dataType.token.type)) {
            throw Exception("Type mismatch")
        }
        val map = variables.toMutableMap()
        map[id] = expression
        variables = map.toMap()
    }

    private fun getExpression(
        node: Node,
        mutable: Boolean,
    ): Literal {
        return when (node) {
            is ExpressionNode.BinaryOperationNode -> {
                binaryExpression(node, mutable)
            }
            is ExpressionNode.LiteralNode -> {
                Literal(node.token.value, node.token.type, mutable)
            }
            is ExpressionNode.TypeNode -> {
                Literal(node.token.value, node.token.type, mutable)
            }
            is ExpressionNode.IdNode -> {
                identifierExpression(node, mutable)
            }
            else -> throw Exception("Unknown ${node::class.simpleName}\" type")
        }
    }

    private fun identifierExpression(
        node: ExpressionNode.IdNode,
        mutable: Boolean,
    ): Literal {
        val id = node.token().value
        if (variables.containsKey(id)) {
            return Literal(
                variables.getValue(id).value,
                variables.getValue(id).type,
                mutable,
            )
        } else {
            throw Exception("Variable $id not found")
        }
    }

    private fun binaryExpression(
        node: ExpressionNode.BinaryOperationNode,
        mutable: Boolean,
    ): Literal {
        val left = getExpression(node.leftChild, mutable)
        val right = getExpression(node.rightChild, mutable)

        return when (node.token.type) {
            TokenType.OPERATOR_PLUS -> {
                evaluateAddition(left, right, mutable)
            }

            TokenType.OPERATOR_MINUS -> {
                evaluateSubtraction(left, right, mutable)
            }

            TokenType.OPERATOR_MULTIPLY -> {
                evaluateMultiplication(left, right, mutable)
            }

            TokenType.OPERATOR_DIVIDE -> {
                evaluateDivision(left, right, mutable)
            }

            TokenType.LITERAL_NUMBER -> {
                return Literal((node.token.value), node.token.type, mutable)
            }

            else -> throw Exception("Unknown operator")
        }
    }

    private fun intChecker(literal: Literal): Literal {
        if (literal.type == TokenType.LITERAL_NUMBER) {
            if (literal.value.contains(".0")) {
                return Literal(literal.value.toDouble().toInt().toString(), literal.type, literal.isMutable)
            }
        }
        return literal
    }

    private fun evaluateAddition(
        left: Literal,
        right: Literal,
        mutable: Boolean,
    ): Literal {
        if (left.type == TokenType.LITERAL_NUMBER && right.type == TokenType.LITERAL_NUMBER) {
            return Literal(
                (left.value.toDouble() + right.value.toDouble()).toString(),
                TokenType.LITERAL_NUMBER,
                mutable,
            )
        }
        val literal = Literal((left.value + right.value), left.type, mutable)
        return intChecker(literal)
    }

    private fun evaluateSubtraction(
        left: Literal,
        right: Literal,
        mutable: Boolean,
    ): Literal {
        val literal =
            Literal(
                (left.value.toDouble() - right.value.toDouble()).toString(),
                left.type,
                mutable,
            )
        return intChecker(literal)
    }

    private fun evaluateMultiplication(
        left: Literal,
        right: Literal,
        mutable: Boolean,
    ): Literal {
        val literal =
            Literal(
                (left.value.toDouble() * right.value.toDouble()).toString(),
                left.type,
                mutable,
            )
        return intChecker(literal)
    }

    private fun evaluateDivision(
        left: Literal,
        right: Literal,
        mutable: Boolean,
    ): Literal {
        val literal =
            Literal(
                (left.value.toDouble() / right.value.toDouble()).toString(),
                left.type,
                mutable,
            )
        return intChecker(literal)
    }

    private fun interpretAssignationNode(node: StatementNode.AssignationNode) {
        val id = node.identifier.token.value
        if (variables.containsKey(id)) {
            val expression = getExpression(node.expression, variables.getValue(id).isMutable)
            if (variables.getValue(id).type != expression.type) {
                throw Exception("Type mismatch")
            }
            if (variables.getValue(id).isMutable) {
                val map = variables.toMutableMap()
                map[id] = expression
                variables = map.toMap()
            } else {
                throw Exception("Variable $id is not mutable")
            }
        } else {
            throw Exception("Variable $id not found")
        }
    }
}

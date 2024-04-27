package org.example.interpreter

import org.example.MutableHelper
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

    override fun interpret(ast: ProgramNode): String {
        val statements = ast.getStatements()
        var string = ""
        var newString: String
        for (statement in statements) {
            newString = interpretStatementNode(statement)
            if (newString != "") {
                if (string.isNotEmpty()) {
                    string += "\n"
                }
                string += newString
            }
        }
        return string
    }

    private fun interpretStatementNode(node: StatementNode): String {
        var string = ""
        when (node) {
            is StatementNode.PrintNode -> string = interpretPrintNode(node)
            is StatementNode.DeclarationAndAssignationNode -> interpretDeclarationAndAssignationNode(node)
            is StatementNode.DeclarationNode -> interpretDeclarationNode(node)
            is StatementNode.AssignationNode -> interpretAssignationNode(node)
            is StatementNode.IfNode -> string = interpretIfNode(node).toString()
        }
        return string
    }

    private fun interpretDeclarationNode(node: StatementNode.DeclarationNode) {
        val id = node.variable.identifier.token.value
        if (variables.containsKey(id)) {
            throw Exception("Variable $id already exists")
        }
        val type = switchType(node.variable.dataType.token.type)
        val map = variables.toMutableMap()
        map[id] = Literal("", type, true)
        variables = map.toMap()
    }

    private fun interpretIfNode(node: StatementNode.IfNode): String {
        val condition = node.condition
        val trueStatement = node.trueStatementNode
        val falseStatement = node.falseStatementNode
        val variable = variables.getValue(condition.token.value).type
        if (variable === TokenType.LITERAL_BOOLEAN) {
            if (variables.getValue(condition.token.value).value == "true") {
                return interpretStatementNode(trueStatement)
            } else if (variables.getValue(condition.token.value).value == "false") {
                return if (falseStatement != null) {
                    interpretStatementNode(falseStatement)
                } else {
                    ""
                }
            }
        } else {
            throw Exception("Invalid Argument Type for if statement")
        }
        return ""
    }

    private fun interpretPrintNode(node: StatementNode.PrintNode): String {
        return when (val printable = node.printable) {
            is ExpressionNode.LiteralNode -> {
                node.printable.token().value
            }
            is ExpressionNode.IdentifierNode -> {
                printValueOfId(printable)
            }
            is ExpressionNode.BinaryOperationNode -> {
                getExpression(printable).value
            }
            else -> throw Exception("Unknown node type")
        }
    }

    private fun printValueOfId(node: ExpressionNode.IdentifierNode): String {
        val id = node.token.value
        if (variables.containsKey(id)) {
            return variables.getValue(id).value
        } else {
            throw Exception("Variable $id not found")
        }
    }

    private fun interpretDeclarationAndAssignationNode(node: StatementNode.DeclarationAndAssignationNode) {
        val expression: Literal
        val id: String = node.variable.identifier.token.value
        if (variables.containsKey(id)) {
            throw Exception("Variable $id already exists")
        }
        if (node.expression.token().type == TokenType.KEYWORD_READ_INPUT) {
            val type = switchType(node.variable.dataType.token.type)
            println("input a $type")
            var value = readLine()
            value = if (type == TokenType.LITERAL_NUMBER) value?.toDouble()?.toString() else value
            expression = value?.let { Literal(it, type, MutableHelper.isMutable(node.expression.token())) }!!
        } else {
            expression = getExpression(node.expression)
        }
        if (expression.type != switchType(node.variable.dataType.token.type)) {
            throw Exception("Type mismatch")
        }
        val map = variables.toMutableMap()
        map[id] = expression
        variables = map.toMap()
    }

    private fun getExpression(node: Node): Literal {
        return when (node) {
            is ExpressionNode.BinaryOperationNode -> {
                binaryExpression(node)
            }
            is ExpressionNode.LiteralNode -> {
                Literal(node.token.value, node.token.type, MutableHelper.isMutable(node.token))
            }
            is ExpressionNode.IdentifierNode -> {
                identifierExpression(node)
            }
            else -> throw Exception("Unknown ${node::class.simpleName}\" type")
        }
    }

    fun switchType(type: TokenType): TokenType {
        return when (type) {
            TokenType.TYPE_NUMBER -> TokenType.LITERAL_NUMBER
            TokenType.TYPE_STRING -> TokenType.LITERAL_STRING
            TokenType.TYPE_BOOLEAN -> TokenType.LITERAL_BOOLEAN
            else -> throw Exception("Unknown type")
        }
    }

    private fun identifierExpression(node: ExpressionNode.IdentifierNode): Literal {
        val id = node.token.value
        if (variables.containsKey(id)) {
            return Literal(
                variables.getValue(id).value,
                variables.getValue(id).type,
                MutableHelper.isMutable(node.token),
            )
        } else {
            throw Exception("Variable $id not found")
        }
    }

    private fun binaryExpression(node: ExpressionNode.BinaryOperationNode): Literal {
        val left = getExpression(node.leftChild)
        val right = getExpression(node.rightChild)

        return when (node.token.type) {
            TokenType.OPERATOR_PLUS -> {
                evaluateAddition(left, right, node)
            }

            TokenType.OPERATOR_MINUS -> {
                evaluateSubtraction(left, right, node)
            }

            TokenType.OPERATOR_MULTIPLY -> {
                evaluateMultiplication(left, right, node)
            }

            TokenType.OPERATOR_DIVIDE -> {
                evaluateDivision(left, right, node)
            }

            TokenType.LITERAL_NUMBER -> {
                return Literal((node.token.value), node.token.type, MutableHelper.isMutable(node.token))
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
        node: ExpressionNode,
    ): Literal {
        if (left.type == TokenType.LITERAL_NUMBER && right.type == TokenType.LITERAL_NUMBER) {
            return Literal(
                (left.value.toDouble() + right.value.toDouble()).toString(),
                TokenType.LITERAL_NUMBER,
                MutableHelper.isMutable(node.token()),
            )
        }
        val literal = Literal((left.value + right.value), left.type, MutableHelper.isMutable(node.token()))
        return intChecker(literal)
    }

    private fun evaluateSubtraction(
        left: Literal,
        right: Literal,
        node: ExpressionNode,
    ): Literal {
        val literal =
            Literal(
                (left.value.toDouble() - right.value.toDouble()).toString(),
                left.type,
                MutableHelper.isMutable(node.token()),
            )
        return intChecker(literal)
    }

    private fun evaluateMultiplication(
        left: Literal,
        right: Literal,
        node: ExpressionNode,
    ): Literal {
        val literal =
            Literal(
                (left.value.toDouble() * right.value.toDouble()).toString(),
                left.type,
                MutableHelper.isMutable(node.token()),
            )
        return intChecker(literal)
    }

    private fun evaluateDivision(
        left: Literal,
        right: Literal,
        node: ExpressionNode,
    ): Literal {
        val literal =
            Literal(
                (left.value.toDouble() / right.value.toDouble()).toString(),
                left.type,
                MutableHelper.isMutable(node.token()),
            )
        return intChecker(literal)
    }

    private fun interpretAssignationNode(node: StatementNode.AssignationNode) {
        val id = node.identifier.token.value
        val expression = getExpression(node.expression)
        if (variables.containsKey(id)) {
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

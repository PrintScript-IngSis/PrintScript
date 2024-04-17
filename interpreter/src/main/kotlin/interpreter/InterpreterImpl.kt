package org.example.interpreter

import org.example.MutableHelper
import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.Node
import org.example.ast.nodes.ProgramNode
import org.example.ast.nodes.StatementNode
import org.example.factories.Literal
import org.example.token.TokenType

class InterpreterImpl(private val ast: ProgramNode) : Interpreter {
    private var variables = mapOf<String, Literal>()

    fun getVariables(): Map<String, Literal> {
        return variables
    }

    override fun interpret(): String {
        val statements = ast.getStatements()
        var string = ""
        for (statement in statements)
            string += interpretStatementNode(statement)
        return string
    }

    private fun interpretStatementNode(node: StatementNode): String {
        var string = ""
        when (node) {
            is StatementNode.PrintNode -> string = interpretPrintNode(node)
            is StatementNode.DeclarationNode -> interpretDeclarationNode(node)
            is StatementNode.AssignationNode -> interpretAssignationNode(node)
            is StatementNode.IfNode -> string = interpretIfNode(node).toString()
        }
        return string
    }

    private fun interpretIfNode(node: StatementNode.IfNode): String? {
        val condition = node.condition
        val trueStatement = node.trueStatementNode
        val falseStatement = node.falseStatementNode
        if (variables.getValue(condition.token.value).value == "true") {
            return interpretStatementNode(trueStatement)
        } else {
            return falseStatement?.let { interpretStatementNode(it) }
        }
    }

    private fun interpretPrintNode(node: StatementNode.PrintNode): String {
        return when (val printable = node.printable) {
            is ExpressionNode.LiteralNode -> {
                printLiteral(printable)
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

    private fun printLiteral(node: ExpressionNode.LiteralNode): String {
        return node.token.value
    }

    private fun printValueOfId(node: ExpressionNode.IdentifierNode): String {
        val id = node.token.value
        if (variables.containsKey(id)) {
            return variables.getValue(id).value
        } else {
            throw Exception("Variable $id not found")
        }
    }

    private fun interpretDeclarationNode(node: StatementNode.DeclarationNode) {
        val id : String
        val expression: Literal
        if(node.expression.token().type == TokenType.KEYWORD_READ_ENV){
            id = (node.expression as ExpressionNode.ReadEnvNode).variable.token.value
            if (variables.containsKey(id)) {
                expression = variables.getValue(id)
                val map = variables.toMutableMap()
                map[node.variable.identifier.token.value] = expression
                variables = map.toMap()
            } else {
                throw Exception("Variable $id not found")
            }
        }else {
            id = node.variable.identifier.token.value
            if (variables.containsKey(id)) {
                throw Exception("Variable $id already exists")
            }
            if (node.expression.token().type == TokenType.KEYWORD_READ_INPUT) {
                val type = switchType(node.variable.dataType.token.type)
                println("input a $type")
                expression = Literal(readlnOrNull().toString(), type, MutableHelper.isMutable(node.expression.token()))
            } else {
                expression = getExpression(node.expression)
            }
            val map = variables.toMutableMap()
            map[id] = expression
            variables = map.toMap()
        }
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
            is ExpressionNode.ReadEnvNode -> {
                readEnvExpression(node)
            }
            else -> throw Exception("Unknown ${node::class.simpleName}\" type")
        }
    }

    private fun readEnvExpression(node: ExpressionNode.ReadEnvNode): Literal {
        val id = node.variable.token.value
        if (variables.containsKey(id)) {
            return Literal(variables.getValue(id).value, variables.getValue(id).type, MutableHelper.isMutable(node.token))
        } else {
            throw Exception("Variable $id not found")
        }
    }

    private fun switchType(type: TokenType): TokenType {
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
            return Literal(variables.getValue(id).value, variables.getValue(id).type, MutableHelper.isMutable(node.token))
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
        return Literal((left.value + right.value), left.type, MutableHelper.isMutable(node.token()))
    }

    private fun evaluateSubtraction(
        left: Literal,
        right: Literal,
        node: ExpressionNode,
    ): Literal {
        return Literal((left.value.toDouble() - right.value.toDouble()).toString(), left.type, MutableHelper.isMutable(node.token()))
    }

    private fun evaluateMultiplication(
        left: Literal,
        right: Literal,
        node: ExpressionNode,
    ): Literal {
        return Literal((left.value.toDouble() * right.value.toDouble()).toString(), left.type, MutableHelper.isMutable(node.token()))
    }

    private fun evaluateDivision(
        left: Literal,
        right: Literal,
        node: ExpressionNode,
    ): Literal {
        return Literal((left.value.toDouble() / right.value.toDouble()).toString(), left.type, MutableHelper.isMutable(node.token()))
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

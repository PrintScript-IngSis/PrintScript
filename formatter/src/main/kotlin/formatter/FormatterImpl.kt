package org.example.formatter

import com.google.gson.Gson
import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.ProgramNode
import org.example.ast.nodes.StatementNode
import org.example.token.TokenType
import java.io.File

class FormatterImpl : Formatter {
    override fun format(ast: ProgramNode): String {
        return format(ast, File("formatter/src/main/resources/rules.json"))
    }

    fun format(
        ast: ProgramNode,
        file: File,
    ): String {
        val json = file.readText()

        val gson = Gson()
        val rulesWrapper: FormattingRulesWrapper = gson.fromJson(json, FormattingRulesWrapper::class.java)
        val rules = rulesWrapper.rules

        return evaluate(ast, rules)
    }

    private fun evaluate(
        ast: ProgramNode,
        rules: FormattingRules,
    ): String {
        var string = ""
        for (statement in ast.getStatements()) {
            string +=
                when (statement) {
                    is StatementNode.PrintNode -> {
                        evaluatePrintNode(statement, rules)
                    }

                    is StatementNode.DeclarationNode -> {
                        evaluateDeclarationNode(statement, rules)
                    }

                    is StatementNode.AssignationNode -> {
                        evaluateAssignationNode(statement, rules)
                    }

                    else -> throw Exception("Unknown node type")
                }
        }
        return string
    }

    private fun evaluatePrintNode(
        node: StatementNode.PrintNode,
        rules: FormattingRules,
    ): String {
        var string = ""
        string += newLines(rules.numberNewLinesBeforePrint)
        string += "println("
        string += evaluateExpressionNode(node.printable, rules)
        string += ");"
        string += "\n"
        return string
    }

    private fun evaluateDeclarationNode(
        node: StatementNode.DeclarationNode,
        rules: FormattingRules,
    ): String {
        var string = "let "
        val id = node.variable.identifier.token.value
        string += id
        string += spacesBetweenOperator(rules.numberSpacesBeforeColon, rules.numberSpaceAfterColon, ":")
        string += type(node.variable.dataType.token.type)
        string += spacesBetweenOperator(rules.numberSpaceBeforeAssignation, rules.numberSpaceAfterAssignation, "=")
        string += evaluateExpressionNode(node.expression, rules)
        string += ";"
        string += "\n"
        return string
    }

    private fun evaluateAssignationNode(
        node: StatementNode.AssignationNode,
        rules: FormattingRules,
    ): String {
        var string = node.identifier.token .value
        string += spacesBetweenOperator(rules.numberSpaceBeforeAssignation, rules.numberSpaceAfterAssignation, "=")
        string += evaluateExpressionNode(node.expression, rules)
        string += ";"
        string += "\n"
        return string
    }

    private fun evaluateExpressionNode(
        node: ExpressionNode,
        rules: FormattingRules,
    ): String {
        return when (node) {
            is ExpressionNode.BinaryOperationNode -> {
                evaluateBinaryOperationNode(node, rules)
            }

            is ExpressionNode.LiteralNode -> {
                node.token.value.toDouble().toInt().toString()
            }

            is ExpressionNode.IdentifierNode -> {
                node.token.value
            }

            is ExpressionNode.TypeNode -> {
                node.token.value
            }

            else -> throw Exception("Unknown node type")
        }
    }

    private fun evaluateBinaryOperationNode(
        node: ExpressionNode.BinaryOperationNode,
        rules: FormattingRules,
    ): String {
        val left = evaluateExpressionNode(node.leftChild, rules)
        val right = evaluateExpressionNode(node.rightChild, rules)
        val operation = node.token.value
        return "$left $operation $right"
    }

    private fun type(type: TokenType): String {
        return when (type) {
            TokenType.TYPE_NUMBER -> "number"
            TokenType.TYPE_STRING -> "string"
            TokenType.TYPE_BOOLEAN -> "boolean"
            else -> throw Exception("Unknown type")
        }
    }

    private fun spacesBetweenOperator(
        before: Int,
        after: Int,
        operator: String,
    ): String {
        var string = ""
        string += " ".repeat(before)
        string += operator
        string += " ".repeat(after)
        return string
    }

    private fun newLines(before: Int): String {
        var string = ""
        string += "\n".repeat(before)
        return string
    }
}

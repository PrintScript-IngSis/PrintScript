package linter

import com.google.gson.Gson
import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.Node
import org.example.ast.nodes.ProgramNode
import org.example.ast.nodes.StatementNode
import org.example.token.Token
import java.io.File

class LinterImpl : Linter {
    override fun checkErrors(
        ast: ProgramNode,
        path: String,
    ): List<Error> {
        return check(ast, File(path))
    }

    private fun check(
        ast: ProgramNode,
        file: File,
    ): List<Error> {
        val json = file.readText()
        val gson = Gson()
        val rulesWrapper: LinterRules.LinterRulesWrapper = gson.fromJson(json, LinterRules.LinterRulesWrapper::class.java)
        val rules = rulesWrapper.rules
        return evaluate(ast, rules)
    }

    private fun evaluate(
        ast: ProgramNode,
        rules: LinterRules.LinterRules,
    ): List<Error> {
        val errors = mutableListOf<Error>()
        for (statement in ast.getStatements()) {
            errors += (switchNode(statement, rules, errors))
        }
        return errors
    }

    private fun switchNode(
        node: Node,
        rules: LinterRules.LinterRules,
        errors: List<Error>,
    ): List<Error> {
        val error = mutableListOf<Error>()
        when (node) {
            is StatementNode.PrintNode -> {
                error += evaluatePrintNode(node, rules, errors)
                error += switchNode(node.printable, rules, errors)
                return error
            }
            is ExpressionNode.BinaryOperationNode -> {
                error += switchNode(node.leftChild, rules, errors)
                error += switchNode(node.rightChild, rules, errors)
                return error
            }
            is ExpressionNode.IdentifierNode -> {
                return evaluateIdentifierNode(node, rules, errors)
            }
            is StatementNode.AssignationNode -> {
                return evaluateIdentifierNode(node.identifier, rules, errors)
            }
            is StatementNode.VariableNode -> {
                return evaluateIdentifierNode(node.identifier, rules, errors)
            }
            is StatementNode.DeclarationNode -> {
                return evaluateIdentifierNode(node.variable.identifier, rules, errors)
            }
            else -> return error
        }
    }

    private fun evaluateIdentifierNode(
        node: ExpressionNode.IdentifierNode,
        rules: LinterRules.LinterRules,
        errors: List<Error>,
    ): List<Error> {
        val clonedErrors = errors.toMutableList()
        clonedErrors +=
            if (rules.idFormatCamelCase) {
                evaluateIfCamelCase(node.token)
            } else {
                evaluateIfSnakeCase(node.token)
            }
        return clonedErrors.toList()
    }

    private fun evaluateIfCamelCase(node: Token): List<Error> {
        val error = listOf<Error>()
        if (!evaluateIfCamelCase(node.value)) {
            return error +
                (
                    Error(
                        "Identifier ${node.value} is not in camelCase format in line " +
                            "${node.position.line} and column " +
                            "${node.position.column}",
                    )
                )
        }
        return error
    }

    private fun evaluateIfSnakeCase(node: Token): List<Error> {
        val error = listOf<Error>()
        if (!evaluateIfSnakeCase(node.value)) {
            return error +
                (
                    Error(
                        "Identifier ${node.value} is not in snake_case format in line " +
                            "${node.position.line} and column " +
                            "${node.position.column}",
                    )
                )
        }
        return error
    }

    private fun evaluateIfCamelCase(id: String): Boolean {
        val matches = id.matches(Regex("\\b([a-z]+[0-9]*)([A-Z][a-z0-9]+)+\\b"))
        return matches
    }

    private fun evaluateIfSnakeCase(id: String): Boolean {
        return id.matches(Regex("\\b([a-z]+)_([a-z]+)+\\b"))
    }

    private fun evaluatePrintNode(
        node: StatementNode.PrintNode,
        rules: LinterRules.LinterRules,
        errors: List<Error>,
    ): List<Error> {
        val clonedErrors = errors.toList()
        if (!rules.operationInPrintln) {
            return clonedErrors + evaluateIfOperationInPrintln(node)
        }
        return clonedErrors
    }

    private fun evaluateIfOperationInPrintln(node: StatementNode.PrintNode): List<Error> {
        val error = listOf<Error>()
        if (node.printable is ExpressionNode.BinaryOperationNode) {
            return error +
                Error(
                    "Binary operation in println in line " +
                        "${node.printable.token().position.line} and column " +
                        "${node.printable.token().position.column}",
                )
        }
        return error
    }
}

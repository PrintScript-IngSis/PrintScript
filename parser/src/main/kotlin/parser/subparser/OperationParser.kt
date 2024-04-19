package org.example.parser.subparser

import org.example.ast.nodes.ExpressionNode
import org.example.token.Token
import org.example.token.TokenType

class OperationParser {
    companion object {
        fun createValueNode(iterator: ListIterator<Token>): ExpressionNode? {
            if (!iterator.hasNext()) return null

            // Try parsing complex expressions first
            val complexNode = createComplexExpressionNode(iterator)
            if (complexNode != null) return complexNode

            // If no complex expression found, try parsing literals
            val token = iterator.next()
            return when (token.type) {
                TokenType.LITERAL_NUMBER, TokenType.LITERAL_STRING -> ExpressionNode.LiteralNode(token)
                TokenType.IDENTIFIER -> ExpressionNode.IdentifierNode(token)
                else -> error("Unexpected token: $token")
            }
        }

        private fun createComplexExpressionNode(iterator: ListIterator<Token>): ExpressionNode? {
            var node = createTermNode(iterator)
            while (iterator.hasNext()) {
                val token = iterator.next()
                if (token.type in listOf(TokenType.OPERATOR_PLUS, TokenType.OPERATOR_MINUS)) {
                    val rightNode = createTermNode(iterator) ?: error("Expected term after operator")
                    node = ExpressionNode.BinaryOperationNode(token, node!!, rightNode)
                } else {
                    iterator.previous()
                    break
                }
            }
            return node
        }

        private fun createTermNode(iterator: ListIterator<Token>): ExpressionNode? {
            var node = createFactorNode(iterator)
            while (iterator.hasNext()) {
                val token = iterator.next()
                if (token.type in listOf(TokenType.OPERATOR_MULTIPLY, TokenType.OPERATOR_DIVIDE)) {
                    val rightNode = createFactorNode(iterator) ?: error("Expected factor after operator")
                    node = ExpressionNode.BinaryOperationNode(token, node!!, rightNode)
                } else {
                    iterator.previous()
                    break
                }
            }
            return node
        }

        private fun createFactorNode(iterator: ListIterator<Token>): ExpressionNode? {
            if (!iterator.hasNext()) return null
            val token = iterator.next()
            return when (token.type) {
                TokenType.LITERAL_NUMBER, TokenType.LITERAL_STRING, TokenType.LITERAL_BOOLEAN -> ExpressionNode.LiteralNode(token)
                TokenType.IDENTIFIER -> ExpressionNode.IdentifierNode(token)
                TokenType.PARENTHESIS_OPEN -> {
                    val node = createComplexExpressionNode(iterator) ?: error("Expected expression inside brackets")
                    if (!iterator.hasNext() || iterator.next().type != TokenType.PARENTHESIS_CLOSE) {
                        error("Expected closing bracket")
                    }
                    node
                }
                TokenType.KEYWORD_READ_INPUT -> ExpressionNode.InputNode(token)
                TokenType.KEYWORD_READ_ENV -> {
                    val variable =
                        createFactorNode(iterator) as? ExpressionNode.IdentifierNode
                            ?: error("Expected identifier after read_env keyword")
                    ExpressionNode.ReadEnvNode(token, variable)
                }
                else -> error("Unexpected token: $token")
            }
        }
    }
}

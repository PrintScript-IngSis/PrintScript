package org.example.parser.subparser

import org.example.ast.nodes.StatementNode
import org.example.parser.Parser
import org.example.parser.subparsers.OperationCropper
import org.example.token.Token
import org.example.token.TokenType

class PrintlnParser(private val tokens: List<Token>) : Parser {
    override fun parse(): StatementNode {
        val valueNode =
            OperationParser.createValueNode(
                OperationCropper.crop(tokens, TokenType.KEYWORD_PRINTLN).listIterator(),
            )
                ?: throw Exception("Expected value after print operator")
        return StatementNode.PrintNode(valueNode)
    }
}

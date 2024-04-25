package org.example.parser.subparser

import org.example.ast.nodes.StatementNode
import org.example.parser.subparsers.OperationCropper
import org.example.token.Token
import org.example.token.TokenType

class PrintlnParser() : Subparser {
    override fun canParse(tokens: List<Token>): Boolean {
        return tokens[0].type == TokenType.KEYWORD_PRINTLN
    }

    override fun parse(tokens: List<Token>): StatementNode {
        val valueNode =
            OperationParser.createValueNode(
                OperationCropper.crop(tokens, TokenType.KEYWORD_PRINTLN).listIterator(),
            )
                ?: throw Exception("Expected value after print operator")
        return StatementNode.PrintNode(valueNode)
    }
}

package org.example.parser.subparser

import org.example.SwitchType
import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.StatementNode
import org.example.parser.TokenSearcher
import org.example.parser.subparsers.OperationCropper
import org.example.token.Token
import org.example.token.TokenType

class DeclarationAndAssignationParser() : Subparser {
    override fun canParse(tokens: List<Token>): Boolean {
        val startsWithLetOrConst = tokens[0].type == TokenType.KEYWORD_LET || tokens[0].type == TokenType.KEYWORD_CONST
        val isAssignation = tokens.any { it.type == TokenType.ASSIGNATOR }
        return startsWithLetOrConst && isAssignation
    }

    override fun parse(tokens: List<Token>): StatementNode {
        val valueNode =
            OperationParser.createValueNode(
                OperationCropper.crop(tokens, TokenType.ASSIGNATOR).listIterator(),
            )
                ?: throw Exception("Expected value after assignment operator")
        return StatementNode.DeclarationAndAssignationNode(createVariableNode(tokens), valueNode)
    }

    private fun createVariableNode(tokens: List<Token>): ExpressionNode.VariableNode {
        val mutable = tokens[0].type == TokenType.KEYWORD_LET
        val idNode = ExpressionNode.IdNode.CreateIdentifierNode(TokenSearcher.searchForToken(tokens, listOf(TokenType.IDENTIFIER)), mutable)
        val token = TokenSearcher.searchForToken(tokens, listOf(TokenType.TYPE_NUMBER, TokenType.TYPE_STRING, TokenType.TYPE_BOOLEAN))
        val typeNode = ExpressionNode.TypeNode(Token(SwitchType.typeToLiteral(token.type), token.value, token.position))
        return ExpressionNode.VariableNode(idNode, typeNode)
    }
}

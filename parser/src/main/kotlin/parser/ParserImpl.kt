package org.example.parser

import org.example.ast.nodes.Node
import org.example.ast.nodes.ProgramNode
import org.example.parser.subparser.DeclarationParser
import org.example.parser.subparser.PrintlnParser
import org.example.parser.subparser.ReassignationParser
import org.example.token.Token
import org.example.token.TokenType

class ParserImpl(private val tokens: List<Token>) : Parser {
    override fun parse(): ProgramNode {
        val statements = separateStatements(tokens)
        val nodes = mutableListOf<Node>()
        for (statement in statements) {
            nodes.add(parseStatement(statement))
        }
        return ProgramNode(nodes)
    }

    private fun parseStatement(tokens: List<Token>): Node {
        val firstToken = tokens[0]
        return when (firstToken.type) {
            TokenType.KEYWORD_LET, TokenType.KEYWORD_CONST -> startAssignationStatement(tokens) // skip Node
            TokenType.OPERATOR_PRINTLN -> startPrintStatement(tokens) // skip Node
            TokenType.IDENTIFIER -> startReasignationStatement(tokens) // identifier Node
            else -> throw Exception("Invalid statement")
        }
    }

    private fun startAssignationStatement(tokens: List<Token>): Node {
        val declarationParser = DeclarationParser(tokens)
        return declarationParser.parse()
    }

    private fun startPrintStatement(tokens: List<Token>): Node {
        val printParser = PrintlnParser(tokens)
        return printParser.parse()
    }

    private fun startReasignationStatement(tokens: List<Token>): Node {
        val reasignationParser = ReassignationParser(tokens)
        return reasignationParser.parse()
    }

    private fun separateStatements(tokens: List<Token>): List<List<Token>> {
        val newList = mutableListOf<List<Token>>()
        var accumulated = mutableListOf<Token>()
        for (token in tokens) {
            if (token.type == TokenType.SEMICOLON) {
                newList.add(accumulated)
                accumulated = mutableListOf()
            } else {
                accumulated.add(token)
            }
        }
        if (accumulated.isNotEmpty()) {
            newList.add(accumulated)
        }
        return newList
    }
}

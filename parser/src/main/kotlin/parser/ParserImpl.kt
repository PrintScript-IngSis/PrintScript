package org.example.parser

import org.example.ast.nodes.ProgramNode
import org.example.ast.nodes.StatementNode
import org.example.parser.subparser.DeclarationParser
import org.example.parser.subparser.IfParser
import org.example.parser.subparser.PrintlnParser
import org.example.parser.subparser.ReassignationParser
import org.example.token.Token
import org.example.token.TokenType

class ParserImpl(private val tokens: List<Token>) : Parser {
    override fun parse(): ProgramNode {
        val statements = separateStatements(tokens)
        val nodes = mutableListOf<StatementNode>()
        for (statement in statements) {
            nodes.add(parseStatement(statement))
        }
        return ProgramNode(nodes.toList())
    }

    private fun parseStatement(tokens: List<Token>): StatementNode {
        val firstToken = tokens[0]
        return when (firstToken.type) {
            TokenType.KEYWORD_LET, TokenType.KEYWORD_CONST -> startAssignationStatement(tokens) // skip Node
            TokenType.KEYWORD_PRINTLN -> startPrintStatement(tokens) // skip Node
            TokenType.IDENTIFIER -> startReasignationStatement(tokens) // identifier Node
            TokenType.KEYWORD_IF -> startIfStatement(tokens) // if Node
            else -> throw Exception("Invalid statement")
        }
    }

    private fun startIfStatement(tokens: List<Token>): StatementNode {
        val ifParser = IfParser(tokens)
        return ifParser.parse()
    }

    private fun startAssignationStatement(tokens: List<Token>): StatementNode {
        val declarationParser = DeclarationParser(tokens)
        return declarationParser.parse()
    }

    private fun startPrintStatement(tokens: List<Token>): StatementNode {
        val printParser = PrintlnParser(tokens)
        return printParser.parse()
    }

    private fun startReasignationStatement(tokens: List<Token>): StatementNode {
        val reasignationParser = ReassignationParser(tokens)
        return reasignationParser.parse()
    }

    private fun separateStatements(tokens: List<Token>): List<List<Token>> {
        val newList = mutableListOf<List<Token>>()
        var accumulated = mutableListOf<Token>()
        var bracketCounter = 0
        for (token in tokens) {
            if (token.type == TokenType.BRACKET_OPEN) {
                bracketCounter++
                accumulated.add(token)
            } else if (token.type == TokenType.BRACKET_CLOSE) {
                bracketCounter--
                accumulated.add(token)
            } else if (token.type == TokenType.SEMICOLON && bracketCounter == 0) {
                newList.add(accumulated)
                accumulated = mutableListOf()
            } else {
                accumulated.add(token)
            }
        }
        if (accumulated.isNotEmpty()) {
            if (accumulated.last().type != TokenType.SEMICOLON) {
                throw Exception("Unfinished statement")
            }
            newList.add(accumulated)
        }
        return newList
    }
}

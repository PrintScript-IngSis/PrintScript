package org.example.parser

import org.example.ast.nodes.ProgramNode
import org.example.ast.nodes.StatementNode
import org.example.parser.subparser.DeclarationAndAssignationParser
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
            TokenType.KEYWORD_LET, TokenType.KEYWORD_CONST -> checkIfDeclarationOrAsignation(tokens) // skip Node
            TokenType.KEYWORD_PRINTLN -> startPrintStatement(tokens) // skip Node
            TokenType.IDENTIFIER -> startReasignationStatement(tokens) // identifier Node
            TokenType.KEYWORD_IF -> startIfStatement(tokens) // if Node
            else -> throw Exception("Invalid statement")
        }
    }

    private fun checkIfDeclarationOrAsignation(tokens: List<Token>): StatementNode {
        for (token in tokens) {
            if (token.type == TokenType.ASSIGNATOR) {
                return startAssignationStatement(tokens)
            }
        }
        return startDeclarationStatement(tokens)
    }

    private fun startDeclarationStatement(tokens: List<Token>): StatementNode {
        val declarationParser = DeclarationParser(tokens)
        return declarationParser.parse()
    }

    private fun startIfStatement(tokens: List<Token>): StatementNode {
        val ifParser = IfParser(tokens)
        return ifParser.parse()
    }

    private fun startAssignationStatement(tokens: List<Token>): StatementNode {
        val declarationAndAssignationParser = DeclarationAndAssignationParser(tokens)
        return declarationAndAssignationParser.parse()
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
        var bracketCounterIf = 0
        var foundElse = false
        for (token in tokens) {
            if (token.type == TokenType.BRACKET_OPEN) {
                bracketCounterIf++
                accumulated.add(token)
            } else if (token.type == TokenType.BRACKET_CLOSE) {
                bracketCounterIf--
                accumulated.add(token)
                if (bracketCounterIf == 0) {
                    if (foundElse) {
                        newList.add(accumulated)
                        accumulated = mutableListOf()
                        foundElse = false
                    } else {
                        // should check if there is a following else statement here (keep going if it is the case, otherwise add to the list)
                        foundElse = true
                    }
                }
            } else if (token.type == TokenType.SEMICOLON && bracketCounterIf == 0) {
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

package org.example.parser

import org.example.ast.nodes.ProgramNode
import org.example.ast.nodes.StatementNode
import org.example.parser.subparser.CompositeParser
import org.example.parser.subparser.DeclarationAndAssignationParser
import org.example.parser.subparser.DeclarationParser
import org.example.parser.subparser.IfParser
import org.example.parser.subparser.PrintlnParser
import org.example.parser.subparser.ReassignationParser
import org.example.token.Token
import org.example.token.TokenType

class ParserImpl() : Parser {
    override fun parse(tokens: List<Token>): ProgramNode {
        val statements = separateStatements(tokens)
        val nodes = mutableListOf<StatementNode>()
        for (statement in statements) {
            nodes.add(parseStatement(statement))
        }
        return ProgramNode(nodes.toList())
    }

    private fun parseStatement(tokens: List<Token>): StatementNode {
        val compositeParser =
            CompositeParser(
                listOf(
                    PrintlnParser(),
                    DeclarationParser(),
                    ReassignationParser(),
                    IfParser(),
                    DeclarationAndAssignationParser(),
                ),
            )
        return compositeParser.parse(tokens)
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
            if (accumulated.last().type != TokenType.SEMICOLON) {
                throw Exception("Unfinished statement")
            }
            newList.add(accumulated)
        }
        return newList
    }
}

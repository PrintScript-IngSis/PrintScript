import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.ProgramNode
import org.example.ast.nodes.StatementNode
import org.example.factories.Position
import org.example.parser.Parser
import org.example.parser.ParserImpl
import org.example.token.Token
import org.example.token.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class ParserTest {
    @Test
    fun testParserWhenGivenCorrectInformationShouldBuildCorrectlyAst() {
        val tokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 2)),
                Token(TokenType.COLON, ":", Position(0, 4)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 21)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 8)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 10)),
                Token(TokenType.SEMICOLON, ";", Position(0, 12)),
            )
        val parser: Parser = ParserImpl(tokens)
        val ast = parser.parse()

        val expectedAST =
            ProgramNode(
                listOf(
                    StatementNode.DeclarationNode(
                        StatementNode.VariableNode(
                            ExpressionNode.IdentifierNode(Token(TokenType.IDENTIFIER, "x", Position(0, 2))),
                            ExpressionNode.TypeNode(Token(TokenType.TYPE_NUMBER, "number", Position(0, 21))),
                        ),
                        ExpressionNode.LiteralNode(Token(TokenType.LITERAL_NUMBER, "5", Position(0, 10))),
                    ),
                ),
            )
        assertEquals(expectedAST, ast)
    }

    @Test
    fun testParserWhenGivenIncorrectInformationShouldThrowException() {
        val tokens =
            listOf(
                Token(TokenType.OPERATOR_PLUS, "+", Position(0, 0)),
            )

        val parser: Parser = ParserImpl(tokens)

        try {
            parser.parse()
        } catch (e: Exception) {
            assertEquals("Invalid statement", e.message)
        }
    }

    @Test
    fun testParserWhenGivenAnIncorrectStatementOfNotALiteralFollowingAnAssignationShouldThrowException() {
        val tokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 2)),
                Token(TokenType.COLON, ":", Position(0, 4)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 21)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 8)),
                Token(TokenType.OPERATOR_PLUS, "+", Position(0, 10)),
                Token(TokenType.SEMICOLON, ";", Position(0, 12)),
            )

        val parser: Parser = ParserImpl(tokens)

        try {
            parser.parse()
        } catch (e: Exception) {
            assertEquals("Unexpected token: Token(type=OPERATOR_PLUS, value=+, position=Position(line=0, column=10))", e.message)
        }
    }

    @Test
    fun testParserWhenGivenAnIncorrectStatementOfParenthesisEmptyShouldThrowException() {
        val tokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 2)),
                Token(TokenType.COLON, ":", Position(0, 4)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 21)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 8)),
                Token(TokenType.PARENTHESIS_OPEN, "(", Position(0, 10)),
                Token(TokenType.PARENTHESIS_CLOSE, ")", Position(0, 12)),
                Token(TokenType.SEMICOLON, ";", Position(0, 14)),
            )

        val parser: Parser = ParserImpl(tokens)

        try {
            parser.parse()
        } catch (e: Exception) {
            assertEquals("Unexpected token: Token(type=PARENTHESIS_CLOSE, value=), position=Position(line=0, column=12))", e.message)
        }
    }
}

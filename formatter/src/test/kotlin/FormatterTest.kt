import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.ProgramNode
import org.example.ast.nodes.StatementNode
import org.example.factories.Position
import org.example.formatter.FormatterImpl
import org.example.token.Token
import org.example.token.TokenType
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterTest {
    @Test
    fun testFormatPrintNode() {
        val tokens =
            ProgramNode(
                listOf(
                    StatementNode.PrintNode(
                        ExpressionNode.LiteralNode(Token(TokenType.LITERAL_NUMBER, "4.0", Position(0, 0))),
                    ),
                ),
            )
        val formatter = FormatterImpl()
        val formatted = formatter.format(tokens, "src/test/resources/Rules.json")
        assertEquals("\n\nprintln(4);\n", formatted)
    }

    @Test
    fun testFormatDeclarationNode() {
        val tokens =
            ProgramNode(
                listOf(
                    StatementNode.DeclarationNode(
                        variable =
                            StatementNode.VariableNode(
                                identifier = ExpressionNode.IdentifierNode(Token(TokenType.IDENTIFIER, "x", Position(2, 0))),
                                dataType = ExpressionNode.TypeNode(Token(TokenType.TYPE_NUMBER, "number", Position(21, 0))),
                            ),
                        expression = ExpressionNode.LiteralNode(Token(TokenType.LITERAL_NUMBER, "5", Position(30, 0))),
                    ),
                ),
            )

        val formatter = FormatterImpl()
        val formatted = formatter.format(tokens, "src/test/resources/Rules.json")
        assertEquals("let x : number = 5;\n", formatted)
    }

    @Test
    fun testFormatAssignationNode() {
        val tokens =
            ProgramNode(
                listOf(
                    StatementNode.AssignationNode(
                        identifier = ExpressionNode.IdentifierNode(Token(TokenType.IDENTIFIER, "x", Position(2, 0))),
                        expression = ExpressionNode.LiteralNode(Token(TokenType.LITERAL_NUMBER, "5", Position(30, 0))),
                    ),
                ),
            )

        val formatter = FormatterImpl()
        val formatted = formatter.format(tokens, "src/test/resources/Rules.json")
        assertEquals("x = 5;\n", formatted)
    }

    @Test
    fun testFormatAssignationNodeWithBinaryOperation() {
        val tokens =
            ProgramNode(
                listOf(
                    StatementNode.AssignationNode(
                        identifier = ExpressionNode.IdentifierNode(Token(TokenType.IDENTIFIER, "x", Position(0, 0))),
                        expression =
                            ExpressionNode.BinaryOperationNode(
                                Token(TokenType.OPERATOR_PLUS, "+", Position(0, 0)),
                                ExpressionNode.LiteralNode(Token(TokenType.LITERAL_NUMBER, "5", Position(0, 0))),
                                ExpressionNode.LiteralNode(Token(TokenType.LITERAL_NUMBER, "5", Position(0, 0))),
                            ),
                    ),
                ),
            )
        val formatter = FormatterImpl()
        val formatted = formatter.format(tokens, "src/test/resources/Rules.json")
        assertEquals("x = 5 + 5;\n", formatted)
    }
}

import linter.LinterImpl
import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.ProgramNode
import org.example.ast.nodes.StatementNode
import org.example.factories.Position
import org.example.token.Token
import org.example.token.TokenType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LinterTest {
    @Test
    fun testLinterWithNoErrors() {
        val ast =
            ProgramNode(
                listOf(
                    StatementNode.PrintNode(
                        ExpressionNode.LiteralNode(Token(TokenType.LITERAL_STRING, "hello", Position(0, 0))),
                    ),
                    StatementNode.PrintNode(
                        ExpressionNode.LiteralNode(Token(TokenType.LITERAL_STRING, "world", Position(0, 0))),
                    ),
                ),
            )
        val linter = LinterImpl()
        val errors = linter.checkErrors(ast)
        assert(errors.isEmpty())
    }

    @Test
    fun testLinterWithOperationInPrintln() {
        val ast =
            ProgramNode(
                listOf(
                    StatementNode.PrintNode(
                        ExpressionNode.BinaryOperationNode(
                            Token(TokenType.OPERATOR_PLUS, "+", Position(0, 2)),
                            ExpressionNode.LiteralNode(Token(TokenType.LITERAL_NUMBER, "5", Position(0, 0))),
                            ExpressionNode.LiteralNode(Token(TokenType.LITERAL_NUMBER, "3", Position(0, 4))),
                        ),
                    ),
                ),
            )
        val linter = LinterImpl()
        val errors = linter.checkErrors(ast)
        assertEquals(errors.size, 1)
        assertEquals(errors[0].message, "Binary operation in println in line 0 and column 2")
    }

    @Test
    fun testLinterWithErrorInFormatIdDeclarationNode() {
        val ast =
            ProgramNode(
                listOf(
                    StatementNode.DeclarationNode(
                        StatementNode.VariableNode(
                            ExpressionNode.IdentifierNode(Token(TokenType.IDENTIFIER, "Helloworld", Position(0, 0))),
                            ExpressionNode.TypeNode(Token(TokenType.TYPE_STRING, "String", Position(0, 0))),
                        ),
                        ExpressionNode.LiteralNode(Token(TokenType.LITERAL_STRING, "world", Position(0, 0))),
                    ),
                ),
            )
        val linter = LinterImpl()
        val errors = linter.checkErrors(ast)
        assertEquals(errors.size, 1)
        assertEquals(errors[0].message, "Identifier Helloworld is not in camelCase format in line 0 and column 0")
    }

    @Test
    fun testLinterWithNoErrorsInFormatId() {
        val ast =
            ProgramNode(
                listOf(
                    StatementNode.DeclarationNode(
                        StatementNode.VariableNode(
                            ExpressionNode.IdentifierNode(Token(TokenType.IDENTIFIER, "helloWorld", Position(0, 0))),
                            ExpressionNode.TypeNode(Token(TokenType.TYPE_STRING, "String", Position(0, 0))),
                        ),
                        ExpressionNode.LiteralNode(Token(TokenType.LITERAL_STRING, "world", Position(0, 0))),
                    ),
                ),
            )
        val linter = LinterImpl()
        val errors = linter.checkErrors(ast)
        assertEquals(errors.size, 0)
    }

    @Test
    fun testLinterWithErrorInFormatIdIdNode() {
        val ast =
            ProgramNode(
                listOf(
                    ExpressionNode.IdentifierNode(Token(TokenType.IDENTIFIER, "hello_world", Position(0, 0))),
                ),
            )
        val linter = LinterImpl()
        val errors = linter.checkErrors(ast)
        assertEquals(errors.size, 1)
        assertEquals(errors[0].message, "Identifier hello_world is not in camelCase format in line 0 and column 0")
    }

    @Test
    fun testLinterWithErrorInFormatIdAssignationNode() {
        val ast =
            ProgramNode(
                listOf(
                    StatementNode.AssignationNode(
                        ExpressionNode.IdentifierNode(Token(TokenType.IDENTIFIER, "HelloWorld", Position(0, 0))),
                        ExpressionNode.LiteralNode(Token(TokenType.LITERAL_STRING, "world", Position(0, 0))),
                    ),
                ),
            )
        val linter = LinterImpl()
        val errors = linter.checkErrors(ast)
        assertEquals(errors.size, 1)
        assertEquals(errors[0].message, "Identifier HelloWorld is not in camelCase format in line 0 and column 0")
    }

    @Test
    fun testLinterWithErrorInFormatIdVariableNode() {
        val ast =
            ProgramNode(
                listOf(
                    StatementNode.VariableNode(
                        ExpressionNode.IdentifierNode(Token(TokenType.IDENTIFIER, "HelloWorld", Position(0, 0))),
                        ExpressionNode.TypeNode(Token(TokenType.TYPE_STRING, "String", Position(0, 0))),
                    ),
                ),
            )
        val linter = LinterImpl()
        val errors = linter.checkErrors(ast)
        assertEquals(errors.size, 1)
        assertEquals(errors[0].message, "Identifier HelloWorld is not in camelCase format in line 0 and column 0")
    }
}

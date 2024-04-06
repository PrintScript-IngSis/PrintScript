import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.ProgramNode
import org.example.factories.Position
import org.example.interpreter.InterpreterImpl
import org.example.parser.ParserImpl
import org.example.token.Token
import org.example.token.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class InterpreterTest {
    @Test
    fun testInterpreterWhenDeclaringAVariableAndItDoesNotExistsShouldSaveIt() {
        val tokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 4)),
                Token(TokenType.COLON, ":", Position(0, 6)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 23)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 10)),
                Token(TokenType.LITERAL_NUMBER, "5.0", Position(0, 12)),
                Token(TokenType.SEMICOLON, ";", Position(0, 14)),
            )

        val parser = ParserImpl(tokens)
        val ast = parser.parse()
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("5.0", interpreter.getVariables()["x"]?.value)
    }

    @Test
    fun testInterpreterWhenDeclaringAVariableAndItAlreadyExistsShouldThrowException() {
        val tokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 4)),
                Token(TokenType.COLON, ":", Position(0, 6)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 23)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 10)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 12)),
                Token(TokenType.SEMICOLON, ";", Position(0, 14)),
            )

        val parser = ParserImpl(tokens)
        val ast = parser.parse()
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        val tokens2 =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 4)),
                Token(TokenType.COLON, ":", Position(0, 6)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 23)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 10)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 12)),
                Token(TokenType.SEMICOLON, ";", Position(0, 14)),
            )

        val parser2 = ParserImpl(tokens2)
        val ast2 = parser2.parse()
        val interpreter2 = InterpreterImpl(ast2)

        try {
            interpreter2.interpret()
        } catch (e: Exception) {
            assertEquals("Variable x already exists", e.message)
        }
    }

    @Test
    fun testInterpreterWhenPrintingAVariableThatDoesNotExistsShouldThrowException() {
        val tokens =
            listOf(
                Token(TokenType.OPERATOR_PRINTLN, "println", Position(0, 0)),
                Token(TokenType.PARENTHESIS_OPEN, "(", Position(0, 1)),
                Token(TokenType.IDENTIFIER, "y", Position(0, 2)),
                Token(TokenType.PARENTHESIS_CLOSE, ")", Position(0, 3)),
                Token(TokenType.SEMICOLON, ";", Position(0, 4)),
            )

        val parser2 = ParserImpl(tokens)
        val ast2 = parser2.parse()
        val interpreter2 = InterpreterImpl(ast2)

        try {
            interpreter2.interpret()
        } catch (e: Exception) {
            assertEquals("Variable y not found", e.message)
        }
    }

    @Test
    fun testInterpreterWhenRecievingAnInvalidNodeShouldThrowException() {
        val ast = ProgramNode(listOf(ExpressionNode.IdentifierNode(Token(TokenType.IDENTIFIER, "x", Position(0, 0)))))
        val interpreter = InterpreterImpl(ast)

        try {
            interpreter.interpret()
        } catch (e: Exception) {
            assertEquals("Unknown node type", e.message)
        }
    }

    @Test
    fun testInterpreterWhenRecievingADeclarationWithABinaryOperationShouldSaveTheResult() {
        val tokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 2)),
                Token(TokenType.COLON, ":", Position(0, 4)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 21)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 8)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 10)),
                Token(TokenType.OPERATOR_PLUS, "+", Position(0, 12)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 14)),
                Token(TokenType.SEMICOLON, ";", Position(0, 16)),
            )

        val parser = ParserImpl(tokens)
        val ast = parser.parse()
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("10.0", interpreter.getVariables()["x"]?.value)
    }

    @Test
    fun testInterpreterWhenRecievingADeclarationWithAnIdentifierShouldSaveTheResult() {
        val tokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 2)),
                Token(TokenType.COLON, ":", Position(0, 4)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 21)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 8)),
                Token(TokenType.LITERAL_NUMBER, "5.0", Position(0, 10)),
                Token(TokenType.SEMICOLON, ";", Position(0, 12)),
                Token(TokenType.KEYWORD_LET, "let", Position(1, 0)),
                Token(TokenType.IDENTIFIER, "y", Position(1, 2)),
                Token(TokenType.COLON, ":", Position(1, 4)),
                Token(TokenType.TYPE_NUMBER, "number", Position(1, 21)),
                Token(TokenType.ASSIGNATOR, "=", Position(1, 8)),
                Token(TokenType.IDENTIFIER, "x", Position(1, 10)),
                Token(TokenType.SEMICOLON, ";", Position(1, 12)),
            )

        val parser = ParserImpl(tokens)
        val ast = parser.parse()
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("5.0", interpreter.getVariables()["y"]?.value)
    }

    @Test
    fun testInterpreterWhenAssignationAVariableToAnotherVariableShouldSaveItCorrectly() {
        val tokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 2)),
                Token(TokenType.COLON, ":", Position(0, 4)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 21)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 8)),
                Token(TokenType.LITERAL_NUMBER, "5.0", Position(0, 10)),
                Token(TokenType.SEMICOLON, ";", Position(0, 12)),
                Token(TokenType.KEYWORD_LET, "let", Position(1, 0)),
                Token(TokenType.IDENTIFIER, "y", Position(1, 2)),
                Token(TokenType.COLON, ":", Position(1, 4)),
                Token(TokenType.TYPE_NUMBER, "number", Position(1, 21)),
                Token(TokenType.ASSIGNATOR, "=", Position(1, 8)),
                Token(TokenType.LITERAL_NUMBER, "4.0", Position(1, 10)),
                Token(TokenType.SEMICOLON, ";", Position(1, 12)),
                Token(TokenType.IDENTIFIER, "y", Position(2, 2)),
                Token(TokenType.ASSIGNATOR, "=", Position(2, 4)),
                Token(TokenType.IDENTIFIER, "x", Position(2, 6)),
            )

        val parser = ParserImpl(tokens)
        val ast = parser.parse()
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("5.0", interpreter.getVariables()["y"]?.value)
    }

    @Test
    fun testInterpreterWhenRecievingADeclarationWithABinaryOperationMinusShouldSaveTheResult() {
        val tokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 2)),
                Token(TokenType.COLON, ":", Position(0, 4)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 21)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 8)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 10)),
                Token(TokenType.OPERATOR_MINUS, "-", Position(0, 12)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 14)),
                Token(TokenType.SEMICOLON, ";", Position(0, 16)),
            )

        val parser = ParserImpl(tokens)
        val ast = parser.parse()
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("0.0", interpreter.getVariables()["x"]?.value)
    }

    @Test
    fun testInterpreterWhenRecievingADeclarationWithABinaryOperationMultiplyShouldSaveTheResult() {
        val tokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 2)),
                Token(TokenType.COLON, ":", Position(0, 4)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 21)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 8)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 10)),
                Token(TokenType.OPERATOR_MULTIPLY, "*", Position(0, 12)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 14)),
                Token(TokenType.SEMICOLON, ";", Position(0, 16)),
            )

        val parser = ParserImpl(tokens)
        val ast = parser.parse()
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("25.0", interpreter.getVariables()["x"]?.value)
    }

    @Test
    fun testInterpreterWhenRecievingADeclarationWithABinaryOperationDivideShouldSaveTheResult() {
        val tokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 2)),
                Token(TokenType.COLON, ":", Position(0, 4)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 21)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 8)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 10)),
                Token(TokenType.OPERATOR_DIVIDE, "/", Position(0, 12)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 14)),
                Token(TokenType.SEMICOLON, ";", Position(0, 16)),
            )

        val parser = ParserImpl(tokens)
        val ast = parser.parse()
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("1.0", interpreter.getVariables()["x"]?.value)
    }
}

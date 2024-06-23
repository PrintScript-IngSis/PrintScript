import lexer.director.LexerDirector
import org.example.factories.Position
import org.example.lexer.Lexer
import org.example.lexer.LexerImpl
import org.example.token.Token
import org.example.token.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class LexerTest {
    private lateinit var lexer: Lexer
    private lateinit var oldLexer: Lexer

    @BeforeEach
    fun setUp() {
        lexer = LexerDirector().createLexer("1.1")
        oldLexer = LexerImpl("1.1")
    }

    @Test
    fun testLexerWhenGivenCorrectInputShouldGiveCorrectTokens() {
        val input = "let x : number = 5;"

        val tokens = lexer.tokenize(input)
        oldLexer.tokenize(input)

        val expectedTokens =
            listOf(
                Token(TokenType.KEYWORD_LET, "let", Position(0, 0)),
                Token(TokenType.IDENTIFIER, "x", Position(0, 4)),
                Token(TokenType.COLON, ":", Position(0, 6)),
                Token(TokenType.TYPE_NUMBER, "number", Position(0, 8)),
                Token(TokenType.ASSIGNATOR, "=", Position(0, 15)),
                Token(TokenType.LITERAL_NUMBER, "5", Position(0, 17)),
                Token(TokenType.SEMICOLON, ";", Position(0, 18)),
            )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun testLexerWhenGivenIncorrectOutputShouldReturnIllegalArgumentException() {
        val input = " @ "

        try {
            lexer.tokenize(input)
        } catch (e: IllegalArgumentException) {
            assertEquals("No matching token for input: @, in line 0 and column 1", e.message)
        }
    }
}

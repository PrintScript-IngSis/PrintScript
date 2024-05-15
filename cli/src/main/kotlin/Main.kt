package org.example

import org.example.interpreter.InterpreterImpl
import org.example.lexer.LexerImpl
import org.example.parser.ParserImpl

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val text =
        "println(\"this is a test\");\n"
    val input = text.repeat(32 * 1024)
    val lexer = LexerImpl("1.1")
    val tokens = lexer.tokenize(input)
    println("lexer done")
    val parser = ParserImpl()
    val ast = parser.parse(tokens)
    println("parser done")
    val interpreter = InterpreterImpl()
    interpreter.interpret(ast)
}

package org.example

import org.example.interpreter.InterpreterImpl
import org.example.lexer.LexerImpl
import org.example.parser.ParserImpl

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val text =
        "const b: number = 5;\n" +
            "b = 2;"
    // \
//    val strDiv = StringDividerImpl()
    val lexer = LexerImpl("1.1")

    val tokens = lexer.tokenize(text)

//    println("Tokens: $tokens")

    val parser = ParserImpl()
    val ast = parser.parse(tokens)

//    println("AST: $ast")

    val interpreter = InterpreterImpl()
    println(interpreter.interpret(ast))
}

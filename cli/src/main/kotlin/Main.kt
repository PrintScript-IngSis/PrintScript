package org.example

import lexer.director.LexerDirector
import org.example.interpreter.InterpreterImpl
import org.example.parser.ParserImpl

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val text =
        "const name: string = readInput(\"Name:\");\n" + "println(\"Hello \" + name + \"!\");"
    val lexer = LexerDirector().createLexer("1.1")
    val tokens = lexer.tokenize(text)
    val parser = ParserImpl()
    val ast = parser.parse(tokens)
    val interpreter = InterpreterImpl()
    interpreter.interpret(ast)
}

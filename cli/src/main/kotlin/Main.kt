package org.example

import lexer.director.LexerDirector
import org.example.interpreter.InterpreterImpl
import org.example.parser.ParserImpl

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val text =
        "const name: string = readInput();\n" +
            "const age: number = readInput();\n" +
            "println(\"Hello, \" + name + \", you are \" + age + \" years old\");"
    val lexer = LexerDirector().createLexer("1.1")
    val tokens = lexer.tokenize(text)
    val parser = ParserImpl()
    val ast = parser.parse(tokens)
    val interpreter = InterpreterImpl()
    interpreter.interpret(ast, true, mutableListOf("John", "25"))
//    val formatter = FormatterImpl()
//    println(formatter.format(ast,"formatter/src/main/resources/Rules.json"))
}

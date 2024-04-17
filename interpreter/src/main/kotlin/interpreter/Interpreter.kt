package org.example.interpreter

import org.example.ast.nodes.ProgramNode

interface Interpreter {
    fun interpret(ast: ProgramNode): String
}

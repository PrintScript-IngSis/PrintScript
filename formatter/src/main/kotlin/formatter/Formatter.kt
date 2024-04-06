package org.example.formatter

import org.example.ast.nodes.ProgramNode

interface Formatter {
    fun format(ast: ProgramNode, path:String): String
}

package org.example

import kotlinx.serialization.json.Json
import org.example.ast.nodes.ProgramNode

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val astJson = """{
  "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":{
                        "line":1,
                        "column":4
                    
                  }
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{
                        "line":1,
                        "column":6
                  }
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"5.0",
               "position":{
                        "line":1,
                        "column":8
               }
            }
         }
      }
   ]
}"""
    val ast = Json.decodeFromString<ProgramNode>(astJson)

    println("AST: $ast")
}

//    val parser = ParserImpl(tokens)
//    val ast = parser.parse()
//    val astJson = Json.encodeToString(ast)
//    println("AST JSON: $astJson")
//
//    println("AST: $ast")
//
// //    val interpreter = InterpreterImpl(ast)
// //    interpreter.interpret()
// //    println(interpreter.getVariables())
// //
//    println(FormatterImpl().format(ast))
// }

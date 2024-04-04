import kotlinx.serialization.json.Json
import org.example.ast.nodes.ExpressionNode
import org.example.ast.nodes.ProgramNode
import org.example.interpreter.InterpreterImpl
import org.example.token.Token
import org.example.token.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class InterpreterTest {
    @Test
    fun testInterpreterWhenDeclaringAVariableAndItDoesNotExistsShouldSaveIt() {
        val astJson = """{
  "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":2
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":21
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"5.0",
               "position":10
            }
         }
      }
   ]
}"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("5.0", interpreter.getVariables()["x"]?.value)
    }

    @Test
    fun testInterpreterWhenDeclaringAVariableAndItAlreadyExistsShouldThrowException() {
        val astJson = """{
   "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":2
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":21
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"5",
               "position":10
            }
         }
      }
   ]
}"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        val astJson2 = """{
   "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":2
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":21
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"5",
               "position":10
            }
         }
      }
   ]
}"""
        val ast2 = Json.decodeFromString<ProgramNode>(astJson2)
        val interpreter2 = InterpreterImpl(ast2)

        try {
            interpreter2.interpret()
        } catch (e: Exception) {
            assertEquals("Variable x already exists", e.message)
        }
    }

    @Test
    fun testInterpreterWhenPrintingAVariableThatDoesNotExistsShouldThrowException() {
        val astJson = """{
   "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.PrintNode",
         "printable":{
            "type":"org.example.ast.nodes.ExpressionNode.IdentifierNode",
            "id":{
               "type":"IDENTIFIER",
               "value":"y",
               "position":2
            }
         }
      }
   ]
}"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl(ast)

        try {
            interpreter.interpret()
        } catch (e: Exception) {
            assertEquals("Variable y not found", e.message)
        }
    }

    @Test
    fun testInterpreterWhenRecievingAnInvalidNodeShouldThrowException() {
        val ast = ProgramNode(listOf(ExpressionNode.IdentifierNode(Token(TokenType.IDENTIFIER, "x", 0))))
        val interpreter = InterpreterImpl(ast)

        try {
            interpreter.interpret()
        } catch (e: Exception) {
            assertEquals("Unknown node type", e.message)
        }
    }

    @Test
    fun testInterpreterWhenRecievingADeclarationWithABinaryOperationShouldSaveTheResult() {
        val astJson = """{
   "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":2
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":21
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.BinaryOperationNode",
            "value":{
               "type":"OPERATOR_PLUS",
               "value":"+",
               "position":12
            },
            "leftChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":10
               }
            },
            "rightChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":14
               }
            }
         }
      }
   ]
}"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("10.0", interpreter.getVariables()["x"]?.value)
    }

    @Test
    fun testInterpreterWhenRecievingADeclarationWithAnIdentifierShouldSaveTheResult() {
        val astJson = """{
   "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":2
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":21
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"5.0",
               "position":10
            }
         }
      },
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"y",
                  "position":2
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":21
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.IdentifierNode",
            "id":{
               "type":"IDENTIFIER",
               "value":"x",
               "position":10
            }
         }
      }
   ]
}"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("5.0", interpreter.getVariables()["y"]?.value)
    }

    @Test
    fun testInterpreterWhenAssignationAVariableToAnotherVariableShouldSaveItCorrectly() {
        val astJson = """{
   "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":2
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":21
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"5.0",
               "position":10
            }
         }
      },
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"y",
                  "position":2
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":21
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"4.0",
               "position":10
            }
         }
      },
      {
         "type":"org.example.ast.nodes.StatementNode.AssignationNode",
         "identifier":{
            "id":{
               "type":"IDENTIFIER",
               "value":"y",
               "position":2
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.IdentifierNode",
            "id":{
               "type":"IDENTIFIER",
               "value":"x",
               "position":6
            }
         }
      }
   ]
}"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("5.0", interpreter.getVariables()["y"]?.value)
    }

    @Test
    fun testInterpreterWhenRecievingADeclarationWithABinaryOperationMinusShouldSaveTheResult() {
        val astJson = """{
   "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":2
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":21
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.BinaryOperationNode",
            "value":{
               "type":"OPERATOR_MINUS",
               "value":"-",
               "position":12
            },
            "leftChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":10
               }
            },
            "rightChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":14
               }
            }
         }
      }
   ]
}"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("0.0", interpreter.getVariables()["x"]?.value)
    }

    @Test
    fun testInterpreterWhenRecievingADeclarationWithABinaryOperationMultiplyShouldSaveTheResult() {
        val astJson = """{
   "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":2
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":21
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.BinaryOperationNode",
            "value":{
               "type":"OPERATOR_MULTIPLY",
               "value":"*",
               "position":12
            },
            "leftChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":10
               }
            },
            "rightChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":14
               }
            }
         }
      }
   ]
}"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("25.0", interpreter.getVariables()["x"]?.value)
    }

    @Test
    fun testInterpreterWhenRecievingADeclarationWithABinaryOperationDivideShouldSaveTheResult() {
        val astJson = """{
   "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "id":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":2
               }
            },
            "dataType":{
               "typeToken":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":21
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.BinaryOperationNode",
            "value":{
               "type":"OPERATOR_DIVIDE",
               "value":"/",
               "position":12
            },
            "leftChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":10
               }
            },
            "rightChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":14
               }
            }
         }
      }
   ]
}"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl(ast)
        interpreter.interpret()

        assertEquals("1.0", interpreter.getVariables()["x"]?.value)
    }
}

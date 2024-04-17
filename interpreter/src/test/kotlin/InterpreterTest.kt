import kotlinx.serialization.json.Json
import org.example.ast.nodes.ProgramNode
import org.example.interpreter.InterpreterImpl
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
               "token":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":{"line":0,"column":2}
               }
            },
            "dataType":{
               "token":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{"line":0,"column":21}
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"5.0",
               "position":{"line":0,"column":10}
            }
         }
      }
   ]
}"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()
        interpreter.interpret(ast)

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
               "token":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":{"line":0,"column":2}
               }
            },
            "dataType":{
               "token":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{"line":0,"column":21}
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"5",
               "position":{"line":0,"column":10}
            }
         }
      }
   ]
}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()
        interpreter.interpret(ast)

        val astJson2 = """{
   "statements":[
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "token":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":{"line":0,"column":2}
               }
            },
            "dataType":{
               "token":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{"line":0,"column":21}
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"5",
               "position":{"line":0,"column":10}
            }
         }
      }
   ]
}
"""
        val ast2 = Json.decodeFromString<ProgramNode>(astJson2)
        val interpreter2 = InterpreterImpl()

        try {
            interpreter2.interpret(ast2)
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
            "token":{
               "type":"IDENTIFIER",
               "value":"y",
               "position":{"line":0,"column":2}
            }
         }
      }
   ]
}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()

        try {
            interpreter.interpret(ast)
        } catch (e: Exception) {
            assertEquals("Variable y not found", e.message)
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
               "token":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":{"line":0,"column":2}
               }
            },
            "dataType":{
               "token":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{"line":0,"column":21}
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.BinaryOperationNode",
            "token":{
               "type":"OPERATOR_PLUS",
               "value":"+",
               "position":{"line":0,"column":12}
            },
            "leftChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":{"line":0,"column":10}
               }
            },
            "rightChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":{"line":0,"column":14}
               }
            }
         }
      }
   ]
}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()
        interpreter.interpret(ast)

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
               "token":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":{"line":0,"column":2}
               }
            },
            "dataType":{
               "token":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{"line":0,"column":21}
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"5.0",
               "position":{"line":0,"column":10}
            }
         }
      },
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "token":{
                  "type":"IDENTIFIER",
                  "value":"y",
                  "position":{"line":0,"column":2}
               }
            },
            "dataType":{
               "token":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{"line":0,"column":21}
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.IdentifierNode",
            "token":{
               "type":"IDENTIFIER",
               "value":"x",
               "position":{"line":0,"column":10}
            }
         }
      }
   ]
}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()
        interpreter.interpret(ast)

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
               "token":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":{"line":0,"column":2}
               }
            },
            "dataType":{
               "token":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{"line":0,"column":21}
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"5.0",
               "position":{"line":0,"column":10}
            }
         }
      },
      {
         "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
         "variable":{
            "identifier":{
               "token":{
                  "type":"IDENTIFIER",
                  "value":"y",
                  "position":{"line":0,"column":2}
               }
            },
            "dataType":{
               "token":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{"line":0,"column":21}
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
            "token":{
               "type":"LITERAL_NUMBER",
               "value":"4.0",
               "position":{"line":0,"column":10}
            }
         }
      },
      {
         "type":"org.example.ast.nodes.StatementNode.AssignationNode",
         "identifier":{
            "token":{
               "type":"IDENTIFIER",
               "value":"y",
               "position":{"line":0,"column":2}
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.IdentifierNode",
            "token":{
               "type":"IDENTIFIER",
               "value":"x",
               "position":{"line":0,"column":6}
            }
         }
      }
   ]
}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()
        interpreter.interpret(ast)

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
               "token":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":{"line":0,"column":2}
               }
            },
            "dataType":{
               "token":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{"line":0,"column":21}
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.BinaryOperationNode",
            "token":{
               "type":"OPERATOR_MINUS",
               "value":"-",
               "position":{"line":0,"column":12}
            },
            "leftChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":{"line":0,"column":10}
               }
            },
            "rightChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":{"line":0,"column":14}
               }
            }
         }
      }
   ]
}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()
        interpreter.interpret(ast)

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
               "token":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":{"line":0,"column":2}
               }
            },
            "dataType":{
               "token":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{"line":0,"column":21}
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.BinaryOperationNode",
            "token":{
               "type":"OPERATOR_MULTIPLY",
               "value":"*",
               "position":{"line":0,"column":12}
            },
            "leftChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":{"line":0,"column":10}
               }
            },
            "rightChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":{"line":0,"column":14}
               }
            }
         }
      }
   ]
}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()
        interpreter.interpret(ast)

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
               "token":{
                  "type":"IDENTIFIER",
                  "value":"x",
                  "position":{"line":0,"column":2}
               }
            },
            "dataType":{
               "token":{
                  "type":"TYPE_NUMBER",
                  "value":"number",
                  "position":{"line":0,"column":21}
               }
            }
         },
         "expression":{
            "type":"org.example.ast.nodes.ExpressionNode.BinaryOperationNode",
            "token":{
               "type":"OPERATOR_DIVIDE",
               "value":"/",
               "position":{"line":0,"column":12}
            },
            "leftChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":{"line":0,"column":10}
               }
            },
            "rightChild":{
               "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
               "token":{
                  "type":"LITERAL_NUMBER",
                  "value":"5",
                  "position":{"line":0,"column":14}
               }
            }
         }
      }
   ]
}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()
        interpreter.interpret(ast)

        assertEquals("1.0", interpreter.getVariables()["x"]?.value)
    }

    @Test
    fun testInterpreterWhenRecievingAnAssignationWhenTheVariableDoesNotExistsShouldThrowException() {
        val astJson = """{
    "statements":[
        {
            "type":"org.example.ast.nodes.StatementNode.AssignationNode",
            "identifier":{
                "token":{
                    "type":"IDENTIFIER",
                    "value":"x",
                    "position":{"line":0,"column":2}
                }
            },
            "expression":{
                "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
                "token":{
                    "type":"LITERAL_NUMBER",
                    "value":"5.0",
                    "position":{"line":0,"column":10}
                }
            }
        }
    ]
}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()

        try {
            interpreter.interpret(ast)
        } catch (e: Exception) {
            assertEquals("Variable x not found", e.message)
        }
    }

    @Test
    fun testInterpreterWhenRecievingAnAssignationWhenTheVariableTypeMismatchesShouldThrowException() {
        val astJson = """{
    "statements":[
        {
            "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
            "variable":{
                "identifier":{
                    "token":{
                        "type":"IDENTIFIER",
                        "value":"x",
                        "position":{"line":0,"column":2}
                    }
                },
                "dataType":{
                    "token":{
                        "type":"TYPE_NUMBER",
                        "value":"number",
                        "position":{"line":0,"column":21}
                    }
                }
            },
            "expression":{
                "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
                "token":{
                    "type":"LITERAL_NUMBER",
                    "value":"5.0",
                    "position":{"line":0,"column":10}
                }
            }
        },
        {
            "type":"org.example.ast.nodes.StatementNode.AssignationNode",
            "identifier":{
                "token":{
                    "type":"IDENTIFIER",
                    "value":"x",
                    "position":{"line":0,"column":2}
                }
            },
            "expression":{
                "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
                "token":{
                    "type":"LITERAL_STRING",
                    "value":"Hello, World!",
                    "position":{"line":0,"column":10}
                }
            }
        }
    ]
}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()

        try {
            interpreter.interpret(ast)
        } catch (e: Exception) {
            assertEquals("Type mismatch", e.message)
        }
    }

    @Test
    fun testInterpreterWhenRecievingAnIfStatementShouldExecuteTheTrueBranch() {
        val astJson = """{
    "statements":[
        {
            "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
            "variable":{
                "identifier":{
                    "token":{
                        "type":"IDENTIFIER",
                        "value":"a",
                        "position":{"line":0,"column":4}
                    }
                },
                "dataType":{
                    "token":{
                        "type":"TYPE_BOOLEAN",
                        "value":"boolean",
                        "position":{"line":0,"column":6}
                    }
                }
            },
            "expression":{
                "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
                "token":{
                    "type":"LITERAL_BOOLEAN",
                    "value":"true",
                    "position":{"line":0,"column":16}
                }
            }
        },
        {
            "type":"org.example.ast.nodes.StatementNode.IfNode",
            "condition":{
                "token":{
                    "type":"IDENTIFIER",
                    "value":"a",
                    "position":{"line":0,"column":25}
                }
            },
            "trueStatementNode":{
                "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
                "variable":{
                    "identifier":{
                        "token":{
                            "type":"IDENTIFIER",
                            "value":"b",
                            "position":{"line":0,"column":32}
                        }
                    },
                    "dataType":{
                        "token":{
                            "type":"TYPE_NUMBER",
                            "value":"number",
                            "position":{"line":0,"column":34}
                        }
                    }
                },
                "expression":{
                    "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
                    "token":{
                        "type":"LITERAL_NUMBER",
                        "value":"1.0",
                        "position":{"line":0,"column":43}
                    }
                }
            },
            "falseStatementNode":{
                "type":"org.example.ast.nodes.StatementNode.DeclarationNode",
                "variable":{
                    "identifier":{
                        "token":{
                            "type":"IDENTIFIER",
                            "value":"x",
                            "position":{"line":0,"column":55}
                        }
                    },
                    "dataType":{
                        "token":{
                            "type":"TYPE_NUMBER",
                            "value":"number",
                            "position":{"line":0,"column":57}
                        }
                    }
                },
                "expression":{
                    "type":"org.example.ast.nodes.ExpressionNode.LiteralNode",
                    "token":{
                        "type":"LITERAL_NUMBER",
                        "value":"0.0",
                        "position":{"line":0,"column":66}
                    }
                }
            }
        }
    ]
}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()
        interpreter.interpret(ast)

        assertEquals("1.0", interpreter.getVariables()["b"]?.value)
    }

    @Test
    fun testInterpreterWhenReadEnvWithVariableInitialized() {
        val astJson =
            """{"statements":[{"type":"org.example.ast.nodes.StatementNode.DeclarationNode",
            |"variable":{"identifier":{"token":{"type":"IDENTIFIER","value":"x","position":{"line":0,"column":4}}},
            |"dataType":{"token":{"type":"TYPE_NUMBER","value":"number","position":{"line":0,"column":6}}}},
            |"expression":{"type":"org.example.ast.nodes.ExpressionNode.LiteralNode","token":{"type":"LITERAL_NUMBER",
            |"value":"5.0","position":{"line":0,"column":15}}}},{"type":"org.example.ast.nodes.StatementNode.DeclarationNode",
            |"variable":{"identifier":{"token":{"type":"IDENTIFIER","value":"value","position":{"line":0,"column":21}}},
            |"dataType":{"token":{"type":"TYPE_NUMBER","value":"number","position":{"line":0,"column":27}}}},
            |"expression":{"type":"org.example.ast.nodes.ExpressionNode.ReadEnvNode","token":{"type":"KEYWORD_READ_ENV",
            |"value":"readEnv","position":{"line":0,"column":36}},"variable":{"token":{"type":"IDENTIFIER","value":"x",
            |"position":{"line":0,"column":44}}}}}]}
            """.trimMargin()
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()
        interpreter.interpret(ast)

        assertEquals("5.0", interpreter.getVariables()["value"]?.value)
    }

    @Test
    fun testInterpreterWhenTryingToUseAVariableNotDeclared() {
        val astJson = """{"statements":[{"type":"org.example.ast.nodes.StatementNode.DeclarationNode",
            "variable":{"identifier":{"token":{"type":"IDENTIFIER","value":"x","position":{"line":0,"column":4}}},
            "dataType":{"token":{"type":"TYPE_STRING","value":"string","position":{"line":0,"column":6}}}},
            "expression":{"type":"org.example.ast.nodes.ExpressionNode.BinaryOperationNode",
            "token":{"type":"OPERATOR_PLUS","value":"+","position":{"line":0,"column":17}},
            "leftChild":{"type":"org.example.ast.nodes.ExpressionNode.IdentifierNode",
            "token":{"type":"IDENTIFIER","value":"a","position":{"line":0,"column":15}}},
            "rightChild":{"type":"org.example.ast.nodes.ExpressionNode.IdentifierNode",
            "token":{"type":"IDENTIFIER","value":"r","position":{"line":0,"column":19}}}}}]}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()

        try {
            interpreter.interpret(ast)
        } catch (e: Exception) {
            assertEquals("Variable a not found", e.message)
        }
    }

    @Test
    fun testInterpreterWhenTryingToReadEnvOfAVariableNotDeclared() {
        val astJson = """{"statements":[{"type":"org.example.ast.nodes.StatementNode.DeclarationNode","variable":
            {"identifier":{"token":{"type":"IDENTIFIER","value":"x","position":{"line":0,"column":4}}},
            "dataType":{"token":{"type":"TYPE_STRING","value":"string","position":{"line":0,"column":6}}}},
            "expression":{"type":"org.example.ast.nodes.ExpressionNode.ReadEnvNode","token":{"type":"KEYWORD_READ_ENV",
            "value":"readEnv","position":{"line":0,"column":15}},"variable":{"token":{"type":"IDENTIFIER","value":"a",
            "position":{"line":0,"column":23}}}}}]}
"""
        val ast = Json.decodeFromString<ProgramNode>(astJson)
        val interpreter = InterpreterImpl()

        try {
            interpreter.interpret(ast)
        } catch (e: Exception) {
            assertEquals("Variable a not found", e.message)
        }
    }
}

package org.example.token

enum class TokenType {
    KEYWORD_LET,
    IDENTIFIER,
    ASSIGNATOR, // =
    COLON,
    SEMICOLON,
    LITERAL_NUMBER,
    LITERAL_STRING,
    TYPE_STRING, // let a:number = 5
    TYPE_NUMBER,
    TYPE_BOOLEAN,
    OPERATOR_PLUS,
    OPERATOR_MINUS,
    OPERATOR_MULTIPLY, // priority 1
    OPERATOR_DIVIDE, // priority 1
    OPERATOR_PRINTLN, // priority last
    PARENTHESIS_OPEN,
    PARENTHESIS_CLOSE,
}

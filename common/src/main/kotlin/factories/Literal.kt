package org.example.factories

import org.example.token.TokenType

data class Literal(val value: String, val type: TokenType, val isMutable : Boolean)

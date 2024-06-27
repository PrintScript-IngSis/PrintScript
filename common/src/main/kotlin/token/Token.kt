package org.example.token

import kotlinx.serialization.Serializable
import org.example.factories.Position

@Serializable
data class Token(val type: TokenType,
                 val value: String,
                 val position: Position)

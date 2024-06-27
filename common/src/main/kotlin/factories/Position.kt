package org.example.factories

import kotlinx.serialization.Serializable

@Serializable
data class Position(val line: Int,
                    val column: Int)

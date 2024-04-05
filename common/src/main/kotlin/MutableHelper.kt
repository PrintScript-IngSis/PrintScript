package org.example

import org.example.token.Token

class MutableHelper {
    companion object {
        fun isMutable(token: Token): Boolean {
            return if (token.type == org.example.token.TokenType.TYPE_BOOLEAN)
                token.value.toBoolean()
            else
                false
        }
    }
}
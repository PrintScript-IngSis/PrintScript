package org.example.lexer.stringDivider

class StringDividerImpl : StringDivider {
    override fun stringToList(input: String): List<List<String>> {
        val lines = input.split("\n")
        val listOfWordLists = mutableListOf<List<String>>()
        for (line in lines) {
            lineToWords(line, listOfWordLists)
        }
        return listOfWordLists
    }

    private fun lineToWords(
        line: String,
        listOfWordLists: MutableList<List<String>>,
    ) {
        val tokens = mutableListOf<String>()
        var currentToken = StringBuilder()
        var withinQuotes = false
        var quoteChar: Char? = null

        for (char in line) {
            when {
                char == '"' || char == '\'' -> {
                    if (!withinQuotes) {
                        withinQuotes = true
                        quoteChar = char
                        currentToken.append(char) // Add the opening quote to the token
                    } else if (char == quoteChar) {
                        withinQuotes = false
                        quoteChar = null
                        currentToken.append(char) // Add the closing quote to the token
                        tokens.add(currentToken.toString()) // Add the token along with quotes to the list
                        currentToken = StringBuilder() // Reset the token
                    } else {
                        currentToken.append(char)
                    }
                }
                withinQuotes -> currentToken.append(char)
                char in setOf(':', ';', '=', '(', ')', '+', '*', '/', '-', '{', '}') -> {
                    if (currentToken.isNotEmpty()) {
                        tokens.add(currentToken.toString())
                        currentToken = StringBuilder()
                    }
                    tokens.add(char.toString())
                }
                char.isWhitespace() -> {
                    if (currentToken.isNotEmpty()) {
                        tokens.add(currentToken.toString())
                        currentToken = StringBuilder()
                    }
                }
                else -> currentToken.append(char)
            }
        }

        if (currentToken.isNotEmpty()) {
            tokens.add(currentToken.toString())
        }

        listOfWordLists.add(tokens)
    }
}

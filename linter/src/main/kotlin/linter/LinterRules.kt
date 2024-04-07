package linter

class LinterRules {
    data class LinterRules(
        val idFormatCamelCase: Boolean,
        // true for camelCase, false for snake_case
        val operationInPrintln: Boolean,
    )

    data class LinterRulesWrapper(
        val rules: LinterRules,
    )
}

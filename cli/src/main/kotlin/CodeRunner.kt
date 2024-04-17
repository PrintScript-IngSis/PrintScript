import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import linter.LinterImpl
import org.example.ast.nodes.ProgramNode
import org.example.formatter.FormatterImpl
import org.example.interpreter.InterpreterImpl
import org.example.lexer.LexerImpl
import org.example.parser.ParserImpl

class CodeRunner : CliktCommand(help = "Run PrintScript code") {
    private val runOption by option("-r", "--run", help = "Run interpreter").flag()
    private val linterOption by option("-l", "--linter", help = "Run linter").flag()
    private val formatOption by option("-f", "--format", help = "Run formatter").flag()
    private val rulesFileOption by option("-rules", "--rules", help = "Custom rules file for formatter or linter")
        .file(canBeDir = false, canBeFile = true)
    private val inputFile by argument(help = "Input file").file(mustExist = true, canBeDir = false, canBeFile = true)
    private val outputFile by argument(help = "Output file for formatter")
        .file(canBeDir = false, canBeFile = true).optional()

    private val defaultFormatterRules = "/DefaultFormatterRules.json"
    private val defaultLinterRules = "/DefaultLinterRules.json"

    override fun run() {
        try {
            println(inputFile)
            inputFile.let { file ->
                val input = file.readText()
                val lexer = LexerImpl()
                val tokens = lexer.tokenize(input)
                val parser = ParserImpl(tokens)
                val ast = parser.parse()

                when {
                    runOption -> runInterpreter(ast)
                    linterOption -> runLinter(ast)
                    formatOption -> runFormatter(ast)
                    else -> {
                        printHelp()
                        throw IllegalArgumentException("Please specify one of -r, -l, or -f option. Type -h or --help for help.")
                    }
                }
            }
        } catch (e: Exception) {
            echo("Error: ${e.message}", err = true)
        }
    }

    private fun runInterpreter(input: ProgramNode) {
        echo("Running file ${inputFile.name}... \n")
        val interpreter = InterpreterImpl()
        interpreter.interpret(input)
    }

    private fun runLinter(input: ProgramNode) {
        echo("Running linter on file ${inputFile.name}... \n")
        val linter = LinterImpl()
        val rulesFile =
            rulesFileOption?.path ?: this::class.java.getResource(defaultLinterRules)?.path
                ?: throw IllegalStateException("Default linter rules file not found")
        linter.checkErrors(input, rulesFile).forEach { echo(it.toString()) }
    }

    private fun runFormatter(input: ProgramNode) {
        val rulesFile =
            rulesFileOption?.path ?: this::class.java.getResource(defaultFormatterRules)?.path
                ?: throw IllegalStateException("Default formatter rules file not found")
        val formattedText = formatFile(input, rulesFile)
        if (outputFile == null) {
            echo("Formatting file ${inputFile.name}... \n")
            inputFile.writeText(formattedText)
        } else {
            echo("Formatting file ${inputFile.name} to ${outputFile!!.name}... \n")
            outputFile!!.writeText(formattedText)
        }
    }

    private fun formatFile(
        input: ProgramNode,
        rulesFile: String,
    ): String {
        val formatter = FormatterImpl()
        return formatter.format(input, rulesFile)
    }

    private fun printHelp() {
        echo(
            """
            |Usage: ./gradlew run --args="[OPTION] INPUT_FILE OPTIONAL_OUTPUT_FILE"
            |
            |Options:
            |  -r, --run            Run interpreter
            |  -l, --linter         Run linter
            |  -f, --format         Run formatter (can add optional output file)
            |  -h, --help           Show this message and exit
            |  
            |You can also specify a custom rules file for the formatter or linter using --rules=RULES_FILE (must be a JSON file)
            """.trimMargin(),
        )
        return
    }
}

fun main(args: Array<String>) = CodeRunner().main(args)

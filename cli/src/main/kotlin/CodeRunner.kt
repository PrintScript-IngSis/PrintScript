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

class CodeRunner : CliktCommand() {
    private val runOption by option("-r", "--run", help = "Run interpreter")
        .flag()

    private val linterOption by option("-l", "--linter", help = "Run linter")
        .flag()

    private val formatOption by option("-f", "--format", help = "Run formatter")
        .flag()

    private val inputFile by argument(help = "Input file").file(mustExist = true, canBeDir = false, canBeFile = true)

    private val outputFile by argument(help = "Output file for formatter")
        .file(canBeDir = false, canBeFile = true).optional()

    private val defaultFormatterRules = "cli/src/main/resources/DefaultFormatterRules.json"
    private val defaultLinterRules = "cli/src/main/resources/DefaultLinterRules.json"

    override fun run() {
        try {
            inputFile.let { file ->
                // Read the input file
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
        val interpreter = InterpreterImpl(input)
        interpreter.interpret()
    }

    private fun runLinter(input: ProgramNode) {
        echo("Running linter on file ${inputFile.name}... \n")
        val linter = LinterImpl()
        linter.checkErrors(input, defaultLinterRules).forEach { echo(it.toString()) }
    }

    private fun runFormatter(input: ProgramNode) {
        if (outputFile == null) {
            echo("Formatting file ${inputFile.name}... \n")
            val formatter = FormatterImpl()
            val formatted = formatter.format(input, defaultFormatterRules)
            inputFile.writeText(formatted)
        } else {
            echo("Formatting file ${inputFile.name} to ${outputFile!!.name}... \n")
            val formatter = FormatterImpl()
            val formatted = formatter.format(input, defaultFormatterRules)
            outputFile!!.writeText(formatted)
        }
    }

    private fun printHelp() {
        echo(
            """
            |Usage: printscript [OPTIONS] INPUT_FILE OPTIONAL_OUTPUT_FILE
            |
            |Options:
            |  -r, --run     Run interpreter
            |  -l, --linter  Run linter
            |  -f, --format  Run formatter (optional output file)
            |  -h, --help    Show this message and exit
            """.trimMargin(),
        )
        return
    }
}

// DELETE THIS, THIS IS TEMPORARY
val run = "-r"
val testFile = "C:\\Users\\Usuario\\OneDrive\\Documentos\\Austral\\4to\\IngSis\\PrintScript\\testFile.txt"
val format = "-f"
val linter = "-l"
val help = "-h"
val output = "C:\\Users\\Usuario\\OneDrive\\Documentos\\Austral\\4to\\IngSis\\PrintScript\\outputFile.txt"

fun main(args: Array<String>) = CodeRunner().main(arrayOf(run, testFile))

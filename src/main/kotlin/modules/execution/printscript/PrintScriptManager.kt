package modules.execution.printscript

import cli.AnalyzeCli
import cli.ExecutionCli
import cli.FormatterCli
import cli.Version
import ingsis.interpreter.interpretStatement.Input
import ingsis.utils.OutputEmitter
import modules.execution.language.LanguageManager
import org.springframework.stereotype.Service
import java.io.InputStream
import java.nio.file.Path

@Service
class PrintScriptManager: LanguageManager {

    private fun parseToPrintScriptVersion(version: String): Version {
        return when (version) {
            "v1" -> Version.VERSION_1
            "v2" -> Version.VERSION_2
            else -> Version.VERSION_1
        }
    }

    override fun execute(
        inputStream: InputStream,
        outputEmitter: OutputEmitter,
        version: String,
        input: Input,
    ) {
        val executionInstance = ExecutionCli(outputEmitter, parseToPrintScriptVersion(version), input)
        executionInstance.executeInputStream(inputStream)
    }

    override fun format(
        rulePath: String,
        inputStream: InputStream,
        outputEmitter: OutputEmitter,
        version: String,
        input: Input,
    ) {
        val formatterInstance = FormatterCli(outputEmitter, parseToPrintScriptVersion(version), input)
        val streamedResult = formatterInstance.formatInputStream(rulePath, inputStream)
        val stringResult = streamedResult.bufferedReader().use { it.readText() }
        outputEmitter.print(stringResult)
    }

    override fun analyze(
        rulePath: String,
        inputStream: InputStream,
        outputEmitter: OutputEmitter,
        version: String,
        input: Input,
    ) {
        val analyzerInstance = AnalyzeCli(outputEmitter, parseToPrintScriptVersion(version), input)
        val result = analyzerInstance.analyzeInputStream(rulePath, inputStream)
        outputEmitter.print(result)
    }
}

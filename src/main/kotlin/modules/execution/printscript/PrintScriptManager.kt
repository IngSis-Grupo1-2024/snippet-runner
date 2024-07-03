package modules.execution.printscript

import cli.AnalyzeCli
import cli.ExecutionCli
import cli.FormatterCli
import cli.Version
import ingsis.interpreter.interpretStatement.Input
import ingsis.utils.OutputEmitter
import modules.execution.controller.ExecutionController
import modules.execution.language.LanguageManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class PrintScriptManager : LanguageManager {
    private val logger = LoggerFactory.getLogger(ExecutionController::class.java)

    override fun execute(
        inputStream: InputStream,
        outputEmitter: OutputEmitter,
        version: String,
        input: Input,
    ) {
        logger.info("Calling printscript to execute snippet")
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
        logger.info("Calling printscript to format snippet")
        val formatterInstance = FormatterCli(outputEmitter, parseToPrintScriptVersion(version), input)
        val streamedResult = formatterInstance.formatInputStream(rulePath, inputStream)
        val stringResult = streamedResult.bufferedReader().use { it.readText() }
        if (stringResult.contains("error:") || stringResult.contains("expected at")) {
            throw Exception(stringResult)
        }
        outputEmitter.print(stringResult)
    }

    override fun analyze(
        rulePath: String,
        inputStream: InputStream,
        outputEmitter: OutputEmitter,
        version: String,
        input: Input,
    ) {
        logger.info("Calling printscript to analyze snippet")
        val analyzerInstance = AnalyzeCli(outputEmitter, parseToPrintScriptVersion(version), input)
        val result = analyzerInstance.analyzeInputStream(rulePath, inputStream)
        outputEmitter.print(result)
    }

    private fun parseToPrintScriptVersion(version: String): Version {
        return when (version) {
            "v1" -> Version.VERSION_1
            "v2" -> Version.VERSION_2
            else -> Version.VERSION_1
        }
    }
}

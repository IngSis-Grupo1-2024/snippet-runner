package utils

import cli.AnalyzeCli
import cli.ExecutionCli
import cli.FormatterCli
import cli.Version
import ingsis.interpreter.interpretStatement.Input
import ingsis.utils.OutputEmitter
import java.nio.file.Path

class PrintScriptManager {

    fun execute(filePath: Path, outputEmitter: OutputEmitter, version: Version, input: Input) {
        val executionInstance = ExecutionCli(outputEmitter, version, input)
        executionInstance.executeFile(filePath)
    }

    fun format(rulePath: String, filePath: Path, outputEmitter: OutputEmitter, version: Version, input: Input) {
        val formatterInstance = FormatterCli(outputEmitter, version, input)
        formatterInstance.formatFile(rulePath, filePath)
    }

    fun analyze(rulePath: String, filePath: Path, outputEmitter: OutputEmitter, version: Version, input: Input) {
        val analyzerInstance = AnalyzeCli(outputEmitter, version, input)
        analyzerInstance.analyzeFile(rulePath, filePath)
    }

}
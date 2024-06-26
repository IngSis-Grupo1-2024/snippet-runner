package modules.execution.language

import ingsis.interpreter.interpretStatement.Input
import ingsis.utils.OutputEmitter
import java.io.InputStream
import java.nio.file.Path

interface LanguageManager {
    fun execute(inputStream: InputStream,
                outputEmitter: OutputEmitter,
                version: String,
                input: Input)
    fun format(rulePath: String,
               inputStream: InputStream,
               outputEmitter: OutputEmitter,
               version: String,
               input: Input)
    fun analyze(rulePath: String,
                inputStream: InputStream,
                outputEmitter: OutputEmitter,
                version: String,
                input: Input)
}
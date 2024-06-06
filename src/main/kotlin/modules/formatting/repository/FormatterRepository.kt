package modules.formatting.repository

import org.springframework.stereotype.Repository
import utils.OutputString
import utils.PrintScriptManager

@Repository
class FormatterRepository {
    private val printScriptManager: PrintScriptManager = PrintScriptManager()
    fun format(snippetId: Int, output: OutputString): OutputString {
        // search snippet in bucket by ID
        // get snippet content and save it to a file
        // look for formatting rules and version in snippet-configuration
        // printScriptManager.format(rulePath, snippetFilePath, outputEmitter, version, "")
        return OutputString()
    }
}
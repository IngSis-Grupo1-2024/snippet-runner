package modules.analyzer.repository

import org.springframework.stereotype.Repository
import utils.OutputString
import utils.PrintScriptManager

@Repository
class AnalyzerRepository {
    private val printScriptManager: PrintScriptManager = PrintScriptManager()
    fun analyze(snippetId: Int, outputString: OutputString): OutputString {
        // search in the bucket by ID
        // get snippet content and save it to a file
        // look for version and linting rules in snippet-configuration

        // printScriptManager.analyze(rulePath, snippetFilePath, version, "")
        return OutputString()
    }
}
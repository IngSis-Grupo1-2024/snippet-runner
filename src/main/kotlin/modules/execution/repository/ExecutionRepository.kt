package modules.execution.repository

import org.springframework.stereotype.Repository
import utils.OutputString
import utils.PrintScriptManager

@Repository
class ExecutionRepository(private val snippet-infra: SnippetServiceClient){

    private val printScriptManager: PrintScriptManager = PrintScriptManager()

    fun execute(snippetId: Int, output: OutputString): OutputString {
        // search in the bucket by ID
        val snippetContent: String = snippetServiceClient.getSnippet(snippetId)
        // get snippet content and save it to a file
        val snippetFilePath = saveSnippetToFile(snippetContent)
        // look for version in snippet-configuration
        val version = getVersionFromConfiguration(snippetFilePath)

        printScriptManager.execute(snippetFilePath, snippetFilePath, version, "")
        return OutputString()
    }
}

    private fun saveSnippetToFile(snippetContent: String): String {
        val filePath = "path/to/save/snippet.ps"
        File(filePath).writeText(snippetContent)
        return filePath
    }

    private fun getVersionFromConfiguration(filePath: String): String {
        // Implementar tu lógica para obtener la versión de la configuración del snippet
        return "version"
    }
}



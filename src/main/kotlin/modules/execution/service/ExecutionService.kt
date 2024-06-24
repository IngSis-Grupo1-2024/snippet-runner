package modules.execution.service

import cli.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import modules.execution.language.LanguageManager
import modules.execution.language.SelectLanguage.Companion.selectLanguage
import modules.execution.model.FormatInput
import modules.execution.model.SnippetInput
import modules.execution.model.InputAdapter
import modules.execution.model.RulesInput
import modules.execution.output.ExecutionOutput
import modules.execution.output.ExecutionOutputDto
import modules.execution.printscript.PrintScriptManager
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Service
class ExecutionService {
    fun execute(snippetInfo: SnippetInput): ResponseEntity<ExecutionOutputDto> {
        val output = ExecutionOutput()
        val queue: Queue<String> = LinkedList(snippetInfo.input)
        val languageManager = selectLanguage(snippetInfo.language)
        try {
            languageManager.execute(
                snippetInfo.content.byteInputStream(),
                output,
                snippetInfo.version,
                InputAdapter(queue),
            )
        } catch (e: Exception) {
            output.handleError(e.message!!)
        }
        return ResponseEntity.ok(output.getOutput())
    }

    fun format(snippetInfo: FormatInput): ResponseEntity<ExecutionOutputDto> {
        val output = ExecutionOutput()
        val inputQueue: Queue<String> = LinkedList(snippetInfo.input)
        val rulePath = rulesListToJson(snippetInfo.rules)
        val languageManager = selectLanguage(snippetInfo.language)
        try {
            languageManager.format(
                rulePath.toString(),
                snippetInfo.content.byteInputStream(),
                output,
                snippetInfo.version,
                InputAdapter(inputQueue)
            )
        } catch (e: Exception) {
            output.handleError(e.message!!)
        }
        return ResponseEntity.ok(output.getOutput())
    }

    private fun rulesListToJson(rulesList: List<RulesInput>): Path {
        val jsonMap = mutableMapOf<String, Map<String, Any>>()

        rulesList.forEach { rule ->
            jsonMap[rule.name] = mapOf(
                "on" to rule.isActive,
                "quantity" to rule.value.toInt()
            )
        }

        val objectMapper = ObjectMapper().registerModule(KotlinModule())
        val jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap)

        val file = File("src/main/kotlin/utils/rules/rules.json")
        file.writeText(jsonString)
        return Paths.get(file.absolutePath)
    }
}
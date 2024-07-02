package modules.execution.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import modules.execution.language.SelectLanguage.Companion.selectLanguage
import modules.execution.model.*
import modules.execution.output.ExecutionOutput
import modules.execution.output.ExecutionOutputDto
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
        val rulePath = formatRulesListToJson(snippetInfo.rules)
        val languageManager = selectLanguage(snippetInfo.language)
        try {
            languageManager.format(
                rulePath.toString(),
                snippetInfo.content.byteInputStream(),
                output,
                snippetInfo.version,
                InputAdapter(inputQueue),
            )
        } catch (e: Exception) {
            output.handleError(e.message!!)
        }
        return ResponseEntity.ok(output.getOutput())
    }

    fun lint(snippetInfo: LinterInput): ResponseEntity<ExecutionOutputDto> {
        val output = ExecutionOutput()
        val inputQueue: Queue<String> = LinkedList(snippetInfo.input)
        val rulePath = lintRulesListToJson(snippetInfo.rules)
        val languageManager = selectLanguage(snippetInfo.language)
        try {
            languageManager.analyze(
                rulePath.toString(),
                snippetInfo.content.byteInputStream(),
                output,
                snippetInfo.version,
                InputAdapter(inputQueue),
            )
        } catch (e: Exception) {
            output.handleError(e.message!!)
        }
        val result = output.getOutput().output.toString()
        if (result == "SUCCESSFUL ANALYSIS") {
            return ResponseEntity.ok(output.getOutput())
        } else {
            output.body = mutableListOf()
            output.handleError(result)
            return ResponseEntity.badRequest().body(output.getOutput())
        }
    }

    private fun formatRulesListToJson(rulesList: List<FormatRulesInput>): Path {
        val jsonMap = mutableMapOf<String, Map<String, Any>>()

        rulesList.forEach { rule ->
            jsonMap[rule.name] =
                mapOf(
                    "on" to rule.isActive,
                    "quantity" to rule.value.toInt(),
                )
        }

        val objectMapper = ObjectMapper().registerModule(KotlinModule())
        val jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap)

        val file = File("src/main/kotlin/utils/rules/format-rules.json")
        file.writeText(jsonString)
        return Paths.get(file.absolutePath)
    }

    private fun lintRulesListToJson(rulesList: List<LintRulesInput>): Path {
        val jsonMap = mutableMapOf<String, Map<String, Any>>()

        rulesList.forEach { rule ->
            jsonMap[rule.name] =
                mapOf(
                    "on" to rule.isActive,
                    "expression" to rule.expression,
                    "identifier" to rule.identifier,
                    "literal" to rule.literal,
                    "format" to rule.format,
                )
        }

        val objectMapper = ObjectMapper().registerModule(KotlinModule())
        val jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap)

        val file = File("src/main/kotlin/utils/rules/lint-rules.json")
        file.writeText(jsonString)
        return Paths.get(file.absolutePath)
    }
}

package modules.execution.service

import com.example.redisevents.LintRulesInput
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import modules.execution.controller.ExecutionController
import modules.execution.input.*
import modules.execution.language.LanguageManager
import modules.execution.language.SelectLanguage.Companion.selectLanguage
import modules.execution.output.ExecutionOutput
import modules.execution.output.ExecutionOutputDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Service
class ExecutionService
    @Autowired
    constructor(
        @Value("\${lint.url}")
        private val lintUrl: String,
        @Value("\${format.url}")
        private val formatUrl: String,
    ) {
        private val logger = LoggerFactory.getLogger(ExecutionController::class.java)

        fun execute(snippetInfo: SnippetInput): ResponseEntity<ExecutionOutputDto> {
            val (output, inputQueue: Queue<String>, languageManager) = getLanguageState(snippetInfo.input, snippetInfo.language)
            getExecutionOutput(languageManager, snippetInfo, output, inputQueue)
            return ResponseEntity.ok(output.getOutput())
        }

        fun format(snippetInfo: FormatInput): ResponseEntity<ExecutionOutputDto> {
            val (output, inputQueue: Queue<String>, languageManager) = getLanguageState(snippetInfo.input, snippetInfo.language)
            val rulePath = formatRulesListToJson(snippetInfo.rules)
            getFormatOutput(languageManager, rulePath, snippetInfo, output, inputQueue)
            return ResponseEntity.ok(output.getOutput())
        }

        fun lint(snippetInfo: LinterInput): ResponseEntity<ExecutionOutputDto> {
            val (output, inputQueue: Queue<String>, languageManager) = getLanguageState(snippetInfo.input, snippetInfo.language)
            val rulePath = lintRulesListToJson(snippetInfo.rules)

            getLintingOutput(languageManager, rulePath, snippetInfo, output, inputQueue)

            return getLintingResult(output)
        }

        private fun getLintingResult(output: ExecutionOutput): ResponseEntity<ExecutionOutputDto> {
            if (output.getOutput().output.isEmpty()) {
                return ResponseEntity.badRequest().body(output.getOutput())
            }
            val result = output.getOutput().output[0]
            if (result == "SUCCESSFUL ANALYSIS") {
                return ResponseEntity.ok(output.getOutput())
            } else {
                output.handleError(result)
                return ResponseEntity.badRequest().body(output.getOutput())
            }
        }

        private fun getLintingOutput(
            languageManager: LanguageManager,
            rulePath: Path,
            snippetInfo: LinterInput,
            output: ExecutionOutput,
            inputQueue: Queue<String>,
        ) {
            try {
                languageManager.analyze(
                    rulePath.toString(),
                    snippetInfo.content.byteInputStream(),
                    output,
                    snippetInfo.version,
                    InputAdapter(inputQueue),
                )
            } catch (e: Exception) {
                logger.info("Handling PrintScript linting error")
                output.handleError(e.message!!)
            }
        }

        private fun formatRulesListToJson(rulesList: List<FormatRulesInput>): Path {
            val jsonMap = createFormatJsonMap(rulesList)

            return writeInFile(jsonMap, formatUrl)
        }

        private fun lintRulesListToJson(rulesList: List<LintRulesInput>): Path {
            val jsonMap = createLintJsonMap(rulesList)

            return writeInFile(jsonMap, lintUrl)
        }

        private fun createFormatJsonMap(rulesList: List<FormatRulesInput>): MutableMap<String, Map<String, Any>> {
            val jsonMap = mutableMapOf<String, Map<String, Any>>()

            rulesList.forEach { rule ->
                jsonMap[rule.name] =
                    mapOf(
                        "on" to rule.isActive,
                        "quantity" to rule.value.toInt(),
                    )
            }
            return jsonMap
        }

        private fun createLintJsonMap(rulesList: List<LintRulesInput>): MutableMap<String, Map<String, Any>> {
            val jsonMap = mutableMapOf<String, Map<String, Any>>()

            rulesList.forEach { rule ->
                if (rule.format == "") {
                    parseBasicRule(jsonMap, rule)
                } else {
                    parseFormatRule(jsonMap, rule)
                }
            }
            return jsonMap
        }

        private fun parseBasicRule(
            jsonMap: MutableMap<String, Map<String, Any>>,
            rule: LintRulesInput,
        ) {
            jsonMap[rule.name] =
                mapOf(
                    "on" to rule.isActive,
                    "expression" to rule.expression,
                    "identifier" to rule.identifier,
                    "literal" to rule.literal,
                )
        }

        private fun parseFormatRule(
            jsonMap: MutableMap<String, Map<String, Any>>,
            rule: LintRulesInput,
        ) {
            jsonMap[rule.name] =
                mapOf(
                    "format" to rule.format,
                )
        }

        private fun writeInFile(
            jsonMap: MutableMap<String, Map<String, Any>>,
            path: String,
        ): Path {
            val objectMapper = ObjectMapper().registerModule(KotlinModule())
            val jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap)

            val file = File(path)
            file.writeText(jsonString)
            return Paths.get(file.absolutePath)
        }

        private fun getExecutionOutput(
            languageManager: LanguageManager,
            snippetInfo: SnippetInput,
            output: ExecutionOutput,
            queue: Queue<String>,
        ) {
            try {
                languageManager.execute(
                    snippetInfo.content.byteInputStream(),
                    output,
                    snippetInfo.version,
                    InputAdapter(queue),
                )
            } catch (e: Exception) {
                logger.info("Handling printscript error")
                output.handleError(e.message!!)
            }
        }

        private fun getFormatOutput(
            languageManager: LanguageManager,
            rulePath: Path,
            snippetInfo: FormatInput,
            output: ExecutionOutput,
            inputQueue: Queue<String>,
        ) {
            try {
                languageManager.format(
                    rulePath.toString(),
                    snippetInfo.content.byteInputStream(),
                    output,
                    snippetInfo.version,
                    InputAdapter(inputQueue),
                )
            } catch (e: Exception) {
                logger.info("Handling printscript error")
                output.handleError(e.message!!)
            }
        }

        private fun getLanguageState(
            input: List<String>,
            language: Language,
        ): Triple<ExecutionOutput, Queue<String>, LanguageManager> {
            val output = ExecutionOutput()
            val inputQueue: Queue<String> = LinkedList(input)
            val languageManager = selectLanguage(language)
            return Triple(output, inputQueue, languageManager)
        }
    }

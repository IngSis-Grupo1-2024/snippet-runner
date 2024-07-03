package execution

import modules.execution.input.Language
import modules.execution.input.LinterInput
import modules.execution.language.LanguageManager
import modules.execution.output.ExecutionOutputDto
import modules.execution.printscript.PrintScriptManager
import modules.execution.service.ExecutionService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class ExecutionServiceTest {
    private lateinit var executionService: ExecutionService

    private lateinit var languageManager: LanguageManager

    @BeforeEach
    fun setUp() {
        val lintUrl = "src/test/resources/lint-rules.json"
        executionService = ExecutionService(lintUrl, "src/test/resources/format-rules.json")
        languageManager = PrintScriptManager()
    }

    @Test
    fun `lint method should return successful analysis`() {
        val language = Language.PRINTSCRIPT
        val rules = listOf(com.example.redisevents.LintRulesInput("println", true, true, true, true, ""))
        val linterInput = LinterInput("println('yes');", language, "v1", rules, listOf(), "1", "1")
        val expectedOutput = "SUCCESSFUL ANALYSIS"
        val expectedResponse = ResponseEntity.ok(ExecutionOutputDto(listOf(expectedOutput), listOf()))

        val response = executionService.lint(linterInput)

        assertEquals(expectedResponse.body?.output, response.body?.output)
    }

    @Test
    fun `lint method should return bad status because it doesn't comply with the rules`() {
        val language = Language.PRINTSCRIPT
        val rules = listOf(com.example.redisevents.LintRulesInput("println", true, false, false, false, ""))
        val linterInput = LinterInput("println('yes');", language, "v1", rules, listOf(), "1", "1")
        val expectedResponse =
            ResponseEntity.badRequest().body(
                ExecutionOutputDto(
                    listOf("PRINT_LINE with STRING is not allowed at line 1 and column 9\n"),
                    listOf("PRINT_LINE with STRING is not allowed at line 1 and column 9\n"),
                ),
            )

        val response = executionService.lint(linterInput)

        assertEquals(expectedResponse.body?.output, response.body?.output)
    }
}

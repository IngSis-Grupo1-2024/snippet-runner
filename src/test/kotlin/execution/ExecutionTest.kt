package execution

import modules.execution.controller.ExecutionController
import modules.execution.input.*
import modules.execution.output.ExecutionOutputDto
import modules.execution.service.ExecutionService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class ExecutionTest {
    private val executionController = ExecutionController(ExecutionService("src/test/resources/lint-rules.json", "src/test/resources/format-rules.json"))
    private val inputPath = "src/test/resources/snippet-inputs"

    @Test
    fun `000 - Simple print execution should return expected output`() {
        val snippetPath = "$inputPath/simple-print-test.ps"
        val snippetInput =
            SnippetInput(
                "simple-print-test",
                File(snippetPath).readText(),
                Language.PRINTSCRIPT,
                "v1",
                "ps",
                listOf(""),
            )
        val response = executionController.executeSnippet(snippetInput)
        val expectedResponse = ExecutionOutputDto(output = listOf("testing a simple print snippet"), error = listOf())
        Assertions.assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `001 - Wrong written print should return expected error`() {
        val snippetPath = "$inputPath/println-wrong.ps"
        val snippetInput =
            SnippetInput(
                "println-wrong",
                File(snippetPath).readText(),
                Language.PRINTSCRIPT,
                "v1",
                "ps",
                listOf(""),
            )
        val response = executionController.executeSnippet(snippetInput)
        val expectedResponse =
            ExecutionOutputDto(
                output = listOf(),
                error =
                listOf(
                    "error: delimiter (;) expected at {\n" +
                            "\tstartOffset: 12,\n" +
                            "\tendOffset: 13,\n" +
                            "\tstartLine: 1,\n" +
                            "\tendLine: 1,\n" +
                            "\tstartColumn: 12,\n" +
                            "\tendColumn: 13}",
                ),
            )
        Assertions.assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `002 - Format snippet should return expected output`() {
        val snippetPath = "$inputPath/double-println-test.ps"
        val formatInput =
            FormatInput(
                File(snippetPath).readText(),
                Language.PRINTSCRIPT,
                "v1",
                listOf(
                    FormatRulesInput(name = "println", isActive = true, value = "2"),
                    FormatRulesInput(name = "afterDeclaration", isActive = false, value = "2"),
                ),
                listOf(""),
            )
        val response = executionController.formatSnippet(formatInput)
        val expectedResponse =
            ExecutionOutputDto(output = listOf("\n\nprintln('first');\n\n\nprintln('second');\n"), error = listOf())
        Assertions.assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `003 - Lint snippet should return expected error`() {
        // Arrange
        val snippetPath = "$inputPath/double-println-test.ps"
        val linterInput = LinterInput(
            File(snippetPath).readText(),
            Language.PRINTSCRIPT,
            "v1",
            listOf(
                com.example.redisevents.LintRulesInput(
                    name = "println",
                    isActive = true,
                    expression = true,
                    identifier = false,
                    literal = true,
                    format = ""
                )
            ),
            listOf(""),
            "1",
            "1"
        )
        val expectedResponse = ExecutionOutputDto(output = listOf("SUCCESSFUL ANALYSIS"), error = listOf())

        val response = executionController.lintSnippet(linterInput)

        Assertions.assertEquals(expectedResponse, response.body)
    }
}

package modules.execution.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import modules.execution.model.FormatInput
import modules.execution.model.LinterInput
import modules.execution.model.SnippetInput
import modules.execution.output.ExecutionOutputDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/execute")
interface ExecutionControllerSpec {
    @PostMapping("/executeSnippet")
    @Operation(
        summary = "Execute a snippet",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(schema = Schema(implementation = ExecutionOutputDto::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Snippet execution failed",
                content = [Content(schema = Schema(implementation = String::class))],
            ),
        ],
    )
    fun executeSnippet(
        @RequestBody snippetInfo: SnippetInput,
    ): ResponseEntity<ExecutionOutputDto>

    @PostMapping("/formatSnippet")
    @Operation(
        summary = "Format snippet",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(schema = Schema(implementation = ExecutionOutputDto::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Snippet formatting failed",
                content = [Content(schema = Schema(implementation = String::class))],
            ),
        ],
    )
    fun formatSnippet(
        @RequestBody snippetInfo: FormatInput,
    ): ResponseEntity<ExecutionOutputDto>

    @PostMapping("/lintSnippet")
    @Operation(
        summary = "Lint snippet",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(schema = Schema(implementation = ExecutionOutputDto::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Snippet linting failed",
                content = [Content(schema = Schema(implementation = String::class))],
            ),
        ],
    )
    fun lintSnippet(
        @RequestBody snippetInfo: LinterInput,
    ): ResponseEntity<ExecutionOutputDto>
}

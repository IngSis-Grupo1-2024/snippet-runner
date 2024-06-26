package modules.execution.controller

import modules.execution.model.SnippetInput
import modules.execution.model.FormatInput
import modules.execution.model.LinterInput
import modules.execution.output.ExecutionOutputDto
import modules.execution.service.ExecutionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ExecutionController
    @Autowired
    constructor(private val executionService: ExecutionService) : ExecutionControllerSpec {
        override fun executeSnippet(snippetInfo: SnippetInput): ResponseEntity<ExecutionOutputDto> {
            return executionService.execute(snippetInfo)
        }
        override fun formatSnippet(snippetInfo: FormatInput): ResponseEntity<ExecutionOutputDto> {
            return executionService.format(snippetInfo)
        }
        override fun lintSnippet(snippetInfo: LinterInput): ResponseEntity<ExecutionOutputDto> {
            return executionService.lint(snippetInfo)
        }
    }

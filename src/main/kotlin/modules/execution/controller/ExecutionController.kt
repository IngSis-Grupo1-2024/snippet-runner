package modules.execution.controller

import modules.execution.input.FormatInput
import modules.execution.input.LinterInput
import modules.execution.input.SnippetInput
import modules.execution.output.ExecutionOutputDto
import modules.execution.service.ExecutionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ExecutionController
    @Autowired
    constructor(private val executionService: ExecutionService) : ExecutionControllerSpec {
        private val logger = LoggerFactory.getLogger(ExecutionController::class.java)

        override fun executeSnippet(snippetInfo: SnippetInput): ResponseEntity<ExecutionOutputDto> {
            logger.info("Executing snippet ${snippetInfo.name}")
            return executionService.execute(snippetInfo)
        }

        override fun formatSnippet(snippetInfo: FormatInput): ResponseEntity<ExecutionOutputDto> {
            logger.info("Formating snippet")
            return executionService.format(snippetInfo)
        }

        override fun lintSnippet(snippetInfo: LinterInput): ResponseEntity<ExecutionOutputDto> {
            logger.info("Linting snippet")
            return executionService.lint(snippetInfo)
        }
    }

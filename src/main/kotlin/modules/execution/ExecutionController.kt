package modules.execution

import modules.execution.service.ExecutionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/execute")
class ExecutionController(private val service: ExecutionService) {
    @PostMapping("/withFile", produces = ["application/json"])
    fun withFile(
        @RequestBody filePath: String,
    ): ResponseEntity<String> {
        val response = service.execute(filePath)
        return ResponseEntity(HttpStatus.OK)
    }
}

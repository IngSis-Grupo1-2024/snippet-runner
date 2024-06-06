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
class ExecutionController(private val service: ExecutionService){
    @PostMapping("/byID", produces = ["application/json"])
    fun byId(@RequestBody snippetId: Int): ResponseEntity<String> {
        return try {
            val outputString = service.execute(snippetId)
            ResponseEntity(outputString.getMessage(), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("An error occurred: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
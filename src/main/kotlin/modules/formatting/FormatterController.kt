package modules.formatting

import modules.formatting.service.FormatterService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpStatus


@RestController
@RequestMapping("/format")
class FormatterController(private val formatterService: FormatterService) {

    @PostMapping("/byID", produces = ["application/json"])
    fun byId(@RequestBody snippetId: Int): ResponseEntity<String> {
        return try {
            val outputString = formatterService.format(snippetId)
            ResponseEntity(outputString.getMessage(), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("An error occurred: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}

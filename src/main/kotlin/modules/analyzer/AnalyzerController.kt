package modules.analyzer

import modules.analyzer.service.AnalyzerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/analyze")
class AnalyzerController(private val service: AnalyzerService){
    @PostMapping("/byID", produces = ["application/json"])
    fun byId(@RequestBody snippetId: Int): ResponseEntity<String> {
        val response = service.analyze(snippetId)
        return ResponseEntity(HttpStatus.OK)
    }

}
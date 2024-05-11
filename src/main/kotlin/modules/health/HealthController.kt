package modules.health

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthControlller {
    @GetMapping("/health")
    fun hello(): String {
        return "Working OK!"
    }
}
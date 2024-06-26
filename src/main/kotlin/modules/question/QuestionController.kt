package modules.user

import modules.question.Question
import modules.question.QuestionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/question")
class QuestionController(
    @Autowired private val questionRepository: QuestionRepository,
) {
    @GetMapping("")
    fun getAllQuestions(): List<Question> = questionRepository.findAll().toList()

    @PostMapping("")
    fun createQuestion(
        @RequestBody question: Question,
    ): ResponseEntity<Question> {
        val createdUser = questionRepository.save(question)
        return ResponseEntity(createdUser, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getQuestionById(
        @PathVariable("id") questionId: Long,
    ): ResponseEntity<Question> {
        val question = questionRepository.findById(questionId).orElse(null)
        return if (question != null) {
            ResponseEntity(question, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{id}")
    fun updateQuestionById(
        @PathVariable("id") questionId: Long,
        @RequestBody question: User,
    ): ResponseEntity<Question> {
        val existingQuestion = questionRepository.findById(questionId).orElse(null)

        if (existingQuestion == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        questionRepository.save(existingQuestion)
        return ResponseEntity(existingQuestion, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteQuestionById(
        @PathVariable("id") questionId: Long,
    ): ResponseEntity<Question> {
        if (!questionRepository.existsById(questionId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        questionRepository.deleteById(questionId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}

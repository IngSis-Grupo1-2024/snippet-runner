package modules.question

import modules.question.Question
import org.springframework.data.repository.CrudRepository

interface QuestionRepository : CrudRepository<Question, Long>
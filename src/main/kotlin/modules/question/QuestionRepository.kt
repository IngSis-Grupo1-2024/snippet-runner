package modules.question

import org.springframework.data.repository.CrudRepository

interface QuestionRepository : CrudRepository<Question, Long>

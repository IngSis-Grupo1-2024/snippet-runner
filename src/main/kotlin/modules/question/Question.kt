package modules.question

import jakarta.persistence.*

@Entity
@Table(name = "questions")
data class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val question: String = "",
    val answer: String = "",
    val keywords: String = "",
)
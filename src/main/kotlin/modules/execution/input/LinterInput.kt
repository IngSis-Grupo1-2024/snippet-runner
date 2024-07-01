package modules.execution.input

import com.example.redisevents.LintRulesInput

class LinterInput(
    val content: String,
    val language: Language,
    val version: String,
    val rules: List<LintRulesInput>,
    val input: List<String>,
    val snippetId: String,
    val userId: String,
)

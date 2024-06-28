package modules.execution.model

class LinterInput(
    val content: String,
    val language: Language,
    val version: String,
    val rules: List<LintRulesInput>,
    val input: List<String>,
)

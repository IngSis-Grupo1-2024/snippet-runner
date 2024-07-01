package modules.execution.input

class FormatInput(
    val content: String,
    val language: Language,
    val version: String,
    val rules: List<FormatRulesInput>,
    val input: List<String>,
)

package modules.execution.input

data class SnippetInput(
    val name: String,
    val content: String,
    val language: Language,
    val version: String,
    val extension: String,
    val input: List<String>,
)

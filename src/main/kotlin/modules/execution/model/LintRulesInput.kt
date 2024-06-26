package modules.execution.model

class LintRulesInput(
    val name: String,
    val isActive: Boolean,
    val expression: Boolean,
    val identifier: Boolean,
    val literal: Boolean,
    val format: String,
)

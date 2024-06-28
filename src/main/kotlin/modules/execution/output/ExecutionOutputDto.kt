package modules.execution.output

data class ExecutionOutputDto(
    val output: List<String>,
    val error: List<String>,
)

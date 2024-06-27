package modules.execution.output

import ingsis.utils.OutputEmitter

class ExecutionOutput(
    var body: MutableList<String> = mutableListOf(),
    private val error: MutableList<String> = mutableListOf(),
) :
    OutputEmitter {
    override fun print(string: String) {
//        println(string)
        body.add(string)
    }

    fun handleError(string: String) {
        error.add(string)
    }

    fun getOutput(): ExecutionOutputDto {
        return ExecutionOutputDto(this.body.toList(), this.error.toList())
    }
}

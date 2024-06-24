package modules.execution.output

import ingsis.utils.OutputEmitter

class FormatOutput (
    val body: String,
    val error: String
) : OutputEmitter {
    override fun print(string: String) {
        println(string)
    }

    fun handleError(string: String) {
        error.plus(string)
    }

    fun getOutput(): FormatOutputDto {
        return FormatOutputDto(this.body, this.error)
    }
}
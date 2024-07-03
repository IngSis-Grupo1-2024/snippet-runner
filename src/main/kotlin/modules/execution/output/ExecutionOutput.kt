package modules.execution.output

import ingsis.utils.OutputEmitter
import org.slf4j.LoggerFactory

class ExecutionOutput(
    private var body: ArrayList<String> = ArrayList(),
    private val error: ArrayList<String> = ArrayList(),
) :
    OutputEmitter {
    private val logger = LoggerFactory.getLogger(ExecutionOutput::class.java)

    override fun print(string: String) {
        body.add(string)
    }

    fun handleError(string: String) {
        logger.info("Adding error to error output: $string")
        error.add(string)
    }

    fun getOutput(): ExecutionOutputDto {
        return ExecutionOutputDto(this.body.toList(), this.error.toList())
    }
}
